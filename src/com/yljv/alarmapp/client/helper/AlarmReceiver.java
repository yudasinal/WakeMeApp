package com.yljv.alarmapp.client.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import com.yljv.alarmapp.client.ui.wakeup.WakeUpActivity;
import com.yljv.alarmapp.server.alarm.AlarmInstance;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {


		//MyAlarmManager.getMyAlarmsFromDatabase();

		int id = intent.getIntExtra(AlarmInstance.COLUMN_ID, 0);
		
		//TODO add alarm to WakeUpActivity to display time and pic/msg if exists
		Intent i = new Intent(context, WakeUpActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(AlarmInstance.COLUMN_ID, id);
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
		 Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		    vibrator.vibrate(2000);
		    
	}
}