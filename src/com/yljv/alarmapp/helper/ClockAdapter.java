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
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.alarm_item, parent, false);
		
		TextView textView = (TextView) rowView.findViewById(R.id.my_text);
		String item = (String) getItem(position);
		textView.setText(item);
/*		String text = item.getName();
		textView.setText(text); 
		
		TextView timeView = (TextView) rowView.findViewById(R.id.my_time);
		Integer time = item.getTime();
		timeView.setText(time);
*/		
		
		
		
				
		return rowView;
	}

}
