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
public class Alarm extends ParseObject implements Comparable<Alarm>{
	
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
		int myMinute = this.getMinute();
		String myAlarmTime = verifyTime(myHour, myMinute);
		return myAlarmTime;
	}
	
	private String verifyTime(int hour, int minute) {
		String myTime = new String();
		String myHourString = String.valueOf(hour);
		String myMinuteString = String.valueOf(minute);
		int lengthMinute = myMinuteString.length();
		if (hour == 12) {
			if(lengthMinute == 1) {
				myTime = myHourString + ":0" + myMinuteString + " " + "PM";
			}
			else {
				myTime = myHourString + ":" + myMinuteString + " " + "PM";
			}
		}

		else if(hour == 0) {
			if(lengthMinute == 1) {
				myTime = "12" + ":0" + myMinuteString + " " + "AM";
			}
			else {
				myTime = "12" + ":" + myMinuteString + " " + "AM";
			}
		}
		else {
			Calendar cal = Calendar.getInstance();
			if (cal.get(Calendar.AM_PM) == Calendar.AM) {
				if(lengthMinute == 1) {
					myTime = myHourString + ":0" + myMinuteString + " " + "AM";
				}
				else {
					myTime = myHourString + ":" + myMinuteString + " " + "AM";
				}
			}
		    else if (cal.get(Calendar.AM_PM) == Calendar.PM)
				if(lengthMinute == 1) {
					myTime = myHourString + ":0" + myMinuteString + " " + "PM";
				}
				else {
					myTime = myHourString + ":" + myMinuteString + " " + "PM";
				}
		}
		return myTime;	
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

	@Override
	public int compareTo(Alarm other) {
		return getAlarmTime().compareTo(other.getAlarmTime());
	}
	
	
}
