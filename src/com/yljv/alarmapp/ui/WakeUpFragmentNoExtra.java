package com.yljv.alarmapp.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fima.glowpadview.GlowPadView;
import com.fima.glowpadview.GlowPadView.OnTriggerListener;
import com.yljv.alarmapp.AddPicForPartnerActivity;
import com.yljv.alarmapp.ChoiceActivity;
//import com.fima.glowpadview.GlowPadView;
//import com.fima.glowpadview.GlowPadView.OnTriggerListener;
import com.yljv.alarmapp.R;
/*
 * Window you see when you wake up
 * Should show you Picture/Message from your boyfriend/girlfriend
 */
/*
 * Window you see when you wake up
 * Should show you Picture/Message from your boyfriend/girlfriend
 */
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.parse.database.AlarmInstance;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

public class WakeUpFragmentNoExtra extends Fragment implements OnTriggerListener {

	
	private GlowPadView mGlowPadView;
	private TextView myTime;
	private TextView mornEv;
	

	MediaPlayer mpintro;
	
	String musicPath;
	
	int id;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.wakeup_layout_no_extra, container, false);
		mGlowPadView = (GlowPadView) view.findViewById(R.id.glow_pad_view);
		myTime = (TextView) view.findViewById(R.id.my_time);
		mornEv = (TextView) view.findViewById(R.id.morningEvening);

		mGlowPadView.setOnTriggerListener(this);
		
		
		// uncomment this to make sure the glowpad doesn't vibrate on touch
		// mGlowPadView.setVibrateEnabled(false);
		
		// uncomment this to hide targets
		mGlowPadView.setShowTargetsOnIdle(true);
		

		Bundle bundle = this.getArguments();
		id = bundle.getInt(AlarmInstance.COLUMN_ID);
		
		Alarm alarm = MyAlarmManager.findAlarmById(id / 10 * 10);
		
		musicPath = alarm.getString(Alarm.COLUMN_MUSIC_URI);
		if(musicPath.equals("")){
			musicPath = "/internal/audio/media/9";
		}
		
		mpintro = MediaPlayer.create(this.getActivity(), Uri.parse(musicPath));
		mpintro.setLooping(true);
		mpintro.start();
		
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
			//TODO snooze alarm 
			Intent intent1 = new Intent(this.getActivity(), ChoiceActivity.class);
			startActivity(intent1);break;

		case R.drawable.checkmark:
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
	

}
