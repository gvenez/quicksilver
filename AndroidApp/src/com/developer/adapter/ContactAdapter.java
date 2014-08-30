package com.developer.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphasigma.kronos.R;
import com.xmppapp.xmpp.entities.Contact;
import com.xmppapp.xmpp.entities.Presences;

public class ContactAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Contact> list;
	private Context mContext;

	public ContactAdapter(Context context, List<Contact> contacts) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.list = contacts;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (view == null) {
			view = mInflater.inflate(R.layout.adapter_contact_list_item, null);
			holder = new ViewHolder();

			holder.user_pic = (ImageView) view.findViewById(R.id.user_pic);
			holder.name = (TextView) view.findViewById(R.id.name);
			holder.contact_status = (TextView) view.findViewById(R.id.contact_status);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.name.setText(list.get(position).getDisplayName());
		getContactStatus(list.get(0).getMostAvailableStatus(), holder.contact_status);
		holder.user_pic.setImageBitmap(list.get(position).getImage(62, mContext));

		return view;
	}

	static class ViewHolder {
		public ImageView user_pic;
		public TextView name, contact_status;
	}

	private void getContactStatus(int mStatus, TextView status) {
		switch (mStatus) {
		case Presences.CHAT:
			status.setText(R.string.contact_status_free_to_chat);
			status.setTextColor(0xFF83b600);
			break;
		case Presences.ONLINE:
			status.setText(R.string.contact_status_online);
			status.setTextColor(0xFF83b600);
			break;
		case Presences.AWAY:
			status.setText(R.string.contact_status_away);
			status.setTextColor(0xFFffa713);
			break;
		case Presences.XA:
			status.setText(R.string.contact_status_extended_away);
			status.setTextColor(0xFFffa713);
			break;
		case Presences.DND:
			status.setText(R.string.contact_status_do_not_disturb);
			status.setTextColor(0xFFe92727);
			break;
		case Presences.OFFLINE:
			status.setText(R.string.contact_status_offline);
			status.setTextColor(0xFFe92727);
			break;
		default:
			status.setText(R.string.contact_status_offline);
			status.setTextColor(0xFFe92727);
			break;
		}

	}

}
