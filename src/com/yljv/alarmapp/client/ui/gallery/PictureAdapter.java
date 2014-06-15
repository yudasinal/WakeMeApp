package com.yljv.alarmapp.client.ui.gallery;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.yljv.alarmapp.R;

public class PictureAdapter extends ArrayAdapter {
	
	private Context context;
	private int layoutResourceId;
	private ArrayList data = new ArrayList();
	
	public PictureAdapter(Context context, int layoutResourceId, ArrayList data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.data = data;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View row = convertView;
		ViewHolder holder = null;
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) row.findViewById(R.id.partner_pic);
			row.setTag(holder);	
		}
		else {
			holder = (ViewHolder) row.getTag();
		}
		
		ImageItem item = (ImageItem) data.get(position);
		holder.image.setImageBitmap(item.getImage());
		return row;
	}
	
	public static class ViewHolder{
		ImageView image;
	}
	
	
}
