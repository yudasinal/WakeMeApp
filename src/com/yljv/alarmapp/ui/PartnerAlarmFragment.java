package com.yljv.alarmapp.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.yljv.alarmapp.R;
import com.yljv.alarmapp.helper.ClockAdapter;
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.MenuMainActivity;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.R;

public class PartnerAlarmFragment extends Fragment {

	public ListView listView; 
	ArrayList<Alarm> list = new ArrayList<Alarm>();
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.partner_clock_layout, container, false);
		listView = (ListView) view.findViewById(R.id.partner_clock_list);
		getActivity().getActionBar().setTitle("Partner Alarms");
		listView.setAdapter(new ClockAdapter(this.getActivity()));
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Fragment newContent = null;
				newContent = new AddPicForPartnerFragment();
				if (getActivity() instanceof MenuMainActivity) {
					MenuMainActivity mma = (MenuMainActivity) getActivity();
					mma.switchContent(newContent);
				} 
			}
		});
		
		
		return view;
	}
		
		//Button on action bar to add a photo
		

}


	
