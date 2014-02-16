package com.yljv.alarmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.parse.ParseException;
import com.yljv.alarmapp.helper.AccountManager;
import com.yljv.alarmapp.parse.database.ParseRegisterListener;

public class RegisterActivity extends Activity implements OnClickListener, ParseRegisterListener {
	
public final static String FIRST_NAME = "com.yljv.alarmapp.FIRST_NAME";
	
	EditText editFirstName; 
	Button btnRegister;
	Button btnBack;
	EditText editLastName; 
	EditText editEmail;
	EditText editPassword;
	RadioButton checkFemale;
	EditText editPassword2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnBack = (Button) findViewById(R.id.btnBack);
		editFirstName = (EditText) findViewById(R.id.editFirstName);
		editLastName = (EditText) findViewById(R.id.editLastName);
		editEmail = (EditText) findViewById(R.id.editEmail);
		editPassword = (EditText) findViewById(R.id.editPassword);
		editPassword2 = (EditText) findViewById(R.id.editPassword2);
		checkFemale = (RadioButton) findViewById(R.id.checkFemale);
		btnRegister.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View register) {
		// TODO Auto-generated method stub
		//TODO create User here
		switch (register.getId()) {
			case R.id.btnRegister:
				registerAttempt();
				break;
			case R.id.btnBack: 
				back();
				break;
		}
	}
	
	
	public void registerAttempt() {
		String firstName = editFirstName.getText().toString();
		String lastName = editLastName.getText().toString();
		String email = editEmail.getText().toString();
		String password = editPassword.getText().toString();
		String password2 = editPassword2.getText().toString();
		Boolean female = checkFemale.isActivated();
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
		} else if (!password.equals(password2)) {
			editPassword.setError("Passwords do not match");
			focusView = editPassword;
			cancel = true;
		} 
		if (TextUtils.isEmpty(password2)) {
			editPassword2.setError("This field is required");
			focusView = editPassword;
			cancel = true;
		}
		if (TextUtils.isEmpty(email)) {
			editEmail.setError("This field is required");
			focusView = editEmail;
			cancel = true;
			
		//TODO check if the email is valid(in the form of ""@"" and has a dot) 
		} else if (!email.contains("@")) {
			editEmail.setError("Invalid email address");
			focusView = editEmail;
			cancel = true; 
		}
		/*
		if (TextUtils.isEmpty(firstName)) {
			editFirstName.setError("This field is required");
			focusView = editFirstName;
			cancel = true;
		}
		if (TextUtils.isEmpty(lastName)) {
			editLastName.setError("This field is required");
			focusView = editLastName;
			cancel = true;
		} 
		if (!checkFemale.isChecked()) {
			checkFemale.setError("The box has to be checked");
			focusView = checkFemale;
			cancel = true;
		} */
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		}
		else {
			AccountManager.register(this, email, password);
		}
	}
	
	@Override
	public void onRegisterSuccess() {
		// TODO Auto-generated method stub
		String name = AccountManager.getName();
		Intent intent = new Intent(this, RegisterPartnerActivity.class);
		intent.putExtra(FIRST_NAME, name);

		startActivity(intent);
	}

	@Override
	public void onRegisterFail(ParseException e) {
		// TODO Auto-generated method stub
		//TODO error messages (passwords do not match, etc
		
	}

	private void back() {
		Intent intent = new Intent(this, ChoiceActivity.class);
		startActivity(intent);
		
	}

}
