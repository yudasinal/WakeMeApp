package com.yljv.alarmapp.parse.database;

import com.parse.ParseUser;

public class User extends ParseUser {
	
	public User(String email, String name, String surname, String password, boolean female){
		super.setEmail(email);
		super.setUsername(email);
		super.setPassword(password);
		this.put("name", name);
		this.put("surname", surname);
		this.put("partner", null);
		this.put("female", female);		
	}
	
	public void changePassword(String password){
		super.setPassword(password);
	}
	
	public void addPartner(User partner){
		this.put("partner", partner);
	}
	
	public void deletePartner(){
		this.put("partner", null);
	}
	
	public User getPartner(){
		return (User) this.get("partner");
	}
}
