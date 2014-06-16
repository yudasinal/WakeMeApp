package com.yljv.alarmapp.server.alarm;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
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
import android.os.Environment;
import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.yljv.alarmapp.client.helper.AlarmReceiver;
import com.yljv.alarmapp.client.helper.ApplicationSettings;
import com.yljv.alarmapp.client.helper.DBHelper;
import com.yljv.alarmapp.client.ui.alarm.ClockAdapter;
import com.yljv.alarmapp.client.ui.alarm.PartnerClockAdapter;
import com.yljv.alarmapp.server.user.AccountManager;

/*
 * THIS IS THE RIGHT ALARM MANAGER
 */

public class MyAlarmManager {

	private static HashMap<Integer, PendingIntent> pendingIntents = new HashMap<Integer, PendingIntent>();

	public static ArrayList<Alarm> myAlarms = new ArrayList<Alarm>();
	public static ArrayList<AlarmInstance> partnerAlarms = new ArrayList<AlarmInstance>();

	private static Context context;
	private static DBHelper dbHelper;
	private static SQLiteDatabase db;

	private static PartnerClockAdapter pcAdapter;
	private static ClockAdapter adapter;

	public static MsgPictureTuple findPicMsgByAlarmId(int id) {

		String selection[] = { AlarmInstance.COLUMN_MSG,
				AlarmInstance.COLUMN_PICTURE, AlarmInstance.COLUMN_ID };
		Cursor c = db.query(AlarmInstance.MY_ALARMINSTANCE_TABLE_NAME,
				selection, AlarmInstance.COLUMN_ID + "=" + id, null, null,
				null, null);
		c.moveToFirst();
		int size = c.getCount();

		String msg = c.getString(c
				.getColumnIndexOrThrow(AlarmInstance.COLUMN_MSG));
		byte[] data = c.getBlob(c
				.getColumnIndexOrThrow(AlarmInstance.COLUMN_PICTURE));

		c.close();

		return new MsgPictureTuple(msg, data);
	}

	public static PartnerClockAdapter getPartnerClockAdapter(Context context) {
		if (pcAdapter == null) {
			pcAdapter = new PartnerClockAdapter(context, partnerAlarms);
		}
		return pcAdapter;
	}

	public static ClockAdapter getClockAdapter(Context context) {
		if (adapter == null) {
			adapter = new ClockAdapter(context, myAlarms);
		}
		return adapter;
	}

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
	private static long addAlarmInstance(final Context context, Alarm alarm,
			int day) {

		GregorianCalendar now = (GregorianCalendar) GregorianCalendar
				.getInstance();
		final GregorianCalendar cn = (GregorianCalendar) GregorianCalendar
				.getInstance();
		cn.set(Calendar.SECOND, 0);
		cn.set(Calendar.MINUTE, alarm.getMinute());
		cn.set(Calendar.HOUR_OF_DAY, alarm.getHourOfDay());
		cn.set(Calendar.DAY_OF_WEEK, day);
		if (cn.getTimeInMillis() < now.getTimeInMillis()) {
			cn.set(Calendar.WEEK_OF_YEAR, now.get(Calendar.WEEK_OF_YEAR) + 1);
		}
		if (cn.getTimeInMillis() < now.getTimeInMillis()) {
			cn.set(Calendar.YEAR, now.get(Calendar.YEAR) + 1);
		}

		final AlarmInstance ai = new AlarmInstance();
		ai.initialize();
		ai.setID(alarm.getAlarmId() + day);
		ai.setName(alarm.getName());
		ai.setTime(cn);
		ai.setMusicPath(alarm.getMusicURIPath());
		ai.setUser();
		ai.setVisibility(alarm.isVisible());

		ai.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				AlarmManager alarmMgr = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);
				ComponentName receiver = new ComponentName(context,
						AlarmReceiver.class);
				PackageManager pm = context.getPackageManager();
				pm.setComponentEnabledSetting(receiver,
						PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
						PackageManager.DONT_KILL_APP);

				Intent intent = new Intent(context, AlarmReceiver.class);
				intent.putExtra(AlarmInstance.COLUMN_ID, ai.getID());
				PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
						ai.getID(), intent, 0);
				alarmMgr.set(AlarmManager.RTC_WAKEUP, cn.getTimeInMillis(),
						alarmIntent);
				pendingIntents.put(ai.getID(), alarmIntent);

				db.insertWithOnConflict(
						AlarmInstance.MY_ALARMINSTANCE_TABLE_NAME, null,
						ai.getValues(), SQLiteDatabase.CONFLICT_REPLACE);

				if (ai.isVisible()) {
					if (AccountManager.hasPartner()) {
						MyAlarmManager.sendUpdateAlarmNotification(ai.getObjectId(), ai.getTimeInMillis());
					}

				}
			}

		});

		return cn.getTimeInMillis();
	}

    private static void sendUpdateAlarmNotification(String objectId, long expirationTime){
        ParsePush push = new ParsePush();
        push.setChannel(AccountManager.getSendingChannel());

        JSONObject json;
        try {
            json = new JSONObject(

                    "{action: \"com.yljv.alarmapp.UPDATE_ALARM\","
                            + "\"id\": " + "\"" + objectId
                            + "\", " + "\"category\": " + "\"update\""
                            + "}");

            push.setData(json);
            push.setExpirationTime(expirationTime);
            push.sendInBackground();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    private static void sendNewPictureNotification(String objectId, long expirationTime ){
        ParsePush push = new ParsePush();
        push.setChannel(AccountManager
                .getSendingChannel());
        try {
            JSONObject json = new JSONObject(

                    "{action: \"com.yljv.alarmapp.UPDATE_ALARM\","
                            + "\"id\": " + "\""
                            + objectId
                            + "\", "
                            + "\"category\": "
                            + "\"picture\"" + "}");

            push.setData(json);
            push.setExpirationTime(expirationTime);
            push.sendInBackground();
        } catch (Exception pe) {
            pe.printStackTrace();
        }
    }

	public static void addPictureOrMessageToPartnerAlarm(
			final AlarmInstance alarm, String picturePath, String message) {

		alarm.setPictureSent(false);
		pcAdapter.notifyDataSetChanged();

		if (message != null) {
			alarm.put(AlarmInstance.COLUMN_MSG, message);
		}

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
										db.insertWithOnConflict(
												AlarmInstance.PARTNER_TABLE_NAME,
												null, alarm.getValues(),
												SQLiteDatabase.CONFLICT_REPLACE);
										if (AccountManager.hasPartner()) {
											MyAlarmManager.sendNewPictureNotification(alarm.getObjectId(), alarm.getTimeInMillis());
                                            alarm.setPictureSent(true);
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
		} else if (message != null) {
			alarm.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException e) {
					db.insertWithOnConflict(AlarmInstance.PARTNER_TABLE_NAME,
							null, alarm.getValues(),
							SQLiteDatabase.CONFLICT_REPLACE);
					if (AccountManager.hasPartner()) {
						ParsePush push = new ParsePush();
						push.setChannel(AccountManager.getSendingChannel());
						try {
							MyAlarmManager.sendNewPictureNotification(alarm.getObjectId(), alarm.getTimeInMillis());
                            alarm.setPictureSent(true);
						} catch (Exception pe) {
							pe.printStackTrace();
						}
					}
				}
			});
		}

	}

    private static void deleteAlarmFromDatabase(Alarm alarm){

    }

    private static void deleteAlarmInstances(int alarmId) {
        AlarmManager alarmMgr = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        ComponentName receiver = new ComponentName(context, AlarmReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        for (int i = 0; i <= 7; i++) {
            db.delete(AlarmInstance.MY_ALARMINSTANCE_TABLE_NAME,
                    AlarmInstance.COLUMN_ID + "=" + alarmId + i, null);
            PendingIntent p = pendingIntents.get(alarmId);
            if (p != null)
                alarmMgr.cancel(p);
            pendingIntents.remove(alarmId + i);
        }

        ParseQuery<AlarmInstance> query = ParseQuery.getQuery("AlarmInstance");
        String email = ApplicationSettings.getUserEmail();
        query.whereGreaterThanOrEqualTo(AlarmInstance.COLUMN_ID, alarmId);
        query.whereLessThanOrEqualTo(AlarmInstance.COLUMN_ID, alarmId + 9);
        query.whereEqualTo(AlarmInstance.COLUMN_USER, email);
        query.findInBackground(new FindCallback<AlarmInstance>() {
            public void done(List<AlarmInstance> list, ParseException e) {
                if (e == null) {
                    for (AlarmInstance ai : list) {
                        ai.deleteEventually();

                        if (AccountManager.hasPartner()) {
                            MyAlarmManager.sendAlarmDeletedNotification(ai.getObjectId());
                        }
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void deleteAlarm(Alarm alarm) {

		myAlarms.remove(alarm);
        adapter.notifyDataSetChanged();

		db.delete(Alarm.TABLE_NAME, Alarm.COLUMN_ID + "=" + alarm.getAlarmId(),
				null);

		MyAlarmManager.deleteAlarmInstances(alarm.getAlarmId());

		alarm.deleteEventually();
	}

	public static void deletePartnerAlarmInstance(String objectId) {

		db.delete(AlarmInstance.PARTNER_TABLE_NAME,
				AlarmInstance.COLUMN_OBJECT_ID + "=" + "'" + objectId + "'", null);

		for (AlarmInstance alarm : partnerAlarms) {
			if (alarm.getObjectId().equals(objectId)) {
				partnerAlarms.remove(alarm);
			}
		}
		pcAdapter.notifyDataSetChanged();
	}

	public static Alarm findAlarmById(int id) {
		for (Alarm alarm : myAlarms) {
			int alarmID = alarm.getAlarmId();
			if (alarmID == id) {
				return alarm;
			}
		}
		return null;
	}

	public static AlarmInstance findPartnerAlarmByObjectId(String id) {
		for (AlarmInstance alarm : partnerAlarms) {
			if (alarm.getObjectId().equals(id)) {
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
				AlarmInstance.COLUMN_OBJECT_ID, AlarmInstance.COLUMN_VISIBLE };

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
			cv.put(AlarmInstance.COLUMN_VISIBLE,
					c.getInt(c.getColumnIndex(AlarmInstance.COLUMN_VISIBLE)) != 0);

			a.setValues(cv);

			if (MyAlarmManager.findPartnerAlarmByObjectId(a.getObjectId()) == null) {
				partnerAlarms.add(a);
			}
			c.move(1);
		}
		c.close();

		pcAdapter.notifyDataSetChanged();
	}

	public static void getPartnerAlarmsFromServer() {

		partnerAlarms.clear();

		ParseQuery<AlarmInstance> query = ParseQuery.getQuery("AlarmInstance");
		query.whereEqualTo(AlarmInstance.COLUMN_USER,
				ApplicationSettings.getPartnerEmail());
		query.orderByAscending(AlarmInstance.COLUMN_TIME);

		query.findInBackground(new FindCallback<AlarmInstance>() {
            public void done(List<AlarmInstance> list, ParseException e) {
                if (e == null) {
                    MyAlarmManager.putPartnerAlarmsToDB(list);
                } else {
                    e.printStackTrace();
                }
            }
        });

	}

	private static void putPartnerAlarmsToDB(List<AlarmInstance> list) {

		for (AlarmInstance ai : list) {
			if (ai.getBoolean(AlarmInstance.COLUMN_VISIBLE)) {

				ContentValues cv = new ContentValues();
				cv.put(AlarmInstance.COLUMN_OBJECT_ID, ai.getObjectId());
				cv.put(AlarmInstance.COLUMN_ID,
						ai.getInt(AlarmInstance.COLUMN_ID));
				cv.put(AlarmInstance.COLUMN_MSG,
						ai.getString(AlarmInstance.COLUMN_MSG));
				cv.put(AlarmInstance.COLUMN_NAME,
						ai.getString(AlarmInstance.COLUMN_NAME));
				cv.put(AlarmInstance.COLUMN_PICTURE,
						ai.getString(AlarmInstance.COLUMN_PICTURE));
				cv.put(AlarmInstance.COLUMN_TIME,
						ai.getLong(AlarmInstance.COLUMN_TIME));
				cv.put(AlarmInstance.COLUMN_VISIBLE,
						ai.getBoolean(AlarmInstance.COLUMN_VISIBLE));

				ai.setValues(cv);
				db.insertWithOnConflict(AlarmInstance.PARTNER_TABLE_NAME, null,
						cv, SQLiteDatabase.CONFLICT_REPLACE);

				if (MyAlarmManager.findPartnerAlarmByObjectId(ai.getObjectId()) == null) {
					partnerAlarms.add(ai);
				}
			}
		}
		pcAdapter.notifyDataSetChanged();

	}

	private static void putMyAlarmsToDB(List<Alarm> list) {

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

			String[] projection = { Alarm.COLUMN_NAME, Alarm.COLUMN_ID,
					Alarm.COLUMN_TIME, Alarm.COLUMN_ACTIVATED,
					Alarm.COLUMN_WEEKDAYS, Alarm.COLUMN_VISIBILITY,
					Alarm.COLUMN_MUSIC_URI, Alarm.COLUMN_VOLUME,
					Alarm.COLUMN_MSG, Alarm.COLUMN_PICTURE,
					Alarm.COLUMN_OBJECT_ID };

			String sortOrder = Alarm.COLUMN_TIME;

			Cursor c = db.query(Alarm.TABLE_NAME, projection, null, null, null,
                    null, sortOrder);
			c.moveToFirst();
			c.close();
			myAlarms.add(alarm);
		}
		adapter.notifyDataSetChanged();
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
		Cursor c = db.query(Alarm.TABLE_NAME, projection, null, null, null,
				null, sortOrder);
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
		adapter.notifyDataSetChanged();

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

	public static void setMyAlarmsOnDevice(List<AlarmInstance> list) {

		for (AlarmInstance alarm : list) {
			AlarmManager alarmMgr = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			ComponentName receiver = new ComponentName(context,
					AlarmReceiver.class);
			PackageManager pm = context.getPackageManager();
			pm.setComponentEnabledSetting(receiver,
					PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
					PackageManager.DONT_KILL_APP);

			GregorianCalendar now = (GregorianCalendar) GregorianCalendar
					.getInstance();
			GregorianCalendar cn = (GregorianCalendar) GregorianCalendar
					.getInstance();
			cn.setTimeInMillis(alarm.getTimeInMillis());

			if (cn.getTimeInMillis() < now.getTimeInMillis()) {
				alarm.deleteInBackground(new DeleteCallback() {

					@Override
					public void done(ParseException e) {
						Log.i("WakeMeApp", "deleted");
					}
				});
			} else {

				ContentValues cv = new ContentValues();
				cv.put(AlarmInstance.COLUMN_OBJECT_ID, alarm.getObjectId());
				cv.put(AlarmInstance.COLUMN_ID,
						alarm.getInt(AlarmInstance.COLUMN_ID));
				cv.put(AlarmInstance.COLUMN_MSG,
						alarm.getString(AlarmInstance.COLUMN_MSG));
				cv.put(AlarmInstance.COLUMN_NAME,
						alarm.getString(AlarmInstance.COLUMN_NAME));
				cv.put(AlarmInstance.COLUMN_TIME,
						alarm.getLong(AlarmInstance.COLUMN_TIME));

				alarm.setValues(cv);

				db.insertWithOnConflict(
						AlarmInstance.MY_ALARMINSTANCE_TABLE_NAME, null,
						alarm.getValues(), SQLiteDatabase.CONFLICT_REPLACE);

				Intent intent = new Intent(context, AlarmReceiver.class);
				intent.putExtra("id", alarm.getID());
				PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
						alarm.getID(), intent, 0);
				alarmMgr.set(AlarmManager.RTC_WAKEUP, cn.getTimeInMillis(),
						alarmIntent);
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

		GregorianCalendar now = (GregorianCalendar) GregorianCalendar
				.getInstance();

		MyAlarmManager.deleteAlarmInstances(alarm.getAlarmId());
		alarm.saveInBackground(new SaveCallback() {
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

		if (counter != 0 && alarm.isVisible() && AccountManager.hasPartner()) {
			MyAlarmManager.sendNewAlarmNotification(timeMillis);
		}

	}


    private static void sendAlarmDeletedNotification(String objectId){
        try {
            JSONObject json = new JSONObject(

                    "{action: \"com.yljv.alarmapp.UPDATE_ALARM\","
                            + "\"id\": " + "\"" + objectId
                            + "\", " + "\"category\": "
                            + "\"delete\"" + "}");

            ParsePush push = new ParsePush();
            push.setChannel(AccountManager
                    .getSendingChannel());
            push.setData(json);
            push.sendInBackground();
        } catch (Exception pe) {
            pe.printStackTrace();
        }
    }

	public static void setNewAlarm(Context context, Alarm alarm) {

		myAlarms.add(alarm);
		adapter.notifyDataSetChanged();

		GregorianCalendar now = (GregorianCalendar) GregorianCalendar
				.getInstance();
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

		if (counter != 0 && alarm.isVisible() && AccountManager.hasPartner()) {
			MyAlarmManager.sendNewAlarmNotification(timeMillis);
		}

		insertAlarmToDatabase(alarm);
	}

    private static void insertAlarmToDatabase(Alarm alarm){
        db.insertWithOnConflict(Alarm.TABLE_NAME, "null", alarm.getValues(),
                SQLiteDatabase.CONFLICT_REPLACE);
    }

	public static void onPartnerAlarmInstanceUpdated(AlarmInstance alarm) {
		List<AlarmInstance> list = new ArrayList<AlarmInstance>();
		list.add(alarm);
		MyAlarmManager.putPartnerAlarmsToDB(list);
	}

	public static void onMyAlarmInstanceUpdated(Context context,
			AlarmInstance alarm) {
		ContentValues cv = new ContentValues();

		ParseFile pic = alarm.getParseFile(AlarmInstance.COLUMN_PICTURE);

		byte[] bytes;

		if (pic != null) {
			try {
				bytes = pic.getData();
				cv.put(AlarmInstance.COLUMN_PICTURE, bytes);

				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
						.format(new Date());
				String imageFileName = "JPEG_" + timeStamp + "_";
				File storageDir = Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				File image = File.createTempFile(imageFileName, /* prefix */
						".jpg", /* suffix */
						storageDir /* directory */
				);

				// Save a file: path for use with ACTION_VIEW intents
				String mCurrentPhotoPath = "file:" + image.getAbsolutePath();

				FileOutputStream fos = new FileOutputStream(image.getPath());
				fos.write(bytes);
				fos.close();
			} catch (ParseException e) {
				Log.e("WakeMeApp", "Exception", e);
			} catch (IOException e) {
				Log.e("WakeMeApp", "Exception", e);
			}
		}

		cv.put(AlarmInstance.COLUMN_OBJECT_ID, alarm.getObjectId());
		cv.put(AlarmInstance.COLUMN_ID, alarm.getInt(AlarmInstance.COLUMN_ID));

		String msg = alarm.getString(AlarmInstance.COLUMN_MSG);
		if (msg != null) {
			cv.put(AlarmInstance.COLUMN_MSG,
					alarm.getString(AlarmInstance.COLUMN_MSG));
		}
		cv.put(AlarmInstance.COLUMN_NAME,
				alarm.getString(AlarmInstance.COLUMN_NAME));
		cv.put(AlarmInstance.COLUMN_TIME,
				alarm.getLong(AlarmInstance.COLUMN_TIME));

		alarm.setValues(cv);
		db.insertWithOnConflict(AlarmInstance.MY_ALARMINSTANCE_TABLE_NAME,
				null, cv, SQLiteDatabase.CONFLICT_REPLACE);
	}

	private static void setMyAlarmInstanceVisible(AlarmInstance ai) {
		if (AccountManager.hasPartner()) {
            MyAlarmManager.sendUpdateAlarmNotification(ai.getObjectId(), ai.getTimeInMillis());
		}
	}

	private static void setMyAlarmInstanceInvisible(AlarmInstance ai) {
		if (AccountManager.hasPartner()) {
            MyAlarmManager.sendAlarmDeletedNotification(ai.getObjectId());
		}
	}
    
	public static void setAlarmActivate(final Alarm alarm) {
		ContentValues cv = alarm.getValues();

		db.update(Alarm.TABLE_NAME, cv,
				Alarm.COLUMN_ID + "=" + alarm.getAlarmId(), null);

		if (alarm.isActivated()) {

			GregorianCalendar now = (GregorianCalendar) GregorianCalendar
					.getInstance();
			long timeMillis = now.getTimeInMillis();

			boolean[] weekdaysR = alarm.getWeekdaysRepeated();

			int counter = 0;
			for (int i = 0; i < weekdaysR.length; i++) {
				if (weekdaysR[i]) {
					timeMillis = addAlarmInstance(context, alarm,
							Calendar.SUNDAY + (i + 1) % 7);
					counter++;
				}
			}

			if (counter == 0) {
				timeMillis = addAlarmInstance(context, alarm,
						now.get(Calendar.DAY_OF_WEEK));
				counter++;
			}

			if (counter != 0 && alarm.isVisible()
					&& AccountManager.hasPartner()) {
				MyAlarmManager.sendNewAlarmNotification(timeMillis);
			}

			adapter.notifyDataSetChanged();

		} else {
			int alarmID = alarm.getAlarmId();
			MyAlarmManager.deleteAlarmInstances(alarmID);
		}
	}

    private static void sendNewAlarmNotification(long expirationTime){
        ParsePush push = new ParsePush();
        push.setChannel(AccountManager.getSendingChannel());
        push.setMessage("Hey, I just set a new Alarm!");
        push.setExpirationTime(expirationTime);
        push.sendInBackground();
    }

    public static void setAlarmInstancesAfterBoot() {

        AlarmManager alarmMgr = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        ComponentName receiver = new ComponentName(context, AlarmReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        String[] projection = { AlarmInstance.COLUMN_TIME,
                AlarmInstance.COLUMN_ID };

        Cursor c = db.query(AlarmInstance.MY_ALARMINSTANCE_TABLE_NAME,
                projection, null, null, null, null, null);
        c.moveToFirst();
        int size = c.getCount();

        for (int i = 0; i < size; i++) {
            long time = c.getLong(c
                    .getColumnIndexOrThrow(AlarmInstance.COLUMN_TIME));
            int id = c.getInt(c.getColumnIndexOrThrow(AlarmInstance.COLUMN_ID));

            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(AlarmInstance.COLUMN_ID, id);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id,
                    intent, 0);
            alarmMgr.set(AlarmManager.RTC_WAKEUP, time, alarmIntent);
            pendingIntents.put(id, alarmIntent);

            c.move(1);
        }
        c.close();
    }


    public static void setNextAlarmInstance(final Alarm alarm) {
		boolean[] weekdays = alarm.getWeekdaysRepeated();

		boolean activated = false;

		for (int i = 0; i < weekdays.length; i++) {
			if (weekdays[i]) {
				activated = true;
				break;
			}
		}

		if (activated) {
			final GregorianCalendar now = (GregorianCalendar) Calendar
					.getInstance();

			ParseQuery<AlarmInstance> query = ParseQuery
					.getQuery("AlarmInstance");
			String email = ApplicationSettings.getUserEmail();
			query.whereEqualTo(
					AlarmInstance.COLUMN_ID,
					alarm.getInt(Alarm.COLUMN_ID)
							+ now.get(Calendar.DAY_OF_WEEK));
			query.whereEqualTo(AlarmInstance.COLUMN_USER, email);
			query.findInBackground(new FindCallback<AlarmInstance>() {
				public void done(List<AlarmInstance> list, ParseException e) {
					if (e == null) {
						for (AlarmInstance ai : list) {
							final String objectId = ai.getObjectId();
							ai.deleteInBackground(new DeleteCallback() {

								@Override
								public void done(ParseException e) {
									if (AccountManager.hasPartner()) {
                                        MyAlarmManager.sendAlarmDeletedNotification(objectId);
									}
									addAlarmInstance(context, alarm,
											now.get(Calendar.DAY_OF_WEEK));
								}

							});

						}
					} else {
						e.printStackTrace();
					}
				}
			});
		} else {
			alarm.setActivated(false);
		}
	}

}
