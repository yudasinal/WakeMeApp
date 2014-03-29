package com.yljv.alarmapp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.yljv.alarmapp.R;
import com.yljv.alarmapp.parse.database.AlarmInstance;

public class PicMsgArrivedFragment extends Fragment {
	
	public static final String MESSAGE_FOR_ALARM = "com.yljv.alarmapp.MESSAGE_FOR_ALARM";
	public static String picturePath;
	
	private AlarmInstance alarm;
	
	Button previewButton;
	ImageView picture;
	EditText message;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getActionBar().hide();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		boolean hasOnlyPic = true;
		View view = inflater.inflate(R.layout.add_for_partner, container, false);
		picture = (ImageView) view.findViewById(R.id.add_picture);
		message = (EditText) view.findViewById(R.id.add_message);
		if(hasOnlyPic) {
			//TODO check if only pic is there, if yes, no need to display the message field
			message.setVisibility(View.GONE);
		}
		
		return view;
	}
	

}