package com.yljv.alarmapp.client.ui.addpicture;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.yljv.alarmapp.R;
import com.yljv.alarmapp.client.helper.SquareImageView;
import com.yljv.alarmapp.server.alarm.AlarmInstance;
import com.yljv.alarmapp.server.alarm.MyAlarmManager;
public class AddPicForPartnerActivity extends Activity implements
		OnClickListener {

	public static final String MESSAGE_FOR_ALARM = "com.yljv.alarmapp.MESSAGE_FOR_ALARM";
	public static String picturePath;

	private AlarmInstance alarm;
	MenuItem deletetion;
	MenuItem savePic;
	MenuItem cancelPic;
	MenuItem addPic;
	int width;
	int height;
	Context context;
	Uri selectedImage;
	String currentPhotoPath;
	
	
	static final int REQUEST_IMAGE_CAPTURE = 1;

	SquareImageView addPicture;
	EditText addMessage;
	String[] picOption = {getString(R.string.take_photo), getString(R.string.choose_gallery)};
	static final int REQUEST_TAKE_PHOTO = 1;
	private static final int RESULT_LOAD_IMAGE = 1;

	View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = this;

		Display display = getWindowManager().getDefaultDisplay();

		String objectId = this.getIntent().getExtras()
				.getString(AlarmInstance.COLUMN_OBJECT_ID);
		alarm = MyAlarmManager.findPartnerAlarmByObjectId(objectId);

		Point size = new Point();
		display.getSize(size);
		width = size.x;
		height = size.y;
		setContentView(R.layout.add_for_partner);
		super.onCreate(savedInstanceState);
		getActionBar().setTitle(R.string.customize);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		addPicture = (SquareImageView) findViewById(R.id.add_picture);
		addMessage = (EditText) findViewById(R.id.add_message);
		addPicture.isClickable();
		addPicture.setOnClickListener(this);
		// addPicture.setOnTouchListener(this);
		/*
		 * Bundle bundle = this.getArguments(); alarm =
		 * MyAlarmManager.findPartnerAlarmById
		 * (bundle.getInt(AlarmInstance.COLUMN_ID));
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.cancel_alarm:
			super.onBackPressed();
			break;
		case android.R.id.home:
			super.onBackPressed();
			break;
		case R.id.save_alarm:
			MyAlarmManager.addPictureOrMessageToPartnerAlarm(alarm,
					picturePath, addMessage.getText().toString());
			super.onBackPressed();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		deletetion = (MenuItem) menu.findItem(R.id.delete_alarm);
		addPic = (MenuItem) menu.findItem(R.id.add_alarm);
		cancelPic = (MenuItem) menu.findItem(R.id.cancel_alarm);
		cancelPic.setVisible(true);
		savePic = (MenuItem) menu.findItem(R.id.save_alarm);
		savePic.setVisible(true);
		deletetion.setVisible(false);
		addPic.setVisible(false);
		return true;
	}
	
	private File createImageFile() throws IOException {
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = R.string.jpeg + " " + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory
				(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(
			imageFileName, 
			".jpg", 
			storageDir);
		
		currentPhotoPath = image.getAbsolutePath();
		
		return image;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_picture:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.add_photo);
			builder.setItems(picOption, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int item) {
					if (picOption[item].equals("Take Photo")) {
						Intent takePictureIntent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						
						//Create a file for the picture and call the method for it
						if (takePictureIntent
								.resolveActivity(getPackageManager()) != null) {
							File photoFile = null;
							try{
								photoFile = createImageFile();
							}
							catch (IOException ex) {
								//Error occurred while creating the File for the picture
							}
							
							if(photoFile != null) {
								selectedImage = Uri.fromFile(photoFile);
								takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, 
										selectedImage);
							}
							startActivityForResult(takePictureIntent,
									REQUEST_IMAGE_CAPTURE);
						}
					} else {
						Intent i = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(i, RESULT_LOAD_IMAGE);
					}

				}
			});
			builder.show();
			break;

		}
	}
	
	
@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE
				&& resultCode == Activity.RESULT_OK) {
			
			Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
			
			try {
				ExifInterface exif = new ExifInterface(currentPhotoPath);
				int orientation = exif.getAttributeInt(
						ExifInterface.TAG_ORIENTATION, 1);
				Log.d("EXIF", "Exif: " + orientation);
				Matrix matrix = new Matrix();
				if (orientation == 6) {
					matrix.postRotate(90);
				} else if (orientation == 3) {
					matrix.postRotate(180);
				} else if (orientation == 8) {
					matrix.postRotate(270);
				}
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
						bitmap.getHeight(), matrix, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			

			addPicture.setImageBitmap(bitmap);

			// TODO delete
		}
	}


}
