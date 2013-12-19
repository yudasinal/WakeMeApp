package com.yljv.alarmapp.parse.database;

import java.util.List;

public interface ParsePartnerAlarmListener {
	
	public void partnerAlarmsFound(List<Alarm> alarms);
	public void partnerAlarmsSearchFailed(Exception e);
}
