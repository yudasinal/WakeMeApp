package com.yljv.alarmapp.helper;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PartnerReceiver extends BroadcastReceiver{
	


	private static final String CATEGORY_KEY = "category";
	
	private static final int PARTNER_REQUEST = 0;
	private static final int PARTNER_CANCEL_REQUEST = 1;
	private static final int PARTNER_ACCEPT_REQUEST = 2;
	private static final int PARTNER_DECLINE_REQUEST = 3;
	private static final int PARTNER_UNLINK = 4;

	private static final String EMAIL_KEY = "email";
	

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			

			JSONObject json = new JSONObject(intent.getExtras().getString(
					"com.parse.Data"));

			String email = json.getString(EMAIL_KEY);
			int cat = json.getInt(CATEGORY_KEY);
			
			switch(cat){
			case PARTNER_REQUEST:
				AccountManager.incomingPartnerRequest(email);
				break;
			case PARTNER_CANCEL_REQUEST:
				AccountManager.onRequestCancelled();
				break;
			case PARTNER_ACCEPT_REQUEST:
				AccountManager.onRequestAccepted();
				break;
			case PARTNER_DECLINE_REQUEST:
				AccountManager.onRequestDeclined();
				break;
			case PARTNER_UNLINK:
				AccountManager.onUnlinked();
				break;
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
