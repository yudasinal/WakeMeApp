package com.yljv.alarmapp.client.ui.alarm;

import java.util.ArrayList;

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
import com.yljv.alarmapp.server.alarm.Alarm;
import com.yljv.alarmapp.server.alarm.MyAlarmManager;

public class ClockAdapter extends ArrayAdapter<Alarm> {
	int red = Color.parseColor("#ff0404");
	
	
	public ClockAdapter(Context context, ArrayList<Alarm> list) {
		super(context, R.layout.alarm_item, list);
		this.addAll(MyAlarmManager.getMyAlarms());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder holder;
		final Alarm myAlarm;
		final ImageView setAlarm;
		
		if(rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			rowView = inflater.inflate(R.layout.alarm_item, null);
			holder = new ViewHolder();
			holder.textView = (TextView) rowView.findViewById(R.id.my_text);
			holder.timeView = (TextView) rowView.findViewById(R.id.my_time);
			holder.morEvView = (TextView) rowView.findViewById(R.id.morning_evening);
			holder.activated = (ImageView) rowView.findViewById(R.id.set_alarm);
			holder.weekdays[0] = (TextView) rowView.findViewById(R.id.mon);
			holder.weekdays[1] = (TextView) rowView.findViewById(R.id.tue);
			holder.weekdays[2] = (TextView) rowView.findViewById(R.id.wed);
			holder.weekdays[3] = (TextView) rowView.findViewById(R.id.thu);
			holder.weekdays[4] = (TextView) rowView.findViewById(R.id.fri);
			holder.weekdays[5] = (TextView) rowView.findViewById(R.id.sat);
			holder.weekdays[6] = (TextView) rowView.findViewById(R.id.sun);
			rowView.setTag(holder);
			setAlarm = (ImageView) rowView.findViewById(R.id.set_alarm);
				ViewHolder myHolder = (ViewHolder)rowView.getTag();
				myAlarm = this.getItem(position);
				String text = myAlarm.getName();
				String time = myAlarm.getTimeAsString();
				String morningEvening = myAlarm.getMorningEveningAsString();
				myHolder.timeView.setText(time);
				myHolder.textView.setText(text);
				myHolder.morEvView.setText(morningEvening);
				boolean[] repeatedDays = myAlarm.getWeekdaysRepeated();
				for(int i = 0; i < repeatedDays.length; i++) {
					if(repeatedDays[i] == true) {
						holder.weekdays[i].setTextColor(red);
					}
				}
				if(myAlarm.isActivated()) {
					myHolder.activated.setImageResource(R.drawable.alarm_activated);
				}
				else {
					myHolder.activated.setImageResource(R.drawable.alarm_deactivated);
				}

				myHolder.activated.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(!myAlarm.isActivated()) {
							setAlarm.setImageResource(R.drawable.alarm_activated);
							myAlarm.setActivated(true);
							
						}
						else {
							setAlarm.setImageResource(R.drawable.alarm_deactivated);
							myAlarm.setActivated(false);
						}
					}
	
				});
			}
		return rowView;
	}
	
	private static class ViewHolder {
		TextView textView;
		TextView timeView;
		TextView morEvView;
		TextView[] weekdays = new TextView[7];
		ImageView activated;
	}

}

















