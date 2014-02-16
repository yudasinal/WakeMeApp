package com.yljv.alarmapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.PushService;
import com.yljv.alarmapp.helper.ApplicationSettings;
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.parse.database.ParsePartnerListener;
import com.yljv.alarmapp.parse.database.ParseRegisterListener;



/*
 * very first screen to see after opening the app
 * disappears after a few seconds
 */
public class SplashActivity extends Activity implements ParsePartnerListener, ParseRegisterListener {

	ParsePartnerListener list;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		list = this;
		
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setContentView(R.layout.splash);

		TextView name = (TextView) findViewById(R.id.name_id);
		name.setText(Html.fromHtml("<b>wakeme</b>app"));


		// initialize Parse
		Parse.initialize(this, "Xhd6iekMpDunfKFfbUxGaAORtC0TwkQ9jYGJHqc4",
				"P7d6CWqkG26FcB6tCXIchuiSFOMwpj1WmfnNGISL");
		ParseAnalytics.trackAppOpened(getIntent());

		// register ParseObject Subclasses
		ParseObject.registerSubclass(Alarm.class);

		// enable Push Notifications
		PushService.setDefaultPushCallback(this, MenuMainActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();

		// initialize Settings
		ApplicationSettings.setSharedPreferences(this);
		ApplicationSettings.preferences = this.getSharedPreferences(
				"Preferences", this.MODE_APPEND);

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				//open next Activity
				cont();
			}
		}, 2000);

	}

	public void cont() {
		Intent intent = new Intent(this, LoginActivity.class);
		this.startActivity(intent);
	}

	@Override
	public void onPartnerFound() {
		
	}

	@Override
	public void onPartnerNotExisting() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPartnerQueryError(Exception e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegisterSuccess() {
		// TODO Auto-generated method stub
		Log.i("WakeMeApp", "registered");
	}

	@Override
	public void onRegisterFail(ParseException e) {
		// TODO Auto-generated method stub
		Log.i("WakeMeApp", "not registered");
	}

}
