package com.yljv.alarmapp.ui;

import android.graphics.Bitmap;

public class ImageItem {
	private Bitmap image;
	
	public ImageItem(Bitmap image) {
		super();
		this.image = image;
	}
	
	public Bitmap getImage() {
		return image;
	}
	
	public void setImage(Bitmap image) {
		this.image = image;
	}

}
