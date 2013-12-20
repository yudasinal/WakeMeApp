package com.yljv.alarmapp.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yljv.alarmapp.R;
<<<<<<< HEAD:src/com/yljv/alarmapp/ui/MyClockFragment.java
=======
import com.yljv.alarmapp.helper.ClockAdapter;
>>>>>>> c9f5ac5d9b8cc0386b28650f65fb5d0141c71723:src/com/yljv/alarmapp/ui/MyAlarmListFragment.java
import com.yljv.alarmapp.parse.database.Alarm;



public class MyAlarmListFragment extends Fragment {
	
	public ListView listView;
	ArrayList<Alarm> list = new ArrayList<Alarm>(); 
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.my_clock_layout, container, false);
		listView = (ListView) view.findViewById(R.id.clock_list);
		listView.setAdapter(new ClockAdapter(this.getActivity()));
		
			
		return view; 
		
	}
	
	
	
}
