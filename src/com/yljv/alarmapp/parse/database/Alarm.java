package com.yljv.alarmapp.parse.database;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.yljv.alarmapp.helper.AccountManager;
import com.yljv.alarmapp.helper.ApplicationSettings;

@ParseClassName("Alarm")
public class Alarm extends ParseObject{
	
	public Alarm(){
		super("Alarm");
	}
	
	public Alarm(String name, Date time){
		super("Alarm");
		//put("time", time);
		put("name", name);
		ParseUser user = AccountManager.getCurrentUser();
		put("user", user);
		put("id", ApplicationSettings.getAlarmId());
		put("time", time);
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
	
	public int getTime(){
		return 2;
	}
	
	public void saveAlarm(final ParseAlarmListener listener){
		this.saveEventually(new SaveCallback(){
			@Override
			public void done(ParseException e) {
				if(e==null){
					listener.onAlarmSaved();
				}else{
					listener.onAlarmSaveFailed(e);
				}
			}
		});
	}
	
	public int getAlarmId(){
		return (Integer) this.get("id");
	}
	
	
}
