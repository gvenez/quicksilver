package com.developer.adapter;


import com.example.androidapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private String[] menuOptions;
	
	public MenuAdapter(final Context context) {
		mInflater = LayoutInflater.from(context);
		menuOptions = context.getResources().getStringArray(R.array.menu);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return menuOptions.length;
		
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_menu_list_item, null);
			holder = new ViewHolder();
			holder.mIcon = (ImageView) convertView.findViewById(R.id.icon);
			holder.menu_lable = (TextView) convertView.findViewById(R.id.menu_lable);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if ( position == 0){
			holder.mIcon.setImageResource(R.drawable.home_menu_icon);
			holder.menu_lable.setText(menuOptions[position].toUpperCase());
		}else if ( position == 1){
			holder.mIcon.setImageResource(R.drawable.chat_menu_icon);
			holder.menu_lable.setText(menuOptions[position].toUpperCase());
		}else if ( position == 2){
			holder.mIcon.setImageResource(R.drawable.contact_menu_icon);
			holder.menu_lable.setText(menuOptions[position].toUpperCase());
		}else if ( position == 3){
			holder.mIcon.setImageResource(R.drawable.shake_menu_icon);
			holder.menu_lable.setText(menuOptions[position].toUpperCase());
		}else if ( position == 4){
			holder.mIcon.setImageResource(R.drawable.settings_menu_icon);
			holder.menu_lable.setText(menuOptions[position].toUpperCase());
		}
		
		return convertView;
	}
	 class ViewHolder {
		ImageView mIcon;
		TextView menu_lable;
		
	}
}
