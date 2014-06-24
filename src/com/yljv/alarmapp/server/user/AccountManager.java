package com.yljv.alarmapp.server.user;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.yljv.alarmapp.client.helper.ApplicationSettings;
import com.yljv.alarmapp.client.helper.DBHelper;
import com.yljv.alarmapp.client.helper.PartnerReceiver;
import com.yljv.alarmapp.server.alarm.MyAlarmManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SignUpCallback;

public class AccountManager {


    public class User {
        public final static String PARTNER_COLUMN = "partner";
        public final static String ID_COLUMN = "id";
        public final static String PARTNER_STATUS_COLUMN = "status";


        public final static String USER_NAME_COLUMN = "user_name";
        public final static String PARTNER_NAME_COLUMN = "partner_name";
        public final static String ALARM_ID_COUNTER_COLUMN = "counter";
        public final static String USER_EMAIL_COLUMN = "email";

        public final static int PARTNER_REQUESTED = 0;
        public final static int NO_PARTNER = 1;
        public final static int PARTNERED = 2;
        public final static int INCOMING_REQUEST = 3;
    }


    public static boolean hasPartner() {
        return ApplicationSettings.getPartnerStatus() == User.PARTNERED;
    }

    public static String getSendingChannel() {
        if(AccountManager.hasPartner()){
            return "user_" + ParseUser.getCurrentUser()
                    .getString(User.PARTNER_COLUMN).replace('.', '_').replace('@', '_');
        }else{
            return null;
        }
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
                        setPartnerEmail(partner);
                    }
                    if (user.get(User.ID_COLUMN) != null) {
                        ApplicationSettings.setAlarmId(user
                                .getInt(User.ID_COLUMN));
                    } else {
                        ApplicationSettings.setAlarmId(0);
                    }
                    ApplicationSettings.setUserEmail(user.getEmail());
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
        setPartnerEmail("");
        setPartnerStatus(User.NO_PARTNER);
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
    

    private static void sendPartnerNotification(int category, String message){
    	
    	ParseCloud.callFunctionInBackground("hello", new HashMap<String, Object>(), new FunctionCallback<String>() {
    		  public void done(String result, ParseException e) {
    		    if (e == null) {
    		      // result is "Hello world!"
    		    }
    		  }
    		});

        String channel = AccountManager.getSendingChannel();

        try {
            JSONObject json = new JSONObject(

                    "{action: \"com.yljv.alarmapp.PARTNER_REQUEST\","
                            + "\"email\": " + "\"" + ApplicationSettings.getUserEmail() + "\", "
                            + "\"category\":" + category
                            +"}"
            );
            ParsePush push = new ParsePush();
            push.setChannel(channel);
            push.setData(json);
            push.sendInBackground();

            sendPushMessage(message);
            
        } catch (Exception pe) {
            pe.printStackTrace();
        }

    }
    
    public static void sendPushMessage(String message){
		ParsePush push = new ParsePush();
		push.setChannel(AccountManager.getSendingChannel());

		JSONObject json;
		try {
			json = new JSONObject(

			"{action: \"com.yljv.alarmapp.NOTIFICATION\"," + "\"message\": " + "\""
					+ message + "\"}");

			push.setData(json);
			push.sendInBackground();
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
    }

    public static void unlink(){
        setPartnerEmail("");
        setPartnerStatus(User.NO_PARTNER);
        sendPartnerNotification(PartnerReceiver.PARTNER_UNLINK, "Your buddy unlinked from you");
    }
    
    public static ParseUser getPartnerUser(){
    	ParseQuery<ParseUser> query = ParseUser.getQuery();
    	query.whereEqualTo(User.USER_EMAIL_COLUMN, ApplicationSettings.getPartnerEmail());
    	try{
    		List<ParseUser> users = query.find();
    		if(users.size()==1){
    			return users.get(0);
    		}
    	}catch(ParseException e){
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public static void acceptPartnerRequest() {
        sendPartnerNotification(PartnerReceiver.PARTNER_ACCEPT_REQUEST, "Your partner accepted your request!");
        setPartnerStatus(User.PARTNERED);
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

                    setPartnerEmail(email);
                    setPartnerStatus(User.PARTNER_REQUESTED);

                    sendPartnerNotification(PartnerReceiver.PARTNER_REQUEST, "Someone wants to be your buddy");

                    listener.onPartnerRequested();

                }
            } catch (ParseException e) {

            }
        }
    }

    public static void cancelPartnerRequest() {
        sendPartnerNotification(PartnerReceiver.PARTNER_CANCEL_REQUEST, "The request was cancelled");
        setPartnerStatus(User.NO_PARTNER);
        setPartnerEmail("");
    }

    public static void declinePartnerRequest(){
        sendPartnerNotification(PartnerReceiver.PARTNER_DECLINE_REQUEST, "Your request has been cancelled");
        setPartnerStatus(User.NO_PARTNER);
        setPartnerEmail("");
    }

    public static void incomingPartnerRequest(String email){
        setPartnerEmail(email);
        setPartnerStatus(User.INCOMING_REQUEST);
    }

    public static void setPartnerEmail(String email){
        ParseUser user = ParseUser.getCurrentUser();
        user.put(User.PARTNER_COLUMN, email);
        user.saveEventually();

        ApplicationSettings.setPartnerEmail(email);
    }

    public static void setPartnerStatus(int status){
        ParseUser user = ParseUser.getCurrentUser();
        user.put(User.PARTNER_STATUS_COLUMN,
                status);
        user.saveEventually();
        ApplicationSettings.setPartnerStatus(status);
    }

    public static void setPartnerName(String name){
        ParseUser user = ParseUser.getCurrentUser();
        user.put(User.PARTNER_NAME_COLUMN, name);
        user.saveEventually();
        ApplicationSettings.setPartnerName(name);
    }

    public static void setUserName(String name){
        ParseUser user = ParseUser.getCurrentUser();
        user.put(User.USER_NAME_COLUMN, name);
        user.saveEventually();
        ApplicationSettings.setUserName(name);
    }

}
