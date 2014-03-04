package com.yljv.alarmapp.ui;


import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.yljv.alarmapp.MenuMainActivity;
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

		ClockAdapter myAdapter = new ClockAdapter(this.getActivity());
		listView.setAdapter(myAdapter);	
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
					int alarmPosition = parent.getSelectedItemPosition();
					EditAlarmFragment editAlarm = new EditAlarmFragment();
					Bundle bundle = new Bundle();
					bundle.putInt("edit alarm", alarmPosition);
					editAlarm.setArguments(bundle);
					if (getActivity() instanceof MenuMainActivity) {
						MenuMainActivity mma = (MenuMainActivity) getActivity();
						mma.switchContent(editAlarm);
					} 
				}
			}
		});
		
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
		deleteAlarm = (MenuItem) menu.findItem(R.id.delete_alarm);
		addAlarm = (MenuItem) menu.findItem(R.id.add_alarm);
		cancelAlarm = (MenuItem) menu.findItem(R.id.cancel_alarm);
		deleteAlarm.setVisible(false);
		cancelAlarm.setVisible(false);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		getActivity().invalidateOptionsMenu();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Fragment newContent = null;
		switch(item.getItemId()) {
		case R.id.add_alarm:
			newContent = new AddAlarmFragment();
			if (getActivity() instanceof MenuMainActivity) {
				MenuMainActivity mma = (MenuMainActivity) getActivity();
				mma.switchContent(newContent);
			} 
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
				    Alarm myAlarm = myAdapter.getItem(selectedPosition);
				    MyAlarmManager.deleteAlarm(myAlarm);
				    myAdapter.notifyDataSetChanged();
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

