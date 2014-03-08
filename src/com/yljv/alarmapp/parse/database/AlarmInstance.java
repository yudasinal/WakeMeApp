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
	public final static String TABLE_NAME = "partner_alarm_entry";
	public final static String  COLUMN_OBJECT_ID = "object_id";
	
	private ContentValues values = new ContentValues();
	

	public AlarmInstance() {
		/*super("AlarmInstance");
		put(COLUMN_USER, ApplicationSettings.getUserEmail());*/
	}
	
	public String getName(){
		return values.getAsString(AlarmInstance.COLUMN_NAME);
	}
	
	public int getID(){
		return values.getAsInteger(AlarmInstance.COLUMN_ID);
	}
	
	public GregorianCalendar getTimeAsCalendar(){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(values.getAsInteger(AlarmInstance.COLUMN_TIME));
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
		boolean am = this.isAM();

		String hourS;
		String minuteS;
		String amS;

		hourS = Integer.toString(myHour);
		minuteS = (myMinute < 10) ? "0" + Integer.toString(myMinute) : Integer
				.toString(myMinute);
		amS = am ? "AM" : "PM";

		return hourS + ":" + minuteS + " " + amS;
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
	
	//TODO picture
	public void setPicture(){
		
	}
	
	public void setValues(ContentValues values){
		this.values = values;
	}
	
}