package com.yljv.alarmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity implements OnClickListener {
	
public final static String FIRST_NAME = "com.yljv.alarmapp.FIRST_NAME";
	
	EditText editFirstName; 
	Button btnRegister;
	
	public void register() {
		Intent intent = new Intent(this, RegisterPartnerActivity.class);
		String firstName = editFirstName.getText().toString();
		intent.putExtra(FIRST_NAME, firstName);
		startActivity(intent);
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
		register();
	}
	

}
