package com.yljv.alarmapp.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fima.glowpadview.GlowPadView;
import com.fima.glowpadview.GlowPadView.OnTriggerListener;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.yljv.alarmapp.ChoiceActivity;
//import com.fima.glowpadview.GlowPadView;
//import com.fima.glowpadview.GlowPadView.OnTriggerListener;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.WakeUpActivity;
import com.yljv.alarmapp.helper.MsgPictureTuple;
import com.yljv.alarmapp.parse.database.Alarm;
/*
 * Window you see when you wake up
 * Should show you Picture/Message from your boyfriend/girlfriend
 */
import com.yljv.alarmapp.parse.database.AlarmInstance;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

public class WakeUpFragment extends Fragment implements OnTriggerListener {

	private GlowPadView mGlowPadView;
	private TextView myTime;
	private TextView mornEv;

	private ParseFile pic;
	private String musicPath;
	private String message;

	MediaPlayer mpintro;

	View view;
	
	private int id;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.wake_up_layout, container, false);
		mGlowPadView = (GlowPadView) view.findViewById(R.id.glow_pad_view);
		myTime = (TextView) view.findViewById(R.id.my_time);
		mornEv = (TextView) view.findViewById(R.id.morningEvening);

		Bundle bundle = this.getArguments();
		id = bundle.getInt(AlarmInstance.COLUMN_ID);

		Alarm alarm = MyAlarmManager.findAlarmById(id / 10 * 10);
		
		musicPath = alarm.getString(Alarm.COLUMN_MUSIC_URI);
		if(musicPath.equals("")){
			musicPath = "content://media/external/audio/media/11";
		}
		

		mGlowPadView.setOnTriggerListener(this);

		// uncomment this to make sure the glowpad doesn't vibrate on touch
		// mGlowPadView.setVibrateEnabled(false);

		// uncomment this to hide targets
		mGlowPadView.setShowTargetsOnIdle(true);

		try{
			 Uri alert =  Uri.parse(musicPath);//RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			 mpintro = new MediaPlayer();
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
		

		 return view; 
		 
		//mpintro = MediaPlayer.create(this.getActivity(), Uri.parse(musicPath));
		//mpintro.setLooping(true);
		//mpintro.start();
		
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
			mpintro.stop();
			break;

		case R.drawable.pic_msg:
			Fragment newContent = new PicMsgArrivedFragment();
			Bundle bundle = new Bundle();
			bundle.putInt(AlarmInstance.COLUMN_ID, id);
			newContent.setArguments(bundle);
			if (getActivity() instanceof WakeUpActivity) {
				WakeUpActivity mma = (WakeUpActivity) getActivity();
				mma.switchContent(newContent);
			}
			mpintro.stop();
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
