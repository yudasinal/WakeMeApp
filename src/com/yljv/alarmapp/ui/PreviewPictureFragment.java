package com.yljv.alarmapp.ui;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.yljv.alarmapp.ui.AddPicForPartnerFragment;

import com.actionbarsherlock.app.SherlockFragment;
import com.yljv.alarmapp.R;

public class PreviewPictureFragment extends SherlockFragment {
	ImageView addPicture;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.preview_for_partner, container, false);
		/*
		Resources res = getContext().getResources();
		Drawable bubbleMessage = res.getDrawable(R.drawable.bubble);
		*/
		addPicture = (ImageView) view.findViewById(R.id.add_picture);
		addPicture.setImageBitmap(BitmapFactory.decodeFile(AddPicForPartnerFragment.picturePath));
		
		return view;
		
	}

	
	
}
