package com.yljv.alarmapp;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class User extends ParseObject {
	
	public void User(String email, String password, boolean female){
		this.put("partner", null);
		this.put("email", email);
		this.put("password", password);
		this.put("female", female);
		this.saveEventually(new SaveCallback(){

			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
			}
			
		});
		
	}
	
	public void changePassword(String password){
		this.put("password", password);
		this.saveEventually(new SaveCallback(){
			@Override
			public void done(ParseException e){
				//TODO
			}
		});
	}
	
	public void addPartner(User partner){
		this.put("partner", partner);
	}
	
	public void deletePartner(){
		this.put("partner", null);
	}
}
