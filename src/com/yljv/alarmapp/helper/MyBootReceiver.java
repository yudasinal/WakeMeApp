package com.yljv.alarmapp.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent){
		if(intent.getAction().equals("android.intent.action.BOOT_COMPLETE")){
			//TODO set the alarm here
			//only for test
			Toast.makeText(context, "Alarm Alarm", Toast.LENGTH_LONG).show();
			Toast.makeText(context, intent.getStringExtra("id"), Toast.LENGTH_LONG).show();
		}
	}
}
