package com.yljv.alarmapp.ui;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.yljv.alarmapp.R;

public class PreviewPictureFragment extends SherlockFragment {
	ImageView addPicture;
	TextView myMessage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.preview_for_partner, container, false);
		
		
		Bundle bundle = this.getArguments();
		myMessage = (TextView) view.findViewById(R.id.my_message);
		myMessage.setText(bundle.getString(AddPicForPartnerFragment.MESSAGE_FOR_ALARM));
		
		Resources res = view.getContext().getResources();
		Drawable bubbleMessage = res.getDrawable(R.drawable.bubble);
		myMessage.setBackgroundDrawable(bubbleMessage);
		
		
		addPicture = (ImageView) view.findViewById(R.id.my_picture);
		addPicture.setImageBitmap(BitmapFactory.decodeFile(AddPicForPartnerFragment.picturePath));
		
		return view;
		
	}

	
	
}
