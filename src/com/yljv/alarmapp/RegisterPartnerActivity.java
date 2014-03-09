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
import android.widget.TextView;

public class RegisterPartnerActivity extends Activity implements OnClickListener {
	
	Button btnSkip;
	Button btnInvite;
	EditText partnerEmail;
	EditText partnerName;
	
	public void registerPartner() {
		
		//TODO click on "Male" radio button does not allow registration
		//TODO Login the user to the MainActivity
		String email = partnerEmail.getText().toString();
		String name = partnerName.getText().toString();
		boolean cancel = false;
		View focusView = null;
		// TODO Error fields (user does not exist, etc)
		//Check for a valid password.
		if (TextUtils.isEmpty(email)) {
			partnerEmail.setError("This field is required");
			focusView = partnerEmail;
			cancel = true;
		}
		if (TextUtils.isEmpty(name)) {
			partnerEmail.setError("Field is required");
			focusView = partnerEmail;
			cancel = true;
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
		Intent main = new Intent(this, MenuMainActivity.class);
	
		startActivity(main);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_partner);
		btnInvite = (Button) findViewById(R.id.btnInvite);
		btnSkip = (Button) findViewById(R.id.btnSkip);
		btnInvite.setOnClickListener(this);
		partnerEmail = (EditText) findViewById(R.id.partner_email);
		partnerName = (EditText) findViewById(R.id.partner_name);
		
		getActionBar().setTitle("Alarm Buddy");
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
	public void onClick(View registerPartner) {
		switch(registerPartner.getId()) {
			case R.id.btnInvite:
				registerPartner();
				break;
			case R.id.btnSkip:
				//TODO login without a partner
				break;
				
		}

		
	}
}
