package com.yljv.alarmapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.yljv.alarmapp.MenuMainActivity;
import com.yljv.alarmapp.R;

public class AddPicForPartnerFragment extends SherlockFragment implements OnClickListener, OnItemClickListener {
	
	public static final String MESSAGE_FOR_ALARM = "com.yljv.alarmapp.MESSAGE_FOR_ALARM";
	public static String picturePath;
	
	Button previewButton;
	ImageView addPicture;
	EditText addMessage;
	ListPopupWindow listPopupWindow;
	String[] picOption = {"Take a picture", "Choose from gallery"};
	static final int REQUEST_TAKE_PHOTO = 1;
	private static final int RESULT_LOAD_IMAGE = 1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getActionBar().setTitle("Customize alarm");
		setHasOptionsMenu(true);
	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.main, menu);
		menu.findItem(R.id.cancel_alarm).setEnabled(true);		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.cancel_alarm:
			getFragmentManager().popBackStackImmediate();
			break;
		//Save button has to be a preview button
		case R.id.delete_alarm: 
			Fragment newFragment = new PreviewPictureFragment();
			if(getActivity() instanceof MenuMainActivity) {
				MenuMainActivity mma = new MenuMainActivity();
				mma.switchContent(newFragment);
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.add_for_partner, container, false);
		previewButton = (Button) view.findViewById(R.id.preview);
		addPicture = (ImageView) view.findViewById(R.id.add_picture);
		addMessage = (EditText) view.findViewById(R.id.add_message);
		addPicture.isClickable();
		addPicture.setOnClickListener(this);
		previewButton.setOnClickListener(this);
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
		switch(v.getId()) {
		case R.id.add_picture:
			listPopupWindow.show();
			break;
		case R.id.preview:
			String myMessage = addMessage.getText().toString();
			Bundle data = new Bundle();
			data.putString(MESSAGE_FOR_ALARM, myMessage);
			Fragment newContent = new PreviewPictureFragment();
			newContent.setArguments(data);
			if (getActivity() instanceof MenuMainActivity) {
				MenuMainActivity mma = (MenuMainActivity) getActivity();
				mma.switchContent(newContent);
			} 
			break;
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
			picturePath = cursor.getString(columnIndex);
			cursor.close();
			
			addPicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		}
	}

}