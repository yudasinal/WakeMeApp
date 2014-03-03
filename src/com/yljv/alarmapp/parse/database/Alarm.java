package com.yljv.alarmapp.parse.database;

import java.net.URI;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.provider.BaseColumns;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.yljv.alarmapp.helper.ApplicationSettings;

@ParseClassName("Alarm")
public class Alarm extends ParseObject implements Comparable<Alarm> {

	public final static int MONDAY = 0;
	public final static int TUESDAY = 1;
	public final static int WEDNESDAY = 2;
	public final static int THURSDAY = 3;
	public final static int FRIDAY = 4;
	public final static int SATURDAY = 5;
	public final static int SUNDAY = 6;

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
	private boolean visible = true;
	private URI musicURI = null;
	private String msg = "";
	private int volume = 5; //TODO find default volume

	public Alarm() {
		super("Alarm");
	}

	public Alarm(String name) {
		super("Alarm");
		this.setName(name);
		ParseUser user = ParseUser.getCurrentUser();
		this.setUser(user);
		this.setID(ApplicationSettings.getAlarmId());
		this.setActivated(true);
	}

	@Override
	public int compareTo(Alarm other) {
		// return this.getTimeAsCalendar().compareTo(other.getTimeAsCalendar());
		return this.name.compareTo(other.name);
	}

	public int getAlarmId() {
		return this.id;
	}

	/*
	 * eg: 18:00 returns 6
	 */
	public int getHour() {
		return this.time.get(Calendar.HOUR);
	}

	/*
	 * eg: 18:00 returns 18
	 */
	public int getHourOfDay() {
		return this.time.get(Calendar.HOUR_OF_DAY);
	}
	
	public String getMessage(){
		return this.msg;
	}

	public int getMinute() {
		return this.time.get(Calendar.MINUTE);
	}
	
	public URI getMusicURI(){
		return this.musicURI;
	}
	
	public int getMusicVolume(){
		return this.volume;
	}

	public String getName() {
		return this.name;
	}

	public GregorianCalendar getTimeAsCalendar() {
		return this.time;
	}

	/*
	 * Prints the time of an alarm in appropriate formatEx.: 9:00 AM, 12:00 PM,
	 * etc
	 */
	public String getTimeAsString() {
		int myHour = this.getHour();
		int myMinute = this.getMinute();
		boolean AM = this.isAM();

		String hourS;
		String minuteS;
		String amS;

		hourS = Integer.toString(myHour);
		minuteS = (myMinute < 10) ? "0" + Integer.toString(myMinute) : Integer
				.toString(myMinute);
		amS = (AM) ? "AM" : "PM";

		return hourS + ":" + minuteS + " " + amS;
	}

	public boolean[] getWeekdaysRepeated() {
		return this.weekdays;
	}

	public boolean isActivated() {
		return this.activated;
	}

	public boolean isAM() {
		int time = this.time.get(Calendar.AM_PM);
		return (time == Alarm.AM) ? true : false;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	private void setID(int id) {
		this.id = id;
		put(ID_COLLUMN, id);
	}
	
	public void setMessage(String msg){
		this.msg = msg;
	}

	
	public void setMusicURI(URI uri){
		this.musicURI = uri;
	}
	
	public void setMusicVolume(int volume){
		this.volume = volume;
	}

	public void setName(String name) {
		this.name = name;
		put(NAME_COLLUMN, name);
	}

	public void setRepeat(int day, boolean activated) {
		weekdays[day] = activated;
	}

	public void setTime(int hour, int minute) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		this.setTime(cal);
	}

	public void setTime(GregorianCalendar cal) {
		this.time = cal;
		put(TIME_COLLUMN, cal.getTime());
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setUser(ParseUser user) {
		put(USER_COLLUMN, user);
	}
	
	/*public static abstract class AlarmEntry implements BaseColumns{
		public static final String TABLE_NAME = "entry";
		public static final String COLLUMN_NAME= "name";
		public static final String COLLUMN_ID = "id";
		public static final String COLLUMN_TIME = "time";
		public static final String COLUMN_ACTIVATED = "activated";
		public static final String COLUMN_WEEKDAYS = "weekdays";
		public static final String COLUMN_VISIBILITY = "visibility";
		public static final String COLUMN_MUSIC_URI = "URI";
		public static final String COLUMN_VOLUME = "volume";
		public static final String COLUMN_MSG = "msg";
		
		public static final String SQL_CREATE_ENTRIES = 
	}
	*/
}
