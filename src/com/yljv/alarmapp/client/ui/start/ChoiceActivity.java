package com.yljv.alarmapp.client.ui.start;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.R.id;
import com.yljv.alarmapp.R.layout;

public class ChoiceActivity extends Activity implements OnClickListener  {
	
	Button btnRegister;
	Button btnLogin;
	static ChoiceActivity choiceActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegister.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		
		choiceActivity = this;
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnRegister:
				toRegister();
				break;
			case R.id.btnLogin:
				toLogin();
				break;
		}
		
	}
	
	//Method to create an instance of this Activity so that it's easy to access it
	//from another activity
	public static ChoiceActivity getInstance() {
		return choiceActivity;
	}
	
	private void toLogin() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	
	private void toRegister() {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

}
