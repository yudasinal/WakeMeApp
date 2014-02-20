package com.yljv.alarmapp.helper;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yljv.alarmapp.R;
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

public class ClockAdapter extends ArrayAdapter<Alarm> {
	
	public ClockAdapter(Context context) {
		super(context, R.layout.alarm_item, new ArrayList<Alarm>());
		this.addAll(MyAlarmManager.getAllAlarms());
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.alarm_item, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.my_text);
		TextView timeView = (TextView) rowView.findViewById(R.id.my_time);
		ArrayList<Alarm> myAlarms = MyAlarmManager.getAllMyAlarms();
		if (myAlarms == null) {
			textView.setText("Hello");
			timeView.setText("Goodbye");
		}
		else if (myAlarms != null) {
			Alarm myAlarm = myAlarms.get(position);
			String text = myAlarm.getName();
			String time = myAlarm.getAlarmTime();
			timeView.setText(time);
			textView.setText(text);
		}
				
		return rowView;
	}

}
