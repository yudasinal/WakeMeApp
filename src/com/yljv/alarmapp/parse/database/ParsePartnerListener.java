package com.yljv.alarmapp.parse.database;

public interface ParsePartnerListener {

	public void onPartnerFound();
	public void onPartnerNotExisting();
	public void onPartnerQueryError(Exception e);
}
