package com.yljv.alarmapp;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Alarm extends ParseObject{
	
	
	public Alarm(int hour, int minute, String name){
		this.put("hour", hour);
		this.put("minute", minute);
		this.put("name", name);
		this.saveAlarm();
	}

	
	public void setTime(int hour, int minute){
		this.put("hour", hour);
		this.put("minute", minute);
	}
	
	public void setName(String name){
		this.put("name", name);
	}
	
	public String getName(){
		return (String) this.get("name");
	}
	
	public int getHour(){
		return (Integer) this.get("hour");
	}
	
	public int getMinute(){
		return (Integer) this.get("minute");
	}
	
	public int getId(){
		return (Integer) this.get("id");
	}
	
	public int getTime(){
		return 2;
	}
	
}
