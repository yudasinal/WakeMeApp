package com.yljv.alarmapp.ui;

import java.util.ArrayList;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragment;
import com.yljv.alarmapp.R;

/*
 * shows you all your saved pictures
 */
public class GalleryFragment extends SherlockFragment{
	private GridView gridView;
	private PictureAdapter myPictureAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.gallery_layout, container, false);
		gridView = (GridView) view.findViewById(R.id.gridView);
		myPictureAdapter = new PictureAdapter(getActivity(), R.layout.gallery_row, getData());
		getActivity().getActionBar().setTitle("Gallery");
		gridView.setAdapter(myPictureAdapter);
		return view;
	}

	private ArrayList getData() {
		final ArrayList pictureItems = new ArrayList();
		TypedArray pics = getResources().obtainTypedArray(R.array.photo_options);
		for (int i = 0; i < pics.length(); i++) {
			Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), 
					pics.getResourceId(i, -1));
			pictureItems.add(new ImageItem(bitmap));
			
			// TODO Auto-generated method stub
		}
		return pictureItems;
	}
	
	
	
	
	
	
}
