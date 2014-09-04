package com.alphasigma.kronos;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout.PanelSlideListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.developer.adapter.MenuAdapter;
import com.developer.adapter.MyInfoWindowAdapter;
import com.developer.model.ChatModel;
import com.developer.model.EventInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.xmppapp.verification.Result;
import com.xmppapp.verification.VerificationHandler;
import com.xmppapp.verification.VerificationHandler.VerificationInterface;
import com.xmppapp.xmpp.entities.Account;
import com.xmppapp.xmpp.entities.Contact;
import com.xmppapp.xmpp.entities.Conversation;
import com.xmppapp.xmpp.services.XmppConnectionService.OnConversationUpdate;
import com.xmppapp.xmpp.ui.XmppActivity;
/*
 * Hide the menu and map button during the first two screens. Display it only when the main screen starts up
 */
public class MainActivity extends XmppActivity implements View.OnClickListener, OnItemClickListener, PanelSlideListener, VerificationInterface {
	public static final String VIEW_CONVERSATION = "viewConversation";
	public static final String CONVERSATION = "conversationUuid";
	public static final String TEXT = "text";
	public static final String PRESENCE = "co.getintouch.im.presence";
	private static final String TAG = "MAIN_ACTIVITY";

	public static final int REQUEST_SEND_MESSAGE = 0x75441;
	public static final int REQUEST_DECRYPT_PGP = 0x76783;
	@SuppressWarnings("unused")
	private Fragment childFragment;
	private ListView menu_list_view;
	public ImprovedSlidingPaneLayout mLayout;
	private boolean isPanelClosed = true;
	private MainMapFragement mapFragment;
	private HashMap<Marker, EventInfo> eventMarkerMap;
	private GoogleMap mMap;
	private VerificationHandler verificationHandler;
	private List<Conversation> conversationList = new ArrayList<Conversation>();
	private Conversation selectedConversation = null;
	
	private OnConversationUpdate onConvChanged = new OnConversationUpdate() {

		@Override
		public void onConversationUpdate() {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					updateConversationList();
				}
			});
		}
	};

	@Override
	protected void onNewIntent(Intent intent) {
		if (xmppConnectionServiceBound) {
			if ((Intent.ACTION_VIEW.equals(intent.getAction()) && (VIEW_CONVERSATION.equals(intent.getType())))) {
				String convToView = (String) intent.getExtras().get(CONVERSATION);
				updateConversationList();
				for (int i = 0; i < conversationList.size(); ++i) {
					if (conversationList.get(i).getUuid().equals(convToView)) {
						setSelectedConversation(conversationList.get(i));
						break;
					}
				}
				String text = intent.getExtras().getString(TEXT, null);
				swapChatDetailFragment().setText(text);
			}
		} else {
			handledViewIntent = false;
			setIntent(intent);
		}
	}

	public FragmentChatDetails swapChatDetailFragment() {
		FragmentChatDetails selectedFragment = new FragmentChatDetails();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_home, selectedFragment, "conversation");
		transaction.commitAllowingStateLoss();
		return selectedFragment;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		verificationHandler = new VerificationHandler(this);

		mLayout = (ImprovedSlidingPaneLayout) findViewById(R.id.sliding_panel_layout);
		mLayout.setPanelSlideListener(this);
//Hide the menu during the first two screens TODO
		// set menu options on the left side menu in home screen
		MenuAdapter adapter = new MenuAdapter(this);
		menu_list_view = (ListView) findViewById(R.id.menu_list);
		menu_list_view.setAdapter(adapter);
		menu_list_view.setOnItemClickListener(this);

		findViewById(R.id.map_button).setOnClickListener(this);
		findViewById(R.id.menu_button).setOnClickListener(this);

		if (verificationHandler.getVerificationStatus() != Result.PASSED) {
			if (verificationHandler.gotoCodeWindow()) {
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home, childFragment = new Screen2()).commitAllowingStateLoss();
			} else {
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home, childFragment = new Screen1()).commitAllowingStateLoss();
			}
		} else {
			getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home, childFragment = new FragmentHome()).commitAllowingStateLoss();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.menu_button) {
			mLayout.openPane();
		} else if (v.getId() == R.id.map_button) {
			setUpEventSpots();
		}
	}

	@Override
	protected void onStop() {
		if (xmppConnectionServiceBound) {
			xmppConnectionService.removeOnConversationListChangedListener();
		}
		super.onStop();
	}

	private void changeFragment(Fragment newFragment) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_home, newFragment);
		ft.commit();
		childFragment = newFragment;
	}

	public List<Conversation> getConversationList() {
		return this.conversationList;
	}

	public Conversation getSelectedConversation() {
		return this.selectedConversation;
	}

	private void setUpEventSpots() {

		if (mapFragment == null) {
			// map fragment
			mapFragment = new MainMapFragement() {
				@Override
				public void onActivityCreated(Bundle savedInstanceState) {
					super.onActivityCreated(savedInstanceState);
					mMap = mapFragment.getMap();
					mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					addMapPins();
				}
			};
		} else {
			if (mMap != null) {
				mMap.clear();
				addMapPins();
			}
		}

		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_home, mapFragment).commit();
	}

	private void addMapPins() {

		// I'm going to make 2 EventInfo objects and place them on the map
		EventInfo firstEventInfo = new EventInfo(new LatLng(50.154, 4.35), "Right now - event", new Date(), "Party", R.drawable.pic6);
		EventInfo secondEventInfo = new EventInfo(new LatLng(51.25, 4.15), "Future Event", new Date(1032, 5, 25), "Convention", R.drawable.pic5);
		// this date constructor is deprecated but it's just to make a simple
		// example

		Marker firstMarker = mapFragment.placeMarker(mMap, firstEventInfo);
		Marker secondMarker = mapFragment.placeMarker(mMap, secondEventInfo);

		eventMarkerMap = new HashMap<Marker, EventInfo>();
		eventMarkerMap.put(firstMarker, firstEventInfo);
		eventMarkerMap.put(secondMarker, secondEventInfo);

		// add the onClickInfoWindowListener
		mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker marker) {
				EventInfo eventInfo = eventMarkerMap.get(marker);
				Toast.makeText(getBaseContext(), "The date of " + eventInfo.getName() + " is " + eventInfo.getSomeDate().toLocaleString(),
						Toast.LENGTH_LONG).show();

			}
		});

		mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(this, eventMarkerMap));

		/**
		 * set map common
		 */
		final LatLngBounds.Builder builder = new LatLngBounds.Builder();

		builder.include(firstMarker.getPosition());
		builder.include(secondMarker.getPosition());

		mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition arg0) {
				mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 2));
				mMap.setOnCameraChangeListener(null);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long arg3) {
		// TODO Auto-generated method stub

		if (isPanelClosed) // do nothing
			return;

		switch (position) {
		case 0:
			changeFragment(new FragmentHome());
			mLayout.closePane();
			break;
		case 1:
			changeFragment(new FragmentChats());
			mLayout.closePane();
			break;
		case 2:
			changeFragment(new FragmentContact());
			mLayout.closePane();
			break;
		case 3:
			changeFragment(new FragmentShakeToMake());
			mLayout.closePane();
			break;
		case 4:
			changeFragment(new FragmentSettings());
			mLayout.closePane();
			break;
		}
	}

	@Override
	public void onPanelClosed(View arg0) {
		// TODO Auto-generated method stub
		this.isPanelClosed = true;
	}

	@Override
	public void onPanelOpened(View arg0) {
		// TODO Auto-generated method stub
		this.isPanelClosed = false;
	}

	@Override
	public void onPanelSlide(View arg0, float arg1) {
	}

	private boolean paneShouldBeOpen = true;
	private boolean useSubject = true;
	private boolean showLastseen = false;
	private ArrayAdapter<Conversation> listAdapter;

	@Override
	public void onStart() {
		super.onStart();
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.useSubject = preferences.getBoolean("use_subject_in_muc", true);
		this.showLastseen = preferences.getBoolean("show_last_seen", false);
		if (this.xmppConnectionServiceBound) {
			this.onBackendConnected();
		}
		if (conversationList.size() >= 1) {
			onConvChanged.onConversationUpdate();
			Log.e(TAG, conversationList.get(0).getName(false));
		}

	}

	
	/**
	 * This needs to be stepped up. Right now using whatsapp model of phone number as account and IMEI as password
	 */
	public void createAccount()
	{
		String accountNum = getVerificationHandler().getNumber();
		TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
		 String password= mngr.getDeviceId();
		Account account = new Account(accountNum, "im.getintouch.co", password);
		account.setOption(Account.OPTION_USETLS, false);
		account.setOption(Account.OPTION_USECOMPRESSION, true);
		this.xmppConnectionService.createAccount(account);
		
	}
	
	public void addTeamAccount()
	{
				
		String contactJid = "team";
		Account account2 = xmppConnectionService.getAccounts().get(0);
		Contact contact = account2.getRoster().getContact(contactJid);
		if (contact.showInRoster()) {
			Log.e(TAG, getString(R.string.contact_already_exists));
		} else {
			xmppConnectionService.createContact(contact);
		}
		Conversation conversation = xmppConnectionService.findOrCreateConversation(contact.getAccount(), contact.getJid(), false);
		
	}
	
	@Override
	protected void onBackendConnected() {
		this.registerListener();
		if (conversationList.size() == 0) {
			updateConversationList();
		}
		Log.e(TAG, "Backhand connected");
//		Account account = new Account("TestAccount", "im.getintouch.co", "12345");
//		account.setOption(Account.OPTION_USETLS, true);
//		account.setOption(Account.OPTION_USECOMPRESSION, true);
//		this.xmppConnectionService.createAccount(account);

//		String accountJid = xmppConnectionService.getAccounts().get(0).getJid();
//		String contactJid = "someone";
//		Account account2 = xmppConnectionService.findAccountByJid(accountJid);
//		Contact contact = account2.getRoster().getContact(contactJid);
//		if (contact.showInRoster()) {
//			Log.e(TAG, getString(R.string.contact_already_exists));
//		} else {
//			xmppConnectionService.createContact(contact);
//		}
//		Conversation conversation = xmppConnectionService.findOrCreateConversation(contact.getAccount(), contact.getJid(), false);

		// if ((getIntent().getAction() != null) &&
		// (getIntent().getAction().equals(Intent.ACTION_VIEW) &&
		// (!handledViewIntent))) {
		// if
		// (getIntent().getType().equals(ConversationActivity.VIEW_CONVERSATION))
		// {
		// handledViewIntent = true;
		//
		// String convToView = (String)
		// getIntent().getExtras().get(CONVERSATION);
		//
		// for (int i = 0; i < conversationList.size(); ++i) {
		// if (conversationList.get(i).getUuid().equals(convToView)) {
		// setSelectedConversation(conversationList.get(i));
		// }
		// }
		// paneShouldBeOpen = false;
		// String text = getIntent().getExtras().getString(TEXT, null);
		// // swapConversationFragment().setText(text);
		// }
		// } else {
		// if (xmppConnectionService.getAccounts().size() == 0) {
		// startActivity(new Intent(this, ManageAccountActivity.class));
		// finish();
		// } else if (conversationList.size() <= 0) {
		// // add no history
		// startActivity(new Intent(this, StartConversationActivity.class));
		// finish();
		// } else {
		// // find currently loaded fragment
		// ConversationFragment selectedFragment = (ConversationFragment)
		// getFragmentManager().findFragmentByTag("conversation");
		// if (selectedFragment != null) {
		// selectedFragment.onBackendConnected();
		// } else {
		// setSelectedConversation(conversationList.get(0));
		// // swapConversationFragment();
		// }
		// ExceptionHelper.checkForCrash(this, this.xmppConnectionService);
		// }
		// }
	}

	public void setSelectedConversation(Conversation conversation) {
		this.selectedConversation = conversation;
	}

	public void registerListener() {
		if (xmppConnectionServiceBound) {
			xmppConnectionService.setOnConversationListChangedListener(this.onConvChanged);
		}
	}

	public void updateConversationList() {
		xmppConnectionService.populateWithOrderedConversations(conversationList);

		FragmentChats fragmentChats = (FragmentChats) getSupportFragmentManager().findFragmentByTag("fragment_chat");
		if (fragmentChats != null) {
			ArrayList<ChatModel> chatModels = new ArrayList<ChatModel>();
			for (Conversation conversation : conversationList) {
				ChatModel chatModel = new ChatModel(conversation.getName(false), conversation.getLatestMessage().getBody(), "Time",
						R.drawable.ic_launcher, R.drawable.ic_launcher);
				chatModels.add(chatModel);
			}
			fragmentChats.updateConversationList(chatModels);
		}
	}

	@Override
	public void onVerificationProcessStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVerificationProcessEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMsgCodeReceived(String mCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMsgSendingStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMsgSendinEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRandomCodeGenerate(String randomCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPhoneNumberFound(String phoneNumber) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVerificationResult(Result mResult) {
		// TODO Auto-generated method stub

	}

	public VerificationHandler getVerificationHandler() {
		return this.verificationHandler;
	}
}
