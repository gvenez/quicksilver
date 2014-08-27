package com.developer.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SafeJSONArray {
	private JSONArray mObject;
	
	public SafeJSONArray() {
		mObject = new JSONArray();
	}
	
	public SafeJSONArray(JSONArray object) {
		mObject = object;
	}
	
	public SafeJSONArray(String value) {
		try {
			mObject = new JSONArray(value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public SafeJSONArray(ArrayList<SafeJSONObject> list) {
		mObject = new JSONArray();
		for (int idx = 0; idx < list.size(); idx++) {
			mObject.put(list.get(idx).getObject());
		}
	}
	
	public SafeJSONObject getJSONObject(int idx) {
		try {
			return new SafeJSONObject(mObject.getJSONObject(idx));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public SafeJSONObject getJSONObject1(int idx) {
		try {
			return (SafeJSONObject) mObject.get(idx);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/** defined by developer */
	public JSONArray getJSONArrayObject (){
		return mObject;
	}
	/** developer end */
	
	public SafeJSONArray getJSONArray(int idx) {
		try {
			return new SafeJSONArray(mObject.getJSONArray(idx));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getString (int idx) {
		try {
			return mObject.getString(idx);
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	public int getInt (int idx) {
		try {
			return mObject.getInt(idx);
		} catch (JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}
	
	public int length() {
		return mObject.length();
	}
	
	public void sortArray(Comparator<SafeJSONObject> comparator) {
		List<SafeJSONObject> safeJsonValues = new ArrayList<SafeJSONObject>();
		for (int idx = 0; idx < mObject.length(); idx++) {
			safeJsonValues.add(getJSONObject(idx));
		}
		
		Collections.sort(safeJsonValues, comparator);
		
		List<JSONObject> jsonValues = new ArrayList<JSONObject>();
		for (int idx = 0; idx < safeJsonValues.size(); idx++) {
			jsonValues.add(safeJsonValues.get(idx).getObject());
		}
		
		mObject = new JSONArray(jsonValues);
	}
	
	public ArrayList<String> toStringArray() {
		try {
			ArrayList<String> list = new ArrayList<String>();
			for (int idx = 0; idx < mObject.length(); idx++) {
				list.add(mObject.getString(idx));
			}
		
			return list;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<JSONObject> toJSONObjectArray() {
		try {
			ArrayList<JSONObject> list = new ArrayList<JSONObject>();
			for (int idx = 0; idx < mObject.length(); idx++) {
				list.add(mObject.getJSONObject(idx));
			}
			
			return list;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public ArrayList<SafeJSONObject> toSafeJSONObjectArray() {
		ArrayList<SafeJSONObject> list = new ArrayList<SafeJSONObject>();
		for (int idx = 0; idx < mObject.length(); idx++) {
			list.add(getJSONObject(idx));
		}
		
		return list;
				
	}
	
	/** developer start */
	public JSONArray remove (int index, SafeJSONArray arrayToArray){
		JSONArray Njarray=new JSONArray();
		try{
			for(int i=0;i<arrayToArray.length();i++){     
				if(i!=index)
					Njarray.put(arrayToArray.getString(i));     
			}
			return Njarray;
		}catch (Exception e){e.printStackTrace();}
		return null;
	}
	/** developer end*/
	

	/** developer start */
	public SafeJSONArray removeSafeJsonObject (int index, SafeJSONArray arrayToArray){
		SafeJSONArray Njarray=new SafeJSONArray();
		try{
			for(int i=0;i<arrayToArray.length();i++){     
				if(i!=index)
					Njarray.put(arrayToArray.getJSONObject(i).getObject());     
			}
			return Njarray;
		}catch (Exception e){e.printStackTrace();}
		return null;
	}
	/** developer end*/
	
	public void addJSONObject(SafeJSONObject jsonObject) {
		if (mObject != null)
			mObject.put(jsonObject.getObject());
	}
	
	public void addString(String string) {
		if (mObject != null)
			mObject.put(string);
	}
	
	public String toString() {
		return mObject.toString();
	}
	
	public void put(JSONObject jsonObject) {
		mObject.put(jsonObject);
	}
	
	public void put(String string) {
		mObject.put(string);
	}
	
	public void put(int value) {
		mObject.put(value);
	}
	
}
