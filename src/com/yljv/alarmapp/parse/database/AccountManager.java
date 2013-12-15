package com.yljv.alarmapp.parse.database;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.yljv.alarmapp.parse.database.ParseLoginListener;
import com.yljv.alarmapp.parse.database.ParseRegisterListener;
import com.yljv.alarmapp.parse.database.User;

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
	
	/*
	public static void registerPartner(final ParseRegisterListener listener, String email){
		User user = new User(email);
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
	*/
	
	
	public static User getCurrentUser(){
		return (User) ParseUser.getCurrentUser();
	}

	
}