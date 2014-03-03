package com.yljv.alarmapp.parse.database;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yljv.alarmapp.helper.AccountManager;
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

	private final static String mFile = "alarmsFile";

	private static long addAlarm(Context context, Alarm alarm, int day) {
	
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
		intent.putExtra("id", alarm.getAlarmId());
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context,
				alarm.getAlarmId(), intent, 0);
		alarmMgr.set(AlarmManager.RTC_WAKEUP, cn.getTimeInMillis(), alarmIntent);
	
		return cn.getTimeInMillis();
	
	}

	public static void addAlarmRepeat(Context context, Alarm alarm) {
		
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
	}

	public static void addTextToPartnerAlarm(final Alarm alarm, String text) {
		alarm.put("message", text);
	}

	public void cancelAlarm(Alarm alarm) {
		alarmManager.cancel(pendingIntents.get(alarm.getAlarmId()));
		pendingIntents.remove(alarm.getAlarmId());
	}

	public static void deleteAlarm(Alarm alarm) {
		//TODO remove alarms from server (with same objectid)
		//TODO remove alarms from local database
		//TODO cancelAlarm(alarm)
	}

	//TODO create Items for each day alarm gets repeated
	public static void findAllPartnerAlarms(
			final ParsePartnerAlarmListener listener) {
		ParseUser partner = AccountManager.getPartner();
	
		ParseQuery<Alarm> query = ParseQuery.getQuery("Alarm");
		query.whereEqualTo("user", partner);
		query.orderByAscending("time");
		query.findInBackground(new FindCallback<Alarm>() {
			public void done(List<Alarm> list, ParseException e) {
				if (e == null) {
					// query worked
					listener.partnerAlarmsFound(list);
				} else {
					// query failed
					listener.partnerAlarmsSearchFailed(e);
				}
			}
		});
	}

	// TODO: check for owner
	//not necessary anymore!
	// should do work in background
	/*
	 * shoudn't get alarms from server, they should be saved on the phone
	 */
	public static ArrayList<Alarm> getAllAlarms() {
		ParseQuery<Alarm> query = ParseQuery.getQuery("Alarm");
		// commented out, until logged in
		query.whereEqualTo("user", ParseUser.getCurrentUser());
		query.orderByAscending("time");
		try {
			myAlarms = (ArrayList<Alarm>) query.find();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return myAlarms;
	}

	public static ArrayList<String> getAllAlarmString() {
		ArrayList<String> alarm = new ArrayList<String>();
		alarm.add("hello");
		alarm.add("bye");
		alarm.add("goodbye");
		alarm.add("good");
		alarm.add("bad");
		/*
		 * ArrayList<Alarm> list = new ArrayList<Alarm>(); list.add(new
		 * Alarm("name1", 12, 4)); list.add(new Alarm("name2", 45, 1));
		 * list.add(new Alarm("name3", 45, 1)); list.add(new Alarm("name4", 45,
		 * 1)); list.add(new Alarm("name5", 45, 1));
		 */
		// MyAlarmManager.retrieveAllMyAlarms();
		// return myAlarms;
		return alarm;
	}

	private static void retrieveAllMyAlarms() {
		ParseQuery<Alarm> query = ParseQuery.getQuery("Alarm");
		query.orderByAscending("time");
		query.findInBackground(new FindCallback<Alarm>() {
			public void done(List<Alarm> list, ParseException e) {
				if (e == null) {
					// query worked
					Iterator<Alarm> it = list.iterator();
					while (it.hasNext()) {
						myAlarms.add(it.next());
					}
				} else {
					// query failed
					// TODO what happens when Alarm retrieval failed
				}
			}
		});
	}

	public static void setNewAlarm(Context context, Alarm alarm) {

		long timeMillis = (new GregorianCalendar()).getTimeInMillis();

		boolean[] weekdaysR = alarm.getWeekdaysRepeated();

		int counter = 0;
		for (int i = 0; i < weekdaysR.length; i++) {
			if (weekdaysR[i]) {
				timeMillis = addAlarm(context, alarm, i);
				counter++;
			}
		}

		if (counter == 0) {
			timeMillis = addAlarm(context, alarm, 0);
			counter++;
		}
		

		if(counter!= 0 && alarm.isVisible()){
			alarm.saveEventually();
			ParsePush push = new ParsePush();
			push.setChannel(AccountManager.getUserChannel());
			push.setMessage("Hey, I just set a new Alarm!");
			push.setExpirationTime(timeMillis);
			push.sendInBackground();
		}
		//TODO addAlarmToFile(alarm);
	}

	/*
	 * private static void addAlarmToFile(Alarm alarm){ FileOutputStream
	 * outStream;
	 * 
	 * try{ outStream = mContext.openFileOutput(mFile, Context.MODE_APPEND |
	 * Context.MODE_PRIVATE); outStream.write(); } }
	 */

}
