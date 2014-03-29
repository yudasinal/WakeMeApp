package com.yljv.alarmapp.parse.database;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.yljv.alarmapp.helper.ApplicationSettings;

@ParseClassName("AlarmInstance")
public class AlarmInstance extends ParseObject{

	final static int AM = Calendar.AM;
	final static int PM = Calendar.PM;

	public final static String COLUMN_NAME = "name";
	public final static String COLUMN_ID = "id";
	public final static String COLUMN_TIME = "time";
	public final static String COLUMN_MSG = "msg";
	public final static String COLUMN_PICTURE = "picture";
	public final static String COLUMN_USER = "user";
	public final static String PARTNER_TABLE_NAME = "partner_alarm_entry";
	public final static String MY_ALARMINSTANCE_TABLE_NAME="my_alarm_instance_entry";
	public final static String  COLUMN_OBJECT_ID = "object_id";
	
	private ContentValues values = new ContentValues();
	private boolean sent = false;
	

	public AlarmInstance() {
	}
	
	public void initialize(){
		put(COLUMN_USER, ApplicationSettings.getUserEmail());
	}
	
	public String getName(){
		return this.getString(AlarmInstance.COLUMN_NAME);
	}
	
	public int getID(){
		return this.getInt(AlarmInstance.COLUMN_ID);
	}
	
	public GregorianCalendar getTimeAsCalendar(){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(this.getLong(AlarmInstance.COLUMN_TIME));
		return cal;
	}
	
	public int getHour(){
		return getTimeAsCalendar().get(Calendar.HOUR);
	}
	
	public int getMinute(){
		return getTimeAsCalendar().get(Calendar.MINUTE);
	}
	
	public boolean isAM(){
		return getTimeAsCalendar().get(Calendar.AM_PM) == Calendar.AM;
	}
	
	public String getTimeAsString() {
		int myHour = this.getHour();
		int myMinute = this.getMinute();

		String hourS;
		String minuteS;

		hourS = Integer.toString(myHour);
		minuteS = (myMinute < 10) ? "0" + Integer.toString(myMinute) : Integer
				.toString(myMinute);
		
		if(hourS.length() == 1) {

			return "0" + hourS + ":" + minuteS;
		}
		else{

			return hourS + ":" + minuteS;
		}
	}
	
	public long getTimeInMillis(){
		return this.getLong(AlarmInstance.COLUMN_TIME);
	}
	
	public String getMorningEveningAsString() {

		boolean am = this.isAM();
		String amS;
		
		amS = am ? "AM" : "PM";
		
		return amS;
	}
	
	public void setName(String name){
		values.put(COLUMN_NAME, name);
		put(COLUMN_NAME, name);
	}
	
	public void setID(int id){
		values.put(AlarmInstance.COLUMN_ID, id);
		put(COLUMN_ID, id);
	}
	
	public void setTime(GregorianCalendar cal){
		values.put(COLUMN_TIME, cal.getTimeInMillis());
		put(COLUMN_TIME, cal.getTimeInMillis());
	}
	
	public void setUser(){
		put(COLUMN_USER, ApplicationSettings.getUserEmail());
	}
	
	public void setMsg(String msg){
		values.put(COLUMN_MSG, msg);
		put(COLUMN_MSG, msg);
	}
	
	public void setPictureSent(){
		this.sent = true;
	}
	
	public void setValues(ContentValues values){
		this.values = values;
	}
	
	public ContentValues getValues(){
		return values;
	}
	
	public boolean isPictureSent(){
		return sent;
	}
}