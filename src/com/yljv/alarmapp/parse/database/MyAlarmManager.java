package com.yljv.alarmapp.parse.database;

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
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yljv.alarmapp.WakeUpActivity;
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
	
	private static int alarmNumber;
	private static AlarmManager alarmManager;
	private static HashMap<Integer, PendingIntent> pendingIntents = new HashMap<Integer,PendingIntent>();


	public static ArrayList<Alarm> myAlarms = new ArrayList<Alarm>();
	
	public static ArrayList<String> getAllAlarmString(){
		ArrayList<String> alarm = new ArrayList<String>();
		alarm.add("hello");
		alarm.add("bye");
		alarm.add("goodbye");
		alarm.add("good");
		alarm.add("bad");
		/*
		ArrayList<Alarm> list = new ArrayList<Alarm>();
		list.add(new Alarm("name1", 12, 4));
		list.add(new Alarm("name2", 45, 1));
		list.add(new Alarm("name3", 45, 1));
		list.add(new Alarm("name4", 45, 1));
		list.add(new Alarm("name5", 45, 1));
		*/
		//MyAlarmManager.retrieveAllMyAlarms();
		//return myAlarms;
		return alarm;
	}
	
	public static ArrayList<Alarm> getAllAlarms(){
		retrieveAllMyAlarms();
		return myAlarms;
	}
	

	public void setName(Alarm alarm, String name){
		alarm.put("name", name);
		alarm.saveEventually();
	}

	/*
	 * sets a NEW Alarm
	 * repeat does not work yet, if you need more alarms, call setAlarm multiple times
	 * please use constants of this class for the day-variable (SUNDAY-SATURDAY)
	 * example: MyAlarmManager.setAlarm(context, MyAlarmManager.SUNDAY, 13, 24, "Wake Up", false)
	 */
	public static void setAlarm(Context context, int day, int hour, int minute, String alarmName, boolean repeat){
		
		GregorianCalendar now = new GregorianCalendar();
		//set alarm to hour:minute
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(System.currentTimeMillis());
		cal.set(Calendar.HOUR_OF_DAY, hour);
		cal.set(Calendar.MINUTE, minute);
		
		//Create new Alarm on server
		Alarm alarm = new Alarm(alarmName, cal.getTime());
		alarm.saveEventually();
		AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);		
		
		//enable BootReceiver
		ComponentName receiver = new ComponentName(context, MyBootReceiver.class);
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
				
		Intent intent = new Intent(context, MyBootReceiver.class);
		Log.d("MyAlarmManager", Integer.toString(alarm.getAlarmId()));
		intent.putExtra("id", alarm.getAlarmId());
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context,  alarm.getAlarmId(), intent, 0);
		
		
		
		
		//TODO: what happens when day is today and time has passed already?
		
		alarmMgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmIntent);
	}
	
	public void cancelAlarm(Alarm alarm){
		alarmManager.cancel(pendingIntents.get(alarm.getAlarmId()));
		pendingIntents.remove(alarm.getAlarmId());
	}
	
	public static void deleteAlarm(){
		
	}


	public static void retrieveAllMyAlarms(){
		ParseQuery<Alarm> query = ParseQuery.getQuery("Alarm");
		query.orderByAscending("time");
		query.findInBackground(new FindCallback<Alarm>() {
		    public void done(List<Alarm> list, ParseException e) {
		        if (e == null) {
		        	//query worked
		        	Iterator<Alarm> it = list.iterator();
		        	while(it.hasNext()){
		        		myAlarms.add(it.next());
		        	}
		        } else {
		        	//query failed
		            //TODO what happens when Alarm retrievel failed
		        }
		    }
		});
	}
	
	public static ArrayList<Alarm> getAllMyAlarms(){
		return myAlarms;
	}
	
	public static void findAllPartnerAlarms(final ParsePartnerAlarmListener listener){
		ParseUser partner = AccountManager.getPartner();
		
		ParseQuery<Alarm> query = ParseQuery.getQuery("Alarm");
		query.whereEqualTo("user", partner);
		query.orderByAscending("time");
		query.findInBackground(new FindCallback<Alarm>() {
		    public void done(List<Alarm> list, ParseException e) {
		        if (e == null) {
		        	//query worked
		        	listener.partnerAlarmsFound(list);
		        } else {
		        	//query failed
		        	listener.partnerAlarmsSearchFailed(e);
		        }
		    }
		});
	}
	
	public static void addTextToAlarm(final Alarm alarm, String text){
		alarm.put("message", text);
	}

	
}
