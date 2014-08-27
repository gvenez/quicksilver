package com.example.androidapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.developer.adapter.ChatAdapter;
import com.developer.model.ChatModel;
import com.xmppapp.xmpp.entities.Conversation;
import com.xmppapp.xmpp.entities.ListItem;

public class FragmentChats extends Fragment implements OnItemClickListener {

	private static final String TAG = "FRAGMENT_CHAT";
	ArrayList<ChatModel> list;
	private ChatAdapter chatAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_chats, container, false);

		ListView listview = (ListView) view.findViewById(R.id.list_view);
		listview.setOnItemClickListener(this);

		addChatList();

		Log.e(TAG, "CONVERSION LIST: " + getParentActivity().getConversationList());

		chatAdapter = new ChatAdapter((MainActivity) getActivity(), getParentActivity().getConversationList());
		listview.setAdapter(chatAdapter);
		return view;
	}

	private List<ListItem> contacts = new ArrayList<ListItem>();

	private void addChatList() {
//		String accountJid = getParentActivity().xmppConnectionService.getAccounts().get(0).getJid();
//		String contactJid = "User1";
//		Account account = getParentActivity().xmppConnectionService.findAccountByJid(accountJid);
//		Contact contact = account.getRoster().getContact(contactJid);
//		if (contact.showInRoster()) {
//			Log.e(TAG, "INNER ALREADY");
//		} else {
//			getParentActivity().xmppConnectionService.createContact(contact);
//			Log.e(TAG, "INNER NEW CONTACT");
//		}
//
//		for (Account account2 : getParentActivity().xmppConnectionService.getAccounts()) {
//			if (account2.getStatus() != Account.STATUS_DISABLED) {
//				for (Contact contact1 : account2.getRoster().getContacts()) {
//					if (contact1.showInRoster()) {
//						this.contacts.add(contact1);
//					}
//				}
//			}
//		}
//		Collections.sort(this.contacts);
//
//		Conversation conversation = getParentActivity().xmppConnectionService.findOrCreateConversation(contact.getAccount(), contact.getJid(), false);
//		// switchToConversation(conversation);
//
//		Log.e(TAG, contacts.get(0).getDisplayName());

		/*
		 * list = new ArrayList<ChatModel>();
		 * 
		 * list.add(new ChatModel("Bobby", "you reckon?", "1:40 AM",
		 * R.drawable.pic1, R.drawable.online)); list.add(new
		 * ChatModel("Dave Hartmann",
		 * "Word bro, we got to setup up the whole stuff by tonight ...",
		 * "2:50", R.drawable.pic2, R.drawable.offline)); list.add(new
		 * ChatModel("Oliver Jackson",
		 * "I'll think about decorations for the party", "4:8 AM",
		 * R.drawable.pic3, R.drawable.online)); list.add(new
		 * ChatModel("Ganesh Jones",
		 * "Man, no one invited me to valentine's day. What should I do?",
		 * "11:25 AM", R.drawable.pic4, R.drawable.online)); list.add(new
		 * ChatModel("Mimi Thomson",
		 * "We are at the surf festival on bells beach. Hang with us?",
		 * "12:55 AM", R.drawable.pic5, R.drawable.offline)); list.add(new
		 * ChatModel("Kalyan Parvati",
		 * "Brooklyn was awesome. My neighbours too", "6:51 AM",
		 * R.drawable.pic6, R.drawable.load));
		 */

	}

	public void switchToConversation(Conversation conversation) {
		switchToConversation(conversation, null, false);
	}

	public void switchToConversation(Conversation conversation, String text, boolean newTask) {
		Intent viewConversationIntent = new Intent(getActivity(), MainActivity.class);
		viewConversationIntent.setAction(Intent.ACTION_VIEW);
		viewConversationIntent.putExtra(MainActivity.CONVERSATION, conversation.getUuid());
		if (text != null) {
			viewConversationIntent.putExtra(MainActivity.TEXT, text);
		}
		viewConversationIntent.setType(MainActivity.VIEW_CONVERSATION);
		if (newTask) {
			viewConversationIntent.setFlags(viewConversationIntent.getFlags() | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		} else {
			viewConversationIntent.setFlags(viewConversationIntent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		startActivity(viewConversationIntent);
	}

	protected FragmentChatDetails swapChatDetialFragment() {
		FragmentChatDetails selectedFragment = new FragmentChatDetails();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_home, selectedFragment, "chat_detail");
		transaction.commitAllowingStateLoss();
		return selectedFragment;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		getParentActivity().setSelectedConversation(getParentActivity().getConversationList().get(position));
		openConversationForContact(position);
	}

	public void updateConversationList(ArrayList<ChatModel> chatModels) {
		list = new ArrayList<ChatModel>();
		list = chatModels;
		chatAdapter.notifyDataSetChanged();
	}

	private MainActivity getParentActivity() {
		return ((MainActivity) getActivity());
	}

	protected void openConversationForContact(int position) {
		// switchToConversation(getParentActivity().getSelectedConversation());
		getParentActivity().swapChatDetailFragment();
	}
}
