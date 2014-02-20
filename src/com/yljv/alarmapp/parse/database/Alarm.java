package com.yljv.alarmapp.parse.database;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.yljv.alarmapp.helper.AccountManager;
import com.yljv.alarmapp.helper.ApplicationSettings;

@ParseClassName("Alarm")
public class Alarm extends ParseObject{
	
	public Alarm(){
		super("Alarm");
	}
	
	public Alarm(String name, Date time){
		super("Alarm");
		//put("time", time);
		put("name", name);
		ParseUser user = ParseUser.getCurrentUser();
		put("user", user);
		put("id", ApplicationSettings.getAlarmId());
		put("time", time);
		put("activated", true);
	}
	
	public String getName(){
		return (String) this.get("name");
	}
	
	public int getHour(){
		GregorianCalendar cal = getTimeAsCalendar();
		return cal.get(Calendar.HOUR);
	}
	
	public int getMinute(){
		GregorianCalendar cal = getTimeAsCalendar();
		return cal.get(Calendar.MINUTE);
	}
	
	
	/*
	 *Prints the time of an alarm in appropriate format 
	 *Ex.: 9:00 AM, 12:00 PM, etc 
	 */
	public String getAlarmTime() {
		int myHour = this.getHour();
		String myMinute = String.valueOf(this.getMinute());
		if(this.getHour() > 12) {
			myHour = myHour - 12;
			String myHourString = String.valueOf(myHour);
			String myTime = myHourString + ":" + myMinute + " " + "PM";
			return myTime;
		}
		else {	
			String myTime = String.valueOf(myHour) + ":" + myMinute + " " + "AM";;
			return myTime;
		}
	}
	
	public Date getTimeAsDate(){
		return (Date) this.get("time");
	}
	
	public boolean isActivated(){
		return (Boolean) this.get("activated");
	}
	
	public GregorianCalendar getTimeAsCalendar(){
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(getTimeAsDate());
		return cal;
	}
	
	public void saveAlarm(final ParseAlarmListener listener){
		this.saveEventually(new SaveCallback(){
			@Override
			public void done(ParseException e) {
				if(e==null){
					listener.onAlarmSaved();
				}else{
					listener.onAlarmSaveFailed(e);
				}
			}
		});
	}
	
	public int getAlarmId(){
		return (Integer) this.get("id");
	}
	
	
}
