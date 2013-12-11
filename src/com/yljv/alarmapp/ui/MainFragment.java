package com.yljv.alarmapp.ui;

import com.yljv.alarmapp.R;
import com.yljv.alarmapp.R.id;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {
	
	TextView txtMe;
	TextView txtHim; 
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		
		txtMe = (TextView) view.findViewById(R.id.main_screen_me);
		txtMe.setText("My Alarm");
		txtHim = (TextView) view.findViewById(R.id.main_screen_his);
		txtHim.setText("His Alarm");
		return view;
		
	}
	
	

}
