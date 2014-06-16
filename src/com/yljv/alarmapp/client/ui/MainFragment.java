package com.yljv.alarmapp.client.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yljv.alarmapp.R;

public class MainFragment extends Fragment {
	
	TextView txtMe;
	TextView txtHim; 
	RadioButton checkFemale; 
	RadioButton checkMale; 
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		
		txtMe = (TextView) view.findViewById(R.id.main_screen_me);
		txtMe.setText("My Alarm");
		txtHim = (TextView) view.findViewById(R.id.main_screen_partner);
		
		return view;
	}
		
}
		/*public void onRadioButtonClicked(View view) {
		    // Is the button now checked?
		    boolean checked = ((RadioButton) view).isChecked();
		    // Check which radio button was clicked
		    switch(view.getId()) {
		        case R.id.checkFemale:
		            if (checked)
		            	txtMe.setText("His Alarms");
		            break;
		        case R.id.checkMale:
		            if (checked)
		                txtMe.setText("Her Alarms");
		            break;
		    }
		}

		
		
	}*/
	
	


