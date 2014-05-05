package com.yljv.alarmapp.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.yljv.alarmapp.R;
import com.yljv.alarmapp.helper.MsgPictureTuple;
import com.yljv.alarmapp.parse.database.Alarm;
import com.yljv.alarmapp.parse.database.AlarmInstance;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

public class PicMsgArrivedFragment extends Fragment {

	public static final String MESSAGE_FOR_ALARM = "com.yljv.alarmapp.MESSAGE_FOR_ALARM";
	public static String picturePath;

	private AlarmInstance alarm;

	Button previewButton;
	ImageView picture;
	EditText message;

	int id;

	View view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getActionBar().hide();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		boolean hasOnlyPic = true;
		view = inflater.inflate(R.layout.add_for_partner, container, false);
		picture = (ImageView) view.findViewById(R.id.add_picture);
		message = (EditText) view.findViewById(R.id.add_message);

		id = this.getArguments().getInt(AlarmInstance.COLUMN_ID);

		MsgPictureTuple tuple = MyAlarmManager.findPicMsgByAlarmId(id);
		byte[] data = tuple.getPicData();
		if (data != null) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			picture.setImageBitmap(bitmap);
		}

		String msg = tuple.getMsg();
		if (msg != null) {
			message.setText(msg);
		} else {
			message.setVisibility(View.GONE);

		}

		new TakeScreenshotTask().execute();
		

		Alarm alarm = MyAlarmManager.findAlarmById(id / 10 * 10);
		if(alarm!=null){
			MyAlarmManager.setNextAlarmInstance(alarm);
		}

		return view;
	}

	private class TakeScreenshotTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Log.e("WakeMeApp", "error", e);
			}
			takeScreenshot();
			return null;
		}
	}

	public void takeScreenshot() {
		View v = view.getRootView();
		v.setDrawingCacheEnabled(true);

		Bitmap bitmap = v.getDrawingCache();

		try {
			
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(new Date());
			String imageFileName = "JPEG_" + timeStamp + "_";
			File storageDir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			File image = File.createTempFile(imageFileName, /* prefix */
					".jpg", /* suffix */
					storageDir /* directory */
			);

			// Save a file: path for use with ACTION_VIEW intents
			String mCurrentPhotoPath = "file:" + image.getAbsolutePath();

			FileOutputStream fos = new FileOutputStream(image.getPath());
			bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
			fos.flush();
			fos.close();
			
			Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		    File f = new File(mCurrentPhotoPath);
		    Uri contentUri = Uri.fromFile(f);
		    mediaScanIntent.setData(contentUri);
		    getActivity().sendBroadcast(mediaScanIntent);
		    
		    //Images.Media.insertImage(getContentResolver(), bitmap, "picture", "description");

			
		} catch (IOException e) {
			Log.e("WakeMeApp", "Exception", e);
		}
		
	}

}