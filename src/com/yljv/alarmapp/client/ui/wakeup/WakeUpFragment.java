package com.yljv.alarmapp.client.ui.wakeup;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fima.glowpadview.GlowPadView;
import com.fima.glowpadview.GlowPadView.OnTriggerListener;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.client.ui.start.ChoiceActivity;
/*
 * Window you see when you wake up
 * Should show you Picture/Message from your boyfriend/girlfriend
 */
import com.yljv.alarmapp.server.alarm.Alarm;
import com.yljv.alarmapp.server.alarm.AlarmInstance;
import com.yljv.alarmapp.server.alarm.MsgPictureTuple;
import com.yljv.alarmapp.server.alarm.MyAlarmManager;

import java.util.GregorianCalendar;

public class WakeUpFragment extends Fragment implements OnTriggerListener {

	private GlowPadView mGlowPadView;
	private TextView myTime;
	private TextView mornEv;

	private String musicPath;

	MediaPlayer mpintro;
	Vibrator vibrator;

	View view;

    long timeMillis;
	
	private int id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

        timeMillis = GregorianCalendar.getInstance().getTimeInMillis() + 300000;

		view = inflater.inflate(R.layout.wake_up_layout, container, false);
		mGlowPadView = (GlowPadView) view.findViewById(R.id.glow_pad_view);
		myTime = (TextView) view.findViewById(R.id.my_time);
		mornEv = (TextView) view.findViewById(R.id.morningEvening);

		Bundle bundle = this.getArguments();
		id = bundle.getInt(AlarmInstance.COLUMN_ID);


		myTime.setText(((WakeUpActivity)getActivity()).getTimeAsString());
		mornEv.setText(((WakeUpActivity)getActivity()).getAmPm());

        musicPath = ((WakeUpActivity) getActivity()).alarm.getString(Alarm.COLUMN_MUSIC_URI);
        if(musicPath == null || musicPath.equals("")){
            musicPath = "content://media/external/audio/media/11";
        }
        
        MsgPictureTuple tuple = ((WakeUpActivity) getActivity()).getTuple();
        if(tuple== null || (tuple.getMsg()==null)&&(tuple.getPicData()==null)){
            mGlowPadView.setTargetResources(R.array.snooze_dismiss_drawables_noextra);
        }

		mGlowPadView.setOnTriggerListener(this);

		// uncomment this to make sure the glowpad doesn'tuple vibrate on touch
		// mGlowPadView.setVibrateEnabled(false);

		// uncomment this to hide targets
		mGlowPadView.setShowTargetsOnIdle(true);

		try{
			 Uri alert =  Uri.parse(musicPath);//RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			 mpintro = new MediaPlayer();
			 mpintro.setAudioStreamType(AudioManager.STREAM_MUSIC);
			 mpintro.setDataSource(this.getActivity(), alert);
			  final AudioManager audioManager = (AudioManager) this.getActivity().getSystemService(Context.AUDIO_SERVICE);
			 if (audioManager.getStreamVolume(AudioManager.STREAM_RING) != 0) {
				 mpintro.setAudioStreamType(AudioManager.STREAM_RING);
			 mpintro.setLooping(true);
			 mpintro.prepare();
			 mpintro.start();
			 }
		}catch(Exception e){
			e.printStackTrace();
		}
		
		vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
		
		Thread mpintroThread = new Thread(){
			@Override
			public void run(){
				mpintro.start();
			}
		};
		Thread vibratorThread = new Thread(){
			@Override
			public void run(){
				long[] pattern = { 0, 200, 500 };
				vibrator.vibrate(pattern, 0);
			}
		};
		mpintroThread.start();
		vibratorThread.start();
		 
		 return view; 
		 
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
			stopAlarm();
            MyAlarmManager.snoozeAlarm(id, timeMillis);
            android.os.Process.killProcess(android.os.Process.myPid());
			break;
		case R.drawable.checkmark:
			stopAlarm();
			MyAlarmManager.setNextAlarmInstance(((WakeUpActivity) getActivity()).alarm);
            android.os.Process.killProcess(android.os.Process.myPid());
			break;
		case R.drawable.pic_msg:
			MyAlarmManager.setNextAlarmInstance(((WakeUpActivity) getActivity()).alarm);
			Fragment newContent = new PicMsgArrivedFragment();
			Bundle bundle = new Bundle();
			bundle.putInt(AlarmInstance.COLUMN_ID, id);
			newContent.setArguments(bundle);
			if (getActivity() instanceof WakeUpActivity) {
				WakeUpActivity mma = (WakeUpActivity) getActivity();
				mma.switchContent(newContent);
			}
			stopAlarm();
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

	@Override
	public void onGrabbed(View v, int handle) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		stopAlarm();
	}
	
	public void stopAlarm(){

		mpintro.stop();
		vibrator.cancel();
	}

}
