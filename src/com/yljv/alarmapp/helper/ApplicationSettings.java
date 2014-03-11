package com.yljv.alarmapp.helper;

import com.parse.ParseUser;
import com.yljv.alarmapp.helper.AccountManager.User;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationSettings {

	public final static String NOTIFICATION_ON_KEY = "notification_on";
	
	private final static String PARTNER_EMAIL_KEY = "parter_email";
	private final static String PARTNER_NAME_KEY = "partner_name";
	private final static String ALARM_ID_COUNTER_KEY = "counter";
	private final static String USER_EMAIL_KEY = "user_email";
	private final static String USER_NAME_KEY = "user_name";
	
	public int alarmIdCounter = 0;
	
	private static SharedPreferences preferences;
	
	

	public static void setSharedPreferences(Context context) {
		// TODO do this at beginning!!!
		preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
	}
	
	public static void setAlarmId(int id){
		preferences.edit().putInt(ALARM_ID_COUNTER_KEY, id);
	}
	
	public static int getAlarmId(){
		int id = preferences.getInt(ALARM_ID_COUNTER_KEY, 0);
		preferences.edit().putInt(ALARM_ID_COUNTER_KEY, id+1).commit();	
		ParseUser user = ParseUser.getCurrentUser();
		user.put(User.ID_COLUMN, id+1);
		user.saveEventually();
		
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
	
	public static String getUserName(){
		String res = preferences.getString(USER_NAME_KEY, "Jane");
		return res;
	}

	public static boolean isNotificationActivated(){
		return preferences.getBoolean(NOTIFICATION_ON_KEY, true);
	}
	
	public static void setNotificationActivated(boolean activated){
		preferences.edit().putBoolean(NOTIFICATION_ON_KEY, activated).commit();
	}
	
	public static void setPartnerEmail(String partnerEmail){
		preferences.edit().putString(PARTNER_EMAIL_KEY, partnerEmail).commit();
	}
	
	public static void setPartnerName(String partnerName){
		preferences.edit().putString(PARTNER_NAME_KEY, partnerName).commit();
		ParseUser user = ParseUser.getCurrentUser();
		user.put(PARTNER_NAME_KEY, partnerName);
	}
	
	public static void setUserEmail(String userEmail){
		preferences.edit().putString(USER_EMAIL_KEY, userEmail).commit();
		ParseUser user = ParseUser.getCurrentUser();
		user.put(USER_EMAIL_KEY, userEmail);
		
	}
	
	public static void setUserName(String userName){
		preferences.edit().putString(USER_NAME_KEY, userName).commit();
		ParseUser user = ParseUser.getCurrentUser();
		user.put(USER_NAME_KEY, userName);
	}
	
	public static void reset(){
		preferences.edit().clear().commit();
	}

}
