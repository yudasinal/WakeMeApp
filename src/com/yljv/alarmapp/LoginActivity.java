package com.yljv.alarmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.PushService;
import com.yljv.alarmapp.helper.AccountManager;
import com.yljv.alarmapp.parse.database.MyAlarmManager;
import com.yljv.alarmapp.parse.database.ParseLoginListener;
import com.yljv.alarmapp.parse.database.ParsePartnerListener;

public class LoginActivity extends Activity implements OnClickListener, ParseLoginListener, ParsePartnerListener  {
	
	EditText editPassword;
	EditText editEmail;
	Button loginBtn;
	Button regBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		loginBtn = (Button) findViewById(R.id.btnLogin);
		regBtn = (Button) findViewById(R.id.btnRegister);
		loginBtn.setOnClickListener(this);
		regBtn.setOnClickListener(this);
		getActionBar().setTitle("Login");
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			super.onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case  R.id.btnLogin: 
				login();
				break;
			case R.id.btnRegister:
				Intent intent = new Intent(this, RegisterActivity.class);
				startActivity(intent);
				break;
		}
	}
		
	public void login() {
		editEmail = (EditText) findViewById(R.id.email);
		editPassword = (EditText) findViewById(R.id.password);
		String email = editEmail.getText().toString();
		String password = editPassword.getText().toString();
		boolean cancel = false;
		View focusView = null;
		// TODO Error fields (user does not exist, etc)
		//Check for a valid password.
		if (TextUtils.isEmpty(password)) {
			editPassword.setError("Field is required");
			focusView = editPassword;
			cancel = true;
		} else if (password.length() < 4) {
			editPassword.setError("Short password");
			focusView = editPassword;
			cancel = true;
		}
		if (TextUtils.isEmpty(email)) {
			editEmail.setError("Field is required");
			focusView = editEmail;
			cancel = true;
			
		//TODO email is not registered in the system 
		} else if (!email.contains("@")) {
			editEmail.setError("Email is not registered");
			focusView = editEmail;
			cancel = true; 
		}
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();

		} else {
			AccountManager.login(this, email, password);
		}
	}
	
	@Override
	public void onLoginSuccessful() {
		PushService.subscribe(this, AccountManager.getPartnerChannel(), MenuMainActivity.class);
		Intent intent = new Intent (this, MenuMainActivity.class);
		startActivity(intent);
		finish();
		
	}

	@Override
	public void onLoginFail(ParseException e) {
		View focusView = null;
		loginBtn.setError("User is not registered, please register first");
		focusView = loginBtn;
	}
	
	//TODO Password and Username didn't match? 
	
	public void backSplash() {
		Intent intent = new Intent(this, ChoiceActivity.class);
		startActivity(intent);	
	}

	@Override
	public void onPartnerFound() {
		// TODO Auto-generated method stub
		Log.i("WakeMeApp", "partner found");
		
	}

	@Override
	public void onPartnerNotExisting() {
		// TODO Auto-generated method stub
		Log.i("WakeMeApp", "partner not found");
	}

	@Override
	public void onPartnerQueryError(Exception e) {
		// TODO Auto-generated method stub
		Log.i("WakeMeApp", "partner error");
		
	}

	
}

	
	
	
	

