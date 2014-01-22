package com.yljv.alarmapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;

import com.yljv.alarmapp.R;

public class AddPicForPartnerFragment extends Fragment implements OnClickListener, OnItemClickListener {
	
	Button addPicture;
	EditText addMessage;
	ListPopupWindow listPopupWindow;
	String[] picOption = {"Take a picture", "Choose from gallery"};
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.add_for_partner, container, false);
		addPicture = (Button) view.findViewById(R.id.add_picture);
		addMessage = (EditText) view.findViewById(R.id.add_message);
		addPicture.setOnClickListener(this);
		listPopupWindow = new ListPopupWindow(this.getActivity());
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.popup_layout, picOption);
		listPopupWindow.setAdapter(arrayAdapter);
		listPopupWindow.setAnchorView(addPicture);
		listPopupWindow.setWidth(500);
		listPopupWindow.setHeight(250);
		listPopupWindow.setHorizontalOffset(300);
		listPopupWindow.setVerticalOffset(200);
		listPopupWindow.setModal(true);
		listPopupWindow.setOnItemClickListener(this);
		return view;
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		listPopupWindow.show();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		if (position == 0) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, 0);
		}
		
	}
	
	

}
