package com.yljv.alarmapp.client.helper;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.PushService;
import com.yljv.alarmapp.server.alarm.Alarm;
import com.yljv.alarmapp.server.alarm.AlarmInstance;

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
		
		super.onCreate();
	}

}
