package com.yljv.alarmapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class RegisterFragment extends Fragment implements OnClickListener {
	public final static String FIRST_NAME = "com.yljv.alarmapp.FIRST_NAME";
	
	EditText editFirstName; 
	Button btnRegister;
	
	public void register(View view) {
		Intent intent = new Intent(this, UserRegisteredFragment.class);
		
		String firstName = editFirstName.getText().toString();
		intent.putExtra(FIRST_NAME, firstName);
		startActivity(intent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.register_layout, container, false);
		btnRegister = (Button) view.findViewById(R.id.btnRegister);
		editFirstName = (EditText) view.findViewById(R.id.editFirstName);
		
		btnRegister.setOnClickListener(this);
		
			
		return view;
	}

	@Override
	public void onClick(View register) {
		// TODO Auto-generated method stub
		//TODO create User here
		register();
		
	}
	
	
	

}

