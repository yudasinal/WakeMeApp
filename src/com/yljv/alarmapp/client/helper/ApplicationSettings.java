package com.yljv.alarmapp.client.helper;

import com.parse.ParseUser;
import com.yljv.alarmapp.server.user.AccountManager.User;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Only sets LOCAL data on the device, no syncing with the server side
 */
public class ApplicationSettings {

	public final static String NOTIFICATION_ON_KEY = "notification_on";
	public final static String NOTIFICATION_SOUND_KEY = "notification_sound";
	public final static String NOTIFICATION_VIBRATION_KEY = "notification_vibration";


    public final static String directory = "/WakeMeApp";
	
	public int alarmIdCounter = 0;
	
	private static SharedPreferences preferences;

	
	public static void setNotificationSound(boolean on){
		preferences.edit().putBoolean(NOTIFICATION_SOUND_KEY, on).commit();
	}
	
	public static boolean getNofiticationSoundActivated(){
		return preferences.getBoolean(NOTIFICATION_SOUND_KEY, true);
	}
	
	public static void setNotificationVibration(boolean on){
		preferences.edit().putBoolean(NOTIFICATION_VIBRATION_KEY, on).commit();
	}
	
	public static boolean getNofiticationVibrationActivated(){
		return preferences.getBoolean(NOTIFICATION_VIBRATION_KEY, true);
	}
	
	public static void setPartnerStatus(int status){
		preferences.edit().putInt(User.PARTNER_STATUS_COLUMN, status).commit();
	}

	public static boolean hasPartner(){
		return preferences.getInt(User.PARTNER_STATUS_COLUMN, User.NO_PARTNER) == User.PARTNERED;
	}
	
	public static int getPartnerStatus(){
		return preferences.getInt(User.PARTNER_STATUS_COLUMN, User.NO_PARTNER);
	}

	public static void setSharedPreferences(Context context) {
		// TODO do this at beginning!!!
		preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
	}
	
	public static void setAlarmId(int id){
		preferences.edit().putInt(User.ALARM_ID_COUNTER_COLUMN, id).commit();
	}
	
	public static int getAlarmId(){
		int id = preferences.getInt(User.ALARM_ID_COUNTER_COLUMN, 0);
		preferences.edit().putInt(User.ALARM_ID_COUNTER_COLUMN, id+1).commit();
		ParseUser user = ParseUser.getCurrentUser();
		user.put(User.ID_COLUMN, id+1);
		user.saveEventually();
		
		return id+1;
	}
	
	
	public static String getPartnerEmail(){
		return preferences.getString(User.PARTNER_COLUMN, "");
	}

	public static String getPartnerName(){
		return preferences.getString(User.PARTNER_COLUMN, "Partner");
	}

	public static String getUserEmail(){
		return preferences.getString(User.USER_EMAIL_COLUMN, "");
	}
	
	public static String getUserName(){
		String res = preferences.getString(User.USER_EMAIL_COLUMN, "");
		return res;
	}

	public static boolean isNotificationActivated(){
		return preferences.getBoolean(NOTIFICATION_ON_KEY, true);
	}
	
	public static void setNotificationActivated(boolean activated){
		preferences.edit().putBoolean(NOTIFICATION_ON_KEY, activated).commit();
	}
	
	public static boolean getNoticationActivated(){
		return preferences.getBoolean(NOTIFICATION_ON_KEY, true);
	}
	
	public static void setPartnerEmail(String partnerEmail){
		preferences.edit().putString(User.PARTNER_COLUMN, partnerEmail).commit();
	}
	
	/*public static void setPartnerName(String partnerName){
		preferences.edit().putString(PARTNER_NAME_KEY, partnerName).commit();
		ParseUser user = ParseUser.getCurrentUser();
		user.put(PARTNER_NAME_KEY, partnerName);
		user.saveEventually();
	}*/

    public static void setUserEmail(String userEmail){
        preferences.edit().putString(User.USER_EMAIL_COLUMN, userEmail).commit();
    }
	
	public static void setUserName(String userName){
		preferences.edit().putString(User.USER_NAME_COLUMN, userName).commit();
	}

    public static void setPartnerName(String name){
        preferences.edit().putString(User.PARTNER_NAME_COLUMN, name).commit();
    }
	
	public static void reset(){
		preferences.edit().clear().commit();
	}

}
