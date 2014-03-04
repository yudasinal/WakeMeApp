package com.yljv.alarmapp.parse.database;

import java.io.File;
import java.net.URI;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.ContentValues;
import android.provider.BaseColumns;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.yljv.alarmapp.helper.ApplicationSettings;

@ParseClassName("Alarm")
public class Alarm extends ParseObject implements Comparable<Alarm> {



	final static int AM = Calendar.AM;
	final static int PM = Calendar.PM;


	
	private ContentValues values;

	public Alarm() {
		super("Alarm");
		values = new ContentValues();
		values.put(AlarmEntry.COLUMN_WEEKDAYS, "0000000");
		this.setName("Alarm");
		this.setID(ApplicationSettings.getAlarmId() * 10);
		this.setMusicVolume(5);
		this.setMessage("");
		for(int i = 0; i < 7; i++){
			this.setRepeat(i, false);
		}
		this.setActivated(true);
		this.setVisible(true);
	}

	
	public Alarm(ContentValues cv){
		this.values = cv;
	}

	@Override
	public int compareTo(Alarm other) {
		return this.getTimeAsCalendar().compareTo(other.getTimeAsCalendar());
		//return this.name.compareTo(other.name);
	}

	public int getAlarmId() {
		return (Integer) values.get(AlarmEntry.COLUMN_ID);
	}
	
	/*
	 * eg: 18 returns 6
	 */
	public int getHour(){
		int hour = ((Integer) values.get(AlarmEntry.COLUMN_TIME)) / 60;
		return (hour > 12) ? hour-12 : hour;
	}

	/*
	 * eg: 18:00 returns 18
	 */
	public int getHourOfDay() {
		return ((Integer) values.get(AlarmEntry.COLUMN_TIME)) / 60;
	}
	
	public String getMessage(){
		return (String) values.get(AlarmEntry.COLUMN_MSG);
	}

	public int getMinute() {
		return ((Integer) values.get(AlarmEntry.COLUMN_TIME)) % 60;
	}
	
	public String getMusicURIPath(){
		return (String) values.get(AlarmEntry.COLUMN_MUSIC_URI);
	}
	
	public int getMusicVolume(){
		return (Integer) values.get(AlarmEntry.COLUMN_VOLUME);
	}

	public String getName() {
		return (String) values.get(AlarmEntry.COLUMN_NAME);
	}

	public GregorianCalendar getTimeAsCalendar() {
		GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
		cal.setTimeInMillis((Integer) values.get(AlarmEntry.COLUMN_TIME));
		return cal;
	}

	/*
	 * Prints the time of an alarm in appropriate formatEx.: 9:00 AM, 12:00 PM,
	 * etc
	 */
	public String getTimeAsString() {
		int myHour = this.getHourOfDay();
		int myMinute = this.getMinute();
		boolean am = true;
		
		if(myHour > 12){
			myHour -= 12;
			am = false;
		}
		if(myHour == 12){
			am = false;
		}

		String hourS;
		String minuteS;
		String amS;

		hourS = Integer.toString(myHour);
		minuteS = (myMinute < 10) ? "0" + Integer.toString(myMinute) : Integer
				.toString(myMinute);
		amS = am ? "AM" : "PM";

		return hourS + ":" + minuteS + " " + amS;
	}
	
	public ContentValues getValues(){
		return this.values;
	}
	
	//TODO public String getDate

	public boolean[] getWeekdaysRepeated() {
		boolean[] resA = new boolean[7];
		String res = (String) values.get(AlarmEntry.COLUMN_WEEKDAYS);
		if(res==null){
			return resA;
		}
		for(int i = 0; i < 7 ; i++){
			if(res.charAt(i) == '1'){
				resA[i] = true;
			}
		}
		return resA;
	}
	
	
	public boolean isActivated() {
		int res =  (Integer) values.get(AlarmEntry.COLUMN_ACTIVATED);
		return (res == 1) ? true : false;
	}
	
	public boolean isAM() {
		int time = (Integer) values.get(AlarmEntry.COLUMN_TIME);
		return (time >= 13*60) ? true : false;
	}

	public boolean isVisible() {
		int res = (Integer) values.get(AlarmEntry.COLUMN_VISIBILITY);
		return (res == 1) ? true : false;
	}

	public void setActivated(boolean activated) {
		if(activated){
			values.put(AlarmEntry.COLUMN_ACTIVATED, 1);
			put(AlarmEntry.COLUMN_ACTIVATED, 1);
		}else{
			values.put(AlarmEntry.COLUMN_ACTIVATED, 0);
			put(AlarmEntry.COLUMN_ACTIVATED, 0);
		}
	}

	private void setID(int id) {
		values.put(AlarmEntry.COLUMN_ID, id);
		put(AlarmEntry.COLUMN_ID, id);
	}

	public void setMessage(String msg){
		values.put(AlarmEntry.COLUMN_MSG, msg);
		put(AlarmEntry.COLUMN_MSG, msg);
	}

	
	public void setMusicURI(URI uri){
		values.put(AlarmEntry.COLUMN_MUSIC_URI, uri.getPath());
		put(AlarmEntry.COLUMN_MUSIC_URI, uri.getPath());
	}
	

	public void setMusicVolume(int volume){
		values.put(AlarmEntry.COLUMN_VOLUME, volume);
		this.put(AlarmEntry.COLUMN_VOLUME, volume);
	}

	public void setName(String name) {
		values.put(AlarmEntry.COLUMN_NAME, name);
		this.put(AlarmEntry.COLUMN_NAME, name);
	}
	
	public void setPicture(File file){
		values.put(AlarmEntry.COLUMN_PICTURE, file.getAbsolutePath());
		this.put(AlarmEntry.COLUMN_PICTURE, file.getAbsoluteFile());
	}

	public void setRepeat(int day, boolean activated) {
		StringBuilder sb = new StringBuilder((String) values.get(AlarmEntry.COLUMN_WEEKDAYS));
		if(activated){
			sb.replace(day, day+1, "1");
		}else{
			sb.replace(day, day+1, "0");
		}
		values.put(AlarmEntry.COLUMN_WEEKDAYS, sb.toString());
		this.put(AlarmEntry.COLUMN_WEEKDAYS, sb.toString());
	}

	public void setTime(int hour, int minute) {
		int time = hour * 60 + minute;
		values.put(AlarmEntry.COLUMN_TIME, time );
		this.put(AlarmEntry.COLUMN_TIME, time);
	}

	public void setTime(GregorianCalendar cal) {
		int time = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
		values.put(AlarmEntry.COLUMN_TIME, time);
		this.put(AlarmEntry.COLUMN_TIME, time);
	}

	public void setValues(ContentValues values){
		this.values = values;
	}
	
	public void setVisible(boolean visible) {
		if(!visible){
			values.put(AlarmEntry.COLUMN_VISIBILITY, 0);
		} else{
			values.put(AlarmEntry.COLUMN_VISIBILITY, 1);
		}
		this.put(AlarmEntry.COLUMN_VISIBILITY, visible);
		
	}

	private void setUser(ParseUser user) {
	}
	
	public static abstract class AlarmEntry implements BaseColumns{
		 public static final String COLUMN_NAME= "alarmName";
		 public static final String COLUMN_ID = "alarmID";
		 public static final String COLUMN_TIME = "time";
	  	 public static final String COLUMN_ACTIVATED = "activated";
	  	 public static final String COLUMN_WEEKDAYS = "weekdays";
	  	 public static final String COLUMN_VISIBILITY = "visibility";
	  	 public static final String COLUMN_MUSIC_URI = "URI";
	  	 public static final String COLUMN_VOLUME = "volume";
	  	 public static final String COLUMN_MSG = "msg";
		 public static final String COLUMN_PICTURE = "picture";
		
		 public static final String TABLE_NAME = "my_alarm_entry";
		 
		 
		 public final static String COLUMN_USER = "user";
	}
	
	public static abstract class PartnerAlarmEntry implements BaseColumns{
		public static final String COLUMN_NAME= "name";
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_TIME = "time";
		public static final String COLUMN_MSG = "msg";
		public static final String TABLE_NAME = "partner_alarm_entry";
		public static final String COLUMN_PICTURE = "picture";
	}

}
