package com.yljv.alarmapp.parse.database;

public interface ParseAlarmListener {
	
	public void onAlarmSaved();
	public void onAlarmSaveFailed(Exception e);

}
