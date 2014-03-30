	package com.yljv.alarmapp;

	import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.FloatMath;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import com.yljv.alarmapp.helper.SquareImageView;
import com.yljv.alarmapp.parse.database.AlarmInstance;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

	public class AddPicForPartnerActivity extends Activity implements OnClickListener {
		
		public static final String MESSAGE_FOR_ALARM = "com.yljv.alarmapp.MESSAGE_FOR_ALARM";
		public static String picturePath;
		
		private AlarmInstance alarm;
		MenuItem deletetion;
		MenuItem savePic;
		MenuItem cancelPic;
		MenuItem addPic;
		int width;
		int height;

		private Matrix matrix = new Matrix();
		private Matrix savedMatrix = new Matrix();
		
		private static final int NONE = 0;
		private static final int DRAG = 1;
		private static final int ZOOM = 2;
		private int mode = NONE;
		
		//zooming
		private PointF start = new PointF();
		private PointF mid = new PointF();
		private float oldDist = 1f;
		private float d = 0f;
		private float newRot = 0f;
		private float[] lastEvent = null;
		
		SquareImageView addPicture;
		EditText addMessage;
		String[] picOption = {"Take Photo", "Choose from gallery"};
		static final int REQUEST_TAKE_PHOTO = 1;
		private static final int RESULT_LOAD_IMAGE = 1;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			Display display = getWindowManager().getDefaultDisplay();
			
			String objectId = this.getIntent().getExtras().getString(AlarmInstance.COLUMN_OBJECT_ID);
			alarm = MyAlarmManager.findPartnerAlarmByObjectId(objectId);
			
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = size.y;
			setContentView(R.layout.add_for_partner);
			super.onCreate(savedInstanceState);
			getActionBar().setTitle("Customize");
			getActionBar().setDisplayHomeAsUpEnabled(true);
			
			addPicture = (SquareImageView) findViewById(R.id.add_picture);
			addMessage = (EditText) findViewById(R.id.add_message);
			addPicture.isClickable();
			addPicture.setOnClickListener(this);
			//addPicture.setOnTouchListener(this);
			/*
			Bundle bundle = this.getArguments();
			alarm = MyAlarmManager.findPartnerAlarmById(bundle.getInt(AlarmInstance.COLUMN_ID));	
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
			switch(item.getItemId()) {
			case R.id.cancel_alarm:
				super.onBackPressed();
			case android.R.id.home:
				super.onBackPressed();
			case R.id.save_alarm:
				MyAlarmManager.addPictureOrMessageToPartnerAlarm(alarm, picturePath, addMessage.getText().toString());
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
		

		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.add_picture:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Add photo");
				builder.setItems(picOption, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int item) {
						// TODO Auto-generated method stub
						if(picOption[item].equals("Take Photo")) {
							Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
						}
						else {
							Intent i = new Intent(
									Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
									startActivityForResult(i, RESULT_LOAD_IMAGE);
						}
						
					}
				});
				builder.show();
				break;
			/*
			case R.id.preview:
				String myMessage = addMessage.getText().toString();
				Bundle data = new Bundle();
				data.putString(MESSAGE_FOR_ALARM, myMessage);
				Fragment newContent = new PreviewPictureFragment();
				newContent.setArguments(data);
				Intent intent = new Intent(this, MenuMainActivity.class);
				startActivity(intent);
				break;
			*/
			}
		}


		/*
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
		
			if (position == 1) {
				Intent i = new Intent(
						Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		}
		*/
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			
			if(requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK && null != data) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				
				Cursor cursor = getContentResolver().query(selectedImage, 
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex);
				cursor.close();
				
				Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
				addPicture.setImageBitmap(bitmap);
				
			}
		}

		/*
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
				}
				lastEvent = new float[4];
				lastEvent[0] = event.getX(0);
				lastEvent[1] = event.getX(1);
				lastEvent[2] = event.getY(0);
				lastEvent[3] = event.getY(1);
				d = rotation(event);
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				lastEvent = null;
				break;
			case MotionEvent.ACTION_MOVE:
			if (mode == ZOOM) {
			
				float newDist = spacing(event);
			
			if (newDist > 10f) {
				matrix.set(savedMatrix);
				float scale = (newDist / oldDist);
				matrix.postScale(scale, scale, mid.x, mid.y);
			}
			if (lastEvent != null && event.getPointerCount() == 3) {
				newRot = rotation(event);
				float r = newRot - d;
				float[] values = new float[9];
				matrix.getValues(values);
				float tx = values[2];
				float ty = values[5];
				float sx = values[0];
				float xc = (addPicture.getWidth() / 2) * sx;
				float yc = (addPicture.getHeight() / 2) * sx;
				matrix.postRotate(r, tx + xc, ty + yc);
			}
			}
			}
			 
			addPicture.setImageMatrix(matrix);
			return true;
			}
			
			 
			/**
			* Determine the space between the first two fingers
			
			
			private float spacing(MotionEvent event) {
				float x = event.getX(0) - event.getX(1);
				float y = event.getY(0) - event.getY(1);
				return FloatMath.sqrt(x * x + y * y);
			}
			*/

			/**
			* Calculate the mid point of the first two fingers
			
			private void midPoint(PointF point, MotionEvent event) {
				float x = event.getX(0) + event.getX(1);
				float y = event.getY(0) + event.getY(1);
				point.set(x / 2, y / 2);
			}
			*/
			
			 
			/**
			* Calculate the degree to be rotated by.
			*
			* @param event
			* @return Degrees
			
			private float rotation(MotionEvent event) {
				double delta_x = (event.getX(0) - event.getX(1));
				double delta_y = (event.getY(0) - event.getY(1));
				double radians = Math.atan2(delta_y, delta_x);
				return (float) Math.toDegrees(radians);
			  
		}
		*/
		
		
	
}
	

