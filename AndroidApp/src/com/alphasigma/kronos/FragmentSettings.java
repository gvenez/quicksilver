package com.alphasigma.kronos;

import com.alphasigma.kronos.R;
import com.developer.adapter.SettingsAdapter;
import com.developer.utils.Const;
import com.developer.utils.SafeJSONArray;
import com.developer.utils.SafeJSONObject;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentSettings extends Fragment{

	private SafeJSONArray list;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_settings,
		        container, false);
		
		ListView listview = (ListView) view.findViewById(R.id.list_view);
		listview.addFooterView(inflater.inflate(R.layout.adapter_settings_footer, null));
		listview.addFooterView(inflater.inflate(R.layout.adapter_settings_footer_app_v, null));
		
		addChatList ();
		
		listview.setAdapter(new SettingsAdapter(getActivity(), list));
		
		return view;
	}
	
	private void addChatList () {
		list = new SafeJSONArray();
		
		SafeJSONObject profile = new SafeJSONObject();
		profile.putString("user_name", "Gunez");
		profile.putString("status", "online");
		profile.putString("item_type", Const.TYPE_PROFILE);
		list.addJSONObject(profile);
		
		SafeJSONObject header = new SafeJSONObject();
		header.putString("header", "YOUR PHONE NUMBER");
		header.putString("item_type", Const.TYPE_HEADER);
		list.addJSONObject(header);

		SafeJSONObject obj1 = new SafeJSONObject();
		obj1.putString("text", "+61 481");
		obj1.putString("item_type", Const.TYPE_TEXT_1);
		list.addJSONObject(obj1);

		SafeJSONObject header2 = new SafeJSONObject();
		header2.putString("header", "SETTINGS");
		header2.putString("item_type", Const.TYPE_HEADER);
		list.addJSONObject(header2);
		
		SafeJSONObject obj2 = new SafeJSONObject();
		obj2.putString("text", "Enable Animations");
		obj2.putBoolean("switch", true);
		obj2.putString("item_type", Const.TYPE_TEXT_SWITCH);
		list.addJSONObject(obj2);

		SafeJSONObject obj3 = new SafeJSONObject();
		obj3.putString("text_1", "Language");
		obj3.putString("text_2", "English");
		obj3.putString("item_type", Const.TYPE_TEXT_2);
		list.addJSONObject(obj3);

		SafeJSONObject obj4 = new SafeJSONObject();
		obj4.putString("text", "Notifications and Sounds");
		obj4.putString("item_type", Const.TYPE_TEXT_1);
		list.addJSONObject(obj4);

		SafeJSONObject obj5 = new SafeJSONObject();
		obj5.putString("text", "Blocked Users");
		obj5.putString("item_type", Const.TYPE_TEXT_1);
		list.addJSONObject(obj5);

		SafeJSONObject obj6 = new SafeJSONObject();
		obj6.putString("text", "Chat Background");
		obj6.putString("item_type", Const.TYPE_TEXT_1);
		list.addJSONObject(obj6);

		SafeJSONObject obj7 = new SafeJSONObject();
		obj7.putString("text", "Terminate All Other Sessions");
		obj7.putString("item_type", Const.TYPE_TEXT_1);
		list.addJSONObject(obj7);

		SafeJSONObject header3 = new SafeJSONObject();
		header3.putString("header", "AUTOMATIC PHOTO DOWNLOAD");
		header3.putString("item_type", Const.TYPE_HEADER);
		list.addJSONObject(header3);
		
		SafeJSONObject obj8 = new SafeJSONObject();
		obj8.putString("text_1", "Groups");
		obj8.putString("text_2", "Enabled");
		obj8.putString("item_type", Const.TYPE_TEXT_2);
		list.addJSONObject(obj8);

		SafeJSONObject obj9 = new SafeJSONObject();
		obj9.putString("text_1", "Private Chats");
		obj9.putString("text_2", "Enabled");
		obj9.putString("item_type", Const.TYPE_TEXT_2);
		list.addJSONObject(obj9);

		SafeJSONObject header4 = new SafeJSONObject();
		header4.putString("header", "AUTOMATIC AUDIO DOWNLOAD");
		header4.putString("item_type", Const.TYPE_HEADER);
		list.addJSONObject(header4);
		
		SafeJSONObject obj10 = new SafeJSONObject();
		obj10.putString("text_1", "Groups");
		obj10.putString("text_2", "Enabled");
		obj10.putString("item_type", Const.TYPE_TEXT_2);
		list.addJSONObject(obj10);

		SafeJSONObject obj11 = new SafeJSONObject();
		obj11.putString("text_1", "Private Chats");
		obj11.putString("text_2", "Enabled");
		obj11.putString("item_type", Const.TYPE_TEXT_2);
		list.addJSONObject(obj11);
		
		SafeJSONObject header5 = new SafeJSONObject();
		header5.putString("header", "MESSAGES");
		header5.putString("item_type", Const.TYPE_HEADER);
		list.addJSONObject(header5);
		
		SafeJSONObject obj12 = new SafeJSONObject();
		obj12.putString("text_1", "Messages Text Size");
		obj12.putString("text_2", "16");
		obj12.putString("item_type", Const.TYPE_TEXT_2);
		list.addJSONObject(obj12);

		SafeJSONObject obj13 = new SafeJSONObject();
		obj13.putString("text", "Send by Enter");
		obj13.putBoolean("switch", false);
		obj13.putString("item_type", Const.TYPE_TEXT_SWITCH);
		list.addJSONObject(obj13);

		SafeJSONObject header6 = new SafeJSONObject();
		header6.putString("header", "SUPPORT");
		header6.putString("item_type", Const.TYPE_HEADER);
		list.addJSONObject(header6);

		SafeJSONObject obj14 = new SafeJSONObject();
		obj14.putString("text", "Telegram FAQ");
		obj14.putString("item_type", Const.TYPE_TEXT_1);
		list.addJSONObject(obj14);

		SafeJSONObject obj15 = new SafeJSONObject();
		obj15.putString("text", "Ask a Question");
		obj15.putString("item_type", Const.TYPE_TEXT_1);
		list.addJSONObject(obj15);
	}
}
