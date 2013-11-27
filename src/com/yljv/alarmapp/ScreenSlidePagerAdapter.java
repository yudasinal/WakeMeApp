package com.yljv.alarmapp;

import android.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
	
	Fragment currentFragment;
	
		
	final static int numberOfPages= 2;
	public ScreenSlidePagerAdapter(FragmentManager fm) {
         super(fm);
         currentFragment = mainFragment; 
     }

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return new ScreenSlidePageFragment();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}



}
