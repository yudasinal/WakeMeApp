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
import com.yljv.alarmapp.parse.database.Alarm.AlarmEntry;

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
		Intent intent = new Intent(context, MyBootReceiver.class);
		intent.putExtra("id", alarm.getAlarmId() + day);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
				alarm.getAlarmId() + day, intent, 0);
		alarmMgr.set(AlarmManager.RTC_WAKEUP, cn.getTimeInMillis(), alarmIntent);
	
		AlarmInstance ai = new AlarmInstance();
		ai.setID(alarm.getAlarmId() + day);
		ai.setName(alarm.getName());
		ai.setTime(cn);
		ai.saveInBackground();
		
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
		
		db.delete(AlarmEntry.TABLE_NAME, AlarmEntry.COLUMN_ID +"="+alarm.getAlarmId(), null);		
		
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
		
		partnerAlarms.clear();
		
		String[] projection = {
				AlarmInstance.COLUMN_NAME,
				AlarmInstance.COLUMN_ID,
				AlarmInstance.COLUMN_TIME,
				AlarmInstance.COLUMN_MSG,
				AlarmInstance.COLUMN_PICTURE
		};
		
		String sortOrder = AlarmEntry.COLUMN_TIME;
		//TODO do order!!!
		
		Cursor c = db.query(
				AlarmEntry.TABLE_NAME, 
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
		
		return partnerAlarms;
	}

	public static void retrievePartnerAlarmsFromParse(
			final ParsePartnerAlarmListener listener) {
		
		ParseQuery<AlarmInstance> query = ParseQuery.getQuery("AlarmInstance");
		query.whereEqualTo("user", ApplicationSettings.getPartnerEmail());
		query.orderByAscending("time");
		query.findInBackground(new FindCallback<AlarmInstance>() {
			public void done(List<AlarmInstance> list, ParseException e) {
				if (e == null) {
					MyAlarmManager.putPartnerAlarmsToDB(list);
					partnerAlarms = (ArrayList<AlarmInstance>) list;
				} else {
					listener.partnerAlarmsSearchFailed(e);
				}
			}
		});
	}
	
	private static void putPartnerAlarmsToDB(List<AlarmInstance> list){
		for(AlarmInstance ai : list){
			ContentValues cv = new ContentValues();
			cv.put(AlarmInstance.COLUMN_ID, ai.getInt(AlarmInstance.COLUMN_ID));
			cv.put(AlarmInstance.COLUMN_MSG, ai.getString(AlarmInstance.COLUMN_MSG));
			cv.put(AlarmInstance.COLUMN_NAME, ai.getString(AlarmInstance.COLUMN_NAME));
			cv.put(AlarmInstance.COLUMN_PICTURE, ai.getString(AlarmInstance.COLUMN_PICTURE));
			cv.put(AlarmInstance.COLUMN_TIME, ai.getInt(AlarmInstance.COLUMN_TIME));
			cv.put(AlarmInstance.COLUMN_USER, ai.getString(AlarmInstance.COLUMN_USER));
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
				AlarmEntry.COLUMN_NAME,
				AlarmEntry.COLUMN_ID,
				AlarmEntry.COLUMN_TIME,
				AlarmEntry.COLUMN_ACTIVATED,
				AlarmEntry.COLUMN_WEEKDAYS,
				AlarmEntry.COLUMN_VISIBILITY,
				AlarmEntry.COLUMN_MUSIC_URI,
				AlarmEntry.COLUMN_VOLUME,
				AlarmEntry.COLUMN_MSG,
				AlarmEntry.COLUMN_PICTURE
		};
		
		String sortOrder = AlarmEntry.COLUMN_TIME;
		//TODO do order!!!
		Cursor c = db.query(
				AlarmEntry.TABLE_NAME, 
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
			
			cv.put(AlarmEntry.COLUMN_ID, c.getInt(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_ID)));
			cv.put(AlarmEntry.COLUMN_TIME, c.getInt(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_TIME)));
			cv.put(AlarmEntry.COLUMN_ACTIVATED, c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_ACTIVATED)));
			cv.put(AlarmEntry.COLUMN_WEEKDAYS, c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_WEEKDAYS)));
			cv.put(AlarmEntry.COLUMN_VISIBILITY, c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_VISIBILITY)));
			cv.put(AlarmEntry.COLUMN_MUSIC_URI, c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_MUSIC_URI)));
			cv.put(AlarmEntry.COLUMN_VOLUME, c.getInt(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_VOLUME)));
			cv.put(AlarmEntry.COLUMN_MSG, c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_MSG)));
			cv.put(AlarmEntry.COLUMN_PICTURE, c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_PICTURE)));
			cv.put(AlarmEntry.COLUMN_NAME, c.getString(c.getColumnIndexOrThrow(AlarmEntry.COLUMN_NAME)));
			
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
		/*
		ContentValues cv = alarm.getValues();
		alarm.put(AlarmEntry.COLUMN_NAME, cv.get(AlarmEntry.COLUMN_NAME));
		alarm.put(AlarmEntry.COLUMN_ID, cv.get(AlarmEntry.COLUMN_ID));
		alarm.put(AlarmEntry.COLUMN_TIME, cv.get(AlarmEntry.COLUMN_TIME));
		alarm.put(AlarmEntry.COLUMN_ACTIVATED, cv.get(AlarmEntry.COLUMN_ACTIVATED));
		alarm.put(AlarmEntry.COLUMN_WEEKDAYS, cv.get(AlarmEntry.COLUMN_WEEKDAYS));
		alarm.put(AlarmEntry.COLUMN_VISIBILITY, cv.get(AlarmEntry.COLUMN_VISIBILITY));
		alarm.put(AlarmEntry.COLUMN_MUSIC_URI, cv.get(AlarmEntry.COLUMN_MUSIC_URI));
		alarm.put(AlarmEntry.COLUMN_VOLUME, cv.get(AlarmEntry.COLUMN_VOLUME));
		alarm.put(AlarmEntry.COLUMN_USER, ApplicationSettings.getUserEmail());
		*/
		
		db.insert(AlarmEntry.TABLE_NAME, "null", alarm.getValues());
		myAlarms.add(alarm);
		
		//TODO make sure that really saved in background -> check
		alarm.saveInBackground();
	}

	/*
	 * private static void addAlarmToFile(Alarm alarm){ FileOutputStream
	 * outStream;
	 * 
	 * try{ outStream = mContext.openFileOutput(mFile, Context.MODE_APPEND |
	 * Context.MODE_PRIVATE); outStream.write(); } }
	 */

}
