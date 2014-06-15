package com.yljv.alarmapp.client.ui.wakeup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fima.glowpadview.GlowPadView;
//import com.fima.glowpadview.GlowPadView;
//import com.fima.glowpadview.GlowPadView.OnTriggerListener;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.client.ui.start.ChoiceActivity;
/*
 * Window you see when you wake up
 * Should show you Picture/Message from your boyfriend/girlfriend
 */
/*
 * Window you see when you wake up
 * Should show you Picture/Message from your boyfriend/girlfriend
 */
import com.yljv.alarmapp.server.alarm.Alarm;
import com.yljv.alarmapp.server.alarm.AlarmInstance;
import com.yljv.alarmapp.server.alarm.MyAlarmManager;

public class WakeUpFragmentNoExtra extends Fragment implements
        GlowPadView.OnTriggerListener {

	private GlowPadView mGlowPadView;
	private TextView myTime;
	private TextView mornEv;

	MediaPlayer mpintro;

	String musicPath;

	int id;

	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.wakeup_layout_no_extra, container,
				false);
		mGlowPadView = (GlowPadView) view.findViewById(R.id.glow_pad_view);
		myTime = (TextView) view.findViewById(R.id.my_time);
		mornEv = (TextView) view.findViewById(R.id.morningEvening);

		mGlowPadView.setOnTriggerListener(this);

		// uncomment this to make sure the glowpad doesn'tuple vibrate on touch
		// mGlowPadView.setVibrateEnabled(false);

		// uncomment this to hide targets
		mGlowPadView.setShowTargetsOnIdle(true);

		Bundle bundle = this.getArguments();
		id = bundle.getInt(AlarmInstance.COLUMN_ID);

		Alarm alarm = MyAlarmManager.findAlarmById(id / 10 * 10);
		int hour = alarm.getHour();
		int minute = alarm.getMinute();

		String time;
		String hourS = (hour < 10) ? "0" + Integer.toString(hour) : Integer
				.toString(hour);
		String minuteS = (minute < 10) ? "0" + Integer.toString(minute)
				: Integer.toString(minute);
		time = hourS + ":" + minuteS;
		myTime.setText(time);
		String am_pm = (alarm.getTimeInMinutes() < 12 * 60) ? "AM" : "PM";
		mornEv.setText(am_pm);

		musicPath = alarm.getString(Alarm.COLUMN_MUSIC_URI);
		if (musicPath == null || musicPath.equals("")) {
			musicPath = "content://media/external/audio/media/11";
		}
		try {
			Uri alert = Uri.parse(musicPath);// RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			mpintro = new MediaPlayer();
			mpintro.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mpintro.setDataSource(this.getActivity(), alert);
			final AudioManager audioManager = (AudioManager) this.getActivity()
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
				mpintro.setAudioStreamType(AudioManager.STREAM_RING);
				mpintro.setLooping(true);
				mpintro.prepare();
				mpintro.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		new TakeScreenshotTask().execute();

		return view;
	}

	@Override
	public void onGrabbed(View v, int handle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReleased(View v, int handle) {
		mGlowPadView.ping();

	}

	@Override
	public void onTrigger(View v, int target) {
		final int resId = mGlowPadView.getResourceIdForTarget(target);
		switch (resId) {
		case R.drawable.snooze_progress1:
			// TODO snooze alarm
			Intent intent1 = new Intent(this.getActivity(),
					ChoiceActivity.class);
			startActivity(intent1);
			break;

		case R.drawable.checkmark:

			mpintro.stop();
			getActivity().finish();

			break;
		default:
			// Code should never reach here.
		}

	}

	@Override
	public void onGrabbedStateChange(View v, int handle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFinishFinalAnimation() {
		// TODO Auto-generated method stub

	}

	private class TakeScreenshotTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Log.e("WakeMeApp", "error", e);
			}
			takeScreenshot();
			return null;
		}
	}

	public void takeScreenshot() {
		View v = view.getRootView();
		v.setDrawingCacheEnabled(true);

		Bitmap bitmap = v.getDrawingCache();

		try {

			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(new Date());
			String imageFileName = "PNG_" + timeStamp + "_";

			File storageDir = new File(Environment
					.getExternalStorageDirectory().getAbsolutePath()
					+ "/WakeMeApp");
			storageDir.mkdirs();

			File image = File.createTempFile(imageFileName, ".png", storageDir);

			// Save a file: path for use with ACTION_VIEW intents
			String mCurrentPhotoPath = "file:" + image.getAbsolutePath();

			FileOutputStream fos = new FileOutputStream(image.getPath());
			bitmap.compress(Bitmap.CompressFormat.PNG, 85, fos);
			fos.flush();
			fos.close();

			new SingleMediaScanner(this.getActivity(), image);

		} catch (IOException e) {
			Log.e("WakeMeApp", "Exception", e);
		}

	}

}
