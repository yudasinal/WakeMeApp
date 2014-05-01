package com.yljv.alarmapp.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

public class GalleryUtils {
	
	private Context _context;
	
	public GalleryUtils(Context context) {
		this._context = context;
	}

	public ArrayList<String> getFilePaths() {
		ArrayList<String> filePaths = new ArrayList<String>();
		
		File rootsd = Environment.getExternalStorageDirectory();
		File directory = new File(rootsd.getAbsolutePath() + "/DCIM" + "/" + GalleryConstants.PHOTO_ALBUM);
		
		if(directory.isDirectory()) {
			File[] listFiles = directory.listFiles();	
			
			if(listFiles.length>0) {
				
				for(int i = 0; i<listFiles.length; i++) {
					
					String filePath = listFiles[i].getAbsolutePath();
					
					if(isSupportedFile(filePath)) {
						filePaths.add(filePath);
					}
				}
			}
			else{
				Toast.makeText(_context, GalleryConstants.PHOTO_ALBUM
                        + " is empty. Please load some images in it !",
                Toast.LENGTH_LONG).show();
			}
		}
		else {
			AlertDialog.Builder alert = new AlertDialog.Builder(_context);
			alert.setTitle("Error!");
            alert.setMessage(GalleryConstants.PHOTO_ALBUM
                    + " directory path is not valid! Please set the image directory name GalleryConstants.java class");
            alert.setPositiveButton("OK", null);
            alert.show();
		}
		return filePaths;
	}
	
	private boolean isSupportedFile(String filePath) {
		String ext = filePath.substring((filePath.lastIndexOf(".") +1), filePath.length());
		
		if(GalleryConstants.FILE_EXTN.contains(ext.toLowerCase(Locale.getDefault()))) {
			return true;
		}
		else{
			return false;
		}
	}
	
	public int getScreenWidth() {
		int columnWidth;
		WindowManager wm = (WindowManager) _context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		final Point point = new Point();
		try{
			display.getSize(point);
		} catch(java.lang.NoSuchMethodError ignore){ 
			point.x = display.getWidth();
			point.y = display.getHeight();
		}
		columnWidth = point.x;
		return columnWidth;
	}
}
