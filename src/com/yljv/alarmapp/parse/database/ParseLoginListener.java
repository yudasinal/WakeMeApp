package com.yljv.alarmapp.parse.database;

import com.parse.ParseException;

public interface ParseLoginListener {
	
	public void onLoginSuccessful();
	public void onLoginFail(ParseException e);

}
