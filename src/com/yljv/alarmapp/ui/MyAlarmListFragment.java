package com.yljv.alarmapp.ui;

import java.util.ArrayList;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
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
		ActionBar myBar = getActivity().getActionBar();
		myBar.setTitle("My Alarms");
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		getActivity().invalidateOptionsMenu();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.add_alarm:
			Fragment newContent = new AddAlarmFragment();
			if (getActivity() instanceof MenuMainActivity) {
				MenuMainActivity mma = (MenuMainActivity) getActivity();
				mma.switchContent(newContent);
			} 
		}
		return super.onOptionsItemSelected(item);
	}
	
}	

