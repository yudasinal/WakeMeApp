package com.yljv.alarmapp.parse.database;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class Alarm extends ParseObject{
	
	
	public Alarm(int hour, int minute, String name){
		super("Alarm");
		put("hour", hour);
		put("minute", minute);
		put("name", name);
		ParseUser user = AccountManager.getCurrentUser();
		put("user", user);
		this.saveEventually();
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
	
}
