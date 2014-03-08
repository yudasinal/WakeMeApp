package com.yljv.alarmapp.ui;

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
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.yljv.alarmapp.MenuMainActivity;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

public class AddAlarmFragment extends SherlockFragment implements OnTimeChangedListener, OnClickListener, OnTouchListener {
	
	public final static String ALARM_NAME = "com.yljv.alarmapp.ALARM_NAME";
	private String chosenRingtone;
	public int changedHour;
	public int changedMinute;

	TimePicker timePicker;
	EditText alarmName;
	SeekBar volume;
	Button ringtoneButton;
	TextView monday;
	TextView tuesday;
	TextView wednesday;
	TextView thursday;
	TextView friday;
	TextView saturday;
	TextView sunday;
	Button setAlarm;
	Button cancelAlarm;
	boolean[] scheduled = new boolean[7];
	int red = Color.parseColor("#ff0404");
	int tintedRed = Color.parseColor("#ffc4a4");
	
	Alarm alarm;
	
	
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
		monday.setOnTouchListener(this);
		tuesday = (TextView) view.findViewById(R.id.tue);
		tuesday.setOnTouchListener(this);
		wednesday = (TextView) view.findViewById(R.id.wed);
		wednesday.setOnTouchListener(this);
		thursday = (TextView) view.findViewById(R.id.thu);
		thursday.setOnTouchListener(this);
		friday = (TextView) view.findViewById(R.id.fri);
		friday.setOnTouchListener(this);
		saturday = (TextView) view.findViewById(R.id.sat);
		saturday.setOnTouchListener(this);
		sunday = (TextView) view.findViewById(R.id.sun);
		sunday.setOnTouchListener(this);
		timePicker.setOnTimeChangedListener(this);
		ringtoneButton = (Button) view.findViewById(R.id.ringtone_button);
		ringtoneButton.setText("Ringtone");
		ringtoneButton.setOnClickListener(this);
		
		alarm = new Alarm();
		alarm.initialize();
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getActivity().getActionBar().setTitle("Set an alarm");
       
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		Calendar then=Calendar.getInstance();

	    then.set(Calendar.HOUR_OF_DAY, hourOfDay);
	    then.set(Calendar.MINUTE, minute);
	    changedHour = view.getCurrentHour();
	    changedMinute = view.getCurrentMinute();
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
		String nameAlarm = alarmName.getText().toString();
		Fragment newContent = new MyAlarmListFragment();
		Bundle data = new Bundle();
		data.putString(ALARM_NAME,nameAlarm);
		newContent.setArguments(data);
		
		alarm.setTime(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
		alarm.setName(nameAlarm);
		for(int i = 0; i < 7; i++){
			if(scheduled[i]){
				alarm.setRepeat(i, true);
			}
		}
		MyAlarmManager.setNewAlarm(this.getActivity(), alarm);
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
	 * Activates or deactivates a day to schedule the alarm. Colour of letter changes onTouch
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(v.getId()) {
		case R.id.mon:
			if(!scheduled[0]){
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                monday.setTextColor(red);
	                scheduled[0] = true;
				}
			}
			else{
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                monday.setTextColor(tintedRed);
	                scheduled[0] = false;
				}
			}
			break;
		case R.id.tue:
			if(!scheduled[1]){
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                tuesday.setTextColor(red);
	                scheduled[1] = true;
				}
			}
			else{
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                tuesday.setTextColor(tintedRed);
	                scheduled[1] = false;
				}
			}
			break;
		case R.id.wed:
			if(!scheduled[2]){
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                wednesday.setTextColor(red);
	                scheduled[2] = true;
				}
			}
			else{
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                wednesday.setTextColor(tintedRed);
	                scheduled[2] = false;
				}
			}
			break;
		case R.id.thu:
			if(!scheduled[3]){
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                thursday.setTextColor(red);
	                scheduled[3] = true;
				}
			}
			else{
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                thursday.setTextColor(tintedRed);
	                scheduled[3] = false;
				}
			}
			break;
		case R.id.fri:
			if(!scheduled[4]){
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                friday.setTextColor(red);
	                scheduled[4] = true;
				}
			}
			else{
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                friday.setTextColor(tintedRed);
	                scheduled[4] = false;
				}
			}
			break;
		case R.id.sat:
			if(!scheduled[5]){
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                saturday.setTextColor(red);
	                scheduled[5] = true;
				}
			}
			else{
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                saturday.setTextColor(tintedRed);
	                scheduled[5] = false;
				}
			}
			break;
		case R.id.sun:
			if(!scheduled[6]){
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                sunday.setTextColor(red);
	                scheduled[6] = true;
				}
			}
			else{
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
	                sunday.setTextColor(tintedRed);
	                scheduled[6] = false;
				}
			}
			break;
		}
		return false;
	}
}
