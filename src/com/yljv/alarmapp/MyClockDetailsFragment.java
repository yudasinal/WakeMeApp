package com.yljv.alarmapp;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

/*
 * shows Details of an Alarm
 * sets Alarm
 */
public class MyClockDetailsFragment extends Fragment implements OnClickListener{
	
	private AlarmManager alarmMgr;
	
	public void setAlarm(int hour, int minute){
		
		//TODO use Date, TextClock

		alarmMgr = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(getActivity(), ClockActivity.class);
		PendingIntent alarmIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
