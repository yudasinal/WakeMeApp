package com.yljv.alarmapp.helper;

import com.yljv.alarmapp.WakeUpActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		//TODO add alarm to WakeUpActivity to display time and pic/msg if exists
		Intent i = new Intent(context, WakeUpActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		
		
		// TODO set the alarm here
		// only for test
		/*Toast.makeText(context, "Alarm Alarm", Toast.LENGTH_LONG).show();
		Toast.makeText(context, Integer.toString(intent.getIntExtra("id", -100)), Toast.LENGTH_LONG)
				.show();*/

		/*
		 * Get the alarm
		 * get picture
		 * get message
		 * get music
		 * set new alarm if repeating
		 */
	}
}
