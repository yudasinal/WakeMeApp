package com.yljv.alarmapp.parse.database;

public interface PartnerRequestListener {

	public void onNoPartnerFound();
	public void onAlreadyPartnered();
	public void onPartnerRequested();
	public void onOlderRequestAlreadySent();
	public void onPartnerNotAvailable();
}
