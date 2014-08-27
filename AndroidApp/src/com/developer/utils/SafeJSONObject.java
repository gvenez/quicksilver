package com.developer.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SafeJSONObject  {
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return mObject.toString();
	}

	private JSONObject 	mObject;
	
	public SafeJSONObject() {
		mObject = new JSONObject();
	}
	
	public SafeJSONObject(JSONObject object) {
		mObject = object;	
	}
	
	public JSONObject getObject() {return mObject;}
	
	public SafeJSONObject(String value) {
		try {
			mObject = new JSONObject(value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** defined by developer used in ActivitySearchDetails.java */
	public SafeJSONObject getRiskyJSONObject (String key){
		try {
			return new SafeJSONObject(mObject.getJSONObject(key));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	public SafeJSONObject getJSONObject(String key) {
		try {
			return new SafeJSONObject(mObject.getJSONObject(key));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			/** developer [return new] */
			return new SafeJSONObject();
		}
		/** developer [add exception][return new] */
		catch (Exception ex){return new SafeJSONObject();}
	}
	
	public SafeJSONArray getJSONArray(String key) {
		try {
			return new SafeJSONArray(mObject.getJSONArray(key));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	public void putBoolean(String key, boolean value) {
		try {
			mObject.put(key, value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void putString(String key, String value) {
		try {
			mObject.put(key, value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getString(String key) {
		String returnString = "";
		try {
			returnString = mObject.getString(key);
			if(returnString.equals("null"))
				return "";
			return mObject.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/** developer [add exception] */
		catch (Exception e){}
		return "";
	}
	
	public int getInt(String key) {
		try {
			
			return mObject.getInt(key);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public void putInt(String key, int value) {
		try {
			mObject.put(key, value);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void putSafeJSONObject(String key, SafeJSONObject jsonObject) {
		try {
			mObject.put(key, jsonObject.getObject());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** developer start */
	public long getLong(String key) {
		try {
			return mObject.getLong(key);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	/** developer end */
	public double getDouble(String key) {
		try {
			return mObject.getDouble(key);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public boolean getBoolean(String key) {
		try {
			return mObject.getBoolean(key);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	/** developer start */
	public void putSafeJSONArray(String name) {
		// TODO Auto-generated method stub
		try {
			mObject.put(name, new JSONArray ());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** developer end */

	/** developer start */
	public void putSafeJSONArray(String name, JSONArray newArray) {
		// TODO Auto-generated method stub
		try {
			mObject.put(name, newArray);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/** developer end */
}
