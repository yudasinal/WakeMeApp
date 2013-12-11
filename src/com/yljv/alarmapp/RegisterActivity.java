package com.yljv.alarmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.parse.ParseException;
import com.yljv.alarmapp.parse.database.AccountManager;
import com.yljv.alarmapp.parse.database.ParseRegisterListener;

public class RegisterActivity extends Activity implements OnClickListener, ParseRegisterListener {
	
public final static String FIRST_NAME = "com.yljv.alarmapp.FIRST_NAME";
	
	EditText editFirstName; 
	Button btnRegister;
	EditText editLastName; 
	EditText editEmail;
	EditText editPassword;
	CheckBox checkFemale;
	
	
	public void registerAttempt() {
		String firstName = editFirstName.getText().toString();
		String lastName = editLastName.getText().toString();
		String email = editEmail.getText().toString();
		String password = editPassword.getText().toString();
		Boolean female = checkFemale.isActivated();
		AccountManager.register(this, email, firstName, lastName, password, female);
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		editFirstName = (EditText) findViewById(R.id.editFirstName);
		btnRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View register) {
		// TODO Auto-generated method stub
		//TODO create User here
		
		registerAttempt();
	}

	@Override
	public void onRegisterSuccess() {
		// TODO Auto-generated method stub
		String name = AccountManager.getCurrentUser().getName();
		Intent intent = new Intent(this, RegisterPartnerActivity.class);
		intent.putExtra(FIRST_NAME, name);

		startActivity(intent);
	}

	@Override
	public void onRegisterFail(ParseException e) {
		// TODO Auto-generated method stub
		//TODO error messages (passwords do not match, etc
		
	}
	

}
