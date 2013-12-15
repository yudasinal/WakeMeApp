package com.yljv.alarmapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterPartnerActivity extends Activity implements OnClickListener {
	
	String firstName; 
	Button btnLogin;
	TextView registerSuccess;
	EditText partnerEmail;
	
	public void registerPartner() {
		
		//TODO Login the user to the MainActivity
		String email = partnerEmail.getText().toString();
		boolean cancel = false;
		View focusView = null;
		// TODO Error fields (user does not exist, etc)
		//Check for a valid password.
		if (TextUtils.isEmpty(email)) {
			partnerEmail.setError("This field is required");
			focusView = partnerEmail;
			cancel = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
		Intent main = new Intent(this, MainActivity.class);
	
		startActivity(main);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_partner);
		Intent intent = getIntent();
		firstName = (String) intent.getStringExtra(RegisterActivity.FIRST_NAME);
		registerSuccess = (TextView) findViewById(R.id.registerSuccess);
		registerSuccess.setText("Thank you, " + firstName + "! You have successfully registered.");
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View registerPartner) {
		// TODO Auto-generated method stub

		registerPartner();
		
	}
	
	
	
	

}
