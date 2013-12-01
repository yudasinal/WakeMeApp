package com.yljv.alarmapp.helper;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yljv.alarmapp.R;

public class ClockAdapter extends ArrayAdapter {
	
	public ClockAdapter(Context context, ArrayList<Integer> timeList) {
		super(context, R.layout.alarm_item, timeList);
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.alarm_item, parent, false);
		
		TextView textView = (TextView) rowView.findViewById(R.id.my_text);
		textView.setText(((Integer) getItem(position)).toString()); 
				
		return rowView;
	}

}
