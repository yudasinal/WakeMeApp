package com.yljv.alarmapp.helper;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MenuAdapter<String> extends BaseAdapter{
	FragmentActivity activity;
	int menuLayout;
	String[] menu;

	public MenuAdapter(FragmentActivity activity, int menuLayout, String[] menu) {
		this.activity = activity;
		this.menuLayout = menuLayout;
		this.menu = menu;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(this.isEnabled(position) == false) {

			convertView.setBackgroundColor(Color.parseColor("#000000"));
		}
		return null;
	}

	@Override
	public boolean isEnabled(int position) {
		
		if(position == 1) {
			return false;
		}
		return true;
	}
	

}
