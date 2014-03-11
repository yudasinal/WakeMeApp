package com.yljv.alarmapp.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.SplashActivity;
import com.yljv.alarmapp.helper.AccountManager;
import com.yljv.alarmapp.helper.ApplicationSettings;

/*
 * shows Settings: 
 * change partner
 */
public class SettingsFragment extends SherlockFragment implements OnClickListener {
	
	private static final int TEXT_ID = 0;
	private static final int TEXT_ID1 = 0;
	private static final int TEXT_ID2 = 0;
	private static final int TEXT_ID3 = 0;
	private static final int TEXT_ID4 = 0;
	TextView myName;
	TextView myEmail;
	TextView partnerName;
	TextView partnerEmail;

	Activity activity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		activity = this.getActivity();
		
		View view = inflater
				.inflate(R.layout.settings_layout, container, false);
		TableRow changeMyName = (TableRow) view.findViewById(R.id.row1);
		TableRow changeMyEmail = (TableRow) view.findViewById(R.id.row2);
		
		if(ApplicationSettings.hasPartner(ApplicationSettings.getUserEmail()) == true) {
			
			TableRow changePartnerName = (TableRow) view.findViewById(R.id.row3);
			TableRow changePartnerEmail = (TableRow) view.findViewById(R.id.row4);
			TableRow unlink = (TableRow) view.findViewById(R.id.row5);
			TableRow addPartner = (TableRow) view.findViewById(R.id.row13);
			addPartner.setVisibility(View.GONE);
			partnerName = (TextView) view.findViewById(R.id.partner_name);
			partnerEmail = (TextView) view.findViewById(R.id.partner_email);
			partnerName.setText(ApplicationSettings.getPartnerName());
			partnerEmail.setText(ApplicationSettings.getPartnerEmail());
			changePartnerName.setOnClickListener(this);
			changePartnerEmail.setOnClickListener(this);
			unlink.setOnClickListener(this);
		}
		
		else{
			
			TableRow addPartner = (TableRow) view.findViewById(R.id.row13);
			TableRow changePartnerName = (TableRow) view.findViewById(R.id.row3);
			changePartnerName.setVisibility(View.GONE);
			TableRow changePartnerEmail = (TableRow) view.findViewById(R.id.row4);
			changePartnerEmail.setVisibility(View.GONE);
			TableRow unlink = (TableRow) view.findViewById(R.id.row5);
			unlink.setVisibility(View.GONE);
			addPartner.setOnClickListener(this);
		}
		
		TableRow signOut = (TableRow) view.findViewById(R.id.row6);
		TableRow deleteAccount = (TableRow) view.findViewById(R.id.row7);
		TableRow likeUs = (TableRow) view.findViewById(R.id.row11);
		TableRow credits = (TableRow) view.findViewById(R.id.row12);
		myName = (TextView) view.findViewById(R.id.my_name);
		myEmail = (TextView) view.findViewById(R.id.my_email);
		
		myName.setText(ApplicationSettings.getUserName());
		myEmail.setText(ApplicationSettings.getUserEmail());

		changeMyName.setOnClickListener(this);
		changeMyEmail.setOnClickListener(this);
		signOut.setOnClickListener(this);
		deleteAccount.setOnClickListener(this);

		return view;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar myBar = getActivity().getActionBar();
		myBar.setTitle("Settings");
		setHasOptionsMenu(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.row1:
			AlertDialog.Builder changeName = new AlertDialog.Builder(this.getActivity());
			changeName.setTitle("Change name");
			final EditText newName = new EditText(this.getActivity());
			newName.setId(TEXT_ID);
			changeName.setView(newName);
			newName.setText(ApplicationSettings.getUserName());
			changeName.setPositiveButton("Save", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					String changedName = newName.getText().toString();
					ApplicationSettings.setUserName(changedName);
					myName.setText(ApplicationSettings.getUserName());
				}
			});
			
			changeName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			changeName.show();
			break;
			
		case R.id.row2:
			AlertDialog.Builder changeEmail = new AlertDialog.Builder(this.getActivity());
			changeEmail.setTitle("Change email");
			final EditText newEmail = new EditText(this.getActivity());
			newEmail.setId(TEXT_ID1);
			changeEmail.setView(newEmail);
			newEmail.setText(ApplicationSettings.getUserEmail());
			changeEmail.setPositiveButton("Save", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					String changedEmail = newEmail.getText().toString();
					ApplicationSettings.setUserEmail(changedEmail);
					myEmail.setText(ApplicationSettings.getUserEmail());
					
				}
			});
			
			changeEmail.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			changeEmail.show();
			break;
			
		case R.id.row3:
			
			AlertDialog.Builder changePartnerName = new AlertDialog.Builder(this.getActivity());
			changePartnerName.setTitle("Change partner's name");
			final EditText newPartnerName = new EditText(this.getActivity());
			newPartnerName.setId(TEXT_ID2);
			changePartnerName.setView(newPartnerName);
			newPartnerName.setText(ApplicationSettings.getPartnerName());
			changePartnerName.setPositiveButton("Save", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					String changedPartnerName = newPartnerName.getText().toString();
					ApplicationSettings.setPartnerName(changedPartnerName);
					partnerName.setText(ApplicationSettings.getPartnerName());
				}
			});
			
			changePartnerName.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			changePartnerName.show();
			break;
			
		case R.id.row4:
			
			AlertDialog.Builder changePartnerEmail = new AlertDialog.Builder(this.getActivity());
			changePartnerEmail.setTitle("Change partner's email");
			final EditText newPartnerEmail = new EditText(this.getActivity());
			newPartnerEmail.setId(TEXT_ID3);
			changePartnerEmail.setView(newPartnerEmail);
			changePartnerEmail.setPositiveButton("Save", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			
			changePartnerEmail.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			changePartnerEmail.show();
			break;
			
		case R.id.row5:
			AlertDialog.Builder unlink = new AlertDialog.Builder(this.getActivity());
			unlink.setTitle("Are you sure you want to unlink from ?");
			unlink.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Save changed name
					
				}
			});
			
			unlink.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			unlink.show();
			break;
			
		case R.id.row6:
			AlertDialog.Builder signOut = new AlertDialog.Builder(this.getActivity());
			signOut.setTitle("Are you sure you want to sign out?");
			signOut.setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Save changed name
					AccountManager.logout();
					Intent intent = new Intent(activity, SplashActivity.class);
					activity.startActivity(intent);
				}
			});
			
			signOut.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			signOut.show();
			break;
			
		case R.id.row7:
			AlertDialog.Builder deleteAccount = new AlertDialog.Builder(this.getActivity());
			deleteAccount.setTitle("Are you sure you want to delete your account?");
			deleteAccount.setMessage("All your information will be deleted.");
			deleteAccount.setPositiveButton("Delete Account", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Save changed name
					
				}
			});
			
			deleteAccount.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			deleteAccount.show();
			break;
			
		case R.id.row13:
			

			AlertDialog.Builder addPartner = new AlertDialog.Builder(this.getActivity());
			addPartner.setTitle("Add an Alarm Buddy");
			LinearLayout layout = new LinearLayout(getActivity());
			layout.setOrientation(LinearLayout.VERTICAL);
			final EditText addPartnerName = new EditText(this.getActivity());
			addPartnerName.setHint("name");
			layout.addView(addPartnerName);
			final EditText addPartnerEmail = new EditText(this.getActivity());
			addPartnerEmail.setHint("email");
			layout.addView(addPartnerEmail);
			addPartner.setView(layout);
			addPartner.setPositiveButton("Save", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					//TODO register partner (what if slow internet?)
					
					Toast.makeText(getActivity(), "Invitation to John is sent", Toast.LENGTH_LONG).show();
				}
			});
			
			addPartner.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			addPartner.show();
			break;
			
		}
			
	}

}
