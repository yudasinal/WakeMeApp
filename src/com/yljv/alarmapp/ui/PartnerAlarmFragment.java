package com.yljv.alarmapp.ui;

import java.util.ArrayList;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yljv.alarmapp.R;
import com.yljv.alarmapp.helper.ClockAdapter;
import com.yljv.alarmapp.parse.database.Alarm;

import com.yljv.alarmapp.R;

import com.yljv.alarmapp.R;

public class PartnerAlarmFragment extends Fragment {

	public ListView listView; 
	ArrayList<Alarm> list = new ArrayList<Alarm>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_clock_layout, container, false);
		listView = (ListView) view.findViewById(R.id.clock_list);
		listView.setAdapter(new ClockAdapter(this.getActivity()));
		
		return view;
		
	}
		//Button on action bar to add a photo
		
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			super.onCreateOptionsMenu(menu, inflater);
		    menu.clear();
		}
		

	}


	
