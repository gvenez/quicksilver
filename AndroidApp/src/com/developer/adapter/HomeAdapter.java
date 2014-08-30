package com.developer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphasigma.kronos.R;

public class HomeAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	
	public HomeAdapter(final Context context) {
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
		
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
			convertView = mInflater.inflate(R.layout.adapter_home_item, null);
			holder = new ViewHolder();
			holder.user_pic = (ImageView) convertView.findViewById(R.id.user_pic);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		
		return convertView;
	}
	 class ViewHolder {
		ImageView user_pic;
		TextView name;
		
	}
}
