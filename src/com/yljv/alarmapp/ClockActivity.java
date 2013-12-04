package com.yljv.alarmapp;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/*
 * should contain all fragments:
 * my clock, his clock, settings, my clock details, settings, gallery
 */
public class ClockActivity extends Activity{

	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//TODO
		
	}
	
	public void setAlarm(int hour, int minute){
		AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		PendingIntent alarmIntent;
		
		Intent intent = new Intent(this, ClockActivity.class);
		alarmIntent = PendingIntent.getBroadcast(this,  0, intent, 0);
		
		//set alarm to hour:minute
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
	}
}
