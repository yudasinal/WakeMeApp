package com.yljv.alarmapp.ui;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

public class MyAlarmListFragment extends SherlockFragment {
	
	private ListView listView;
	public final static String  EDIT_ALARM = "com.yljv.alarmapp.ui.EDIT_ALARM";
	ArrayList<Alarm> list = new ArrayList<Alarm>();
	public int myAlarmID;
	private MenuItem deleteAlarm;
	private int selectedPosition;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
		View view = inflater.inflate(R.layout.my_clock_layout, container, false);	
		listView = (ListView) view.findViewById(R.id.clock_list);

		ClockAdapter myAdapter = new ClockAdapter(this.getActivity());
		listView.setAdapter(myAdapter);	
		listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				deleteAlarm.setVisible(true);
				selectedPosition = position;
				listView.setItemChecked(position, true);
				return true;
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
					deleteAlarm.setVisible(false);
			}
			
		});
		

		//listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		//listView.setSelector(R.drawable.background_list_item);
		/*
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Alarm myAlarm = (Alarm) parent.getSelectedItem();
				EditAlarmFragment editAlarm = new EditAlarmFragment();
				myAlarmID = myAlarm.getAlarmId();
				Bundle bundle = new Bundle();
				bundle.putInt(EDIT_ALARM, myAlarmID);
				editAlarm.setArguments(bundle);
				if (getActivity() instanceof MenuMainActivity) {
					MenuMainActivity mma = (MenuMainActivity) getActivity();
					mma.switchContent(editAlarm);
				} 
			}
		});
		*/
		
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
		deleteAlarm.setVisible(false);
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
		case R.id.edit_alarm:
			newContent = new EditAlarmFragment();
			if (getActivity() instanceof MenuMainActivity) {
				MenuMainActivity mma = (MenuMainActivity) getActivity();
				mma.switchContent(newContent);
			}
			break;
		case R.id.delete_alarm:
			AlertDialog.Builder deleteDialog = new AlertDialog.Builder(this.getActivity());
			deleteDialog.setMessage("Delete selected alarm?");
			deleteDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ClockAdapter myAdapter = (ClockAdapter)listView.getAdapter();
				    myAdapter.remove(myAdapter.getItem(selectedPosition));
				    myAdapter.notifyDataSetChanged();
				    deleteAlarm.setVisible(false);
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

