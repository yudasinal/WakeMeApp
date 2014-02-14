package com.yljv.alarmapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.parse.PushService;
import com.yljv.alarmapp.helper.ApplicationSettings;
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

/*
 * very first screen to see after opening the app
 * disappears after a few seconds
 */
public class SplashActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setContentView(R.layout.splash);

		TextView name = (TextView) findViewById(R.id.name_id);
		name.setText(Html.fromHtml("<b>wakeme</b>app"));

		// test Alarm
		/*
		 * MyAlarmManager.setAlarm(this, MyAlarmManager.THURSDAY, 10, 56,
		 * "myalarm", false);
		 */

		// initialize Parse
		Parse.initialize(this, "Xhd6iekMpDunfKFfbUxGaAORtC0TwkQ9jYGJHqc4",
				"P7d6CWqkG26FcB6tCXIchuiSFOMwpj1WmfnNGISL");
		ParseAnalytics.trackAppOpened(getIntent());

		// register ParseObject Subclasses
		ParseObject.registerSubclass(Alarm.class);

		// enable Push Notifications
		PushService.setDefaultPushCallback(this, MenuMainActivity.class);
		ParseAnalytics.trackAppOpened(getIntent());

		// initialize Settings
		ApplicationSettings.setSharedPreferences(this);
		ApplicationSettings.preferences = this.getSharedPreferences(
				"Preferences", this.MODE_APPEND);
		
		//Thread to display the splash activity 
		
		Thread splashScreen = new Thread() {
			
			public void run() {
				try {
					sleep(3500);
					startActivity(new Intent(getApplicationContext(), MenuMainActivity.class));
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				finally {
					finish();
				}		
			}
		};
		
		splashScreen.start();
	}
}
