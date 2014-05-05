package com.yljv.alarmapp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.yljv.alarmapp.FullScreenViewImage;


public class GridViewImageAdapter extends BaseAdapter {

	 	private Activity _activity;
	    private ArrayList<String> _filePaths = new ArrayList<String>();
	    private int imageWidth;
	 
	    public GridViewImageAdapter(Activity activity, ArrayList<String> filePaths,
	            int imageWidth) {
	        this._activity = activity;
	        this._filePaths = filePaths;
	        this.imageWidth = imageWidth;
	    }
	 
	    @Override
	    public int getCount() {
	        return this._filePaths.size();
	    }
	 
	    @Override
	    public Object getItem(int position) {
	        return this._filePaths.get(position);
	    }
	 
	    @Override
	    public long getItemId(int position) {
	        return position;
	    }
	 
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView;
	        ViewHolder holder;
	        if (convertView == null) {
	        	/*
	        	holder = new ViewHolder();
	        	holder.image = (ImageView) findViewById(R.id.i)
	        	*/
	            imageView = new ImageView(_activity);
	        } else {
	            imageView = (ImageView) convertView;
	        }
	 
	        // get screen dimensions
	        String picturePath = _filePaths.get(position);
	        Bitmap image = decodeFile(picturePath, imageWidth,
	                imageWidth);
	 
	        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	        imageView.setLayoutParams(new GridView.LayoutParams(imageWidth,
	                imageWidth));
	        
	        try{
				ExifInterface exif = new ExifInterface(picturePath);
				int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
				Log.d("EXIF", "Exif: " + orientation);
				Matrix matrix = new Matrix();
				if(orientation == 6) {
					matrix.postRotate(90);
				}
				else if(orientation == 3) {
					matrix.postRotate(180);
				}
				else if(orientation == 8) {
					matrix.postRotate(270);
				}
				image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
			}
			catch(Exception e) {
				
			}
	        
	        imageView.setImageBitmap(image);
	 
	        // image view click listener
	        imageView.setOnClickListener(new OnImageClickListener(position));
	 
	        return imageView;
	    }
	 
	    class OnImageClickListener implements OnClickListener {
	 
	        int _postion;
	 
	        // constructor
	        public OnImageClickListener(int position) {
	            this._postion = position;
	        }
	 
	        @Override
	        public void onClick(View v) {
	            // on selecting grid view image
	            // launch full screen activity
	            Intent i = new Intent(_activity, FullScreenViewImage.class);
	            i.putExtra("position", _postion);
	            _activity.startActivity(i);
	        }
	 
	    }
	 
	    /*
	     * Resizing image size
	     */
	    public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
	        try {
	 
	            File f = new File(filePath);
	 
	            BitmapFactory.Options o = new BitmapFactory.Options();
	            o.inJustDecodeBounds = true;
	            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
	 
	            final int REQUIRED_WIDTH = WIDTH;
	            final int REQUIRED_HIGHT = HIGHT;
	            int scale = 1;
	            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
	                    && o.outHeight / scale / 2 >= REQUIRED_HIGHT)
	                scale *= 2;
	 
	            BitmapFactory.Options o2 = new BitmapFactory.Options();
	            o2.inSampleSize = scale;
	            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    
	    private static class ViewHolder{
	    	ImageView image;
	    }
	 
}
