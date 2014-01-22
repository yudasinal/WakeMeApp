package com.yljv.alarmapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
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
		
		setContentView(R.layout.splash);
		
		TextView name = (TextView) findViewById(R.id.name_id);
		name.setText(Html.fromHtml("<b>wakeme</b>app"));
		
		// initialize Parse
				Parse.initialize(this, "Xhd6iekMpDunfKFfbUxGaAORtC0TwkQ9jYGJHqc4",
						"P7d6CWqkG26FcB6tCXIchuiSFOMwpj1WmfnNGISL");
				ParseAnalytics.trackAppOpened(getIntent());
				
		// register ParseObject Subclasses
		ParseObject.registerSubclass(Alarm.class);
		
		

		
		//enable Push Notifications
		PushService.setDefaultPushCallback(this, MenuMainActivity.class);
		ParseAnalytics.trackAppOpened(getIntent());
		// initialize Settings
		ApplicationSettings.preferences = this.getSharedPreferences(
				"Preferences", this.MODE_APPEND);

		/*
		// Alarm Test
		MyAlarmManager.setAlarm(this, MyAlarmManager.WEDNESDAY, 15, 40,
				"first alarm", false);

		// Storing Bitmaps test
		Bitmap bitmap = Bitmap.createBitmap(3, 3, Bitmap.Config.ARGB_8888);
		*/
	}
}
