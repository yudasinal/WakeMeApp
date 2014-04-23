package com.yljv.alarmapp.helper;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.yljv.alarmapp.WakeUpActivity;
import com.yljv.alarmapp.parse.database.AlarmInstance;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		MyAlarmManager.getMyAlarmsFromDatabase();

		int id = intent.getExtras().getInt(AlarmInstance.COLUMN_ID);
		
		//TODO add alarm to WakeUpActivity to display time and pic/msg if exists
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
		
		
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