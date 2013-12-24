package com.yljv.alarmapp.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		// TODO set the alarm here
		// only for test
		Toast.makeText(context, "Alarm Alarm", Toast.LENGTH_LONG).show();
		int id = intent.getIntExtra("id", -100);
		Toast.makeText(context, intent.getIntExtra("id", -100), Toast.LENGTH_LONG)
				.show();

	}
}
