package com.yljv.alarmapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;

import com.yljv.alarmapp.R;

public class AddPicForPartnerFragment extends Fragment implements OnClickListener, OnItemClickListener {
	
	ImageView addPicture;
	EditText addMessage;
	ListPopupWindow listPopupWindow;
	String[] picOption = {"Take a picture", "Choose from gallery"};
	static final int REQUEST_TAKE_PHOTO = 1;
	private static final int RESULT_LOAD_IMAGE = 1;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.add_for_partner, container, false);
		addPicture = (ImageView) view.findViewById(R.id.add_picture);
		addMessage = (EditText) view.findViewById(R.id.add_message);
		addPicture.isClickable();
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
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
		}
		
		/*
		//Code to check if the phone has a Camera at all, so that it doesn't crash, if it hasn't
		if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				//TODO
			}
			
			if (photoFile != null) {
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
			}
			*/
	/*
	private File createImageFile() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	*/
		if (position == 1) {
			Intent i = new Intent(
					Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, RESULT_LOAD_IMAGE);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		getActivity();
		if(requestCode == RESULT_LOAD_IMAGE && resultCode == FragmentActivity.RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			
			Cursor cursor = getActivity().getContentResolver().query(selectedImage, 
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			
			addPicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		}
	}

}