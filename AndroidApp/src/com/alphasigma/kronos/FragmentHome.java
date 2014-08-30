package com.alphasigma.kronos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.alphasigma.kronos.R;
import com.developer.adapter.HomeAdapter;

public class FragmentHome extends Fragment implements OnItemClickListener{
	

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_home,
		        container, false);
		
		ListView listview = (ListView) view.findViewById(R.id.list_view);
		listview.setOnItemClickListener(this);
		
		listview.setAdapter(new HomeAdapter(getActivity()));
		
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
	
}
