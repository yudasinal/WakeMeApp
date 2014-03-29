package com.yljv.alarmapp.helper;

import java.util.List;
import java.util.Set;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SignUpCallback;
import com.yljv.alarmapp.MenuMainActivity;
import com.yljv.alarmapp.parse.database.MyAlarmManager;
import com.yljv.alarmapp.parse.database.ParseLoginListener;
import com.yljv.alarmapp.parse.database.ParsePartnerListener;
import com.yljv.alarmapp.parse.database.ParseRegisterListener;

public class AccountManager{
	
	 
	public static boolean hasPartner(){
		return ParseUser.getCurrentUser().getString(User.PARTNER_COLUMN) != null;
	}
	public static void findPartner(final ParsePartnerListener listener, String email){
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("username", email);
		query.findInBackground(new FindCallback<ParseUser>() {
		  public void done(List<ParseUser> objects, ParseException e) {
		    if (e == null) {
		        // The query was successful.
		    	if(objects.size()==1){
		    		//One User with the matching email was found
		    		setPartner(objects.get(0));
		    		listener.onPartnerFound();
		    	}else{
		    		//no user with matching email was found
		    		listener.onPartnerNotExisting();
		    	}
		    } else {
		    	//Query wasn't successful
		        listener.onPartnerQueryError(e);
		    }
		  }
		});
	}
	
	
	private static void setPartner(ParseUser partner){
		ParseUser.getCurrentUser().put("partner", partner);
		ParseUser.getCurrentUser().saveInBackground();
	}
	
	public static String getSendingChannel(){
		String email = ParseUser.getCurrentUser().getEmail().replace('.', '_');
		email = email.replace('@', '_');
		return "user_" + email;
	}
	
	public static String getSubscribedChannel(){
		String email = ParseUser.getCurrentUser().getString(User.PARTNER_COLUMN).replace('.', '_');
		email = email.replace('@', '_');
		return "user_" + email;
	}
	
	
	
	public static void login(final ParseLoginListener listener, String email, String password) {
		ParseUser.logInInBackground(email, password, new LogInCallback(){
			@Override
			public void done(ParseUser user, ParseException e){
				if(user!=null){
					ApplicationSettings.setUserEmail(user.getEmail());
					ApplicationSettings.setUserName(user.getUsername());
					
					//TODO
					if(user.getString(User.PARTNER_COLUMN)!=null){
						ApplicationSettings.setPartnerEmail(user.getString(User.PARTNER_COLUMN));
					}
					ApplicationSettings.setAlarmId(user.getInt(User.ID_COLUMN));
					
					ApplicationSettings.setUserEmail(user.getUsername());
					listener.onLoginSuccessful();
				}else{
					Log.d("LoginException", e.getMessage());
					listener.onLoginFail(e);
				}
				ParseUser newUser = (ParseUser) user;
			}
		});
	}
	
	public static void register(final ParseRegisterListener listener, final String email, final String password){
		ParseUser user = new ParseUser();
		user.setUsername(email);
		user.setEmail(email);
		user.put("female", true);
		user.setPassword(password);
		user.signUpInBackground(new SignUpCallback(){
			@Override
			public void done(ParseException e){
				if(e==null){
					ParseUser user = ParseUser.getCurrentUser();
					ApplicationSettings.setUserEmail(user.getEmail());
					ApplicationSettings.setUserName(user.getUsername());
					if(user.getString(User.PARTNER_COLUMN)!=null){
						ApplicationSettings.setPartnerEmail(user.getString(User.PARTNER_COLUMN));
					}
					if(user.get(User.ID_COLUMN)!=null){
						ApplicationSettings.setAlarmId(user.getInt(User.ID_COLUMN));
					}else{
						ApplicationSettings.setAlarmId(0);
					}
					ApplicationSettings.setUserEmail(user.getUsername());
					listener.onRegisterSuccess();
				}else{
					listener.onRegisterFail(e);
				}
			}
		});
	}
	
	
	/*public static ParseUser getPartner(){
		return (ParseUser) ParseUser.getCurrentUser().get("partner");
	}*/

	
	public static String getEmail(){
		return ApplicationSettings.getUserEmail();
	}
	
	public static void invitePartner(String email){
		
	}
	
	public class User{
		public final static String NAME_COLUMN = "name";
		public final static String PARTNER_COLUMN = "partner";
		public final static String ID_COLUMN = "id";
		public final static String PARTNER_ID_COLUMN = "partner_id";
	}
	
	public static void logout(Context context){
		MyAlarmManager.removeDataBase();
		ParseUser.logOut();
		Set<String> channels = PushService.getSubscriptions(context);
		for(String channel : channels){
			PushService.unsubscribe(context, channel);
		}
		ApplicationSettings.reset();
	    DBHelper dbHelper = MyAlarmManager.getDBHelper();
	    if (dbHelper != null) {
	        dbHelper.close();
	    }
	}
}


