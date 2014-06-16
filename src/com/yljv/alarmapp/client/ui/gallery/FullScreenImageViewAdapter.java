package com.yljv.alarmapp.client.ui.gallery;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yljv.alarmapp.R;

public class FullScreenImageViewAdapter extends PagerAdapter {
	
	private Activity activity;
	private ArrayList<String> imagePaths;
	
	public FullScreenImageViewAdapter(Activity activity, ArrayList<String> imagePaths) {
		this.activity = activity;
		this.imagePaths = imagePaths;
	}
	
	@Override
	public int getCount() {
		return this.imagePaths.size();
	}
	
	@Override 
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}
	
	@Override 
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView image;
		
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewLayout = inflater.inflate(R.layout.detail_fullscreen_image, container, false);
		
		image = (ImageView) viewLayout.findViewById(R.id.image);
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		options.inSampleSize = 8;
		String picturePath = imagePaths.get(position);
		Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);
		
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
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		}
		catch(Exception e) {
			
		}
		
		image.setImageBitmap(bitmap);
		
		((ViewPager) container).addView(viewLayout);
		
		return viewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView((RelativeLayout) object);
	}
		

}
