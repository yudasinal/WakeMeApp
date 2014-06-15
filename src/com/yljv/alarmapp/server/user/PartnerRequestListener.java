package com.yljv.alarmapp.server.user;

public interface PartnerRequestListener {

	public void onNoPartnerFound();
	public void onAlreadyPartnered();
	public void onPartnerRequested();
	public void onOlderRequestAlreadySent();
	public void onPartnerNotAvailable();
}
