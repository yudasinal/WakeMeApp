package com.yljv.alarmapp.client.ui.start;

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
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.client.helper.MenuMainActivity;
import com.yljv.alarmapp.server.alarm.MyAlarmManager;
import com.yljv.alarmapp.server.user.AccountManager;
import com.yljv.alarmapp.server.user.ParseLoginListener;

public class LoginActivity extends Activity implements OnClickListener, ParseLoginListener {

	private EditText editPassword;
    private EditText editEmail;
    private Button loginBtn;
    private Button regBtn;
    private TextView wrongCred;
    private ProgressBar progress;

    private boolean visible;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_layout);

		loginBtn = (Button) findViewById(R.id.btnLogin);
        loginBtn.setOnClickListener(this);
		regBtn = (Button) findViewById(R.id.btnRegister);
        regBtn.setOnClickListener(this);
		wrongCred = (TextView) findViewById(R.id.wrong_credentials);
		progress = (ProgressBar) findViewById(R.id.progress);
		progress.setVisibility(View.GONE);
		
		visible = true;

		getActionBar().setTitle(R.string.login);
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

		if(inputManager!=null && getCurrentFocus()!=null){
			inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
	                   InputMethodManager.HIDE_NOT_ALWAYS);
		}

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
			editPassword.setError(getString(R.string.field_is_required));
			focusView = editPassword;
			cancel = true;
			loginBtn.setTextColor(Color.parseColor("#3f2860"));
			progress.setVisibility(View.GONE);
		} else if (password.length() < 4) {
			editPassword.setError(getString(R.string.short_password));
			focusView = editPassword;
			cancel = true;
			loginBtn.setTextColor(Color.parseColor("#3f2860"));
			progress.setVisibility(View.GONE);
		}
		if (TextUtils.isEmpty(email)) {
			editEmail.setError(getString(R.string.field_is_required));
			focusView = editEmail;
			cancel = true;
			loginBtn.setTextColor(Color.parseColor("#3f2860"));
			progress.setVisibility(View.GONE);

			// TODO email is not registered in the system
		} else if (!email.contains("@")) {
			editEmail.setError(getString(R.string.email_not_registered));
			focusView = editEmail;
			cancel = true;
			loginBtn.setTextColor(Color.parseColor("#3f2860"));
			progress.setVisibility(View.GONE);
		}

		if (cancel) {
			// There was an error; don'tuple attempt login and focus the first
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
			MyAlarmManager.getPartnerAlarmsFromServer();
		}
		MyAlarmManager.getMyAlarmsFromServer();
		
		PushService.subscribe(this, AccountManager.getSubscribedChannel(), MenuMainActivity.class);
		cont();
		finish();
		
		//Kill the previous activity 
		ChoiceActivity.getInstance().finish();
		
	}
	
	@Override
	public void onLoginFail(ParseException e) {
		if(!isNetworkAvailable()) {
			wrongCred.setText(R.string.no_or_slow_internet);
			loginBtn.setTextColor(Color.parseColor("#3f2860"));
			progress.setVisibility(View.GONE);
		} else {
			e.printStackTrace();
			wrongCred.setText(R.string.wrong_email_password);
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

	public void cont() {
		visible = false;
		Intent intent = new Intent(this, MenuMainActivity.class);
		startActivity(intent);
	}

}
