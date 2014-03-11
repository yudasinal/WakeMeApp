package com.yljv.alarmapp.helper;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;
import com.yljv.alarmapp.MenuMainActivity;
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.parse.database.AlarmInstance;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

public class WakeMeApp extends Application {

	@Override
	public void onCreate() {

		// initialize Parse
		Parse.initialize(this, "Xhd6iekMpDunfKFfbUxGaAORtC0TwkQ9jYGJHqc4",
				"P7d6CWqkG26FcB6tCXIchuiSFOMwpj1WmfnNGISL");

		// register ParseObject Subclasses
		ParseObject.registerSubclass(Alarm.class);
		ParseObject.registerSubclass(AlarmInstance.class);

		// enable Push Notifications
		PushService.setDefaultPushCallback(this, MenuMainActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();

		// initialize Settings
		ApplicationSettings.setSharedPreferences(this);
		
		MyAlarmManager.setContext(this);
		// getAlarms from Database
		if (ParseUser.getCurrentUser() != null) {
			MyAlarmManager.getPartnerAlarmsFromDatabase();
			MyAlarmManager.getMyAlarmsFromDatabase();
		}
		super.onCreate();
	}

}
