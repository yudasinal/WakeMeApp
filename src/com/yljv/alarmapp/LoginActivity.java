package com.yljv.alarmapp;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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
	//ProgressButton loginBtn;
	Button loginBtn;
	TextView wrongCred;
	ProgressBar progress;
	
	boolean visible;
	Button regBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		loginBtn = (Button) findViewById(R.id.btnLogin);
		//loginBtn = (ProgressButton) findViewById(R.id.btnLogin);
		regBtn = (Button) findViewById(R.id.btnRegister);
		loginBtn.setOnClickListener(this);
		wrongCred = (TextView) findViewById(R.id.wrong_credentials);
		progress = (ProgressBar) findViewById(R.id.progress);
		progress.setVisibility(View.GONE);
		
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
		InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE); 

		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                   InputMethodManager.HIDE_NOT_ALWAYS);
		switch(v.getId()){
			case  R.id.btnLogin:
				loginBtn.setTextColor(Color.parseColor("#fa8b60"));
				progress.setVisibility(View.VISIBLE);
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
		wrongCred.setText("");
		// TODO Error fields (user does not exist, etc)
		// Check for a valid password.
		if (TextUtils.isEmpty(password)) {
			editPassword.setError("Field is required");
			focusView = editPassword;
			cancel = true;
			loginBtn.setTextColor(Color.parseColor("#3f2860"));
			progress.setVisibility(View.GONE);
		} else if (password.length() < 4) {
			editPassword.setError("Short password");
			focusView = editPassword;
			cancel = true;
			loginBtn.setTextColor(Color.parseColor("#3f2860"));
			progress.setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(email)) {
			editEmail.setError("Field is required");
			focusView = editEmail;
			cancel = true;
			loginBtn.setTextColor(Color.parseColor("#3f2860"));
			progress.setVisibility(View.GONE);

			// TODO email is not registered in the system
		} else if (!email.contains("@")) {
			editEmail.setError("Email is not registered");
			focusView = editEmail;
			cancel = true;
			loginBtn.setTextColor(Color.parseColor("#3f2860"));
			progress.setVisibility(View.GONE);
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
			loginBtn.setTextColor(Color.parseColor("#3f2860"));
			progress.setVisibility(View.GONE);

		} else {
			AccountManager.login(this, email, password);
		}
	}

	@Override
	public void onLoginSuccessful() {
		
		
		if(AccountManager.hasPartner()){
			MyAlarmManager.getPartnerAlarmsFromServer(this);
		}
		MyAlarmManager.getMyAlarmsFromServer();
		
		if(AccountManager.hasPartner()){
			PushService.subscribe(this, AccountManager.getSubscribedChannel(), MenuMainActivity.class);
		}
		Intent intent = new Intent (this, MenuMainActivity.class);
		startActivity(intent);
		finish();
		
	}
	
	@Override
	public void onLoginFail(ParseException e) {
		boolean isThereInternet = isNetworkAvailable();
		if(isThereInternet == false) {
			wrongCred.setText("No or slow internet connection :(");
			loginBtn.setTextColor(Color.parseColor("#3f2860"));
			progress.setVisibility(View.GONE);
		}
		else {
			e.printStackTrace();
			wrongCred.setText("Wrong email or password!");
			loginBtn.setTextColor(Color.parseColor("#3f2860"));
			progress.setVisibility(View.GONE);
		}
	}
	
	private boolean isNetworkAvailable(){
		
		ConnectivityManager connectivityManager = 
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
