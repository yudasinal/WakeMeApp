package com.yljv.alarmapp;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;
import com.yljv.alarmapp.helper.ApplicationSettings;
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.parse.database.MyAlarmManager;
import com.yljv.alarmapp.ui.MenuList;
import com.yljv.alarmapp.ui.MyAlarmListFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yljv.alarmapp.ui.MenuList;

public class MenuMainActivity extends BaseActivity {

	private Fragment mainView;

	public MenuMainActivity() {
		super(R.string.app_name);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// register ParseObject Subclasses
		ParseObject.registerSubclass(Alarm.class);
		
		// initialize Parse
		Parse.initialize(this, "Xhd6iekMpDunfKFfbUxGaAORtC0TwkQ9jYGJHqc4",
				"P7d6CWqkG26FcB6tCXIchuiSFOMwpj1WmfnNGISL");
		ParseAnalytics.trackAppOpened(getIntent());

		ApplicationSettings.setSharedPreferences(this);
		
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mainView = getSupportFragmentManager().getFragment(
					savedInstanceState, "mainView");
		}
		if (savedInstanceState == null) {
			mainView = (Fragment) new MyAlarmListFragment();
		}

		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mainView).commit();

		setBehindContentView(R.layout.menu_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MenuList()).commit();

		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		//test Alarm
		MyAlarmManager.setAlarm(this, MyAlarmManager.THURSDAY, 10, 56, "myalarm", false);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mainView", mainView);
	}

	public void switchContent(Fragment fragment) {
		mainView = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		getSlidingMenu().showContent();
	}
}
