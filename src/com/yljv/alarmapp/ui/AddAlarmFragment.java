package com.yljv.alarmapp.ui;

import java.util.Calendar;

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
		// TODO Auto-generated method stub
		saveAlarm();
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
	
}
