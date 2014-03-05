package com.yljv.alarmapp.helper;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yljv.alarmapp.R;
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

public class ClockAdapter extends ArrayAdapter<Alarm> {
	boolean alarmSet;
	int red = Color.parseColor("#fa8b60");
	
	
	public ClockAdapter(Context context) {
		super(context, R.layout.alarm_item, new ArrayList<Alarm>());
		this.addAll(MyAlarmManager.getAllAlarms());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder holder;
		ArrayList<Alarm> myAlarms;
		Alarm myAlarm;
		final ImageView setAlarm;
		
		if(rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			rowView = inflater.inflate(R.layout.alarm_item, null);
			holder = new ViewHolder();
			holder.textView = (TextView) rowView.findViewById(R.id.my_text);
			holder.timeView = (TextView) rowView.findViewById(R.id.my_time);
			holder.monday = (TextView) rowView.findViewById(R.id.mon);
			holder.tuesday = (TextView) rowView.findViewById(R.id.tue);
			holder.wednesday = (TextView) rowView.findViewById(R.id.wed);
			holder.thursday = (TextView) rowView.findViewById(R.id.thu);
			holder.friday = (TextView) rowView.findViewById(R.id.fri);
			holder.saturday = (TextView) rowView.findViewById(R.id.sat);
			holder.sunday = (TextView) rowView.findViewById(R.id.sun);
			myAlarms = MyAlarmManager.getAllAlarms();
			Collections.sort(myAlarms);
			rowView.setTag(holder);
			setAlarm = (ImageView) rowView.findViewById(R.id.set_alarm);
			setAlarm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(!alarmSet) {
						setAlarm.setImageResource(R.drawable.alarm_activated);
						alarmSet = true;
					}
					else {
						setAlarm.setImageResource(R.drawable.alarm_deactivated);
						alarmSet = false;
					}
				}

			});
			
			if (myAlarms == null) {
				LayoutInflater inflater1 = LayoutInflater.from(getContext());
				rowView = inflater1.inflate(R.layout.no_alarms_to_display, null);
				/*
				*ViewHolder myHolder = (ViewHolder)rowView.getTag();
				*myHolder.textView.setText("Hello");
				*myHolder.timeView.setText("Goodbye");
				*/
			}
			else if (myAlarms != null) {
				ViewHolder myHolder = (ViewHolder)rowView.getTag();
				myAlarm = myAlarms.get(position);
				String text = myAlarm.getName();
				String time = myAlarm.getTimeAsString();
				myHolder.timeView.setText(time);
				myHolder.textView.setText(text);
				boolean[] repeatedDays = myAlarm.getWeekdaysRepeated();
				for(int i = 0; i < repeatedDays.length - 1; i++) {
					if(repeatedDays[i] == true) {
						switch(i) {
						case 0:
							holder.monday.setTextColor(red);
							break;
						case 1:
							holder.tuesday.setTextColor(red);
							break;
						case 2:
							holder.wednesday.setTextColor(red);
							break;
						case 3: 
							holder.thursday.setTextColor(red);
							break;
						case 4: 
							holder.friday.setTextColor(red);
							break;
						case 5:
							holder.saturday.setTextColor(red);
							break;
						case 6:
							holder.sunday.setTextColor(red);
							break;
						}
					}
				}
			}
		}
		else {
			holder = (ViewHolder) rowView.getTag();
		}

		return rowView;
	}
	
	static class ViewHolder {
		TextView textView;
		TextView timeView;
		TextView monday;
		TextView tuesday;
		TextView wednesday;
		TextView thursday;
		TextView friday;
		TextView saturday;
		TextView sunday;
	}

}

















