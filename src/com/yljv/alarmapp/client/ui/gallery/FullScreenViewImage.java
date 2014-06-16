package com.yljv.alarmapp.client.ui.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.yljv.alarmapp.R;
import com.yljv.alarmapp.R.id;
import com.yljv.alarmapp.R.layout;
import com.yljv.alarmapp.R.menu;

public class FullScreenViewImage extends Activity {
	
	private GalleryUtils utils;
	private FullScreenImageViewAdapter adapter;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getActionBar().setTitle("Gallery");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.fullscreen_image);
		viewPager = (ViewPager) findViewById(R.id.pager);
		
		Intent intent = getIntent();
		int position = intent.getIntExtra("position", 0);
		
		utils = new GalleryUtils(getApplicationContext());
		adapter = new FullScreenImageViewAdapter(FullScreenViewImage.this, utils.getFilePaths());
		
		viewPager.setAdapter(adapter);
		
		viewPager.setCurrentItem(position);
		
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
			break;
		case android.R.id.home:
			super.onBackPressed();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem deletetion = (MenuItem) menu.findItem(R.id.delete_alarm);
		MenuItem addPic = (MenuItem) menu.findItem(R.id.add_alarm);
		MenuItem cancelPic = (MenuItem) menu.findItem(R.id.cancel_alarm);
		cancelPic.setVisible(true);
		MenuItem savePic = (MenuItem) menu.findItem(R.id.save_alarm);
		savePic.setVisible(false);
		deletetion.setVisible(false);
		addPic.setVisible(false);
		return true;
	}
	

}
