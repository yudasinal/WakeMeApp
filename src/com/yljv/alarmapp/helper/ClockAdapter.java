package com.yljv.alarmapp.helper;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
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
	
	
	public ClockAdapter(Context context) {
		super(context, R.layout.alarm_item, new ArrayList<Alarm>());
		this.addAll(MyAlarmManager.getAllAlarms());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ViewHolder holder;
		ArrayList<Alarm> myAlarms;
		final ImageView setAlarm;
		
		if(rowView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			rowView = inflater.inflate(R.layout.alarm_item, null);
			holder = new ViewHolder();
			holder.textView = (TextView) rowView.findViewById(R.id.my_text);
			holder.timeView = (TextView) rowView.findViewById(R.id.my_time);
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
				ViewHolder myHolder = (ViewHolder)rowView.getTag();
				myHolder.textView.setText("Hello");
				myHolder.timeView.setText("Goodbye");
			}
			else if (myAlarms != null) {
				ViewHolder myHolder = (ViewHolder)rowView.getTag();
				Alarm myAlarm = myAlarms.get(position);
				String text = myAlarm.getName();
				String time = myAlarm.getAlarmTime();
				myHolder.timeView.setText(time);
				myHolder.textView.setText(text);
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
	}

}

















