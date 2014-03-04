package com.yljv.alarmapp.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationSettings {

	public final static String NOTIFICATION_ON_KEY = "notification_on";
	
	private final static String PARTNER_EMAIL_KEY = "parter_email";
	private final static String PARTNER_NAME_KEY = "partner_name";
	private final static String ALARM_ID_COUNTER_KEY = "counter";
	private final static String USER_EMAIL_KEY = "user";
	
	public int alarmIdCounter = 0;
	
	private static SharedPreferences preferences;
	
	

	public static void setSharedPreferences(Context context) {
		// TODO do this at beginning!!!
		preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
	}
	
	public static int getAlarmId(){
		int id = preferences.getInt(ALARM_ID_COUNTER_KEY, 0);
		preferences.edit().putInt(ALARM_ID_COUNTER_KEY, id+1).commit();		
		return id+1;
	}
	
	
	public static String getPartnerEmail(){

		return preferences.getString(PARTNER_EMAIL_KEY, "john@gmail.com");
	}

	public static String getPartnerName(){
		return preferences.getString(PARTNER_NAME_KEY, "Partner");
	}

	public static String getUserEmail(){

		return preferences.getString(USER_EMAIL_KEY, "jane@gmail.com");
	}

	public static boolean isNotificationActivated(){
		return preferences.getBoolean(NOTIFICATION_ON_KEY, true);
	}
	
	public static void setNotificationActivated(boolean activated){
		preferences.edit().putBoolean(NOTIFICATION_ON_KEY, activated);
	}
	public static void setPartnerEmail(String partnerEmail){
		preferences.edit().putString(PARTNER_EMAIL_KEY, partnerEmail);
	}
	
	public static void setPartnerName(String partnerName){
		preferences.edit().putString(PARTNER_NAME_KEY, partnerName);
	}
	
	public static void setUserEmail(String userEmail){
		preferences.edit().putString(USER_EMAIL_KEY, userEmail);
	}
	

}
