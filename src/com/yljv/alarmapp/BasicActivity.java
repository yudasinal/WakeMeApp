package com.yljv.alarmapp;

import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

public class BasicActivity extends Activity{

	Handler mHandler = new Handler();
	public String errorMessage;
	Activity me;
	
	public void handleError(Exception e){
		errorMessage = e.getMessage();
		mHandler.post(rError);
	}
	
	public void handleError(String message){
		errorMessage = message;
		mHandler.post(rError);
	}
	
	Runnable rError = new Runnable(){
		@Override
		public void run(){
			Toast toast = Toast.makeText(me, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	};
}
