package com.yljv.alarmapp.client.ui.menu;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.client.helper.MenuMainActivity;
import com.yljv.alarmapp.client.ui.SettingsFragment;
import com.yljv.alarmapp.client.ui.alarm.MyAlarmListFragment;
import com.yljv.alarmapp.client.ui.alarm.PartnerAlarmFragment;
import com.yljv.alarmapp.client.ui.gallery.GalleryFragment;

public class MenuList extends SherlockListFragment {
	
	String[] menu = new String[4];
	
	//TODO make a method that takes partner's name for an item in the menu
	/*
	String menuItem1 = context.getResources().getString(R.string.me);
	String menuItem2 = context.getResources().getString(R.string.buddy);
	String menuItem3 = context.getResources().getString(R.string.gallery);
	String menuItem4 = context.getResources().getString(R.string.settings);
	String[] menu = {menuItem1, menuItem2, menuItem3, menuItem4};
	*/
	
	 public MenuList(){
         super();
         Context context = getActivity();
         menu[0] = context.getResources().getString(R.string.me);

         menu[1] = context.getResources().getString(R.string.buddy);

         menu[2] = context.getResources().getString(R.string.gallery);

         menu[3] = context.getResources().getString(R.string.settings);
         
     }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list, container, false);
		TextView name = (TextView) view.findViewById(R.id.name_id);
		name.setText(Html.fromHtml("<b>wakeme</b>app"));
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getActivity(),
				R.layout.menu_layout, menu);
		
		setListAdapter(menuAdapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Fragment newContent = null; 
		switch(position) {
		case 0: 
			newContent = new MyAlarmListFragment();
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
	
}