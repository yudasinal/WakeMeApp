package com.yljv.alarmapp;

import java.util.ArrayList;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yljv.alarmapp.helper.ClockAdapter;



public class MyClockFragment extends Fragment {
	
	public ListView listView;
	ArrayList<Integer> list = new ArrayList<Integer>(); 
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.my_clock_layout, container, false);
		//View view = super.onCreateView(inflater, container, saveInstanceState);
		for(int i = 0; i<40; i++) {
			list.add(i);
		}
		listView = (ListView) view.findViewById(R.id.clock_list);
		ClockAdapter clockAdapter = new ClockAdapter(this.getActivity(), list);
		listView.setAdapter(clockAdapter);
		
			
		return view; 
		
	}
	
	
	
}
