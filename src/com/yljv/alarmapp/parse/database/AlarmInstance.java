package com.yljv.alarmapp.parse.database;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.ContentValues;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.yljv.alarmapp.helper.ApplicationSettings;

@ParseClassName("AlarmInstance")
public class AlarmInstance extends ParseObject{

	final static int AM = Calendar.AM;
	final static int PM = Calendar.PM;

	final static String COLUMN_NAME = "name";
	final static String COLUMN_ID = "id";
	final static String COLUMN_TIME = "time";
	final static String COLUMN_MSG = "msg";
	final static String COLUMN_PICTURE = "picture";
	final static String COLUMN_USER = "user";
	
	private ContentValues values;

	public AlarmInstance() {
		super("AlarmInstance");
		put(COLUMN_USER, ApplicationSettings.getUserEmail());
	}
	
	public void setName(String name){
		put(COLUMN_NAME, name);
	}
	
	public void setID(int id){
		put(COLUMN_ID, id);
	}
	
	public void setTime(GregorianCalendar cal){
		put(COLUMN_TIME, cal.getTimeInMillis());
	}
	
	public void setMsg(String msg){
		put(COLUMN_MSG, msg);
	}
	
	//TODO picture
	public void setPicture(){
		
	}
}