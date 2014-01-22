package com.yljv.alarmapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.yljv.alarmapp.R;

public class AddPicForPartnerFragment extends Fragment implements OnClickListener {
	
	Button addPicture;
	EditText addMessage;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.add_for_partner, container, false);
		addPicture = (Button) view.findViewById(R.id.add_picture);
		addMessage = (EditText) view.findViewById(R.id.add_message);
		addPicture.setOnClickListener(this);
		return view;
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		LayoutInflater inflator = (LayoutInflater)getActivity().getBaseContext().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
		View popUpView = inflator.inflate(R.layout.popup_layout, null);
		final PopupWindow popUp = new PopupWindow(popUpView,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
	}
	
	

}
