package com.yljv.alarmapp.helper;
	
import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yljv.alarmapp.MenuMainActivity;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.parse.database.AlarmInstance;
import com.yljv.alarmapp.parse.database.MyAlarmManager;
import com.yljv.alarmapp.ui.AddPicForPartnerFragment;

	public class PartnerClockAdapter extends ArrayAdapter<AlarmInstance> {
		
		boolean pictureSet;
		public ImageView setPicture;
		private AlarmInstance partnerAlarm;
		public int alarmPosition;
		
		
		public PartnerClockAdapter(Context context) {
			super(context, R.layout.partner_alarm_item, new ArrayList<AlarmInstance>());
			this.addAll(MyAlarmManager.getPartnerAlarms());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = convertView;
			ViewHolder holder;
			ArrayList<AlarmInstance> partnerAlarms;
			
			if(rowView == null) {
				LayoutInflater inflater = LayoutInflater.from(getContext());
				rowView = inflater.inflate(R.layout.partner_alarm_item, null);
				holder = new ViewHolder();
				//holder.textView = (TextView) rowView.findViewById(R.id.partner_text);
				holder.timeView = (TextView) rowView.findViewById(R.id.partner_time);
				holder.dayView = (TextView) rowView.findViewById(R.id.partner_day);
				holder.dateView = (TextView) rowView.findViewById(R.id.partner_date);
				holder.morEv = (TextView) rowView.findViewById(R.id.morning_evening);
				
				partnerAlarms = MyAlarmManager.getPartnerAlarms();
				
				rowView.setTag(holder);
				setPicture = (ImageView) rowView.findViewById(R.id.set_picture);
				setPicture.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(!pictureSet) {
							Fragment newContent = null;
							newContent = new AddPicForPartnerFragment();
							if (getContext() instanceof MenuMainActivity) {
								MenuMainActivity mma = (MenuMainActivity) getContext();
								mma.switchContent(newContent);
							} 
						}
						/*
						else {
							Bundle bundle = new Bundle();
							alarmPosition = partnerAlarms.
							bundle.putInt("pictureMessage", );
							setPicture.setImageResource(R.drawable.ic_action_picture);
							pictureSet = true;
						}
						*/
					}

				});
				
				if (partnerAlarms == null) {
					LayoutInflater inflater1 = LayoutInflater.from(getContext());
					rowView = inflater1.inflate(R.layout.no_alarms_to_display, null);
				}
				else if (partnerAlarms != null) {
					ViewHolder myHolder = (ViewHolder)rowView.getTag();
					partnerAlarm = partnerAlarms.get(position);
					String text = partnerAlarm.getName();
					String time = partnerAlarm.getTimeAsString();
					String morEv = partnerAlarm.getMorningEveningAsString();
					//String date = partnerAlarm.getDate();
					//String day = partnerAlarm.getDay();
					myHolder.timeView.setText(time);
					myHolder.morEv.setText(morEv);
					//myHolder.textView.setText(text);
					//myHolder.dateView.setText(date);
					//myHolder.dayView.setText(day);
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
			TextView morEv;
		}

	}




