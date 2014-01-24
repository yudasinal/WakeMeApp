package com.yljv.alarmapp.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.MenuItem;
import com.yljv.alarmapp.MenuMainActivity;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.helper.ClockAdapter;
import com.yljv.alarmapp.parse.database.Alarm;



public class MyAlarmListFragment extends SherlockFragment {
	
	public ListView listView;
	ArrayList<Alarm> list = new ArrayList<Alarm>(); 
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.my_clock_layout, container, false);	
		listView = (ListView) view.findViewById(R.id.clock_list);
		listView.setAdapter(new ClockAdapter(this.getActivity()));	
		return view; 
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.add_alarm:
			//Fragment newContent = new AddPicForPartnerFragment();
			Fragment newContent = new AddPicForPartnerFragment();
			if (getActivity() instanceof MenuMainActivity) {
				MenuMainActivity mma = (MenuMainActivity) getActivity();
				mma.switchContent(newContent);
			} 
		}
		return super.onOptionsItemSelected(item);
	}
	/*
	public void onListItemClick(ListView l, View v, int position, long id) {
		Fragment newContent = null; 
		switch(position) {
		case 0: 
			newContent = new MyAlarmFragment();
			break;
		case 1: 
			newContent = new PartnerAlarmFragment();
			break;
		case 2: 
			newContent = new GalleryFragment();
			break;
		case 3: 
			newContent = new SettingsFragment();
			break;
		}
		if (newContent != null) {
			switchFragment(newContent);
		}
	}
	
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null) {
			return;
		}
		
		if (getActivity() instanceof MenuMainActivity) {
			MenuMainActivity mma = (MenuMainActivity) getActivity();
			mma.switchContent(fragment);
		} 
	}
	*/

}	

