package com.yljv.alarmapp.client.ui.start;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.yljv.alarmapp.R;
import com.yljv.alarmapp.R.id;
import com.yljv.alarmapp.R.layout;
import com.yljv.alarmapp.client.helper.ApplicationSettings;
import com.yljv.alarmapp.client.helper.MenuMainActivity;
import com.yljv.alarmapp.server.user.AccountManager;
import com.yljv.alarmapp.server.user.PartnerRequestListener;

public class RegisterPartnerActivity extends Activity implements OnClickListener, PartnerRequestListener {
	
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
			partnerEmail.setError(getString(R.string.field_is_required));
			focusView = partnerEmail;
			cancel = true;
		}
		if (TextUtils.isEmpty(name)) {
			partnerEmail.setError(getString(R.string.field_is_required));
			focusView = partnerEmail;
			cancel = true;
		}
		if (cancel) {
			// There was an error; don'tuple attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			AccountManager.sendPartnerRequest(email, this);
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
		btnSkip.setOnClickListener(this);
		partnerEmail = (EditText) findViewById(R.id.partner_email);
		partnerName = (EditText) findViewById(R.id.partner_name);
		
		getActionBar().setTitle(R.string.alarm_buddy);
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
		InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE); 

		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                   InputMethodManager.HIDE_NOT_ALWAYS);
		switch(registerPartner.getId()) {
			case R.id.btnInvite:
				registerPartner();
				break;
			case R.id.btnSkip:
				Intent intent = new Intent(this, MenuMainActivity.class);
				startActivity(intent);
				break;
				
		}

		
		// TODO click on "Male" radio button does not allow registration
		// TODO Login the user to the MainActivity
		String email = partnerEmail.getText().toString();
		boolean cancel = false;
		View focusView = null;
		// TODO Error fields (user does not exist, etc)
		// Check for a valid password.
		if (TextUtils.isEmpty(email)) {
			partnerEmail.setError(getString(R.string.field_is_required));
			focusView = partnerEmail;
			cancel = true;
		}
		if (cancel) {
			// There was an error; don'tuple attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			AccountManager.sendPartnerRequest(email, this);
		}

	}
	


	@Override
	public void onNoPartnerFound() {
		// TODO Auto-generated method stub
		Toast.makeText(this, R.string.no_buddy_found,
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onAlreadyPartnered() {
		// TODO Auto-generated method stub
		Toast.makeText(this, R.string.you_have_buddy,
				Toast.LENGTH_LONG).show();

	}

	@Override
	public void onPartnerRequested() {
		// TODO Auto-generated method stub

		String toast = R.string.invitation_to + " "
		  + ApplicationSettings.getPartnerName() + " " + R.string.is_sent;
		Toast.makeText(this,
				toast,
				Toast.LENGTH_LONG).show();

		Intent main = new Intent(this, MenuMainActivity.class);
		startActivity(main);

	}

	@Override
	public void onOlderRequestAlreadySent() {
		// TODO Auto-generated method stub
		Toast.makeText(this, R.string.you_sent_request_before,
				Toast.LENGTH_LONG).show();

	}

	@Override
	public void onPartnerNotAvailable() {
		// TODO Auto-generated method stub
		Toast.makeText(this,
				R.string.requested_buddy_not_available, Toast.LENGTH_LONG)
				.show();

	}
}
