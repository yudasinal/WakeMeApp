package com.yljv.alarmapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InvitationActivity extends Activity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.invitation_layout);
		Button acceptBtn = (Button) findViewById(R.id.btnAccept);
		Button declineBtn = (Button) findViewById(R.id.btnDecline);
		acceptBtn.setOnClickListener(this);
		declineBtn.setOnClickListener(this);
		TextView invitation = (TextView) findViewById(R.id.invite);
		invitation.setText("Awesome! Jasmin (jasmin.vu@gmail.com wants to be alarm buddies with you!");
		super.onCreate(savedInstanceState);
		getActionBar().setTitle("Invitation");
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnAccept:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;
			
		case R.id.btnDecline:
			Intent otherIntent = new Intent(this, ChoiceActivity.class);
			startActivity(otherIntent);
			break;
		}
		
		
		
	}
	
	

}
