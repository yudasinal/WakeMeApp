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

public class UpdateReceiver extends BroadcastReceiver {
	private static final String TAG = "MyCustomReceiver";

	private static final String DELETE_CATEGORY = "delete";
	private static final String UPDATE_CATEGORY = "update";
	private static final String PICTURE_CATEGORY = "picture";

	private static final String CATEGORY_KEY = "category";

	@Override
	public void onReceive(final Context context, Intent intent) {
		try {

			JSONObject json = new JSONObject(intent.getExtras().getString(
					"com.parse.Data"));

			final String id = json.getString("id");

			final String cat = json.getString(CATEGORY_KEY);

			if (cat.equals(DELETE_CATEGORY)) {
				MyAlarmManager.onPartnerAlarmInstanceDeleted(id);
			} else if (cat.equals(UPDATE_CATEGORY)) {
				ParseQuery<AlarmInstance> query = ParseQuery
						.getQuery("AlarmInstance");
				query.whereEqualTo("objectId", id);

				query.findInBackground(new FindCallback<AlarmInstance>() {
					public void done(List<AlarmInstance> list, ParseException e) {
						if (e == null) {
							if (list.size() == 1) {
								MyAlarmManager.onPartnerAlarmInstanceUpdated(list
										.get(0));
							}
						}
					}

				});
			} else if (cat.equals(PICTURE_CATEGORY)) {
				ParseQuery<AlarmInstance> query = ParseQuery
						.getQuery("AlarmInstance");
				query.whereEqualTo(AlarmInstance.COLUMN_USER,
						ApplicationSettings.getUserEmail());
				query.whereEqualTo("objectId", id);

				query.findInBackground(new FindCallback<AlarmInstance>() {
					public void done(List<AlarmInstance> list, ParseException e) {
						if (e == null) {
							if (list.size() == 1) {
								MyAlarmManager.onMyAlarmInstanceUpdated(context, list
										.get(0));
							}
						}
					}
				});
			}

		} catch (JSONException e) {
			Log.d(TAG, "JSONException: " + e.getMessage());
		}
	}
}
