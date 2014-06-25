package com.yljv.alarmapp.server.alarm;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.ContentValues;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.yljv.alarmapp.client.helper.ApplicationSettings;
import com.yljv.alarmapp.server.user.AccountManager;

@ParseClassName("Alarm")
public class Alarm extends ParseObject implements Comparable<Alarm> {

	public static final String COLUMN_NAME = "alarmName";
	public static final String COLUMN_ID = "alarmID";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_ACTIVATED = "activated";
	public static final String COLUMN_WEEKDAYS = "weekdays";
	public static final String COLUMN_VISIBILITY = "visibility";
	public static final String COLUMN_MUSIC_URI = "URI";
	public static final String COLUMN_VOLUME = "volume";
	public static final String COLUMN_MSG = "msg";
	public static final String COLUMN_PICTURE = "picture";
	public static final String COLUMN_OBJECT_ID = "objectId";

	public static final String TABLE_NAME = "my_alarm_entry";

	public final static String COLUMN_USER = "user";

	final static int AM = Calendar.AM;
	final static int PM = Calendar.PM;

	private ContentValues values;
	

	public Alarm() {
		super("Alarm");
		
	}

	public Alarm(ContentValues cv) {
		this.values = cv;
	}
	
	public void initialize(){
		setACL(new ParseACL(ParseUser.getCurrentUser()));
		values = new ContentValues();
		values.put(Alarm.COLUMN_WEEKDAYS, "0000000");
		setUser();
		setName("Alarm");
		setID(ApplicationSettings.getAlarmId() * 10);
		setMusicVolume(5);
		setMessage("");
		setMusicString("");
		for (int i = 0; i < 7; i++) {
			this.setRepeat(i, false);
		}
		values.put(Alarm.COLUMN_ACTIVATED, true);
		put(Alarm.COLUMN_ACTIVATED, true);
		this.setVisible(true);
	}

	@Override
	public int compareTo(Alarm other) {
		return this.getTimeAsCalendar().compareTo(other.getTimeAsCalendar());
		// return this.name.compareTo(other.name);
	}

	public int getAlarmId() {
		return (int) values.getAsInteger(Alarm.COLUMN_ID);
	}

	/*
	 * eg: 18 returns 6
	 */
	public int getHour() {
		int hour = (int) (long) (values.getAsLong(Alarm.COLUMN_TIME) / 60);
		return (hour > 12) ? hour - 12 : hour;
	}

	/*
	 * eg: 18:00 returns 18
	 */
	public int getHourOfDay() {
		return (int) (long) (values.getAsLong(Alarm.COLUMN_TIME) / 60);
	}

	public String getMessage() {
		return (String) values.get(Alarm.COLUMN_MSG);
	}

	public int getMinute() {
		return (int) (long) (values.getAsLong(Alarm.COLUMN_TIME) % 60);
	}

	public String getMusicURIPath() {
		return (String) values.get(Alarm.COLUMN_MUSIC_URI);
	}

	public int getMusicVolume() {
		return (Integer) values.get(Alarm.COLUMN_VOLUME);
	}

	public String getName() {
		return (String) values.getAsString(Alarm.COLUMN_NAME);
	}

	public GregorianCalendar getTimeAsCalendar() {
		GregorianCalendar cal = (GregorianCalendar) GregorianCalendar
				.getInstance();
		cal.setTimeInMillis((int) (long) values.getAsLong(Alarm.COLUMN_TIME));
		return cal;
	}
	
	public int getTimeInMinutes(){
		return (Integer) values.getAsInteger(Alarm.COLUMN_TIME);
	}

	/*
	 * Prints the time of an alarm in appropriate formatEx.: 9:00 AM, 12:00 PM,
	 * etc
	 */
	public String getTimeAsString() {
		int myHour = this.getHourOfDay();
		int myMinute = this.getMinute();
		boolean am = true;

		if (myHour > 12) {
			myHour -= 12;
			am = false;
		}
		if (myHour == 12) {
			am = false;
		}

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
	
	public String getMorningEveningAsString() {

		boolean am = this.isAM();
		String amS;
		
		amS = am ? "AM" : "PM";
		
		return amS;
	}

	public ContentValues getValues() {
		return this.values;
	}

	public boolean[] getWeekdaysRepeated() {
		boolean[] resA = new boolean[7];
		String res = (String) values.get(Alarm.COLUMN_WEEKDAYS);
		if (res == null) {
			return resA;
		}
		for (int i = 0; i < 7; i++) {
			if (res.charAt(i) == '1') {
				resA[i] = true;
			}
		}
		return resA;
	}

	public boolean isActivated() {
		return values.getAsBoolean(Alarm.COLUMN_ACTIVATED);
	}

	public boolean isAM() {
		int time = (int) (long) values.getAsLong(Alarm.COLUMN_TIME);
		return (time >= 13 * 60) ? false : true;
	}

	public boolean isVisible() {
		int res = (Integer) values.get(Alarm.COLUMN_VISIBILITY);
		return (res == 1) ? true : false;
	}

	public void setActivated(final boolean activated) {
		
		final Alarm alarm = this;
		alarm.getValues().put(Alarm.COLUMN_ACTIVATED, activated);
		MyAlarmManager.setAlarmActivate(alarm);
		
		ParseQuery<Alarm> query = ParseQuery.getQuery("Alarm");
		String objectId = alarm.getValues().getAsString(COLUMN_OBJECT_ID);
		query.whereEqualTo(Alarm.COLUMN_OBJECT_ID, alarm.getValues().getAsString(COLUMN_OBJECT_ID));
		query.findInBackground(new FindCallback<Alarm>() {
			public void done(List<Alarm> list, ParseException e) {
				if (e == null) {
					if(list.size()==1){
						list.get(0).put(Alarm.COLUMN_ACTIVATED, activated);
						try{
							list.get(0).save();
						}catch(ParseException pe){
							pe.printStackTrace();
						}
					}
				} else {
					e.printStackTrace();
				}
			}
		});
		
	}

	private void setID(int id) {
		values.put(Alarm.COLUMN_ID, id);
		put(Alarm.COLUMN_ID, id);
	}

	public void setMessage(String msg) {
		values.put(Alarm.COLUMN_MSG, msg);
		put(Alarm.COLUMN_MSG, msg);
	}

	public void setMusicString(String path) {
		if(path!=null){
			values.put(Alarm.COLUMN_MUSIC_URI, path);
			put(Alarm.COLUMN_MUSIC_URI, path);
		}
	}

	public void setMusicVolume(int volume) {
		values.put(Alarm.COLUMN_VOLUME, volume);
		this.put(Alarm.COLUMN_VOLUME, volume);
	}

	public void setName(String name) {
		values.put(Alarm.COLUMN_NAME, name);
		this.put(Alarm.COLUMN_NAME, name);
	}

	public void setPicture(File file) {
		values.put(Alarm.COLUMN_PICTURE, file.getAbsolutePath());
		this.put(Alarm.COLUMN_PICTURE, file.getAbsoluteFile());
	}

	public void setRepeat(int day, boolean activated) {
		StringBuilder sb = new StringBuilder(
				(String) values.get(Alarm.COLUMN_WEEKDAYS));
		if (activated) {
			sb.replace(day, day + 1, "1");
		} else {
			sb.replace(day, day + 1, "0");
		}
		values.put(Alarm.COLUMN_WEEKDAYS, sb.toString());
		this.put(Alarm.COLUMN_WEEKDAYS, sb.toString());
	}

	public void setTime(int hour, int minute) {
		long time = hour * 60 + minute;
		values.put(Alarm.COLUMN_TIME, time);
		this.put(Alarm.COLUMN_TIME, time);
	}

	public void setTime(GregorianCalendar cal) {
		long time = cal.get(Calendar.HOUR_OF_DAY) * 60
				+ cal.get(Calendar.MINUTE);
		values.put(Alarm.COLUMN_TIME, time);
		this.put(Alarm.COLUMN_TIME, time);
	}

	public void setValues(ContentValues values) {
		this.values = values;
	}

	public void setVisible(boolean visible) {
		if (!visible) {
			values.put(Alarm.COLUMN_VISIBILITY, 0);
		} else {
			values.put(Alarm.COLUMN_VISIBILITY, 1);
		}
		this.put(Alarm.COLUMN_VISIBILITY, visible);


	}
	
	private void setUser(){
		values.put(Alarm.COLUMN_USER, ApplicationSettings.getUserEmail());
		this.put(Alarm.COLUMN_USER, ApplicationSettings.getUserEmail());
	}
}
