package com.yljv.alarmapp.helper;

import android.content.SharedPreferences;

public class ApplicationSettings {

	public static String TIME_SCALE = "time_scale";
	public static String NOTIFICATION_ON = "notification_on";
	public static String VIBRATION_ON = "vibration_on";
	
	public int alarmIdCounter = 0;
	
	public static SharedPreferences preferences;
	
	public static int getAlarmId(){
		int id = preferences.getInt("counter", 0);
		preferences.edit().putInt("counter", id+1);
		return id+1;
	}
}