package com.yljv.alarmapp.helper;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.yljv.alarmapp.parse.database.AlarmInstance;
import com.yljv.alarmapp.parse.database.MyAlarmManager;

public class PictureReceiver extends BroadcastReceiver {
	private static final String TAG = "MyCustomReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			
			JSONObject json = new JSONObject(intent.getExtras().getString(
					"com.parse.Data"));

			String id = json.getString("id");
			
			ParseQuery<AlarmInstance> query = ParseQuery.getQuery("AlarmInstance");
			query.whereEqualTo(AlarmInstance.COLUMN_USER,
					ApplicationSettings.getUserEmail());
			query.whereEqualTo("objectId", id);

			query.findInBackground(new FindCallback<AlarmInstance>() {
				public void done(List<AlarmInstance> list, ParseException e) {
					if (e == null) {
						if(list.size() == 1){
							AlarmInstance alarm = list.get(0);
							MyAlarmManager.updateAlarmInstance(alarm);
							System.out.println("Alarm updated");
						}
					} else {
						System.out.println("Alarm not received");
					}
				}
			});
			
		} catch (JSONException e) {
			Log.d(TAG, "JSONException: " + e.getMessage());
		}
	}
}
