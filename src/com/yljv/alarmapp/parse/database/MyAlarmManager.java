package com.yljv.alarmapp.parse.database;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.yljv.alarmapp.helper.AccountManager;
import com.yljv.alarmapp.helper.AlarmBroadcastReceiver;
import com.yljv.alarmapp.helper.ApplicationSettings;
import com.yljv.alarmapp.helper.DBHelper;

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

	private static HashMap<Integer, PendingIntent> pendingIntents = new HashMap<Integer, PendingIntent>();

	public static ArrayList<Alarm> myAlarms = new ArrayList<Alarm>();
	public static ArrayList<AlarmInstance> partnerAlarms = new ArrayList<AlarmInstance>();

	private static Context context;
	private static DBHelper dbHelper;
	private static SQLiteDatabase db;

	public static void cancelAllAlarms() {
		for (PendingIntent intent : pendingIntents.values()) {
			AlarmManager alarmMgr = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			alarmMgr.cancel(intent);
			pendingIntents.remove(intent);
		}
	}

	/*
	 * creates Alarm on device
	 */
	private static long addAlarmInstance(Context context, Alarm alarm, int day) {

		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		ComponentName receiver = new ComponentName(context,
				AlarmBroadcastReceiver.class);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);

		GregorianCalendar now = new GregorianCalendar();
		GregorianCalendar cn = null;

		int min = alarm.getMinute();
		int hour = alarm.getHourOfDay();
		cn = new GregorianCalendar();
		cn.set(Calendar.MINUTE, alarm.getMinute());
		cn.set(Calendar.HOUR_OF_DAY, alarm.getHourOfDay());
		cn.set(Calendar.DAY_OF_WEEK, day);
		if (cn.getTimeInMillis() < now.getTimeInMillis()) {
			cn.set(Calendar.WEEK_OF_YEAR, now.get(Calendar.WEEK_OF_YEAR) + 1);
		}
		if (cn.getTimeInMillis() < now.getTimeInMillis()) {
			cn.set(Calendar.YEAR, now.get(Calendar.YEAR) + 1);
		}
		Log.i("WakMeApp", cn.toString());

		AlarmInstance ai = new AlarmInstance();
		ai.initialize();
		ai.setID(alarm.getAlarmId() + day);
		ai.setName(alarm.getName());
		ai.setTime(cn);
		ai.setUser();

		if (alarm.isVisible()) {
			ai.saveInBackground();
		}

		db.insertWithOnConflict(AlarmInstance.MY_ALARMINSTANCE_TABLE_NAME,
				null, ai.getValues(), SQLiteDatabase.CONFLICT_REPLACE);

		Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
		intent.putExtra("id", ai.getID());
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
				ai.getID(), intent, 0);
		alarmMgr.set(AlarmManager.RTC_WAKEUP, cn.getTimeInMillis(), alarmIntent);
		pendingIntents.put(ai.getID(), alarmIntent);

		return cn.getTimeInMillis();

	}

	public static void addPictureOrMessageToPartnerAlarm(
			final AlarmInstance alarm, String picturePath, String message) {

		if (message != null) {
			alarm.put(AlarmInstance.COLUMN_MSG, message);
		}
		alarm.put("message", message);

		if (picturePath != null) {
			alarm.getValues().put(AlarmInstance.COLUMN_PICTURE, picturePath);
			Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream);
			byte[] data = stream.toByteArray();
			try {
				stream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			final ParseFile file = new ParseFile(data);
			file.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException e) {
					if (e == null) {
						if (file.isDirty()) {
							System.out.println("File is dirty");
						} else {
							alarm.put(AlarmInstance.COLUMN_PICTURE, file);

							if (alarm.getString(Alarm.COLUMN_MSG) != null
									&& file != null) {
								alarm.saveInBackground(new SaveCallback() {

									@Override
									public void done(ParseException e) {
										alarm.setPictureSent();
										db.insertWithOnConflict(
												AlarmInstance.PARTNER_TABLE_NAME,
												null, alarm.getValues(),
												SQLiteDatabase.CONFLICT_REPLACE);
										ParsePush push = new ParsePush();
										push.setChannel(AccountManager
												.getSendingChannel());
										try {
											JSONObject json = new JSONObject(

											"{action: \"com.yljv.alarmapp.UPDATE_ALARM\","
													+ "\"id\": "
													+ alarm.getObjectId() + "}");

											push.setData(json);
											push.setExpirationTime(alarm
													.getTimeAsCalendar()
													.getTimeInMillis());
											push.sendInBackground();
										} catch (Exception pe) {
											pe.printStackTrace();
										}
									}

								});
								;

							}
						}

					} else {
						e.printStackTrace();
					}
				}

			});
		}

	}

	public static ParseFile getPictureFromAlarm(AlarmInstance alarm) {
		return alarm.getParseFile(AlarmInstance.COLUMN_PICTURE);
	}

	public static String getMessageFromAlarm(AlarmInstance alarm) {
		return alarm.getString(AlarmInstance.COLUMN_MSG);
	}

	public static void deleteAlarm(Alarm alarm) {

		myAlarms.remove(alarm);

		db.delete(Alarm.TABLE_NAME, Alarm.COLUMN_ID + "=" + alarm.getAlarmId(),
				null);

		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		int alarmID = alarm.getAlarmId();
		for (int i = 0; i < 7; i++) {
			db.delete(AlarmInstance.MY_ALARMINSTANCE_TABLE_NAME, AlarmInstance.COLUMN_ID + "=" + alarmID + i,
					null);
			PendingIntent p = pendingIntents.get(alarm.getAlarmId());
			if (p != null)
				alarmMgr.cancel(p);
			pendingIntents.remove(alarm.getAlarmId() + i);
		}

		ParseQuery<AlarmInstance> query = ParseQuery.getQuery("AlarmInstance");
		String email = ApplicationSettings.getUserEmail();
		query.whereGreaterThanOrEqualTo(AlarmInstance.COLUMN_ID,
				alarm.getAlarmId());
		query.whereLessThanOrEqualTo(AlarmInstance.COLUMN_ID,
				alarm.getAlarmId() + 9);
		query.whereEqualTo(AlarmInstance.COLUMN_USER, email);
		query.findInBackground(new FindCallback<AlarmInstance>() {
			public void done(List<AlarmInstance> list, ParseException e) {
				if (e == null) {
					for (AlarmInstance ai : list) {
						ai.deleteEventually();
					}
				} else {
					e.printStackTrace();
				}
			}
		});

		alarm.deleteEventually();

	}

	public static Alarm findAlarmById(int id) {
		for (Alarm alarm : myAlarms) {
			if (alarm.getAlarmId() == id) {
				return alarm;
			}
		}
		return null;
	}

	public static AlarmInstance findPartnerAlarmById(int id) {
		for (AlarmInstance alarm : partnerAlarms) {
			if (alarm.getID() == id) {
				return alarm;
			}
		}
		return null;
	}

	public static ArrayList<AlarmInstance> getPartnerAlarms() {
		return partnerAlarms;
	}

	public static void getPartnerAlarmsFromDatabase() {

		partnerAlarms.clear();

		String[] projection = { AlarmInstance.COLUMN_NAME,
				AlarmInstance.COLUMN_ID, AlarmInstance.COLUMN_TIME,
				AlarmInstance.COLUMN_MSG, AlarmInstance.COLUMN_PICTURE,
				AlarmInstance.COLUMN_OBJECT_ID };

		String sortOrder = AlarmInstance.COLUMN_TIME;

		Cursor c = db.query(AlarmInstance.PARTNER_TABLE_NAME, projection, null,
				null, sortOrder, null, null);
		c.moveToFirst();
		int size = c.getCount();
		AlarmInstance a;
		for (int i = 0; i < size; i++) {
			a = new AlarmInstance();
			ContentValues cv = new ContentValues();

			a.setObjectId(c.getString(c
					.getColumnIndexOrThrow(AlarmInstance.COLUMN_OBJECT_ID)));
			cv.put(AlarmInstance.COLUMN_OBJECT_ID, c.getString(c
					.getColumnIndexOrThrow(AlarmInstance.COLUMN_OBJECT_ID)));
			cv.put(AlarmInstance.COLUMN_ID,
					c.getInt(c.getColumnIndexOrThrow(AlarmInstance.COLUMN_ID)));
			cv.put(AlarmInstance.COLUMN_TIME, c.getLong(c
					.getColumnIndexOrThrow(AlarmInstance.COLUMN_TIME)));
			cv.put(AlarmInstance.COLUMN_MSG, c.getString(c
					.getColumnIndexOrThrow(AlarmInstance.COLUMN_MSG)));
			cv.put(AlarmInstance.COLUMN_PICTURE, c.getString(c
					.getColumnIndexOrThrow(AlarmInstance.COLUMN_PICTURE)));
			cv.put(AlarmInstance.COLUMN_NAME, c.getString(c
					.getColumnIndexOrThrow(AlarmInstance.COLUMN_NAME)));

			a.setValues(cv);

			partnerAlarms.add(a);
			c.move(1);
		}
		c.close();
	}

	public static void getPartnerAlarmsFromServer(
			final ParsePartnerAlarmListener listener) {

		partnerAlarms.clear();

		ParseQuery<AlarmInstance> query = ParseQuery.getQuery("AlarmInstance");
		query.whereEqualTo(AlarmInstance.COLUMN_USER,
				ApplicationSettings.getPartnerEmail());
		query.orderByAscending(AlarmInstance.COLUMN_TIME);

		query.findInBackground(new FindCallback<AlarmInstance>() {
			public void done(List<AlarmInstance> list, ParseException e) {
				if (e == null) {
					MyAlarmManager.putPartnerAlarmsToDB(list);
					listener.partnerAlarmsFound(list);
				} else {
					listener.partnerAlarmsSearchFailed(e);
				}
			}
		});

	}

	private static void putPartnerAlarmsToDB(List<AlarmInstance> list) {
		// db.delete(AlarmInstance.TABLE_NAME, null, null);

		for (AlarmInstance ai : list) {
			ContentValues cv = new ContentValues();
			cv.put(AlarmInstance.COLUMN_OBJECT_ID, ai.getObjectId());
			cv.put(AlarmInstance.COLUMN_ID, ai.getInt(AlarmInstance.COLUMN_ID));
			cv.put(AlarmInstance.COLUMN_MSG,
					ai.getString(AlarmInstance.COLUMN_MSG));
			cv.put(AlarmInstance.COLUMN_NAME,
					ai.getString(AlarmInstance.COLUMN_NAME));
			cv.put(AlarmInstance.COLUMN_PICTURE,
					ai.getString(AlarmInstance.COLUMN_PICTURE));
			cv.put(AlarmInstance.COLUMN_TIME,
					ai.getLong(AlarmInstance.COLUMN_TIME));

			ai.setValues(cv);
			db.insertWithOnConflict(AlarmInstance.PARTNER_TABLE_NAME, null, cv,
					SQLiteDatabase.CONFLICT_REPLACE);
			partnerAlarms.add(ai);
			// db.insert(AlarmInstance.TABLE_NAME, "null", cv);
		}

	}

	private static void putMyAlarmsToDB(List<Alarm> list) {
		// db.delete(AlarmInstance.TABLE_NAME, null, null);

		for (Alarm alarm : list) {
			ContentValues cv = new ContentValues();

			cv.put(Alarm.COLUMN_OBJECT_ID, alarm.getObjectId());
			cv.put(Alarm.COLUMN_NAME, alarm.getString(Alarm.COLUMN_NAME));
			cv.put(Alarm.COLUMN_ID, alarm.getInt(Alarm.COLUMN_ID));
			cv.put(Alarm.COLUMN_TIME, alarm.getLong(Alarm.COLUMN_TIME));
			cv.put(Alarm.COLUMN_ACTIVATED, alarm.getInt(Alarm.COLUMN_ACTIVATED));
			cv.put(Alarm.COLUMN_WEEKDAYS,
					alarm.getString(Alarm.COLUMN_WEEKDAYS));
			cv.put(Alarm.COLUMN_VISIBILITY,
					alarm.getInt(Alarm.COLUMN_VISIBILITY));
			cv.put(Alarm.COLUMN_MUSIC_URI,
					alarm.getString(Alarm.COLUMN_MUSIC_URI));
			cv.put(Alarm.COLUMN_VOLUME, alarm.getInt(Alarm.COLUMN_VOLUME));
			cv.put(Alarm.COLUMN_MSG, alarm.getString(Alarm.COLUMN_MSG));
			try {
				ParseFile pic = alarm.getParseFile(Alarm.COLUMN_PICTURE);
				if (pic != null) {
					cv.put(Alarm.COLUMN_PICTURE,
							alarm.getParseFile(Alarm.COLUMN_PICTURE).getData());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

			alarm.setValues(cv);
			long success = db.insertWithOnConflict(Alarm.TABLE_NAME, null, cv,
					SQLiteDatabase.CONFLICT_REPLACE);
			
			String[] projection = { Alarm.COLUMN_NAME, Alarm.COLUMN_ID,
					Alarm.COLUMN_TIME, Alarm.COLUMN_ACTIVATED,
					Alarm.COLUMN_WEEKDAYS, Alarm.COLUMN_VISIBILITY,
					Alarm.COLUMN_MUSIC_URI, Alarm.COLUMN_VOLUME, Alarm.COLUMN_MSG,
					Alarm.COLUMN_PICTURE, Alarm.COLUMN_OBJECT_ID };

			String sortOrder = Alarm.COLUMN_TIME;
			
			
			//Check how many 
			Cursor c = db.query(Alarm.TABLE_NAME, projection, null, null,
					null, null, sortOrder);
			c.moveToFirst();
			int size = c.getCount();
			Log.i("MyAlarm", Integer.toString(size));
			c.close();
			myAlarms.add(alarm);
		}
	}

	public static ArrayList<Alarm> getMyAlarms() {
		return myAlarms;
	}

	public static void getMyAlarmsFromDatabase() {
		String[] projection = { Alarm.COLUMN_NAME, Alarm.COLUMN_ID,
				Alarm.COLUMN_TIME, Alarm.COLUMN_ACTIVATED,
				Alarm.COLUMN_WEEKDAYS, Alarm.COLUMN_VISIBILITY,
				Alarm.COLUMN_MUSIC_URI, Alarm.COLUMN_VOLUME, Alarm.COLUMN_MSG,
				Alarm.COLUMN_PICTURE, Alarm.COLUMN_OBJECT_ID };

		String sortOrder = Alarm.COLUMN_TIME;
		// TODO do order!!!
		Cursor c = db.query(Alarm.TABLE_NAME, projection, null, null, null, null,
				sortOrder);
		c.moveToFirst();
		int size = c.getCount();
		Alarm a;
		for (int i = 0; i < size; i++) {
			a = new Alarm();
			a.setObjectId(c.getString(c
					.getColumnIndexOrThrow(Alarm.COLUMN_OBJECT_ID)));
			ContentValues cv = new ContentValues();

			cv.put(Alarm.COLUMN_ID,
					c.getInt(c.getColumnIndexOrThrow(Alarm.COLUMN_ID)));
			cv.put(Alarm.COLUMN_TIME,
					c.getLong(c.getColumnIndexOrThrow(Alarm.COLUMN_TIME)));
			cv.put(Alarm.COLUMN_ACTIVATED,
					c.getInt(c.getColumnIndexOrThrow(Alarm.COLUMN_ACTIVATED)));
			cv.put(Alarm.COLUMN_WEEKDAYS,
					c.getString(c.getColumnIndexOrThrow(Alarm.COLUMN_WEEKDAYS)));
			cv.put(Alarm.COLUMN_VISIBILITY,
					c.getInt(c.getColumnIndexOrThrow(Alarm.COLUMN_VISIBILITY)));
			cv.put(Alarm.COLUMN_MUSIC_URI, c.getString(c
					.getColumnIndexOrThrow(Alarm.COLUMN_MUSIC_URI)));
			cv.put(Alarm.COLUMN_VOLUME,
					c.getInt(c.getColumnIndexOrThrow(Alarm.COLUMN_VOLUME)));
			cv.put(Alarm.COLUMN_MSG,
					c.getString(c.getColumnIndexOrThrow(Alarm.COLUMN_MSG)));
			cv.put(Alarm.COLUMN_PICTURE,
					c.getString(c.getColumnIndexOrThrow(Alarm.COLUMN_PICTURE)));
			cv.put(Alarm.COLUMN_NAME,
					c.getString(c.getColumnIndexOrThrow(Alarm.COLUMN_NAME)));

			a.setValues(cv);

			myAlarms.add(a);
			c.move(1);
		}
		c.close();

	}

	public static void getMyAlarmsFromServer() {

		myAlarms.clear();

		ParseQuery<Alarm> query = ParseQuery.getQuery("Alarm");
		query.whereEqualTo(Alarm.COLUMN_USER,
				ApplicationSettings.getUserEmail());
		query.orderByAscending(Alarm.COLUMN_TIME);

		query.findInBackground(new FindCallback<Alarm>() {
			public void done(List<Alarm> list, ParseException e) {
				if (e == null) {
					MyAlarmManager.putMyAlarmsToDB(list);
				} else {
				}
			}
		});
		
		ParseQuery<AlarmInstance> query2 = ParseQuery.getQuery("AlarmInstance");
		query2.whereEqualTo(Alarm.COLUMN_USER,
				ApplicationSettings.getUserEmail());
		query2.orderByAscending(Alarm.COLUMN_TIME);

		query2.findInBackground(new FindCallback<AlarmInstance>() {
			public void done(List<AlarmInstance> list, ParseException e) {
				if (e == null) {
					MyAlarmManager.setMyAlarmsOnDevice(list);
				} else {
				}
			}
		});
	}

	public static void setMyAlarmsOnDevice(List<AlarmInstance> list){
		
		for(AlarmInstance alarm : list){
			AlarmManager alarmMgr = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			ComponentName receiver = new ComponentName(context,
					AlarmBroadcastReceiver.class);
			PackageManager pm = context.getPackageManager();
			pm.setComponentEnabledSetting(receiver,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);

			GregorianCalendar now = new GregorianCalendar();
			GregorianCalendar cn = new GregorianCalendar();
			cn.setTimeInMillis(alarm.getTimeInMillis());

			if (cn.getTimeInMillis() < now.getTimeInMillis()) {
				alarm.deleteInBackground(new DeleteCallback(){

					@Override
					public void done(ParseException e) {
						Log.i("WakeMeApp", "deleted");
					}
				});
			}else{
				
				ContentValues cv = new ContentValues();
				cv.put(AlarmInstance.COLUMN_OBJECT_ID, alarm.getObjectId());
				cv.put(AlarmInstance.COLUMN_ID, alarm.getInt(AlarmInstance.COLUMN_ID));
				cv.put(AlarmInstance.COLUMN_MSG,
						alarm.getString(AlarmInstance.COLUMN_MSG));
				cv.put(AlarmInstance.COLUMN_NAME,
						alarm.getString(AlarmInstance.COLUMN_NAME));
				cv.put(AlarmInstance.COLUMN_TIME,
						alarm.getLong(AlarmInstance.COLUMN_TIME));

				alarm.setValues(cv);
				
				db.insertWithOnConflict(AlarmInstance.MY_ALARMINSTANCE_TABLE_NAME,
						null, alarm.getValues(), SQLiteDatabase.CONFLICT_REPLACE);

				Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
				intent.putExtra("id", alarm.getID());
				PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
						alarm.getID(), intent, 0);
				alarmMgr.set(AlarmManager.RTC_WAKEUP, cn.getTimeInMillis(), alarmIntent);
				pendingIntents.put(alarm.getID(), alarmIntent);
			}
		}
		
	}
	
	
	public static Context getContext() {
		return context;
	}

	public static DBHelper getDBHelper() {
		return dbHelper;
	}

	public static void removeDataBase() {
		db.delete(Alarm.TABLE_NAME, null, null);
		db.delete(AlarmInstance.PARTNER_TABLE_NAME, null, null);
		db.delete(AlarmInstance.MY_ALARMINSTANCE_TABLE_NAME, null, null);
	}

	public static void setContext(Context c) {
		context = c;
		dbHelper = new DBHelper(context);
		db = dbHelper.getReadableDatabase();
	}

	/*
	 * TODO dont sent notifications?
	 */
	public static void editAlarm(Context context, Alarm alarm) {
		
		GregorianCalendar now = new GregorianCalendar();
		
		MyAlarmManager.deleteAlarmInstances(alarm);
		alarm.saveInBackground(new SaveCallback(){
			@Override
			public void done(ParseException e) {
				Log.i("WakeMeApp", "edited alarm saved");
			}
		});

		long timeMillis = now.getTimeInMillis();

		boolean[] weekdaysR = alarm.getWeekdaysRepeated();

		int counter = 0;
		for (int i = 0; i < weekdaysR.length; i++) {
			if (weekdaysR[i]) {
				timeMillis = addAlarmInstance(context, alarm, Calendar.SUNDAY
						+ (i + 1) % 7);
				counter++;
			}
		}

		if (counter == 0) {
			timeMillis = addAlarmInstance(context, alarm,
					now.get(Calendar.DAY_OF_WEEK));
			counter++;
		}

		if (counter != 0 && alarm.isVisible()) {
			ParsePush push = new ParsePush();
			push.setChannel(AccountManager.getSendingChannel());
			push.setMessage("Hey, I just set a new Alarm!");
			push.setExpirationTime(timeMillis);
			push.sendInBackground();
		}
		
	}

	private static void deleteAlarmInstances(Alarm alarm){
		AlarmManager alarmMgr = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		ComponentName receiver = new ComponentName(context,
				AlarmBroadcastReceiver.class);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(receiver,
				PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				PackageManager.DONT_KILL_APP);
		
		int alarmID = alarm.getAlarmId();
		
		for (int i = 0; i < 7; i++) {
			db.delete(AlarmInstance.MY_ALARMINSTANCE_TABLE_NAME, AlarmInstance.COLUMN_ID + "=" + alarmID + i,
					null);
			PendingIntent p = pendingIntents.get(alarm.getAlarmId());
			if (p != null)
				alarmMgr.cancel(p);
			pendingIntents.remove(alarm.getAlarmId() + i);
		}

		ParseQuery<AlarmInstance> query = ParseQuery.getQuery("AlarmInstance");
		String email = ApplicationSettings.getUserEmail();
		query.whereGreaterThanOrEqualTo(AlarmInstance.COLUMN_ID,
				alarmID);
		query.whereLessThanOrEqualTo(AlarmInstance.COLUMN_ID,
				alarmID + 9);
		query.whereEqualTo(AlarmInstance.COLUMN_USER, email);
		query.findInBackground(new FindCallback<AlarmInstance>() {
			public void done(List<AlarmInstance> list, ParseException e) {
				if (e == null) {
					for (AlarmInstance ai : list) {
						ai.deleteEventually();
					}
				} else {
					e.printStackTrace();
				}
			}
		});
	}
	public static void setNewAlarm(Context context, Alarm alarm) {

		GregorianCalendar now = new GregorianCalendar();
		long timeMillis = now.getTimeInMillis();

		boolean[] weekdaysR = alarm.getWeekdaysRepeated();

		int counter = 0;
		for (int i = 0; i < weekdaysR.length; i++) {
			if (weekdaysR[i]) {
				timeMillis = addAlarmInstance(context, alarm, Calendar.SUNDAY
						+ (i + 1) % 7);
				counter++;
			}
		}

		if (counter == 0) {
			timeMillis = addAlarmInstance(context, alarm,
					now.get(Calendar.DAY_OF_WEEK));
			counter++;
		}

		if (counter != 0 && alarm.isVisible()) {
			ParsePush push = new ParsePush();
			push.setChannel(AccountManager.getSendingChannel());
			push.setMessage("Hey, I just set a new Alarm!");
			push.setExpirationTime(timeMillis);
			push.sendInBackground();
		}

		db.insertWithOnConflict(Alarm.TABLE_NAME, "null", alarm.getValues(),
				SQLiteDatabase.CONFLICT_REPLACE);
		myAlarms.add(alarm);

		// TODO make sure that really saved in background -> check

		alarm.saveInBackground();
	}

	public static void updateAlarmInstance(AlarmInstance alarm) {
		ContentValues cv = new ContentValues();

		ParseFile pic = alarm.getParseFile(AlarmInstance.COLUMN_PICTURE);
		try {
			byte[] bytes = pic.getData();
			cv.put(AlarmInstance.COLUMN_PICTURE, bytes);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cv.put(AlarmInstance.COLUMN_OBJECT_ID, alarm.getObjectId());
		cv.put(AlarmInstance.COLUMN_ID, alarm.getInt(AlarmInstance.COLUMN_ID));
		cv.put(AlarmInstance.COLUMN_MSG,
				alarm.getString(AlarmInstance.COLUMN_MSG));
		cv.put(AlarmInstance.COLUMN_NAME,
				alarm.getString(AlarmInstance.COLUMN_NAME));
		cv.put(AlarmInstance.COLUMN_TIME,
				alarm.getLong(AlarmInstance.COLUMN_TIME));

		alarm.setValues(cv);
		db.update(AlarmInstance.MY_ALARMINSTANCE_TABLE_NAME, cv,
				AlarmInstance.COLUMN_OBJECT_ID + "=" + alarm.getObjectId(),
				null);
	}
}
