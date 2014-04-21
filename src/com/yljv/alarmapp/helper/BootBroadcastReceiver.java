package com.yljv.alarmapp.helper;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yljv.alarmapp.WakeUpActivity;
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.parse.database.AlarmInstance;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Thread thread = new Thread(){
			@Override
			public void run(){
				MyAlarmManager.setAlarmInstancesAutomatically();
			}
		};
		thread.start();
	}
}
