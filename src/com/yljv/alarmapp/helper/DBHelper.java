package com.yljv.alarmapp.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yljv.alarmapp.parse.database.Alarm.AlarmEntry;
import com.yljv.alarmapp.parse.database.Alarm.PartnerAlarmEntry;

public class DBHelper extends SQLiteOpenHelper{

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Alarms.db";

	public static final String COMMA = ", ";
	public static final String TEXT_TYPE = " TEXT";
	public static final String INT_TYPE = " INTEGER";
	
	
	
	public static final String SQL_CREATE_ALARM_ENTRIES = 
			"CREATE TABLE " + AlarmEntry.TABLE_NAME + " (" + 
			AlarmEntry.COLUMN_ID + " INTEGER PRIMARY KEY" + COMMA +
			AlarmEntry.COLUMN_NAME + TEXT_TYPE + COMMA + 
			AlarmEntry.COLUMN_TIME + INT_TYPE + COMMA + 
			AlarmEntry.COLUMN_ACTIVATED + INT_TYPE + COMMA +
			AlarmEntry.COLUMN_VISIBILITY + INT_TYPE + COMMA + 
			AlarmEntry.COLUMN_MUSIC_URI + TEXT_TYPE + COMMA + 
			AlarmEntry.COLUMN_VOLUME + INT_TYPE  + COMMA + 
			AlarmEntry.COLUMN_MSG + TEXT_TYPE + COMMA + 
			AlarmEntry.COLUMN_PICTURE + TEXT_TYPE + COMMA + 
			AlarmEntry.COLUMN_WEEKDAYS + TEXT_TYPE + 
			" );";
	
	public static final String SQL_CREATE_PARTNER_ALARM_ENTRIES = 
			"CREATE TABLE " + PartnerAlarmEntry.TABLE_NAME + " (" + 
			PartnerAlarmEntry.COLUMN_ID + " INTEGER PRIMARY KEY" + COMMA +
			PartnerAlarmEntry.COLUMN_NAME + TEXT_TYPE + COMMA + 
			PartnerAlarmEntry.COLUMN_TIME + INT_TYPE + COMMA + 
			PartnerAlarmEntry.COLUMN_MSG + TEXT_TYPE + COMMA + 
			PartnerAlarmEntry.COLUMN_PICTURE + TEXT_TYPE + 
			" );";
	
	
	public static final String SQL_DELETE_ALARM_ENTRIES = 
			"DROP TABLE IF EXISTS " + AlarmEntry.TABLE_NAME;
	public static final String SQL_DELETE_PARTNER_ALARM_ENTRIES = 
			"DROP TABLE IF EXISTS " + AlarmEntry.TABLE_NAME;
	
	public DBHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ALARM_ENTRIES);
		db.execSQL(SQL_CREATE_PARTNER_ALARM_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ALARM_ENTRIES);
		db.execSQL(SQL_DELETE_PARTNER_ALARM_ENTRIES);
		onCreate(db);
	}
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
		onUpgrade(db, oldVersion, newVersion);
	}
}
