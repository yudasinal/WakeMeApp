package com.yljv.alarmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.yljv.alarmapp.parse.database.AccountManager;
import com.yljv.alarmapp.parse.database.ParseLoginListener;

public class LoginActivity extends Activity implements OnClickListener, ParseLoginListener  {
	
	EditText editPassword;
	EditText editEmail;
	Button loginBtn;
	Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		btnBack = (Button) findViewById(R.id.btnBack);
		loginBtn.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case  R.id.loginBtn: 
				login();
			case R.id.btnBack:
				backSplash();
		}
	}
		
	public void login() {
		editEmail = (EditText) findViewById(R.id.editEmail);
		editPassword = (EditText) findViewById(R.id.editPassword);
		String email = editEmail.getText().toString();
		String password = editPassword.getText().toString();
		AccountManager.login(this, email, password);
	}
	
	@Override
	public void onLoginSuccessful() {
		// TODO Auto-generated method stub
		Intent intent = new Intent (this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	public void onLoginFail(ParseException e) {
		// TODO Error fields (user does not exist, etc)
		
	}
	
	public void backSplash() {
		Intent intent = new Intent(this, SplashActivity.class);
		startActivity(intent);	
	}

	
}


	
	
	
	

