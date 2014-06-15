package com.yljv.alarmapp.server.user;

import java.util.List;
import java.util.Set;

import android.accounts.Account;
import android.provider.Telephony;
import android.test.ApplicationTestCase;

import com.yljv.alarmapp.client.helper.ApplicationSettings;
import com.yljv.alarmapp.client.helper.DBHelper;
import com.yljv.alarmapp.client.helper.PartnerReceiver;
import com.yljv.alarmapp.server.alarm.MyAlarmManager;

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

    private static void sendPartnerNotification(int category, String message){
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

            ParsePush pushy = new ParsePush();
            pushy.setChannel(channel);
            pushy.setMessage(message);
            pushy.sendInBackground();
        } catch (Exception pe) {
            pe.printStackTrace();
        }

    }

    public static void unlink(){


        ApplicationSettings.unlinkPartner();
    }
    public static void acceptPartnerRequest() {
        sendPartnerNotification(PartnerReceiver.PARTNER_ACCEPT_REQUEST, "Your partner accepted your request!");
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


                    sendPartnerNotification(PartnerReceiver.PARTNER_REQUEST, "Someone wants to be your buddy");
                    listener.onPartnerRequested();

                }
            } catch (ParseException e) {

            }
        }
    }

    public static void cancelPartnerRequest() {

        sendPartnerNotification(PartnerReceiver.PARTNER_CANCEL_REQUEST, "The request was cancelled");

        ApplicationSettings.cancelRequest();
    }

    public static void declinePartnerRequest(){

        sendPartnerNotification(PartnerReceiver.PARTNER_DECLINE_REQUEST, "Your request has been cancelled");

        ApplicationSettings.unlinkPartner();
    }

    public static void incomingPartnerRequest(String email){
        ApplicationSettings.incomingRequest(email);
    }
}
