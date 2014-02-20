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

public class ClockAdapter extends ArrayAdapter<String> {

	public ClockAdapter(Context context) {
		super(context, R.layout.alarm_item, new ArrayList<String>());
		this.addAll(MyAlarmManager.getAllAlarmString());

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/*
		 * LayoutInflater inflater = (LayoutInflater)
		 * getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); View
		 * rowView = inflater.inflate(R.layout.alarm_item, parent, false);
		 * TextView textView = (TextView) rowView.findViewById(R.id.my_text);
		 * String item = (String) getItem(position); textView.setText(item);
		 * String text = item.getName(); textView.setText(text);
		 * 
		 * TextView timeView = (TextView) rowView.findViewById(R.id.my_time);
		 * Integer time = item.getTime(); timeView.setText(time);
		 */

		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.alarm_item, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.my_text);
		TextView timeView = (TextView) rowView.findViewById(R.id.my_time);
		ArrayList<Alarm> myAlarms = MyAlarmManager.getAllAlarms();
		if (myAlarms == null) {
			textView.setText("Hello");
			timeView.setText("Goodbye");
		} else if (myAlarms != null) {
			Alarm myAlarm = myAlarms.get(position);
			String text = myAlarm.getName();
			Integer time = myAlarm.getHour();
			timeView.setText(time.toString());
			textView.setText(text);
		}

		return rowView;
	}

}
