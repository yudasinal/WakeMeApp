package com.yljv.alarmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.parse.Parse;
import com.parse.ParseAnalytics;

public class ChoiceActivity extends Activity implements OnClickListener  {
	
	Button btnRegister;
	Button btnLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnRegister.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnRegister:
				toRegister();
				break;
			case R.id.btnLogin:
				toLogin();
		}
		
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
