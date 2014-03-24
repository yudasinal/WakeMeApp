package com.yljv.alarmapp;

import java.util.List;

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
import com.parse.PushService;
import com.yljv.alarmapp.helper.AccountManager;
import com.yljv.alarmapp.parse.database.AlarmInstance;
import com.yljv.alarmapp.parse.database.MyAlarmManager;
import com.yljv.alarmapp.parse.database.ParseLoginListener;
import com.yljv.alarmapp.parse.database.ParsePartnerAlarmListener;

public class LoginActivity extends Activity implements OnClickListener, ParseLoginListener,
		ParsePartnerAlarmListener {

	EditText editPassword;
	EditText editEmail;
	Button loginBtn;
	
	boolean visible;
	Button regBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		loginBtn = (Button) findViewById(R.id.btnLogin);
		regBtn = (Button) findViewById(R.id.btnRegister);
		loginBtn.setOnClickListener(this);
		
		visible = true;
		
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
		// Check for a valid password.
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

			// TODO email is not registered in the system
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
		

		MyAlarmManager.getPartnerAlarmsFromServer(this);
		MyAlarmManager.getMyAlarmsFromServer();
		
		PushService.subscribe(this, AccountManager.getPartnerChannel(), MenuMainActivity.class);
		Intent intent = new Intent (this, MenuMainActivity.class);
		startActivity(intent);
		finish();
		
	}

	@Override
	public void onLoginFail(ParseException e) {
		e.printStackTrace();
		if (e.getMessage().equals("invalid login credentials")) {
			loginBtn.setError("Wrong username or password");
		} else {
			loginBtn.setError("User is not registered, please register first");
		}
	}

	public void backSplash() {
		Intent intent = new Intent(this, ChoiceActivity.class);
		startActivity(intent);
	}

	public void cont() {
		visible = false;
		Intent intent = new Intent(this, MenuMainActivity.class);
		startActivity(intent);
		
	}

	@Override
	public void partnerAlarmsFound(List<AlarmInstance> alarms) {
		if(visible){
			cont();
		}
	}

	@Override
	public void partnerAlarmsSearchFailed(Exception e) {
		if(visible){
			cont();
		}
	}

}
