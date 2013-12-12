package com.yljv.alarmapp.parse.database;

public interface ParsePartnerListener {

	public void onPartnerAddedSuccessfully();
	public void onPartnerNotExisting();
	public void onPartnerAddedFail(Exception e);
}
