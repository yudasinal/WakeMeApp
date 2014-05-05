package com.yljv.alarmapp.helper;
	
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yljv.alarmapp.AddPicForPartnerActivity;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.parse.database.AlarmInstance;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

	public class PartnerClockAdapter extends ArrayAdapter<AlarmInstance> {
		
		boolean pictureSet;
		public ImageView setPicture;
		private AlarmInstance partnerAlarm;
		public int alarmPosition;
		private Context context;
		
		
		public PartnerClockAdapter(Context context, ArrayList<AlarmInstance> list) {
			super(context, R.layout.partner_alarm_item, list);
			this.addAll(MyAlarmManager.getPartnerAlarms());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = convertView;
			ViewHolder holder;
			ArrayList<AlarmInstance> partnerAlarms;
			boolean hasPicture = false;
			boolean picLoading = true;
			
			if(rowView == null) {
				LayoutInflater inflater = LayoutInflater.from(getContext());
				rowView = inflater.inflate(R.layout.partner_alarm_item, null);
				holder = new ViewHolder();
				//holder.textView = (TextView) rowView.findViewById(R.id.partner_text);
				holder.timeView = (TextView) rowView.findViewById(R.id.partner_time);
				holder.dayView = (TextView) rowView.findViewById(R.id.partner_day);
				holder.dateView = (TextView) rowView.findViewById(R.id.partner_date);
				holder.morEv = (TextView) rowView.findViewById(R.id.morning_evening);
				holder.monthView = (TextView) rowView.findViewById(R.id.partner_month);
				holder.yearView = (TextView) rowView.findViewById(R.id.partner_year);
				holder.progress = (ProgressBar) rowView.findViewById(R.id.progress);
				holder.picture = (ImageView) rowView.findViewById(R.id.set_picture);
				holder.progress.setVisibility(View.GONE);
				
				
				partnerAlarms = MyAlarmManager.getPartnerAlarms();
				
				rowView.setTag(holder);
				setPicture = (ImageView) rowView.findViewById(R.id.set_picture);
				
				//TODO this did not work properly
				/*setPicture.setOnClickListener(new OnClickListener() {
				holder.picture = (ImageView) rowView.findViewById(R.id.set_picture);
				holder.picture.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(!pictureSet) {
							Intent intent = new Intent(getContext(), AddPicForPartnerActivity.class);
							context.startActivity(intent);
						}
						
						else {
							Bundle bundle = new Bundle();
							alarmPosition = partnerAlarms.
							bundle.putInt("pictureMessage", );
							setPicture.setImageResource(R.drawable.ic_action_picture);
							pictureSet = true;
						}
						
					}

				});*/
				
				if (partnerAlarms != null) {
					ViewHolder myHolder = (ViewHolder)rowView.getTag();
					partnerAlarm = partnerAlarms.get(position);
					Calendar cal = partnerAlarm.getTimeAsCalendar();
					int day = cal.DAY_OF_WEEK;
					String dayString = null;
					switch(day) {
					case 1:
						dayString = "Sunday";
						break;
					case 2:
						dayString = "Monday";
						break;
					case 3:
						dayString = "Tuesday";
						break;
					case 4:
						dayString = "Wednesday";
						break;
					case 5:
						dayString = "Thursday";
						break;
					case 6:
						dayString = "Friday";
						break;
					case 7: 
						dayString = "Saturday";
						break;
					}
					String time = partnerAlarm.getTimeAsString();
					String morEv = partnerAlarm.getMorningEveningAsString();
					myHolder.timeView.setText(time);
					myHolder.morEv.setText(morEv);
					myHolder.dayView.setText(dayString);
					
					if(partnerAlarm.isPictureSent()){
						
						//TODO if the picture is already there
						myHolder.picture.setImageResource(R.drawable.ic_action_picture);
						myHolder.progress.setVisibility(View.GONE);
					}
					else if(picLoading) {
						
						//TODO picture is loading
						myHolder.picture.setVisibility(View.GONE);
						myHolder.progress.setVisibility(View.VISIBLE);
					}
					String date = Integer.toString(cal.get(cal.DAY_OF_MONTH)) + ".";
					if(date.length() == 2) {
						myHolder.dateView.setText("0" + date);
					}
					else{
						myHolder.dateView.setText(date);
					}
					String month = Integer.toString(cal.get(cal.MONTH)+1) + ".";
					if(month.length() == 2) {
						myHolder.monthView.setText("0"+ month);
					}
					else{
						myHolder.monthView.setText(month);
					}
					String year = Integer.toString(cal.get(cal.YEAR));
					myHolder.yearView.setText(year);
				}
			}
			else {
				holder = (ViewHolder) rowView.getTag();
			}

			return rowView;
		}
		
		static class ViewHolder {
			//TextView textView;
			TextView timeView;
			TextView dayView;
			TextView dateView;
			TextView monthView;
			TextView yearView;
			TextView morEv;
			ImageView picture;
			ProgressBar progress;
		}

	}




