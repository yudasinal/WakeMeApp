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
	
	final static int MONDAY = 0;
	final static int TUESDAY = 1;
	final static int WEDNESDAY = 2;
	final static int THURSDAY = 3;
	final static int FRIDAY = 4;
	final static int SATURDAY = 5;
	final static int SUNDAY= 6;
	
	final static int AM = Calendar.AM;
	final static int PM = Calendar.PM;
	
	final static String NAME_COLLUMN = "name";
	final static String USER_COLLUMN = "user";
	final static String ID_COLLUMN = "id";
	final static String TIME_COLLUMN = "time";

	
	private String name;
	private int id;
	private GregorianCalendar time;
	private boolean activated = true;
	private boolean[] weekdays = new boolean[7];
	
	
	
	public Alarm(){
		super("Alarm");
	}
	
	public Alarm(String name){
		super("Alarm");
		this.setName(name);
		ParseUser user = ParseUser.getCurrentUser();
		this.setUser(user);
		this.setID(ApplicationSettings.getAlarmId());
		this.setActivated(true);
	}

	public void setTime(int hour, int minute){
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		this.setTime(cal);
	}
	
	/*
	 *Prints the time of an alarm in appropriate format 
	 *Ex.: 9:00 AM, 12:00 PM, etc 
	 */
	public String getTimeAsString() {
		int myHour = this.getHour();
		int myMinute = this.getMinute();
		boolean AM = this.isAM();
		
		String hourS;
		String minuteS;
		String amS;
		
		hourS = Integer.toString(myHour);
		minuteS = (myMinute < 10) ? "0" + Integer.toString(myMinute) : Integer.toString(myMinute);
		amS = (AM) ? "AM" : "PM";

		return hourS + ":" + minuteS + " " + amS;
	}
	
	/*
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
					myTime = myHourString + ":" + myMinuteString + " " + "AM,";
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
	}*/
	
	public int getHourOfDay(){
		return this.time.get(Calendar.HOUR_OF_DAY);
	}
	
	public int getHour(){
		return this.time.get(Calendar.HOUR);
	}
	
	public int getMinute(){
		return this.time.get(Calendar.MINUTE);
	}
	
	public boolean isAM(){
		int time = this.time.get(Calendar.AM_PM);
		return (time==Alarm.AM) ? true : false;
	}
	
	public GregorianCalendar getTimeAsCalendar(){
		return this.time;
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
	
	public void setRepeat(int day, boolean activated){
		weekdays[day] = activated;
	}
	
	public void setName(String name){
		this.name = name;
		put(NAME_COLLUMN, name);
	}
	
	public void setTime(GregorianCalendar cal){
		this.time = cal;
		put(TIME_COLLUMN, cal.getTime());
	}
	
	public void setUser(ParseUser user){
		put(USER_COLLUMN, user);
	}
	
	public void setID(int id){
		this.id = id;
		put(ID_COLLUMN, id);
	}
	
	public void setActivated(boolean activated){
		this.activated = activated;
	}
	
	public String getName(){
		return this.name;
	}
	
	public boolean isActivated(){
		return this.activated;
	}
	
	public boolean[] getWeekdaysRepeated(){
		return this.weekdays;
	}
}
