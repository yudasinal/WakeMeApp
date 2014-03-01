package com.yljv.alarmapp.ui;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.yljv.alarmapp.MenuMainActivity;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

public class EditAlarmFragment extends SherlockFragment implements OnTimeChangedListener, OnClickListener {
	
	public final static String ALARM_NAME = "com.yljv.alarmapp.ALARM_NAME";
	private String chosenRingtone;
	public int changedHour;
	public int changedMinute;
	public int currentDay;
	public boolean repeatAlarm = false;

	TimePicker timePicker;
	EditText alarmName;
	SeekBar volume;
	Button ringtoneButton;
	TextView monday;
	TextView tuesday;
	TextView wednesday;
	Button setAlarm;
	Button cancelAlarm;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_an_alarm, container, false);
		timePicker = (TimePicker) view.findViewById(R.id.timePicker);
		setAlarm = (Button) view.findViewById(R.id.set_alarm);
		cancelAlarm = (Button) view.findViewById(R.id.cancel_alarm);
		setAlarm.setOnClickListener(this);
		cancelAlarm.setOnClickListener(this);
		alarmName = (EditText) view.findViewById(R.id.alarm_name);
		monday = (TextView) view.findViewById(R.id.mon);
		monday.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				boolean scheduled = false;
				if(!scheduled){
					switch(event.getAction()) {
					case MotionEvent.ACTION_DOWN:
	                    monday.setTextColor(Color.BLUE);
	                    scheduled = true;
	                    break;
					}
				}
				else{
					switch(event.getAction()) {
					case MotionEvent.ACTION_UP:
	                    monday.setTextColor(Color.BLACK);
	                    scheduled = true;
	                    break;
					}
				}
				return false;
			}
		});
		tuesday = (TextView) view.findViewById(R.id.tue);
		tuesday.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				boolean scheduled  = false;
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
                    tuesday.setTextColor(Color.BLUE);
                    scheduled = true;
                    break;
				}
				return false;
			}
		});
		wednesday = (TextView) view.findViewById(R.id.wed);
		wednesday.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				boolean scheduled = true;
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
                    wednesday.setTextColor(Color.BLUE);
                    scheduled = true;
                    break;
				}
				return false;
			}
		});
		timePicker.setOnTimeChangedListener(this);
		ringtoneButton = (Button) view.findViewById(R.id.ringtone_button);
		ringtoneButton.setText("Ringtone");
		ringtoneButton.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getActivity().getActionBar().setTitle("Set an alarm");
       
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		Calendar then=Calendar.getInstance();
	    then.set(Calendar.HOUR_OF_DAY, hourOfDay);
	    then.set(Calendar.MINUTE, minute);
	    changedHour = view.getCurrentHour();
	    changedMinute = view.getCurrentMinute();
	    currentDay = then.get(Calendar.DAY_OF_WEEK);
	  }
		

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ringtone_button:
				Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
				this.startActivityForResult(intent, 5);
				break;
			case R.id.set_alarm:
				getFragmentManager().popBackStackImmediate();
				break;
			case R.id.cancel_alarm:
				saveAlarm();
				break;
		}
	}

	private void saveAlarm() {
		// TODO Auto-generated method stub
		String nameAlarm = alarmName.getText().toString();
		Fragment newContent = new MyAlarmListFragment();
		Bundle data = new Bundle();
		data.putString(ALARM_NAME,nameAlarm);
		newContent.setArguments(data);
		
		//TODO save alarm
		//MyAlarmManager.setAlarm(getActivity(), currentDay, changedHour, changedMinute, nameAlarm, repeatAlarm);
		if (getActivity() instanceof MenuMainActivity) {
			MenuMainActivity mma = (MenuMainActivity) getActivity();
			mma.switchContent(newContent);
		} 
		
	}
	
	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
	     if (resultCode == Activity.RESULT_OK && requestCode == 5) {
	          Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

	          if (uri != null) {
	              this.chosenRingtone = uri.toString();
	              Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), uri);
	              ringtoneButton.setText(ringtone.getTitle(getActivity()));
	          }
	          else {
	              this.chosenRingtone = null;
	          }
	      }            
	  }
	
	/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		//menu.findItem(R.id.add_alarm).setVisible(true);
		//menu.findItem(R.id.cancel_alarm).setVisible(true);	
		menu.findItem(R.id.cancel_alarm).setEnabled(true);
		menu.findItem(R.id.add_alarm).setEnabled(false);
		menu.findItem(R.id.add_alarm).setVisible(false);
		menu.findItem(R.id.save_alarm).setEnabled(true);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.save_alarm:
			saveAlarm();
			break;
		case R.id.cancel_alarm:
			getFragmentManager().popBackStackImmediate();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	*/
}
