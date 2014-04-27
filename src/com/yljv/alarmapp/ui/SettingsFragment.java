package com.yljv.alarmapp.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.yljv.alarmapp.helper.AccountManager.User;
import com.yljv.alarmapp.helper.ApplicationSettings;
import com.yljv.alarmapp.parse.database.PartnerRequestListener;

/*
 * shows Settings: 
 * change partner
 */
public class SettingsFragment extends SherlockFragment implements
		OnClickListener, PartnerRequestListener {

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
	PartnerRequestListener fragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		fragment = this;

		activity = this.getActivity();

		View view = inflater
				.inflate(R.layout.settings_layout, container, false);
		TableRow changeMyName = (TableRow) view.findViewById(R.id.row1);
		TableRow changeMyEmail = (TableRow) view.findViewById(R.id.row2);

		TableRow changePartnerName;
		TableRow changePartnerEmail;
		TableRow partnerAction;
		TextView partnerActionUp;
		TextView partnerActionDown;


		changePartnerName = (TableRow) view
				.findViewById(R.id.row3);
		changePartnerEmail = (TableRow) view
				.findViewById(R.id.row4);
		
		partnerAction = (TableRow) view.findViewById(R.id.row5);
		partnerAction.setOnClickListener(this);
		partnerActionUp = (TextView) partnerAction.findViewById(R.id.partner_action);
		partnerActionDown = (TextView) partnerAction.findViewById(R.id.partner_action_subtitle);
		
		switch(ApplicationSettings.getPartnerStatus()){
		case User.INCOMING_REQUEST:
			changePartnerName.setVisibility(View.GONE);
			changePartnerEmail.setVisibility(View.GONE);
			partnerActionUp.setText("Accept/Decline partner request");
			String email = ApplicationSettings.getPartnerEmail();
			partnerActionDown.setText(email + " wants to be your alarm buddy");
			break;
		case User.NO_PARTNER:
			changePartnerName.setVisibility(View.GONE);
			changePartnerEmail.setVisibility(View.GONE);
			partnerActionUp.setText("Add Partner");
			partnerActionDown.setText(" ");
			break;
		case User.PARTNER_REQUESTED:
			changePartnerName.setVisibility(View.GONE);
			changePartnerEmail.setVisibility(View.GONE);
			partnerActionUp.setText("Cancel Partner Request");
			email = ApplicationSettings.getPartnerEmail();
			partnerActionDown.setText("Request sent to " + email);
			break;
		case User.PARTNERED:
			partnerName = (TextView) view.findViewById(R.id.partner_name);
			partnerName.setText(ApplicationSettings.getPartnerName());
			partnerEmail = (TextView) view.findViewById(R.id.partner_email);
			partnerEmail.setText(ApplicationSettings.getPartnerEmail());
			changePartnerName.setOnClickListener(this);
			changePartnerEmail.setOnClickListener(this);
			partnerActionUp.setText("Unlink");
			partnerActionDown.setText("Become Independent :)");
			break;
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
			AlertDialog.Builder changeName = new AlertDialog.Builder(
					this.getActivity());
			changeName.setTitle("Change name");
			final EditText newName = new EditText(this.getActivity());
			newName.setId(TEXT_ID);
			changeName.setView(newName);
			newName.setText(ApplicationSettings.getUserName());
			changeName.setPositiveButton("Save",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String changedName = newName.getText().toString();
							ApplicationSettings.setUserName(changedName);
							myName.setText(ApplicationSettings.getUserName());
						}
					});

			changeName.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			changeName.show();
			break;

		case R.id.row2:
			AlertDialog.Builder changeEmail = new AlertDialog.Builder(
					this.getActivity());
			changeEmail.setTitle("Change email");
			final EditText newEmail = new EditText(this.getActivity());
			newEmail.setId(TEXT_ID1);
			changeEmail.setView(newEmail);
			newEmail.setText(ApplicationSettings.getUserEmail());
			changeEmail.setPositiveButton("Save",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String changedEmail = newEmail.getText().toString();
							ApplicationSettings.setUserEmail(changedEmail);
							myEmail.setText(ApplicationSettings.getUserEmail());

						}
					});

			changeEmail.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			changeEmail.show();
			break;

		case R.id.row3:

			AlertDialog.Builder changePartnerName = new AlertDialog.Builder(
					this.getActivity());
			changePartnerName.setTitle("Change partner's name");
			final EditText newPartnerName = new EditText(this.getActivity());
			newPartnerName.setId(TEXT_ID2);
			changePartnerName.setView(newPartnerName);
			newPartnerName.setText(ApplicationSettings.getPartnerName());
			changePartnerName.setPositiveButton("Save",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String changedPartnerName = newPartnerName
									.getText().toString();
							ApplicationSettings
									.setPartnerName(changedPartnerName);
							partnerName.setText(ApplicationSettings
									.getPartnerName());
						}
					});

			changePartnerName.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			changePartnerName.show();
			break;

		case R.id.row4:

			AlertDialog.Builder changePartnerEmail = new AlertDialog.Builder(
					this.getActivity());
			changePartnerEmail.setTitle("Change partner's email");
			final EditText newPartnerEmail = new EditText(this.getActivity());
			newPartnerEmail.setId(TEXT_ID3);
			changePartnerEmail.setView(newPartnerEmail);
			changePartnerEmail.setPositiveButton("Save",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String changedPartnerEmail = newPartnerEmail
									.getText().toString();
							ApplicationSettings
									.setPartnerName(changedPartnerEmail);
							partnerName.setText(ApplicationSettings
									.getPartnerEmail());

						}
					});

			changePartnerEmail.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			changePartnerEmail.show();
			break;

		case R.id.row5:
			AlertDialog.Builder action = new AlertDialog.Builder(
					this.getActivity());

			int status = ApplicationSettings.getPartnerStatus();
			
			if(status == User.NO_PARTNER){

				action.setTitle("Add an Alarm Buddy");
				LinearLayout layout = new LinearLayout(getActivity());
				layout.setOrientation(LinearLayout.VERTICAL);
				final EditText addPartnerName = new EditText(this.getActivity());
				addPartnerName.setHint("name");
				layout.addView(addPartnerName);
				final EditText addPartnerEmail = new EditText(this.getActivity());
				addPartnerEmail.setHint("email");
				layout.addView(addPartnerEmail);
				action.setView(layout);
				action.setPositiveButton("Send request",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO register partner (what if slow internet?)

								if(addPartnerEmail.getText() != null){
									AccountManager.sendPartnerRequest(addPartnerEmail
											.getText().toString(), fragment);
								}
							}
						});

				action.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
				action.show();
			}else if(status == User.INCOMING_REQUEST){
				action.setTitle("Do you want to accept the buddy request?");
				action.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AccountManager.acceptPartnerRequest();
						Toast.makeText(getActivity(),
								"Partner request accepted",
								Toast.LENGTH_LONG).show();
					}
				});
				action.setNegativeButton("Decline request", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AccountManager.declinePartnerRequest();
						Toast.makeText(getActivity(),
								"Partner request declined",
								Toast.LENGTH_LONG).show();
					}
				});
				action.show();
			}else if(status == User.PARTNER_REQUESTED){
				action.setTitle("Do you want to cancel your request?");
				action.setPositiveButton("Cancel request", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(getActivity(),
								"Partner request cancelled",
								Toast.LENGTH_LONG).show();
						AccountManager.cancelPartnerRequest();
					}
				});
				action.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
				action.show();
			}else if(status == User.PARTNERED){
				action.setTitle("Are you sure you want to unlink from" + ApplicationSettings.getPartnerName() + " ");
				action.setPositiveButton("Unlink", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AccountManager.unlink();
						Toast.makeText(getActivity(),
								"Unlinked",
								Toast.LENGTH_LONG).show();
					}
				});

				action.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
				action.show();
			}
			break;

		case R.id.row6:
			AlertDialog.Builder signOut = new AlertDialog.Builder(
					this.getActivity());
			signOut.setTitle("Are you sure you want to sign out?");
			signOut.setPositiveButton("Sign Out",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Save changed name
							AccountManager.logout(activity);
							Intent intent = new Intent(activity,
									SplashActivity.class);
							activity.startActivity(intent);
							getActivity().finish();
						}
					});

			signOut.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			signOut.show();
			break;

		case R.id.row7:
			AlertDialog.Builder deleteAccount = new AlertDialog.Builder(
					this.getActivity());
			deleteAccount
					.setTitle("Are you sure you want to delete your account?");
			deleteAccount.setMessage("All your information will be deleted.");
			deleteAccount.setPositiveButton("Delete Account",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Save changed name

						}
					});

			deleteAccount.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			deleteAccount.show();
			break;

		}

	}

	@Override
	public void onNoPartnerFound() {
		// TODO Auto-generated method stub
		Toast.makeText(this.getActivity(), "No Partner Found",
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onAlreadyPartnered() {
		// TODO Auto-generated method stub
		Toast.makeText(this.getActivity(), "You already have a partner",
				Toast.LENGTH_LONG).show();

	}

	@Override
	public void onPartnerRequested() {
		// TODO Auto-generated method stub

		Toast.makeText(getActivity(),
				"Invitation sent",
				Toast.LENGTH_LONG).show();

	}

	@Override
	public void onOlderRequestAlreadySent() {
		// TODO Auto-generated method stub
		Toast.makeText(this.getActivity(), "You have sent a request before",
				Toast.LENGTH_LONG).show();

	}

	@Override
	public void onPartnerNotAvailable() {
		// TODO Auto-generated method stub
		Toast.makeText(this.getActivity(),
				"The requested Buddy is not available", Toast.LENGTH_LONG)
				.show();

	}

}
