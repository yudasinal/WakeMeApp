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

public class EditAlarmFragment extends SherlockFragment implements
		OnTimeChangedListener, OnClickListener, OnTouchListener {

	public final static String ALARM_NAME = "com.yljv.alarmapp.ALARM_NAME";
	private final static int red = Color.parseColor("#ff0404");
	private final static int tintedRed = Color.parseColor("#ffc4a4");

	// TODO default ringtone
	private Uri ringtone;
	public int changedHour;
	public int changedMinute;
	public int currentDay;
	boolean[] scheduled = new boolean[7];

	TimePicker timePicker;
	EditText alarmName;
	SeekBar volume;
	Button ringtoneButton;
	TextView[] weekdays = new TextView[7];
	Button setAlarm;
	Button cancelAlarm;

	Alarm alarm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_an_alarm, container, false);

		Bundle bundle = this.getArguments();
		alarm = MyAlarmManager.findAlarmById(bundle.getInt("edit alarm"));

		timePicker = (TimePicker) view.findViewById(R.id.timePicker);
		timePicker.setCurrentHour(alarm.getHour());
		timePicker.setCurrentMinute(alarm.getMinute());
		setAlarm = (Button) view.findViewById(R.id.set_alarm);
		cancelAlarm = (Button) view.findViewById(R.id.cancel_alarm);
		setAlarm.setOnClickListener(this);
		cancelAlarm.setOnClickListener(this);
		alarmName = (EditText) view.findViewById(R.id.alarm_name);
		alarmName.setText(alarm.getName());

		weekdays[0] = (TextView) view.findViewById(R.id.mon);
		weekdays[0].setOnTouchListener(this);
		weekdays[1] = (TextView) view.findViewById(R.id.tue);
		weekdays[1].setOnTouchListener(this);
		weekdays[2] = (TextView) view.findViewById(R.id.wed);
		weekdays[2].setOnTouchListener(this);
		weekdays[3] = (TextView) view.findViewById(R.id.thu);
		weekdays[3].setOnTouchListener(this);
		weekdays[4] = (TextView) view.findViewById(R.id.fri);
		weekdays[4].setOnTouchListener(this);
		weekdays[5] = (TextView) view.findViewById(R.id.sat);
		weekdays[5].setOnTouchListener(this);
		weekdays[6] = (TextView) view.findViewById(R.id.sun);
		weekdays[6].setOnTouchListener(this);
		timePicker.setOnTimeChangedListener(this);
		ringtoneButton = (Button) view.findViewById(R.id.ringtone_button);
		ringtoneButton.setText("Ringtone");
		ringtoneButton.setOnClickListener(this);

		scheduled = alarm.getWeekdaysRepeated();
		for (int i = 0; i < 7; i++) {
			if(scheduled[i]) weekdays[i].setTextColor(red);
		}
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getActionBar().setTitle("Edit alarm");
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		Calendar then = Calendar.getInstance();
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
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
					RingtoneManager.TYPE_ALARM);
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
					(Uri) null);
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

		// TODO what do I need this for?
		/*
		 * Bundle data = new Bundle(); data.putString(ALARM_NAME, nameAlarm);
		 * newContent.setArguments(data);
		 */

		// TODO set volume, repeated, etc..
		alarm.setName(nameAlarm);
		alarm.setActivated(true);
		alarm.setTime(timePicker.getCurrentHour(),
				timePicker.getCurrentMinute());
		if (ringtone != null) {
			alarm.setMusicURI(ringtone);
		}
		for (int i = 0; i < 7; i++) {
			alarm.setRepeat(i, scheduled[i]);
		}

		MyAlarmManager.editAlarm(this.getActivity(), alarm);

		if (getActivity() instanceof MenuMainActivity) {
			MenuMainActivity mma = (MenuMainActivity) getActivity();
			mma.switchContent(newContent);
		}

	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode,
			final Intent intent) {
		if (resultCode == Activity.RESULT_OK && requestCode == 5) {
			Uri uri = intent
					.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

			if (uri != null) {
				ringtone = uri;
				Ringtone ringtone = RingtoneManager.getRingtone(getActivity(),
						uri);
				ringtoneButton.setText(ringtone.getTitle(getActivity()));
			} else {
				ringtone = null;
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch (v.getId()) {
			case R.id.mon:
				if (!scheduled[0]) {
					weekdays[0].setTextColor(red);
					scheduled[0] = true;
				} else {
					weekdays[0].setTextColor(tintedRed);
					scheduled[0] = false;
				}
				break;
			case R.id.tue:
				if (!scheduled[1]) {
					weekdays[1].setTextColor(red);
					scheduled[1] = true;
				} else {
					weekdays[1].setTextColor(tintedRed);
					scheduled[1] = false;
				}
				break;
			case R.id.wed:
				if (!scheduled[2]) {
					weekdays[2].setTextColor(red);
					scheduled[2] = true;
				} else {
					weekdays[2].setTextColor(tintedRed);
					scheduled[2] = false;
				}
				break;
			case R.id.thu:
				if (!scheduled[3]) {
					weekdays[3].setTextColor(red);
					scheduled[3] = true;
				} else {
					weekdays[3].setTextColor(tintedRed);
					scheduled[3] = false;
				}
				break;
			case R.id.fri:
				if (!scheduled[4]) {
					weekdays[4].setTextColor(red);
					scheduled[4] = true;
				} else {
					weekdays[4].setTextColor(tintedRed);
					scheduled[4] = false;
				}
				break;
			case R.id.sat:
				if (!scheduled[5]) {
					weekdays[5].setTextColor(red);
					scheduled[5] = true;
				} else {
					weekdays[5].setTextColor(tintedRed);
					scheduled[5] = false;
				}
				break;
			case R.id.sun:
				if (!scheduled[6]) {
					weekdays[6].setTextColor(red);
					scheduled[6] = true;
				} else {
					weekdays[6].setTextColor(tintedRed);
					scheduled[6] = false;
				}
				break;
			}
		}

		return false;
	}

	/*
	 * @Override public void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); setHasOptionsMenu(true); }
	 * 
	 * public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	 * inflater.inflate(R.menu.main, menu);
	 * //menu.findItem(R.id.add_alarm).setVisible(true);
	 * //menu.findItem(R.id.cancel_alarm).setVisible(true);
	 * menu.findItem(R.id.cancel_alarm).setEnabled(true);
	 * menu.findItem(R.id.add_alarm).setEnabled(false);
	 * menu.findItem(R.id.add_alarm).setVisible(false);
	 * menu.findItem(R.id.save_alarm).setEnabled(true);
	 * 
	 * }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) {
	 * switch(item.getItemId()) { case R.id.save_alarm: saveAlarm(); break; case
	 * R.id.cancel_alarm: getFragmentManager().popBackStackImmediate(); break; }
	 * return super.onOptionsItemSelected(item); }
	 */
}
