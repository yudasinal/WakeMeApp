package com.yljv.alarmapp.server.user;

import com.parse.ParseException;

public interface ParseRegisterListener {

	/*
	 * gets called after Registration has been attempted
	 * use these methods where you are waiting for the registration to complete
	 * 
	 * onRegisterSuccess: User has been successfully added to the database
	 * onRegisterFail(e): Registration has failed, more information in e
	 */
	public void onRegisterSuccess();
	public void onRegisterFail(ParseException e);
}


