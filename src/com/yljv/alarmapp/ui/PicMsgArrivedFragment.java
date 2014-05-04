package com.yljv.alarmapp.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
		Bitmap b = v.getDrawingCache();
		String extr = Environment.getExternalStorageDirectory().toString();
		long currentTime = System.currentTimeMillis();
		File myPath = new File(extr, Long.toString(currentTime) + ".jpg");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(myPath);
			b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
			MediaStore.Images.Media.insertImage(getActivity()
					.getContentResolver(), b, "Screen", "screen");
		} catch (FileNotFoundException e) {
			Log.e("WakeMeApp", "Error", e);
		} catch (Exception e) {
			Log.e("WakeMeApp", "Error", e);
		}
	}

}