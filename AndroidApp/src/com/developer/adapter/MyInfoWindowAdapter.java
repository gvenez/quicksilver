package com.developer.adapter;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.developer.model.EventInfo;
import com.example.androidapp.R;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class MyInfoWindowAdapter implements InfoWindowAdapter{


	private View window;
	private HashMap<Marker, EventInfo> eventMarkerMap;
	
	public MyInfoWindowAdapter (Context ctx, HashMap<Marker, EventInfo> eventMarkerMap){
		window = LayoutInflater.from(ctx).inflate(R.layout.custom_window, null);
		this.eventMarkerMap = eventMarkerMap;
	}
	 
	@Override
	
	public View getInfoWindow(Marker marker) {
		
		EventInfo eventInfo = eventMarkerMap.get(marker);
		String title = marker.getTitle();
	 
	             TextView txtTitle = ((TextView) window.findViewById(R.id.txtInfoWindowTitle));
	 
	             if (title != null) {
	 
	                 // Spannable string allows us to edit the formatting of the text.
	 
	                 SpannableString titleText = new SpannableString(title);
	 
	                 titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
	 
	                 txtTitle.setText(titleText);
	 
	             } else {
	 
	                 txtTitle.setText("");
	 
	             }
	             
	             TextView txtType = ((TextView) window.findViewById(R.id.txtInfoWindowEventType));
	 
	             txtType.setText(eventInfo.getType());

	             ImageView pic = ((ImageView) window.findViewById(R.id.ivInfoWindowMain));
	              
	             pic.setImageResource(eventInfo.getPic());
	             
	    return window;
	 
	   }
	 
	    
	 
	   @Override
	 
	   public View getInfoContents(Marker marker) {
	 
	    //this method is not called if getInfoWindow(Marker) does not return null
	 
	    return null;
	 
	   }
}
