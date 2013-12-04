package com.yljv.alarmapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.app.AlarmManager;
import android.content.Context;

public class MyAlarmManager {
	

	private static int alarmNumber;
	private static Calendar calendar;
	private static AlarmManager alarmManager;
	private static HashMap<Integer, PendingIntent> pendingIntents = new HashMap<Integer,PendingIntent>();

	
	public static ArrayList<Alarm> getAlarms(){
		ArrayList<Alarm> list = new ArrayList<Alarm>();
		return list;
	}
	
	
	public static int getNewId(){
		alarmNumber++;
		return alarmNumber;
	}
	
	public void setAlarm(Context context, Alarm alarm){
		AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);		
		
		Intent intent = new Intent(context, ClockActivity.class);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(context,  alarm.getId(), intent, 0);
		pendingIntents.put((Integer)alarm.getId(), alarmIntent);
		
		//set alarm to hour:minute
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
		calendar.set(Calendar.MINUTE, alarm.getMinute());
		
		alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
	}
	
	public void cancelAlarm(Integer id){
		alarmManager.cancel(pendingIntents.get(id));
		pendingIntents.remove(id);
	}
}
