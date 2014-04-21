package com.yljv.alarmapp.ui;


import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.yljv.alarmapp.AddAlarmActivity;
import com.yljv.alarmapp.EditAlarmActivity;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.helper.ClockAdapter;
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

public class MyAlarmListFragment extends SherlockFragment {
	
	private ListView listView;
	public int myAlarmID;
	private MenuItem deleteAlarm;
	private MenuItem cancelAlarm;
	private MenuItem addAlarm;
	private int selectedPosition;
	private int count = 0;
	private boolean[] selectedItems;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.my_clock_layout, container, false);	
		listView = (ListView) view.findViewById(R.id.clock_list);

		ClockAdapter myAdapter = MyAlarmManager.getClockAdapter(this.getActivity());
		listView.setAdapter(myAdapter);	
		listView.setEmptyView(view.findViewById(R.id.empty_list));
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		selectedItems = new boolean[myAdapter.getCount()];
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				count++;
				deleteAlarm.setVisible(true);
				addAlarm.setVisible(false);
				cancelAlarm.setVisible(true);
				selectedPosition = position;
				listView.setItemChecked(position, true);
				selectedItems[position] = true;
				
				return true;
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(count != 0) {
					if(!listView.isItemChecked(position) && count == 1) {
						Log.e("AlarmApp", "count == 1 item checked");
						listView.setItemChecked(position, false);
						count--;
						deleteAlarm.setVisible(false);
						cancelAlarm.setVisible(false);
						addAlarm.setVisible(true);
						selectedItems[position] = false;
					}
					else if(listView.isItemChecked(position)) {
						Log.e("AlarmApp", "count != 0 item not checked");
						listView.setItemChecked(position, true);
						selectedItems[position] = true;
						count++;
					}
					else {
						Log.e("AlarmApp", "count != 0 item checked");
						listView.setItemChecked(position, false);
						selectedItems[position] = false;
						count--;
					}	
				}
				else{
					Log.e("AlarmApp", "count == 0");
					Alarm alarm = (Alarm) listView.getAdapter().getItem(position);
					int alarmID = alarm.getAlarmId();
					Intent intent = new Intent(getActivity(), EditAlarmActivity.class);
					intent.putExtra("edit alarm", alarmID);
					listView.setItemChecked(position, false);
					startActivity(intent);
					/*
					EditAlarmFragment editAlarm = new EditAlarmFragment();
					Bundle bundle = new Bundle();
					bundle.putInt("edit alarm", alarmID);
					editAlarm.setArguments(bundle);
					if (getActivity() instanceof MenuMainActivity) {
						listView.setItemChecked(position, false);
						MenuMainActivity mma = (MenuMainActivity) getActivity();
						mma.switchContent(editAlarm);
					} 
					*/
				}
			}
		});
		
		return view; 
	}
	
	public boolean[] getSelected() {
		return this.selectedItems;
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
		deleteAlarm = (MenuItem) menu.findItem(R.id.delete_alarm);
		addAlarm = (MenuItem) menu.findItem(R.id.add_alarm);
		cancelAlarm = (MenuItem) menu.findItem(R.id.cancel_alarm);
		MenuItem saveAlarm = (MenuItem) menu.findItem(R.id.save_alarm);
		saveAlarm.setVisible(false);
		deleteAlarm.setVisible(false);
		cancelAlarm.setVisible(false);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		//getActivity().invalidateOptionsMenu();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.add_alarm:
			Intent intent = new Intent(this.getActivity(), AddAlarmActivity.class);
			getActivity().startActivity(intent);
			/*
			if (getActivity() instanceof MenuMainActivity) {
				MenuMainActivity mma = (MenuMainActivity) getActivity();
				mma.switchContent(newContent);
			} 
			*/
			break;
		case R.id.cancel_alarm:
			for(int i = 0; i < selectedItems.length; i++) {
				if(selectedItems[i] == true) {
					selectedItems[i] = false;
					listView.setItemChecked(i, false);
				}
			}
			deleteAlarm.setVisible(false);
			cancelAlarm.setVisible(false);
			addAlarm.setVisible(true);
			break;
		case R.id.delete_alarm:
			AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this.getActivity());
			if(count > 1) {
				deleteDialog.setMessage("Delete selected alarms?");
			}
			else {
				deleteDialog.setMessage("Delete selected alarm?");
			}
			deleteDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					/*ClockAdapter myAdapter = (ClockAdapter)listView.getAdapter();
					Alarm myAlarm = myAdapter.getItem(selectedPosition);
				    myAdapter.remove(myAlarm);
				    myAdapter.notifyDataSetChanged();
				    deleteAlarm.setVisible(false);*/
					ClockAdapter myAdapter = (ClockAdapter)listView.getAdapter();
					ArrayList<Alarm> alarms = new ArrayList<Alarm>();
					for(int i = 0; i < getSelected().length; i++) {
						if(selectedItems[i] == true) {
							alarms.add(myAdapter.getItem(i)); 
						}
					}
					for(Alarm alarm : alarms){
						myAdapter.remove(alarm);
					    MyAlarmManager.deleteAlarm(alarm);
					    myAdapter.notifyDataSetChanged();
					}
					deleteAlarm.setVisible(false);
					cancelAlarm.setVisible(false);
					addAlarm.setVisible(true);
				}
			});
			deleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
		    deleteDialog.show();
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
}	

