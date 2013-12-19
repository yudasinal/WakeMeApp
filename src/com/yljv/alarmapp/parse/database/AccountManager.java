package com.yljv.alarmapp.parse.database;

import java.util.List;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class AccountManager{
	
	
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
		getCurrentUser().put("partner", partner);
	}
	
	
	public static void login(final ParseLoginListener listener, String email, String password) {
		ParseUser.logInInBackground(email, password, new LogInCallback(){
			@Override
			public void done(ParseUser user, ParseException e){
				if(user!=null){
					listener.onLoginSuccessful();
				}else{
					Log.d("LoginException", e.getMessage());
					listener.onLoginFail(e);
				}
				ParseUser newUser = (ParseUser) user;
			}
		});
	}
	
	public static void register(final ParseRegisterListener listener, String email, String name, String surname, String password, boolean female){
		ParseUser user = new ParseUser();
		user.setUsername(email);
		user.setEmail(email);
		user.put("name", name);
		user.put("surname", surname);
		user.put("female", true);
		//user.put("partner", null);
		user.setPassword(password);
		user.signUpInBackground(new SignUpCallback(){
			@Override
			public void done(ParseException e){
				if(e==null){
					listener.onRegisterSuccess();
				}else{
					listener.onRegisterFail(e);
				}
			}
		});
	}
	
	
	public static ParseUser getCurrentUser(){
		return (ParseUser) ParseUser.getCurrentUser();
	}
	
	public static ParseUser getPartner(){
		return (ParseUser) ParseUser.getCurrentUser().get("partner");
	}
	
	public static String getName(){
		return (String) ParseUser.getCurrentUser().get("name");
	}
	
	public static String getSurname(){
		return (String) ParseUser.getCurrentUser().get("surname");
	}
	
	public static String getEmail(){
		return (String) ParseUser.getCurrentUser().getEmail();
	}
	
	public static void invitePartner(String email){
		
	}
	

	
}