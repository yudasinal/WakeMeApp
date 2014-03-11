package com.yljv.alarmapp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;

import com.parse.ParseAnalytics;
import com.parse.ParseUser;
import com.yljv.alarmapp.parse.database.AlarmInstance;
import com.yljv.alarmapp.parse.database.MyAlarmManager;
import com.yljv.alarmapp.parse.database.ParsePartnerAlarmListener;

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

		ParseAnalytics.trackAppOpened(getIntent());

		// Initialize MyAlarmManager
		MyAlarmManager.setContext(this);

		Thread t = new Thread(){
			@Override
			public void run(){
				try{
					Thread.sleep(2000);
					cont();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		};
		t.start();

	}

	public void cont() {
		Intent intent;
		if(ParseUser.getCurrentUser() == null){
			intent = new Intent(this, ChoiceActivity.class);
		}else{
			intent = new Intent(this, MenuMainActivity.class);
		}
		startActivity(intent);
	}
}
