package com.yljv.alarmapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.yljv.alarmapp.helper.ApplicationSettings;
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

/*
 * shows Login if neccessary
 * shows MainPage
 * slide to left -> go to own Clock
 * slide to right -> go to better halfs Clock ;)
 */
public class MainActivity extends SlidingActivity {

	private ViewPager mPager;

	private PagerAdapter mPagerAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		//this is random stuff to make the app run
		setContentView(R.layout.login_layout);

		
		// register ParseObject Subclasses
				ParseObject.registerSubclass(Alarm.class);
				// initialize Parse
				Parse.initialize(this, "Xhd6iekMpDunfKFfbUxGaAORtC0TwkQ9jYGJHqc4",
						"P7d6CWqkG26FcB6tCXIchuiSFOMwpj1WmfnNGISL");
				ParseAnalytics.trackAppOpened(getIntent());

		//initialize Settings
		ApplicationSettings.preferences = this.getSharedPreferences("Preferences", this.MODE_APPEND);
		
		
		MyAlarmManager.setAlarm(this, MyAlarmManager.WEDNESDAY, 15, 40, "first alarm", false);
		Intent intent = new Intent(this, SplashActivity.class);
		startActivity(intent);

	}

	@Override
	public void onBackPressed() {
		// TODO
		if (mPager.getCurrentItem() == 0) {
			// If the user is currently looking at the first step, allow the
			// system to handle the
			// Back button. This calls finish() on this activity and pops the
			// back stack.
			super.onBackPressed();
		} else {
			// Otherwise, select the previous step.
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
