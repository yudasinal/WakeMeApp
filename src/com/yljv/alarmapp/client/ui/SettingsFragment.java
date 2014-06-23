package com.yljv.alarmapp.client.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
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
import com.parse.ParseUser;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.server.user.AccountManager;
import com.yljv.alarmapp.server.user.PartnerRequestListener;
import com.yljv.alarmapp.server.user.AccountManager.User;
import com.yljv.alarmapp.client.helper.ApplicationSettings;
import com.yljv.alarmapp.client.ui.start.SplashActivity;

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
	TextView myName;
	TextView myEmail;
	TextView partnerName;
	TextView partnerEmail;
	TableRow rowPartnerName;
	TableRow rowPartnerEmail;
	TableRow partnerAction;
	TextView partnerActionUp;
	TextView partnerActionDown;
	String name;
	String changedPartnerName;

	Activity activity;
	PartnerRequestListener fragment;
	
	View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		fragment = this;

		activity = this.getActivity();

		view = inflater
				.inflate(R.layout.settings_layout, container, false);
		TableRow changeMyName = (TableRow) view.findViewById(R.id.row1);
		TableRow changeMyEmail = (TableRow) view.findViewById(R.id.row2);



		rowPartnerName = (TableRow) view
				.findViewById(R.id.row3);
		rowPartnerEmail = (TableRow) view
				.findViewById(R.id.row4);
		
		partnerAction = (TableRow) view.findViewById(R.id.row5);
		partnerAction.setOnClickListener(this);
		partnerActionUp = (TextView) partnerAction.findViewById(R.id.partner_action);
		partnerActionDown = (TextView) partnerAction.findViewById(R.id.partner_action_subtitle);
		
		switch(ApplicationSettings.getPartnerStatus()){
		case User.INCOMING_REQUEST:
			rowPartnerName.setVisibility(View.GONE);
			rowPartnerEmail.setVisibility(View.GONE);
			partnerActionUp.setText(R.string.accept_decline_request);
			name = ApplicationSettings.getPartnerName();
			partnerActionDown.setText(name + " " + R.string.wants_be_buddy);
			break;
		case User.NO_PARTNER:
			rowPartnerName.setVisibility(View.GONE);
			rowPartnerEmail.setVisibility(View.GONE);
			partnerActionUp.setText(R.string.add_buddy);
			partnerActionDown.setText(R.string.cause_awesome);
			break;
		case User.PARTNER_REQUESTED:
			rowPartnerName.setVisibility(View.GONE);
			rowPartnerEmail.setVisibility(View.GONE);
			partnerActionUp.setText(R.string.cancel_request);
			name = ApplicationSettings.getPartnerName();
			partnerActionDown.setText(R.string.request_sent_to + name);
			break;
		case User.PARTNERED:
			partnerName = (TextView) view.findViewById(R.id.partner_name);
			partnerName.setText(ApplicationSettings.getPartnerName());
			partnerEmail = (TextView) view.findViewById(R.id.partner_email);
			partnerEmail.setText(ApplicationSettings.getPartnerEmail());
			rowPartnerName.setOnClickListener(this);
			rowPartnerEmail.setOnClickListener(this);
			partnerActionUp.setText(R.string.unlink);
			partnerActionDown.setText(R.string.independent);
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
		myBar.setTitle(R.string.settings);
		setHasOptionsMenu(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.row1:
			AlertDialog.Builder changeName = new AlertDialog.Builder(
					this.getActivity());
			changeName.setTitle(R.string.change_name);
			final EditText newName = new EditText(this.getActivity());
			newName.setId(TEXT_ID);
			changeName.setView(newName);
			newName.setText(ApplicationSettings.getUserName());
			changeName.setPositiveButton(R.string.save,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							AccountManager.setUserName(newName.getText().toString());
							myName.setText(ApplicationSettings.getUserName());
						}
					});

			changeName.setNegativeButton(R.string.cancel,
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
			changeEmail.setTitle(R.string.change_email);
			final EditText newEmail = new EditText(this.getActivity());
			newEmail.setId(TEXT_ID1);
			changeEmail.setView(newEmail);
			newEmail.setText(ApplicationSettings.getUserEmail());
			changeEmail.setPositiveButton(R.string.save,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String changedEmail = newEmail.getText().toString();
							ApplicationSettings.setUserEmail(changedEmail);
							myEmail.setText(ApplicationSettings.getUserEmail());

						}
					});

			changeEmail.setNegativeButton(R.string.cancel,
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
			changePartnerName.setTitle(R.string.change_buddy_name);
			final EditText newPartnerName = new EditText(this.getActivity());
			newPartnerName.setId(TEXT_ID2);
			changePartnerName.setView(newPartnerName);
			newPartnerName.setText(ApplicationSettings.getPartnerName());
			changePartnerName.setPositiveButton(R.string.save,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String changedPartnerName = newPartnerName
									.getText().toString();
                            AccountManager.setPartnerName(changedPartnerName);
							partnerName.setText(ApplicationSettings
									.getPartnerName());
						}
					});

			changePartnerName.setNegativeButton(R.string.cancel,
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
			changePartnerEmail.setTitle(R.string.change_buddy_email);
			final EditText newPartnerEmail = new EditText(this.getActivity());
			newPartnerEmail.setId(TEXT_ID3);
			newPartnerEmail.setText(ApplicationSettings.getPartnerEmail());
			changePartnerEmail.setView(newPartnerEmail);
			changePartnerEmail.setPositiveButton(R.string.save, 
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							String changedPartnerEmail = newPartnerEmail
									.getText().toString();
                            AccountManager.setPartnerName(changedPartnerName);
							partnerEmail.setText(ApplicationSettings
									.getPartnerEmail());

						}
					});

			changePartnerEmail.setNegativeButton(R.string.cancel, 
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

				action.setTitle(R.string.add_alarm_buddy);
				LinearLayout layout = new LinearLayout(getActivity());
				layout.setOrientation(LinearLayout.VERTICAL);
				final EditText addPartnerName = new EditText(this.getActivity());
				addPartnerName.setHint(R.string.name);
				layout.addView(addPartnerName);
				final EditText addPartnerEmail = new EditText(this.getActivity());
				addPartnerEmail.setHint(R.string.email);
				layout.addView(addPartnerEmail);
				action.setView(layout);
				action.setPositiveButton(R.string.send_request,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO register partner (what if slow internet?)

								if(addPartnerEmail.getText() != null){
									AccountManager.sendPartnerRequest(addPartnerEmail
											.getText().toString(), fragment);
									
								}
								AccountManager.setPartnerName(addPartnerName
                                        .getText().toString());
							}
						});

				action.setNegativeButton(R.string.cancel, 
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
				action.show();
			}else if(status == User.INCOMING_REQUEST){
				action.setTitle(R.string.do_you_want_request);
				action.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AccountManager.acceptPartnerRequest();
						partnerActionUp.setText(R.string.unlink);
						partnerActionDown.setText(R.string.independent);

						rowPartnerName.setVisibility(View.VISIBLE);
						rowPartnerEmail.setVisibility(View.VISIBLE);
						partnerName = (TextView) view.findViewById(R.id.partner_name);
						partnerName.setText(ApplicationSettings.getPartnerName());
						partnerEmail = (TextView) view.findViewById(R.id.partner_email);
						partnerEmail.setText(ApplicationSettings.getPartnerEmail());
						
						Toast.makeText(getActivity(),
								R.string.buddy_request_accepted,
								Toast.LENGTH_LONG).show();
					}
					
					
					
				});
				action.setNegativeButton(R.string.decline_request, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AccountManager.declinePartnerRequest();
						Toast.makeText(getActivity(),
								R.string.buddy_request_declined,
								Toast.LENGTH_LONG).show();

						partnerActionUp.setText(R.string.add_alarm_buddy);
						partnerActionDown.setText(R.string.cause_awesome);
						
					}
				});
				action.show();
			}else if(status == User.PARTNER_REQUESTED){
				action.setTitle(R.string.do_you_want_cancel);
				action.setPositiveButton(R.string.delete_request, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						partnerActionUp.setText(R.string.add_alarm_buddy);
						partnerActionDown.setText(R.string.cause_awesome);
						
						Toast.makeText(getActivity(),
								R.string.buddy_request_cancel,
								Toast.LENGTH_LONG).show();
						AccountManager.cancelPartnerRequest();
					}
				});
				action.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
				action.show();
			}else if(status == User.PARTNERED){
				action.setTitle(R.string.are_you_sure_unlink + " " + ApplicationSettings.getPartnerName() + "?");
				action.setPositiveButton(R.string.unlink, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AccountManager.unlink();
						name = ApplicationSettings.getPartnerName();
						rowPartnerName.setVisibility(View.GONE);
						rowPartnerEmail.setVisibility(View.GONE);
						Toast.makeText(getActivity(),
								R.string.you_and + " " + name + " " + R.string.are_not_buddies,
								Toast.LENGTH_LONG).show();

						partnerActionUp.setText(R.string.add_alarm_buddy);
						partnerActionDown.setText(R.string.cause_awesome);
					}
					
					
				});

				action.setNegativeButton(R.string.cancel,
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
			signOut.setTitle(R.string.are_you_sure_sign_out);
			signOut.setPositiveButton(R.string.sign_out,
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

			signOut.setNegativeButton(R.string.cancel,
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
					.setTitle(R.string.are_you_sure_delete_account);
			deleteAccount.setMessage(R.string.all_info_delelted);
			deleteAccount.setPositiveButton(R.string.delete_account,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							//TODO
							ParseUser user = ParseUser.getCurrentUser();
							user.deleteInBackground();
							Log.e("WakeMeApp", "Screenshot");
						}
					});

			deleteAccount.setNegativeButton(R.string.cancel,
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
		Toast.makeText(this.getActivity(), R.string.no_buddy_found,
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void onAlreadyPartnered() {
		// TODO Auto-generated method stub
		Toast.makeText(this.getActivity(), R.string.you_have_buddy,
				Toast.LENGTH_LONG).show();

	}

	@Override
	public void onPartnerRequested() {
		// TODO Auto-generated method stub

        AccountManager.setPartnerName(changedPartnerName);
		partnerActionUp.setText(R.string.cancel_buddy_request);
		name = ApplicationSettings.getPartnerName();
		partnerActionDown.setText(R.string.request_sent_to + " " + name);
		String toast = R.string.invitation_to + " " + ApplicationSettings.getPartnerName() + " " + R.string.is_sent;
		Toast.makeText(getActivity(),
				toast,
				Toast.LENGTH_LONG).show();

	}

	@Override
	public void onOlderRequestAlreadySent() {
		// TODO Auto-generated method stub
		Toast.makeText(this.getActivity(), R.string.you_sent_request_before,
				Toast.LENGTH_LONG).show();

	}

	@Override
	public void onPartnerNotAvailable() {
		// TODO Auto-generated method stub
		Toast.makeText(this.getActivity(),
				R.string.requested_buddy_not_available, Toast.LENGTH_LONG)
				.show();

	}

}
