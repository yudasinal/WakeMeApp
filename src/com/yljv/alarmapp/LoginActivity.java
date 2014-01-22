package com.yljv.alarmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.yljv.alarmapp.helper.AccountManager;
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
				break;
			case R.id.btnBack:
				backSplash();
				break;
		}
	}
		
	public void login() {
		editEmail = (EditText) findViewById(R.id.editEmail);
		editPassword = (EditText) findViewById(R.id.editPassword);
		String email = editEmail.getText().toString();
		String password = editPassword.getText().toString();
		boolean cancel = false;
		View focusView = null;
		// TODO Error fields (user does not exist, etc)
		//Check for a valid password.
		if (TextUtils.isEmpty(password)) {
			editPassword.setError("This field is required");
			focusView = editPassword;
			cancel = true;
		} else if (password.length() < 4) {
			editPassword.setError("Short password");
			focusView = editPassword;
			cancel = true;
		}
		if (TextUtils.isEmpty(email)) {
			editEmail.setError("This field is required");
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
		// TODO Auto-generated method stub
		Intent intent = new Intent (this, MenuMainActivity.class);
		startActivity(intent);
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

	
}

	
	
	
	

