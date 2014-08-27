package com.developer.model;

import android.text.Spanned;

/**
 * Message is a Custom Object to encapsulate message information/fields
 *
 */
public class Messages {
	Spanned spanMsg;
	/**
	 * Time of the message when message is received or sent
	 */
	String time;
	
	/**
	 * boolean to determine, who is sender of this message
	 */
	boolean isMine;
	
	/**
	 * image resource id
	 */
	int imgRes;

	public Messages(Spanned spanned, String time, boolean isMine) {
		super();
		this.spanMsg = spanned;
		this.time	 = time;
		this.isMine = isMine;
	}
	
	public Messages(Spanned spanned, String time, int imgRes, boolean isMine) {
		this.spanMsg	= spanned;
		this.time 		= time;
		this.imgRes		= imgRes; 
		this.isMine 	= isMine;
	}

	public Spanned getSpanMessage() {
		return spanMsg;
	}
	
	public boolean isMine() {
		return isMine;
	}
	
	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}
	
	public String getTimeOfMessage (){
		return this.time;
	}
	
	public void setTimeOfMessage (String time){
		this.time = time;
	}
	
	public int getImageResourceId (){
		return imgRes;
	}
	
}
