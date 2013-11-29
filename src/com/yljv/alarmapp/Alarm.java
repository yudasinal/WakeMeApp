package com.yljv.alarmapp;

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
