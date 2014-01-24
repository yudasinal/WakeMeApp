package com.yljv.alarmapp;

import android.app.Activity;
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
		
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
	    getActionBar().hide();
		setContentView(R.layout.splash);
		
		TextView name = (TextView) findViewById(R.id.name_id);
		name.setText(Html.fromHtml("<b>wakeme</b>app"));
		
		
	}
}
