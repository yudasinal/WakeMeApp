package com.yljv.alarmapp.parse.database;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.yljv.alarmapp.helper.AccountManager;
import com.yljv.alarmapp.helper.ApplicationSettings;


public class PartnerAlarmManager {
	
	public static ArrayList<Alarm> partnerAlarms = new ArrayList<Alarm>();
	
	//TODO create Items for each day alarm gets repeated
	public static void findAllPartnerAlarms(
			final ParsePartnerAlarmListener listener) {
		
		ParseQuery<Alarm> query = ParseQuery.getQuery("AlarmInstance");
		query.whereEqualTo("user", ApplicationSettings.getPartnerEmail());
		query.orderByAscending("time");
		query.findInBackground(new FindCallback<Alarm>() {
			public void done(List<Alarm> list, ParseException e) {
				if (e == null) {
					// query worked
					listener.partnerAlarmsFound(list);
				} else {
					// query failed
					listener.partnerAlarmsSearchFailed(e);
				}
			}
		});
	}
		
		public static ArrayList<Alarm> getPartnerAlarms() {
			//TODO get a list of partner alarms 
			return partnerAlarms;
		}

}
