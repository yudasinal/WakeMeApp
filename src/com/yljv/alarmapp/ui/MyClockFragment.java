package com.yljv.alarmapp.ui;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yljv.alarmapp.R;
import com.yljv.alarmapp.R.id;
import com.yljv.alarmapp.R.layout;
import com.yljv.alarmapp.parse.database.Alarm;



public class MyClockFragment extends Fragment {
	
	public ListView listView;
	ArrayList<Alarm> list = new ArrayList<Alarm>(); 
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.my_clock_layout, container, false);

		
			
		return view; 
		
	}
	
	
	
}
