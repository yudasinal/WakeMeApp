package com.yljv.alarmapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.yljv.alarmapp.helper.ApplicationSettings;
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.parse.database.AlarmInstance;
import com.yljv.alarmapp.parse.database.MyAlarmManager;
import com.yljv.alarmapp.ui.WakeUpFragment;
import com.yljv.alarmapp.ui.WakeUpFragmentNoExtra;

public class WakeUpActivity extends FragmentActivity {

	private Fragment mainView;
	boolean isThereSomething = true;

	AlarmInstance alarm;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();

		int id = this.getIntent().getIntExtra(AlarmInstance.COLUMN_ID, 0);

		if (savedInstanceState != null) {
			mainView = getSupportFragmentManager().getFragment(
					savedInstanceState, "mainView");
		}
		if (savedInstanceState == null) {
			if (ApplicationSettings.hasPartner()) {
				if (isThereSomething) {
					// TODO check if there's something uploaded to an alarm
					Bundle bundle = new Bundle();
					bundle.putInt(AlarmInstance.COLUMN_ID, id);
					mainView = (Fragment) new WakeUpFragment();
					mainView.setArguments(bundle);
				} else {
					mainView = (Fragment) new WakeUpFragmentNoExtra();
				}
			} else {
				mainView = (Fragment) new WakeUpFragmentNoExtra();
			}
		}


		Alarm alarm = MyAlarmManager.findAlarmById(id / 10 * 10);
		MyAlarmManager.setNextAlarmInstance(alarm);
		
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mainView).commit();


	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mainView", mainView);
	}

	public void switchContent(Fragment fragment) {
		mainView = fragment;
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).addToBackStack(null)
				.commit();
		this.invalidateOptionsMenu();
	}

	@Override
	public void onBackPressed() {
		if (getIntent().getBooleanExtra("EXIT", false)) {
			finish();
		}
	}
}
