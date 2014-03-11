package com.yljv.alarmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.PushService;
import com.yljv.alarmapp.helper.AccountManager;
import com.yljv.alarmapp.helper.ApplicationSettings;
import com.yljv.alarmapp.helper.AccountManager.User;
import com.yljv.alarmapp.parse.database.ParseRegisterListener;

public class RegisterActivity extends Activity implements OnClickListener,
		ParseRegisterListener {

	public final static String FIRST_NAME = "com.yljv.alarmapp.FIRST_NAME";

	EditText editFirstName;
	Button btnRegister;
	EditText editEmail;
	EditText confirmEmail;
	EditText editPassword;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		editFirstName = (EditText) findViewById(R.id.editFirstName);
		editEmail = (EditText) findViewById(R.id.editEmail);
		editPassword = (EditText) findViewById(R.id.editPassword);
		btnRegister.setOnClickListener(this);
		getActionBar().setTitle("Register");
        getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public void onClick(View register) {
		// TODO create User here
		switch (register.getId()) {
			case R.id.btnRegister: 
				registerAttempt();
				break;
		}
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home) {
			super.onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	public void registerAttempt() {
		String firstName = editFirstName.getText().toString();
		String email = editEmail.getText().toString();
		String emailConfirm = confirmEmail.getText().toString();
		String password = editPassword.getText().toString();
		boolean cancel = false;
		View focusView = null;
		// TODO Error fields (user does not exist, etc)
		// Check for a valid password.
		if (TextUtils.isEmpty(password)) {
			editPassword.setError("This field is required");
			focusView = editPassword;
			cancel = true;
		} else if (password.length() < 6) {
			editPassword.setError("Short password");
			focusView = editPassword;
			cancel = true;
		if(TextUtils.isEmpty(firstName)){
			editFirstName.setError("Field is required");
		}
		if (TextUtils.isEmpty(email)) {
			editEmail.setError("Field is required");
			focusView = editEmail;
			cancel = true;
		//TODO check if the email is valid(in the form of ""@"" and has a dot) 
		} else if (!email.contains("@")) {
			editEmail.setError("Invalid email address");
			focusView = editEmail;
			cancel = true; 
		}
		if (!email.equals(emailConfirm)){
			editEmail.setError("Emails are not the same");
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			AccountManager.register(this, email, password);
		}
		}
	}

	@Override
	public void onRegisterSuccess() {

		PushService.subscribe(this, AccountManager.getPartnerChannel(),
				MenuMainActivity.class);
		
		//PushService.subscribe(this, AccountManager.getEmail(), MenuMainActivity.class);
		//TODO invalid username

		String name = ApplicationSettings.getUserName();
		Intent intent = new Intent(this, RegisterPartnerActivity.class);
		intent.putExtra(FIRST_NAME, name);

		startActivity(intent);
	}

	@Override
	public void onRegisterFail(ParseException e) {
		//TODO spinner in a button if connection is slow
		// TODO Auto-generated method stub
		// TODO error messages (passwords do not match, etc

	}

	private void back() {
		Intent intent = new Intent(this, ChoiceActivity.class);
		startActivity(intent);

	}

}
