package com.yljv.alarmapp.client.helper;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.yljv.alarmapp.R;

public class NotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String message = "";
		try {

			JSONObject json = new JSONObject(intent.getExtras().getString(
					"com.parse.Data"));

			message = json.getString("message");
			
			if(!ApplicationSettings.getNoticationActivated()){
				return;
			}
			
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context).setSmallIcon(R.drawable.launchericon_artwork)
					.setContentText(message).setContentTitle("1 new Alarm").setDefaults(Notification.DEFAULT_LIGHTS);
			
			if(ApplicationSettings.getNofiticationSoundActivated()){
				mBuilder.setDefaults(Notification.DEFAULT_SOUND);
			}
			
			if(ApplicationSettings.getNofiticationVibrationActivated()){
				mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
			}
			
			
			
			
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(context, MenuMainActivity.class);

			// The stack builder object will contain an artificial back stack for
			// the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(MenuMainActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
					PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
				    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			mNotificationManager.notify(1, mBuilder.build());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		
	}

}
