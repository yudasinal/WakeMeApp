package com.yljv.alarmapp.server.user;

import com.parse.ParseException;

public interface ParseLoginListener {
	
	public void onLoginSuccessful();
	public void onLoginFail(ParseException e);

}
