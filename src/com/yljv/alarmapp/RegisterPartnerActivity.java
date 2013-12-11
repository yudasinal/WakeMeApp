package com.yljv.alarmapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RegisterPartnerActivity extends Activity implements OnClickListener {
	
	String firstName; 
	Button btnLogin;
	TextView registerSuccess;
	
	public void registerPartner() {
		
		//TODO Login the user to the MainActivity
		
		Intent main = new Intent(this, MainActivity.class);
	
		startActivity(main);
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
