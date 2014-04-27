package com.yljv.alarmapp.helper;

import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SignUpCallback;
import com.yljv.alarmapp.parse.database.MyAlarmManager;
import com.yljv.alarmapp.parse.database.ParseLoginListener;
import com.yljv.alarmapp.parse.database.ParseRegisterListener;
import com.yljv.alarmapp.parse.database.PartnerRequestListener;

public class AccountManager {


	public class User {
		public final static String NAME_COLUMN = "name";
		public final static String PARTNER_COLUMN = "partner";
		public final static String ID_COLUMN = "id";
		public final static String PARTNER_ID_COLUMN = "partner_id";
		public final static String PARTNER_STATUS_COLUMN = "status";

		public final static int PARTNER_REQUESTED = 0;
		public final static int NO_PARTNER = 1;
		public final static int PARTNERED = 2;
		public final static int INCOMING_REQUEST = 3;

	}
	

	public static boolean hasPartner() {
		return ParseUser.getCurrentUser().getString(User.PARTNER_COLUMN) != null;
	}

	public static String getSendingChannel() {

		String email = ParseUser.getCurrentUser()
				.getString(User.PARTNER_COLUMN).replace('.', '_');
		email = email.replace('@', '_');
		return "user_" + email;
	}

	public static String getSubscribedChannel() {

		String email = ParseUser.getCurrentUser().getEmail().replace('.', '_');
		email = email.replace('@', '_');
		return "user_" + email;
	}

	public static void login(final ParseLoginListener listener, String email,
			String password) {
		ParseUser.logInInBackground(email, password, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					ApplicationSettings.setUserEmail(user.getEmail());
					ApplicationSettings.setUserName(user.getUsername());
					int status = user.getInt(User.PARTNER_STATUS_COLUMN);
					ApplicationSettings.setPartnerStatus(status);
					String partner = user.getString(User.PARTNER_COLUMN);
					if(partner!=null){
						ApplicationSettings.setPartnerEmail(partner);
					}
					ApplicationSettings.setAlarmId(user.getInt(User.ID_COLUMN));
					listener.onLoginSuccessful();
				} else {
					Log.d("LoginException", e.getMessage());
					listener.onLoginFail(e);
				}
			}
		});
	}

	public static void register(final ParseRegisterListener listener,
			final String email, final String password) {
		ParseUser user = new ParseUser();
		user.setUsername(email);
		user.setEmail(email);
		user.put(User.PARTNER_STATUS_COLUMN, User.NO_PARTNER);
		user.setPassword(password);
		user.signUpInBackground(new SignUpCallback() {
			@Override
			public void done(ParseException e) {
				if (e == null) {
					ParseUser user = ParseUser.getCurrentUser();
					user.put(User.PARTNER_STATUS_COLUMN, User.NO_PARTNER);
					ApplicationSettings.setUserEmail(user.getEmail());
					ApplicationSettings.setUserName(user.getUsername());
					String partner = user.getString(User.PARTNER_COLUMN);
					if (partner != null) {
						ApplicationSettings.setPartnerEmail(partner);
					}
					if (user.get(User.ID_COLUMN) != null) {
						ApplicationSettings.setAlarmId(user
								.getInt(User.ID_COLUMN));
					} else {
						ApplicationSettings.setAlarmId(0);
					}
					ApplicationSettings.setUserEmail(user.getUsername());
					user.saveEventually();
					listener.onRegisterSuccess();
				} else {
					listener.onRegisterFail(e);
				}
			}
		});
	}

	/*
	 * public static ParseUser getPartner(){ return (ParseUser)
	 * ParseUser.getCurrentUser().get("partner"); }
	 */

	public static String getEmail() {
		return ApplicationSettings.getUserEmail();
	}

	public static void invitePartner(String email) {

	}
	
	public static void onRequestCancelled(){
		ApplicationSettings.setPartnerStatus(User.NO_PARTNER);
		ApplicationSettings.setPartnerEmail("");
	}
	
	public static void onRequestAccepted(){
		ApplicationSettings.setPartnerStatus(User.PARTNERED);
		MyAlarmManager.getPartnerAlarmsFromServer();
	}
	
	public static void onRequestDeclined(){
		ApplicationSettings.setPartnerEmail("");
		ApplicationSettings.setPartnerStatus(User.NO_PARTNER);
	}

	public static void onUnlinked(){
		ApplicationSettings.setPartnerEmail("");
		ApplicationSettings.setPartnerStatus(User.NO_PARTNER);
	}
	
	public static void logout(Context context) {
		MyAlarmManager.cancelAllAlarms();
		MyAlarmManager.removeDataBase();
		ParseUser.logOut();
		Set<String> channels = PushService.getSubscriptions(context);
		for (String channel : channels) {
			PushService.unsubscribe(context, channel);
		}
		ApplicationSettings.reset();
		DBHelper dbHelper = MyAlarmManager.getDBHelper();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}

	public static void unlink(){
		
		String channel = "user_" + ApplicationSettings.getPartnerEmail().replace('@', '_').replace('.', '_');
		
		try {
			JSONObject json = new JSONObject(

			"{action: \"com.yljv.alarmapp.PARTNER_REQUEST\","
					+ "\"email\": " + "\"" + ApplicationSettings.getUserEmail() + "\", " 
					+ "\"category\":" + "4"
					+"}"
					);

			ParsePush push = new ParsePush();
			push.setChannel(channel);
			push.setData(json);
			push.sendInBackground();
			
			ParsePush pushy = new ParsePush();
			pushy.setChannel(channel);
			pushy.setMessage("unlink");
			pushy.sendInBackground();
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		ApplicationSettings.unlinkPartner();
	}
	public static void acceptPartnerRequest() {
		
		String channel = "user_" + ApplicationSettings.getPartnerEmail().replace('@', '_').replace('.', '_');
		
		try {
			JSONObject json = new JSONObject(

			"{action: \"com.yljv.alarmapp.PARTNER_REQUEST\","
					+ "\"email\": " + "\"" + ApplicationSettings.getUserEmail() + "\", " 
					+ "\"category\":" + "2" 
					+"}"
					);

			ParsePush push = new ParsePush();
			push.setChannel(channel);
			push.setData(json);
			push.sendInBackground();
			
			ParsePush p = new ParsePush();
			p.setChannel(channel);
			p.setMessage("Request accepted");
			p.sendInBackground();
		} catch (Exception pe) {
			pe.printStackTrace();
		}
		
		ApplicationSettings.acceptRequest();
	}

	public static void sendPartnerRequest(String email,
			final PartnerRequestListener listener) {
		int status = ParseUser.getCurrentUser().getInt(
				User.PARTNER_STATUS_COLUMN);
		if (status == User.PARTNERED) {
			listener.onAlreadyPartnered();
		} else if (status == User.PARTNER_REQUESTED) {
			listener.onOlderRequestAlreadySent();
		} else if (status == User.NO_PARTNER) {
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			query.whereEqualTo("username", email);
			try {
				List<ParseUser> list = query.find();
				if (list.size() == 0) {
					listener.onNoPartnerFound();
				} else {

					if (list.get(0).getInt(User.PARTNER_STATUS_COLUMN) != User.NO_PARTNER) {
						listener.onPartnerNotAvailable();
						return;
					}

					ParseUser.getCurrentUser().put(User.PARTNER_COLUMN, email);
					ParseUser.getCurrentUser().put(User.PARTNER_STATUS_COLUMN,
							User.PARTNER_REQUESTED);
					ParseUser.getCurrentUser().saveEventually();
					ApplicationSettings.setPartnerRequest(email);

					String channel;
					String mail = email.replace('@', '_').replace('.', '_');
					channel = "user_" + mail;
					
					try {
						JSONObject json = new JSONObject(

						"{action: \"com.yljv.alarmapp.PARTNER_REQUEST\","
								+ "\"email\": " + "\"" + ApplicationSettings.getUserEmail() + "\", " 
								+ "\"category\":" + "0" 
								+"}"
								);

						ParsePush push = new ParsePush();
						push.setChannel(channel);
						push.setData(json);
						push.sendInBackground();
						

						ParsePush p = new ParsePush();
						p.setChannel(channel);
						p.setMessage("Request");
						p.sendInBackground();
					} catch (Exception pe) {
						pe.printStackTrace();
					}
					listener.onPartnerRequested();

				}
			} catch (ParseException e) {

			}
		}
	}

	public static void cancelPartnerRequest() {
		
		String channel = "user_" + ApplicationSettings.getPartnerEmail().replace('@', '_').replace('.', '_');
		
		try {
			JSONObject json = new JSONObject(

			"{action: \"com.yljv.alarmapp.PARTNER_REQUEST\","
					+ "\"email\": " + "\"" + ApplicationSettings.getUserEmail() + "\", " 
					+ "\"category\":" + "1" 
					+"}"
					);

			ParsePush push = new ParsePush();
			push.setChannel(channel);
			push.setData(json);
			push.sendInBackground();
			
			ParsePush p = new ParsePush();
			p.setChannel(channel);
			p.setMessage("Request cancelled");
			p.sendInBackground();
		} catch (Exception pe) {
			pe.printStackTrace();
		}

		ApplicationSettings.cancelRequest();
	}
	
	public static void declinePartnerRequest(){

		String channel = "user_" + ApplicationSettings.getPartnerEmail().replace('@', '_').replace('.', '_');
		
		try {
			JSONObject json = new JSONObject(

			"{action: \"com.yljv.alarmapp.PARTNER_REQUEST\","
					+ "\"email\": " + "\"" + ApplicationSettings.getUserEmail() + "\", " 
					+ "\"category\":" + "3" 
					+"}"
					);

			ParsePush push = new ParsePush();
			push.setChannel(channel);
			push.setData(json);
			push.sendInBackground();
			
			ParsePush p = new ParsePush();
			p.setChannel(channel);
			p.setMessage("Request declined");
			p.sendInBackground();
			
		} catch (Exception pe) {
			pe.printStackTrace();
		}
		
		ApplicationSettings.unlinkPartner();
	}
	
	public static void incomingPartnerRequest(String email){
		ApplicationSettings.incomingRequest(email);
	}
}
