package com.yljv.alarmapp.client.ui.wakeup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.yljv.alarmapp.R;
import com.yljv.alarmapp.server.alarm.AlarmInstance;
import com.yljv.alarmapp.server.alarm.MsgPictureTuple;
import com.yljv.alarmapp.server.alarm.MyAlarmManager;
import com.yljv.alarmapp.R.id;
import com.yljv.alarmapp.R.layout;
import com.yljv.alarmapp.client.helper.ApplicationSettings;

public class WakeUpActivity extends FragmentActivity {

	private Fragment mainView;
	boolean isThereSomething = true;

    MsgPictureTuple tuple;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();

		int id = this.getIntent().getIntExtra(AlarmInstance.COLUMN_ID, 0);

        tuple = MyAlarmManager.findPicMsgByAlarmId(id);

		if (savedInstanceState != null) {
			mainView = getSupportFragmentManager().getFragment(
					savedInstanceState, "mainView");
		}
		if (savedInstanceState == null) {
			mainView = new WakeUpFragment();
			Bundle bundle = new Bundle();
			bundle.putInt(AlarmInstance.COLUMN_ID, id);
			mainView.setArguments(bundle);

		}

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

    public MsgPictureTuple getTuple(){
        return tuple;
    }
}
