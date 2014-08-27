package com.developer.adapter;

import java.util.ArrayList;

import com.developer.model.ContactModel;
import com.example.androidapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private ArrayList<ContactModel> list;

	public ContactAdapter (Context context, ArrayList<ContactModel> list) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		this.list = list;
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
		
		holder.name.setText(list.get(position).name);
		holder.contact_status.setText(list.get(position).status);
		holder.user_pic.setImageResource(list.get(position).imgRes);
		
		return view;
	}
	
	static class ViewHolder {
		public ImageView user_pic;
		public TextView
		name,
		contact_status;
	}

}
