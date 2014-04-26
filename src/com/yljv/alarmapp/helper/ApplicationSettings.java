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
	private final static String USER_EMAIL_KEY = "email";
	private final static String USER_NAME_KEY = "username";
	private final static String PARTNER_STATUS = "partner_status";
	
	public int alarmIdCounter = 0;
	
	private static SharedPreferences preferences;
	
	public static void setPartnerStatus(int status){
		preferences.edit().putInt(PARTNER_STATUS, status).commit();
		ParseUser.getCurrentUser().put(User.PARTNER_STATUS_COLUMN, status);
		ParseUser.getCurrentUser().saveEventually();
	}
	public static void cancelRequest(){
		preferences.edit().putInt(PARTNER_STATUS, User.NO_PARTNER).commit();
		preferences.edit().putString(PARTNER_EMAIL_KEY, "").commit();
		ParseUser.getCurrentUser().put(User.PARTNER_STATUS_COLUMN, User.NO_PARTNER);
		ParseUser.getCurrentUser().put(User.PARTNER_COLUMN, "");
		ParseUser.getCurrentUser().saveEventually();
	}
	
	public static void incomingRequest(String email){
		preferences.edit().putInt(PARTNER_STATUS, User.INCOMING_REQUEST).commit();
		preferences.edit().putString(PARTNER_EMAIL_KEY, email).commit();
		ParseUser.getCurrentUser().put(User.PARTNER_STATUS_COLUMN, User.INCOMING_REQUEST);
		ParseUser.getCurrentUser().put(User.PARTNER_COLUMN, email);
		ParseUser.getCurrentUser().saveEventually();
	
	}
	
	public static void unlinkPartner(){
		preferences.edit().putInt(PARTNER_STATUS, User.NO_PARTNER).commit();
		preferences.edit().putString(PARTNER_EMAIL_KEY, "").commit();
		ParseUser user = ParseUser.getCurrentUser();
		user.put(User.PARTNER_COLUMN, "");
		user.put(User.PARTNER_STATUS_COLUMN, User.NO_PARTNER);
		user.saveEventually();
	}
	public static void setPartnerRequest(String email){
		preferences.edit().putInt(PARTNER_STATUS, User.PARTNER_REQUESTED).commit();
		preferences.edit().putString(PARTNER_EMAIL_KEY, email).commit();
		ParseUser.getCurrentUser().put(User.PARTNER_STATUS_COLUMN, User.PARTNER_REQUESTED);
		ParseUser.getCurrentUser().put(User.PARTNER_COLUMN, email);
		ParseUser.getCurrentUser().saveEventually();
	}
	
	public static boolean hasPartner(String userEmail){
		return preferences.getInt(PARTNER_STATUS, User.NO_PARTNER) == User.PARTNERED;
	}
	
	public static int getPartnerStatus(){
		return preferences.getInt(PARTNER_STATUS, User.NO_PARTNER);
	}

	public static void setSharedPreferences(Context context) {
		// TODO do this at beginning!!!
		preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
	}
	
	public static void setAlarmId(int id){
		preferences.edit().putInt(ALARM_ID_COUNTER_KEY, id).commit();
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
		return preferences.getString(PARTNER_EMAIL_KEY, "");
	}

	public static String getPartnerName(){
		//TODO
		return preferences.getString(PARTNER_NAME_KEY, "Partner");
	}

	public static String getUserEmail(){
		//TODO
		return preferences.getString(USER_EMAIL_KEY, "");
	}
	
	public static String getUserName(){
		//TODO
		String res = preferences.getString(USER_NAME_KEY, "");
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
		ParseUser user = ParseUser.getCurrentUser();
		user.put(User.PARTNER_COLUMN, partnerEmail);
		user.saveEventually();
	}
	
	public static void setPartnerName(String partnerName){
		preferences.edit().putString(PARTNER_NAME_KEY, partnerName).commit();
		ParseUser user = ParseUser.getCurrentUser();
		user.put(PARTNER_NAME_KEY, partnerName);
		user.saveEventually();
	}
	
	public static void setUserEmail(String userEmail){
		preferences.edit().putString(USER_EMAIL_KEY, userEmail).commit();
		ParseUser user = ParseUser.getCurrentUser();
		user.setEmail(userEmail);
		user.setUsername(userEmail);
		user.saveEventually();
		
	}
	
	public static void setUserName(String userName){
		preferences.edit().putString(USER_NAME_KEY, userName).commit();
		ParseUser user = ParseUser.getCurrentUser();
		user.put(USER_NAME_KEY, userName);
		user.saveEventually();
	}
	
	public static void reset(){
		preferences.edit().clear().commit();
	}

}
