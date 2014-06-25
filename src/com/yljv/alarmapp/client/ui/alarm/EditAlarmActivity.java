package com.yljv.alarmapp.client.ui.alarm;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.yljv.alarmapp.R;
import com.yljv.alarmapp.client.helper.MenuMainActivity;
import com.yljv.alarmapp.server.alarm.Alarm;
import com.yljv.alarmapp.server.alarm.MyAlarmManager;
import com.yljv.alarmapp.R.id;
import com.yljv.alarmapp.R.layout;
import com.yljv.alarmapp.R.menu;

public class EditAlarmActivity extends Activity implements OnTimeChangedListener, OnClickListener, OnTouchListener {
	
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
	TextView[] weekdays = new TextView[7];
	boolean[] scheduled = new boolean[7];
	int red = Color.parseColor("#ff0404");
	int turquoise = Color.parseColor("#90c5a9");
	MenuItem deleteAlarm;
	MenuItem cancelThisAlarm;
	MenuItem addAlarm;
	MenuItem saveAlarm;
	int i = 0;
	Uri chosenRingtoneUri;
	
	Alarm alarm;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Intent intent = getIntent();
		alarm = MyAlarmManager.findAlarmById(intent.getIntExtra("edit alarm", 0));
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_an_alarm);
		timePicker = (TimePicker) findViewById(R.id.timePicker);
		timePicker.setCurrentHour(alarm.getHour());
		timePicker.setCurrentMinute(alarm.getMinute());
		
		alarmName = (EditText) findViewById(R.id.alarm_name);
		alarmName.setText(alarm.getName());
		
		monday = (TextView) findViewById(R.id.mon);
		monday.setOnTouchListener(this);
		tuesday = (TextView) findViewById(R.id.tue);
		tuesday.setOnTouchListener(this);
		wednesday = (TextView) findViewById(R.id.wed);
		wednesday.setOnTouchListener(this);
		thursday = (TextView) findViewById(R.id.thu);
		thursday.setOnTouchListener(this);
		friday = (TextView) findViewById(R.id.fri);
		friday.setOnTouchListener(this);
		saturday = (TextView) findViewById(R.id.sat);
		saturday.setOnTouchListener(this);
		sunday = (TextView) findViewById(R.id.sun);
		sunday.setOnTouchListener(this);
		timePicker.setOnTimeChangedListener(this);
		ringtoneButton = (Button) findViewById(R.id.ringtone_button);
		ringtoneButton.setText(R.string.ringtone);
		ringtoneButton.setOnClickListener(this);
		
		boolean[] scheduledDays = new boolean[7];
		scheduledDays = alarm.getWeekdaysRepeated();
		for(int i = 0; i<scheduledDays.length - 1; i++) {
			if(scheduledDays[i] == true) {
				switch(i) {
				case 0:
					monday.setTextColor(red);
					break;
				case 1:
					tuesday.setTextColor(red);
					break;
				case 2: 
					wednesday.setTextColor(red);
					break;
				case 3: 
					thursday.setTextColor(red);
					break;
				case 4:
					friday.setTextColor(red);
					break;
				case 5:
					saturday.setTextColor(red);
					break;
				case 6:
					sunday.setTextColor(red);
					break;
				}
			}
		}
		
        getActionBar().setTitle(R.string.set_alarm);
        getActionBar().setDisplayHomeAsUpEnabled(true);
       
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}



	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		deleteAlarm = (MenuItem) menu.findItem(R.id.delete_alarm);
		addAlarm = (MenuItem) menu.findItem(R.id.add_alarm);
		cancelThisAlarm = (MenuItem) menu.findItem(R.id.cancel_alarm);
		saveAlarm = (MenuItem) menu.findItem(R.id.save_alarm);
		saveAlarm.setVisible(true);
		deleteAlarm.setVisible(false);
		addAlarm.setVisible(false);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.cancel_alarm) {
			super.onBackPressed();
		}
		if(item.getItemId() == android.R.id.home){
			super.onBackPressed();
		}
		if(item.getItemId() == R.id.save_alarm) {
			saveAlarm();
		}
		return super.onOptionsItemSelected(item);
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
		if(v.getId() == R.id.ringtone_button) {
				Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, chosenRingtoneUri);
				this.startActivityForResult(intent, 5);
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
		alarm.setVisible(true);
		for(int i = 0; i < 7; i++){
			if(scheduled[i]){
				alarm.setRepeat(i, true);
			}
		}
		MyAlarmManager.editAlarm(this, alarm);
		
		Intent intent = new Intent(this, MenuMainActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
	     if (resultCode == Activity.RESULT_OK && requestCode == 5) {
	          Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

	          if (uri != null) {
	              this.chosenRingtone = uri.toString();
	              chosenRingtoneUri = uri;
	              
	              //Uncomment this code if you want the button text to change to the 
	              //ringtone name
	              //Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
	              //ringtoneButton.setText(ringtone.getTitle(this));
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
	                monday.setTextColor(turquoise);
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
	                tuesday.setTextColor(turquoise);
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
	                wednesday.setTextColor(turquoise);
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
	                thursday.setTextColor(turquoise);
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
	                friday.setTextColor(turquoise);
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
	                saturday.setTextColor(turquoise);
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
	                sunday.setTextColor(turquoise);
	                scheduled[6] = false;
				}
			}
			break;
		}
		return false;
	}
}
