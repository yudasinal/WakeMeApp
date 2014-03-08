package com.yljv.alarmapp;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;

import com.parse.ParseAnalytics;
import com.yljv.alarmapp.parse.database.AlarmInstance;
import com.yljv.alarmapp.parse.database.MyAlarmManager;
import com.yljv.alarmapp.parse.database.ParsePartnerAlarmListener;



/*
 * very first screen to see after opening the app
 * disappears after a few seconds
 */
public class SplashActivity extends Activity implements ParsePartnerAlarmListener{

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setContentView(R.layout.splash);

		TextView name = (TextView) findViewById(R.id.name_id);
		name.setText(Html.fromHtml("<b>wakeme</b>app"));

		ParseAnalytics.trackAppOpened(getIntent());		
		
		try{
			Thread.sleep(1500);
		}catch(Exception e){
			e.printStackTrace();
		}

		// Initialize MyAlarmManager
		MyAlarmManager.setContext(this);
		MyAlarmManager.updatePartnerAlarms(this);

	}

	public void cont() {
		Intent intent = new Intent(this, MenuMainActivity.class);
		this.startActivity(intent);
	}

	@Override
	public void partnerAlarmsFound(List<AlarmInstance> alarms) {
		cont();
	}

	@Override
	public void partnerAlarmsSearchFailed(Exception e) {
		cont();
	}

}
