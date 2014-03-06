package com.yljv.alarmapp.ui;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.actionbarsherlock.app.SherlockFragment;
import com.yljv.alarmapp.R;

/*
 * shows Settings: 
 * change partner
 */
public class SettingsFragment extends SherlockFragment implements
		OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.settings_layout, container, false);
		TableRow changeMyName = (TableRow) view.findViewById(R.id.row1);
		TableRow changeMyEmail = (TableRow) view.findViewById(R.id.row2);
		TableRow changePartnerName = (TableRow) view.findViewById(R.id.row3);
		TableRow changePartnerEmail = (TableRow) view.findViewById(R.id.row4);
		TableRow unlink = (TableRow) view.findViewById(R.id.row5);
		TableRow signOut = (TableRow) view.findViewById(R.id.row6);
		TableRow deleteAccount = (TableRow) view.findViewById(R.id.row7);

		changeMyName.setOnClickListener(this);
		changeMyEmail.setOnClickListener(this);
		changePartnerName.setOnClickListener(this);
		changePartnerEmail.setOnClickListener(this);
		unlink.setOnClickListener(this);
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

		}

	}

}
