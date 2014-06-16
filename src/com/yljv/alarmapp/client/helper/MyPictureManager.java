package com.yljv.alarmapp.client.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

public class MyPictureManager {

	private File storeImage(Context context, Bitmap imageData, String fileName){
		File file;

		if (fileName==null){
			file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "AlarmApp");
			if(! file.exists()){
				if(! file.mkdirs()){
					return null;
				}
			}
			
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			fileName = "IMG_" + timeStamp + ".png";
		}
		
		file = new File(context.getFilesDir(), fileName);
		
		try{
			OutputStream outStream = new FileOutputStream(file);
			imageData.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return file;
		
	}
}
