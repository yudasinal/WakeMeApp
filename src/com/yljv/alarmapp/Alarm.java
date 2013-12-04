package com.yljv.alarmapp;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class Alarm extends ParseObject{

	int hour;
	int minute;
	String name;
	int id;
	
	public Alarm(){
		id = MyAlarmManager.getNewId();
		this.saveAlarm();
	}
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
	
	public void saveAlarm(){
		this.saveEventually(new SaveCallback(){

			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public int getTime(){
		return 2;
	}
}
