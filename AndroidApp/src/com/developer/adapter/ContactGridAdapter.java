package com.developer.adapter;

import java.util.List;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.androidapp.R;
import com.xmppapp.xmpp.entities.Contact;

public class ContactGridAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private List<Contact> list;
	private int width;
	private Activity mActivity;

	public ContactGridAdapter(Activity context, List<Contact> contacts) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		this.list = contacts;
		this.mActivity = context;
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
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
			view = mInflater.inflate(R.layout.adapter_contact_grid_item, null);
			holder = new ViewHolder();

			holder.user_pic = (ImageView) view.findViewById(R.id.user_pic);
			holder.name = (TextView) view.findViewById(R.id.name);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.name.setText(list.get(position).getDisplayName());
		holder.user_pic.setScaleType(ImageView.ScaleType.FIT_XY);
		holder.user_pic.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, width / 3)); // 100,
																															// 115

		holder.user_pic.setImageBitmap(list.get(position).getImage(48, mActivity));

		return view;
	}

	static class ViewHolder {
		public ImageView user_pic;
		public TextView name;
	}

}
