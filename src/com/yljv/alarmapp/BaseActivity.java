package com.yljv.alarmapp;

import com.yljv.alarmapp.R;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.yljv.alarmapp.ui.MenuList;

public class BaseActivity extends SlidingFragmentActivity {
	
	private int appTitle;
	protected SherlockListFragment menuFragment; 
	public Menu menu;
	
	public BaseActivity(int title){
		appTitle = title;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setTitle(appTitle);
		setBehindContentView(R.layout.menu_frame);
		android.support.v4.app.FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		menuFragment = new MenuList();
		ft.replace(R.id.menu_frame, menuFragment);
		ft.commit();
		
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidth(15);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(140);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setIcon(R.drawable.launchericon_googleplay);
	}
	
	@Override	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		this.menu = menu;
		/*menu.findItem(R.id.add_alarm).setVisible(false);
		menu.findItem(R.id.save_alarm).setVisible(false);
		menu.findItem(R.id.cancel_alarm).setVisible(false);
		menu.findItem(R.id.cancel_alarm).setEnabled(false);
		menu.findItem(R.id.save_alarm).setEnabled(false);
		menu.findItem(R.id.add_alarm).setEnabled(false);
		
		return super.onCreateOptionsMenu(menu);
	}
	*/
	
}