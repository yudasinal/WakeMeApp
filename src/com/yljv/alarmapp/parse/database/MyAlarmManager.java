package com.yljv.alarmapp.parse.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.yljv.alarmapp.helper.AccountManager;
import com.yljv.alarmapp.helper.ApplicationSettings;
import com.yljv.alarmapp.helper.DBHelper;
import com.yljv.alarmapp.helper.MyBootReceiver;

/*
 * THIS IS THE RIGHT ALARM MANAGER
 */
public class MyAlarmManager {

	public static int SUNDAY = 1;
	public static int MONDAY = 2;
	public static int TUESDAY = 3;
	public static int WEDNESDAY = 4;
	public static int THURSDAY = 5;
	public static int FRIDAY = 6;
	public static int SATURDAY = 7;

	private static AlarmManager alarmManager;
	private static HashMap<Integer, PendingIntent> pendingIntents = new HashMap<Integer, PendingIntent>();

	public static ArrayList<Alarm> myAlarms = new ArrayList<Alarm>();
	public static ArrayList<AlarmInstance> partnerAlarms = new ArrayList<AlarmInstance>();

	private static Context context;
	private static DBHelper dbHelper;
	private static SQLiteDatabase db;
	private final static String mFile = "alarmsFile";
	
	
	/*
	 * creates Alarm on device
	 */
	private static long addAlarmInstance(Context context, Alarm alarm, int day) {
	
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		ComponentName receiver = new ComponentName(context,
				MyBootReceiver.class);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
	
		GregorianCalendar now = new GregorianCalendar();
		GregorianCalendar cn = null;
	
		cn = new GregorianCalendar();
		cn.set(Calendar.MINUTE, alarm.getMinute());
		cn.set(Calendar.HOUR_OF_DAY, alarm.getHourOfDay());
		cn.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY + day);
		if (cn.getTimeInMillis() < now.getTimeInMillis()) {
			cn.set(Calendar.WEEK_OF_YEAR, now.get(Calendar.WEEK_OF_YEAR) + 1);
		}
		if (cn.getTimeInMillis() < now.getTimeInMillis()) {
			cn.set(Calendar.YEAR, now.get(Calendar.YEAR) + 1);
		}
	
		AlarmInstance ai = new AlarmInstance();
		ai.setID(alarm.getAlarmId() + day);
		ai.setName(alarm.getName());
		ai.setTime(cn);
		ai.setUser();
		ai.saveInBackground();

		Intent intent = new Intent(context, MyBootReceiver.class);
		intent.putExtra("id", ai.getID());
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
				ai.getID(), intent, 0);
		alarmMgr.set(AlarmManager.RTC_WAKEUP, cn.getTimeInMillis(), alarmIntent);
		
		return cn.getTimeInMillis();
	
	}

	/*public static void addAlarmRepeat(Context context, Alarm alarm) {
		
		GregorianCalendar now = new GregorianCalendar();
		int today = now.get(Calendar.DAY_OF_WEEK);
		
		boolean[] weekdays = alarm.getWeekdaysRepeated();
		
		if(alarm.isActivated() && weekdays[today]){
			long timeMillis = addAlarm(context, alarm, today);
			ParsePush push = new ParsePush();
			push.setChannel(AccountManager.getUserChannel());
			push.setMessage("Hey, I just set a new Alarm!");
			push.setExpirationTime(timeMillis);
			push.sendInBackground();
		}
	}*/

	public static void addTextToPartnerAlarm(final Alarm alarm, String text) {
		alarm.put("message", text);
	}


	public static void deleteAlarm(Alarm alarm) {
		
		myAlarms.remove(alarm);				
		
		db.delete(Alarm.TABLE_NAME, Alarm.COLUMN_ID +"="+alarm.getAlarmId(), null);		
		
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		for(int i = 0; i < 7; i++){
			PendingIntent p = pendingIntents.get(alarm.getAlarmId());
			if(p!=null)	alarmMgr.cancel(p);
			pendingIntents.remove(alarm.getAlarmId() + i);
		}
		
		//TODO remove alarms from server (with same objectid)
		//remove alarms from local database
		// cancelAlarm(alarm)
	}
	
	public static Alarm findAlarmById(int id){
		for(Alarm alarm : myAlarms){
			if(alarm.getAlarmId() == id){
				return alarm;
			}
		}
		return null;
	}
	
	public static ArrayList<AlarmInstance> getPartnerAlarms(){
		return partnerAlarms;
		/*
		partnerAlarms.clear();
		
		String[] projection = {
				AlarmInstance.COLUMN_NAME,
				AlarmInstance.COLUMN_ID,
				AlarmInstance.COLUMN_TIME,
				AlarmInstance.COLUMN_MSG,
				AlarmInstance.COLUMN_PICTURE
		};
		
		String sortOrder = AlarmInstance.COLUMN_TIME;
		//TODO do order!!!
		
		Cursor c = db.query(
				AlarmInstance.TABLE_NAME, 
				projection, 
				null, 
				null, 
				sortOrder, 
				null, 
				null);		
		c.moveToFirst();
		int size = c.getCount();
		AlarmInstance a;
		for(int i = 0; i < size; i++){
			a = new AlarmInstance();
			ContentValues cv = new ContentValues();
			
			cv.put(AlarmInstance.COLUMN_ID, c.getInt(c.getColumnIndexOrThrow(AlarmInstance.COLUMN_ID)));
			cv.put(AlarmInstance.COLUMN_TIME, c.getInt(c.getColumnIndexOrThrow(AlarmInstance.COLUMN_TIME)));
			cv.put(AlarmInstance.COLUMN_MSG, c.getString(c.getColumnIndexOrThrow(AlarmInstance.COLUMN_MSG)));
			cv.put(AlarmInstance.COLUMN_PICTURE, c.getString(c.getColumnIndexOrThrow(AlarmInstance.COLUMN_PICTURE)));
			cv.put(AlarmInstance.COLUMN_NAME, c.getString(c.getColumnIndexOrThrow(AlarmInstance.COLUMN_NAME)));
			
			a.setValues(cv);
			
			partnerAlarms.add(a);
			c.move(1);
		}
		
		return partnerAlarms;*/
	}

	public static void updatePartnerAlarms(final ParsePartnerAlarmListener listener) {
		
		partnerAlarms.clear();
		
		ParseQuery<AlarmInstance> query = ParseQuery.getQuery("AlarmInstance");
		String email = ApplicationSettings.getPartnerEmail();
		query.whereEqualTo(AlarmInstance.COLUMN_USER, ApplicationSettings.getPartnerEmail());
		query.orderByAscending(AlarmInstance.COLUMN_TIME);

		query.findInBackground(new FindCallback<AlarmInstance>() {
			public void done(List<AlarmInstance> list, ParseException e) {
				if (e == null) {
					for(AlarmInstance ai : list){
						partnerAlarms.add(ai);
					}
					MyAlarmManager.putPartnerAlarmsToDB();
					listener.partnerAlarmsFound(list);
				} else {
					listener.partnerAlarmsSearchFailed(e);
				}
			}
		});
		
		
	}
	
	private static void putPartnerAlarmsToDB(){
		//db.delete(AlarmInstance.TABLE_NAME, null, null);
		
		for(AlarmInstance ai : partnerAlarms){
			ContentValues cv = new ContentValues();
			//cv.put(AlarmInstance.COLUMN_OBJECT_ID, ai.getObjectId());
			cv.put(AlarmInstance.COLUMN_ID, ai.getInt(AlarmInstance.COLUMN_ID));
			cv.put(AlarmInstance.COLUMN_MSG, ai.getString(AlarmInstance.COLUMN_MSG));
			cv.put(AlarmInstance.COLUMN_NAME, ai.getString(AlarmInstance.COLUMN_NAME));
			cv.put(AlarmInstance.COLUMN_PICTURE, ai.getString(AlarmInstance.COLUMN_PICTURE));
			cv.put(AlarmInstance.COLUMN_TIME, ai.getInt(AlarmInstance.COLUMN_TIME));
			
			ai.setValues(cv);
			db.insertWithOnConflict(AlarmInstance.TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
			//db.insert(AlarmInstance.TABLE_NAME, "null", cv);
		}
		
	}
	
	
	public static ArrayList<Alarm> getAllAlarms() {
		if(myAlarms.isEmpty()){
			retrieveAllMyAlarms();
		}
		return myAlarms;
	}
	
	public static Context getContext(){
		return context;
	}
	
	public static DBHelper getDBHelper(){
		return dbHelper;
	}
	
	private static void retrieveAllMyAlarms() {
		String[] projection = {
				Alarm.COLUMN_NAME,
				Alarm.COLUMN_ID,
				Alarm.COLUMN_TIME,
				Alarm.COLUMN_ACTIVATED,
				Alarm.COLUMN_WEEKDAYS,
				Alarm.COLUMN_VISIBILITY,
				Alarm.COLUMN_MUSIC_URI,
				Alarm.COLUMN_VOLUME,
				Alarm.COLUMN_MSG,
				Alarm.COLUMN_PICTURE
		};
		
		String sortOrder = Alarm.COLUMN_TIME;
		//TODO do order!!!
		Cursor c = db.query(
				Alarm.TABLE_NAME, 
				projection, 
				null, 
				null, 
				sortOrder, 
				null, 
				null);		
		c.moveToFirst();
		int size = c.getCount();
		Alarm a;
		for(int i = 0; i < size; i++){
			a = new Alarm();
			ContentValues cv = new ContentValues();
			
			cv.put(Alarm.COLUMN_ID, c.getInt(c.getColumnIndexOrThrow(Alarm.COLUMN_ID)));
			cv.put(Alarm.COLUMN_TIME, c.getInt(c.getColumnIndexOrThrow(Alarm.COLUMN_TIME)));
			cv.put(Alarm.COLUMN_ACTIVATED, c.getInt(c.getColumnIndexOrThrow(Alarm.COLUMN_ACTIVATED)));
			cv.put(Alarm.COLUMN_WEEKDAYS, c.getString(c.getColumnIndexOrThrow(Alarm.COLUMN_WEEKDAYS)));
			cv.put(Alarm.COLUMN_VISIBILITY, c.getInt(c.getColumnIndexOrThrow(Alarm.COLUMN_VISIBILITY)));
			cv.put(Alarm.COLUMN_MUSIC_URI, c.getString(c.getColumnIndexOrThrow(Alarm.COLUMN_MUSIC_URI)));
			cv.put(Alarm.COLUMN_VOLUME, c.getInt(c.getColumnIndexOrThrow(Alarm.COLUMN_VOLUME)));
			cv.put(Alarm.COLUMN_MSG, c.getString(c.getColumnIndexOrThrow(Alarm.COLUMN_MSG)));
			cv.put(Alarm.COLUMN_PICTURE, c.getString(c.getColumnIndexOrThrow(Alarm.COLUMN_PICTURE)));
			cv.put(Alarm.COLUMN_NAME, c.getString(c.getColumnIndexOrThrow(Alarm.COLUMN_NAME)));
			
			a.setValues(cv);
			
			myAlarms.add(a);
			c.move(1);
		}
		
	}

	public static void setContext(Context c){
		context = c;
		dbHelper = new DBHelper(context);
		db = dbHelper.getReadableDatabase();
	}
	
	/*
	 * TODO dont sent notifications?
	 */
	public static void editAlarm(Context context, Alarm alarm){
		MyAlarmManager.deleteAlarm(alarm);
		MyAlarmManager.setNewAlarm(context, alarm);
	}
	
	public static void setNewAlarm(Context context, Alarm alarm) {

		long timeMillis = (new GregorianCalendar()).getTimeInMillis();

		boolean[] weekdaysR = alarm.getWeekdaysRepeated();

		int counter = 0;
		for (int i = 0; i < weekdaysR.length; i++) {
			if (weekdaysR[i]) {
				timeMillis = addAlarmInstance(context, alarm, i);
				counter++;
			}
		}

		if (counter == 0) {
			timeMillis = addAlarmInstance(context, alarm, 0);
			counter++;
		}
		

		if(counter!= 0 && alarm.isVisible()){
			//alarm.saveEventually();
			ParsePush push = new ParsePush();
			push.setChannel(AccountManager.getUserChannel());
			push.setMessage("Hey, I just set a new Alarm!");
			push.setExpirationTime(timeMillis);
			push.sendInBackground();
		}
		
		db.insert(Alarm.TABLE_NAME, "null", alarm.getValues());
		myAlarms.add(alarm);
		
		//TODO make sure that really saved in background -> check
		
		alarm.saveInBackground();
	}

}
