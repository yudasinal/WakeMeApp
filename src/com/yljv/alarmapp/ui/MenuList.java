package com.yljv.alarmapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.yljv.alarmapp.MenuMainActivity;
import com.yljv.alarmapp.MyAlarmFragment;
import com.yljv.alarmapp.R;

public class MenuList extends SherlockListFragment {
	
	//TODO make a method that takes partner's name for an item in the menu
	String[] menu = {"My Alarms", "His Alarms", "Settings"};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, menu);
		setListAdapter(menuAdapter);
	}

	@Override
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
	
}
