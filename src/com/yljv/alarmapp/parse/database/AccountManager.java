package com.yljv.alarmapp.parse.database;

import java.util.List;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class AccountManager{
	
	public static void login(final ParseLoginListener listener, String email, String password) {
		ParseUser.logInInBackground(email, password, new LogInCallback(){
			@Override
			public void done(ParseUser user, ParseException e){
				if(user!=null){
					listener.onLoginSuccessful();
				}else{
					listener.onLoginFail(e);
				}
				User newUser = (User) user;
			}
		});
	}
	
	public static void register(final ParseRegisterListener listener, String email, String name, String surname, String password, boolean female){
		User user = new User(email, name, surname, password, female);
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
		return ParseUser.getCurrentUser();
	}
	
	
	/*public static boolean setPartner(final ParsePartnerListener listener, String email){
		//find user;
		final ParseUser partner;
		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.whereEqualTo("email", email);
		query.findInBackground(new FindCallback<ParseUser>() {
		  public void done(List<ParseUser> objects, ParseException e) {
		    if (e == null) {
		        // The query was successful.
		    	if(objects==null){
		    		listener.onPartnerNotExisting();
		    	}else{
		    		partner = objects.get(0);
		    		listener.onPartnerAddedSuccessfully();

		    		getCurrentUser().put("partner", partner);
		    	}
		    } else {
		        // Something went wrong.
		    	listener.onPartnerAddedFail(e);
		    }
		  }
		});
	}*/
	public static ParseUser getPartner(){
		return (ParseUser) getCurrentUser().get("partner");
	}
	
	public static void deletePartner(){
		
	}
	
}