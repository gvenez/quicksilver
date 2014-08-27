package com.example.androidapp;


import com.developer.model.EventInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainMapFragement extends SupportMapFragment {
	
	public Marker placeMarker(GoogleMap mMap, EventInfo eventInfo) {
		Marker m = null;
		if (mMap != null){
		m  = mMap.addMarker(new MarkerOptions()
			.position(eventInfo.getLatLong())
			.title(eventInfo.getName()));
		}
		
		return m;
	}
}
