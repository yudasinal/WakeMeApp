package com.yljv.alarmapp.parse.database;

public class MyAlarmManager {

	public static void getAllAlarms(){
		
	}
	
	
	public static void setTime(Alarm alarm, int hour, int minute){
		alarm.put("hour", hour);
		alarm.put("minute", minute);
	}
	
	public void setName(Alarm alarm, String name){
		alarm.put("name", name);
	}
}
