package com.yljv.alarmapp.ui;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.yljv.alarmapp.MenuMainActivity;
import com.yljv.alarmapp.R;

public class AddAlarmFragment extends Fragment implements OnTimeChangedListener, OnClickListener {
	
	public final static String ALARM_NAME = "com.yljv.alarmapp.ALARM_NAME";

	TimePicker timePicker;
	EditText alarmName;
	SeekBar volume;
	CheckBox checkbox_mon;
	CheckBox checkbox_tue;
	CheckBox checkbox_wed;
	CheckBox checkbox_thu;
	CheckBox checkbox_fri;
	CheckBox checkbox_sat;
	CheckBox checkbox_sun;
	Button saveButton;
	Button ringtoneButton;
	private String chosenRingtone;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_an_alarm, container, false);
		timePicker = (TimePicker) view.findViewById(R.id.timePicker);
		alarmName = (EditText) view.findViewById(R.id.alarm_name);
		checkbox_mon = (CheckBox) view.findViewById(R.id.checkbox_mon);
		checkbox_mon = (CheckBox) view.findViewById(R.id.checkbox_tue);
		checkbox_mon = (CheckBox) view.findViewById(R.id.checkbox_wed);
		checkbox_mon = (CheckBox) view.findViewById(R.id.checkbox_thu);
		checkbox_mon = (CheckBox) view.findViewById(R.id.checkbox_fri);
		checkbox_mon = (CheckBox) view.findViewById(R.id.checkbox_sat);
		checkbox_mon = (CheckBox) view.findViewById(R.id.checkbox_sun);	
		timePicker.setOnTimeChangedListener(this);
		saveButton = (Button) view.findViewById(R.id.save_button);
		saveButton.setOnClickListener(this);
		ringtoneButton = (Button) view.findViewById(R.id.ringtone_button);
		ringtoneButton.setText("Ringtone");
		ringtoneButton.setOnClickListener(this);
		return view;
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		Calendar then=Calendar.getInstance();

	    then.set(Calendar.HOUR_OF_DAY, hourOfDay);
	    then.set(Calendar.MINUTE, minute);
	  }
	
	/*public void onTimeSet(TimePicker view, int hourOfDat, int minute) {
		getTime();
	}
	*/

	private void getTime() {
		//TODO Parse
		/*timePicker.getMinute();
		timePicker.getHour();
		Boolean checkMon = checkbox_mon.isActivated();
		Boolean checkTue = checkbox_tue.isActivated();
		Boolean checkWed = checkbox_wed.isActivated();
		Boolean checkThu = checkbox_thu.isActivated();
		Boolean checkFri = checkbox_fri.isActivated();
		Boolean checkSat = checkbox_sat.isActivated();
		Boolean checkSun = checkbox_sun.isActivated();
			if (checkMon == true) {
				alarm();
			}
		*/
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ringtone_button:
				Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
				intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
				this.startActivityForResult(intent, 5);
				break;
			case R.id.save_button:
				saveAlarm();
				break;
		};
	}

	private void saveAlarm() {
		// TODO Auto-generated method stub
		String nameAlarm = alarmName.getText().toString();
		Fragment newContent = new MyAlarmListFragment();
		if (getActivity() instanceof MenuMainActivity) {
			MenuMainActivity mma = (MenuMainActivity) getActivity();
			mma.switchContent(newContent);
		} 
		Bundle data = new Bundle();
		data.putString(ALARM_NAME,nameAlarm);
		newContent.setArguments(data);
	}
	
	
	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
	     if (resultCode == Activity.RESULT_OK && requestCode == 5)
	     {
	          Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

	          if (uri != null)
	          {
	              this.chosenRingtone = uri.toString();
	              Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), uri);
	              ringtoneButton.setText(ringtone.getTitle(getActivity()));
	          }
	          else
	          {
	              this.chosenRingtone = null;
	          }
	      }            
	  }
}
