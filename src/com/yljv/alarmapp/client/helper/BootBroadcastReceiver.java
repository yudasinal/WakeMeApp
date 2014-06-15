package com.yljv.alarmapp.client.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yljv.alarmapp.server.alarm.MyAlarmManager;

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
