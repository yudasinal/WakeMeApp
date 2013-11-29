package com.yljv.alarmapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
	
	Fragment currentFragment;
	
		
	final static int numberOfPages= 2;
	public ScreenSlidePagerAdapter(FragmentManager fm) {
         super(fm);
         //currentFragment = new MainFragment(); 
     }

	@Override
	public Fragment getItem(int position) {
		Fragment fragment = currentFragment;
		switch(position){
		case 1:
			break;
		case 2:
			break;
		default:
			return null;
		}
		// TODO Auto-generated method stub
		//return new ScreenSlidePageFragment();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}



}
