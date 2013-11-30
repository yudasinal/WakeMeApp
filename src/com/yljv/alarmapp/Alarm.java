package com.yljv.alarmapp;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Alarm {

	int hour;
	int minute;
	String name;
	String id;
	
	public Alarm(String id, int hour, int minute, String name){
		this.id = id;
		this.hour = hour;
		this.minute = minute;
		this.name = name;
	}
	
	public void setTime(int hour, int minute){
		this.hour = hour;
		this.minute = minute;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public int getHour(){
		return hour;
	}
	
	public int getMinute(){
		return minute;
	}
	
	public String getId(){
		return id;
	}
}
