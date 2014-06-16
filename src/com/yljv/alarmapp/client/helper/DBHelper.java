package com.yljv.alarmapp.client.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yljv.alarmapp.server.alarm.Alarm;
import com.yljv.alarmapp.server.alarm.AlarmInstance;

public class DBHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "Alarms.db";

	public static final String COMMA = ", ";
	public static final String TEXT_TYPE = " TEXT";
	public static final String INT_TYPE = " INTEGER";

	public static final String SQL_CREATE_ALARM_ENTRIES = "CREATE TABLE "
			+ Alarm.TABLE_NAME + " (" + Alarm.COLUMN_ID
			+ " INTEGER PRIMARY KEY" + COMMA + Alarm.COLUMN_NAME + TEXT_TYPE
			+ COMMA + Alarm.COLUMN_TIME + INT_TYPE + COMMA
			+ Alarm.COLUMN_ACTIVATED + INT_TYPE + COMMA
			+ Alarm.COLUMN_USER + TEXT_TYPE + COMMA
			+ Alarm.COLUMN_VISIBILITY + INT_TYPE + COMMA
			+ Alarm.COLUMN_OBJECT_ID + TEXT_TYPE + COMMA
			+ Alarm.COLUMN_MUSIC_URI + TEXT_TYPE + COMMA + Alarm.COLUMN_VOLUME
			+ INT_TYPE + COMMA + Alarm.COLUMN_MSG + TEXT_TYPE + COMMA
			+ Alarm.COLUMN_PICTURE + TEXT_TYPE + COMMA + Alarm.COLUMN_WEEKDAYS
			+ TEXT_TYPE + " );";

	public static final String SQL_CREATE_PARTNER_ALARM_ENTRIES = "CREATE TABLE "
			+ AlarmInstance.PARTNER_TABLE_NAME
			+ " ("
			+ AlarmInstance.COLUMN_ID
			+ " INTEGER PRIMARY KEY"
			+ COMMA
			+ AlarmInstance.COLUMN_OBJECT_ID + TEXT_TYPE + COMMA
			+ AlarmInstance.COLUMN_NAME
			+ TEXT_TYPE
			+ COMMA
			+ AlarmInstance.COLUMN_TIME
			+ INT_TYPE
			+ COMMA
			+ AlarmInstance.COLUMN_MSG
			+ TEXT_TYPE
			+ COMMA
			+ AlarmInstance.COLUMN_VISIBLE 
			+ INT_TYPE
			+ COMMA
			+ AlarmInstance.COLUMN_PICTURE + TEXT_TYPE + " );";
	
	public static final String SQL_CREATE_MY_ALARM_INSTANCES = "CREATE TABLE "
			+ AlarmInstance.MY_ALARMINSTANCE_TABLE_NAME
			+ " ("
			+ AlarmInstance.COLUMN_ID
			+ " INTEGER PRIMARY KEY"
			+ COMMA
			+ AlarmInstance.COLUMN_NAME
			+ TEXT_TYPE
			+ COMMA
			+ AlarmInstance.COLUMN_TIME
			+ INT_TYPE
			+ COMMA
			+ AlarmInstance.COLUMN_MSG
			+ TEXT_TYPE
			+ COMMA
			+ AlarmInstance.COLUMN_MUSIC
			+ TEXT_TYPE
			+ COMMA
			+ AlarmInstance.COLUMN_VISIBLE
			+ INT_TYPE
			+ COMMA
			+ AlarmInstance.COLUMN_OBJECT_ID + TEXT_TYPE + COMMA 
			+ AlarmInstance.COLUMN_PICTURE + TEXT_TYPE + " );";
	

	public static final String SQL_DELETE_ALARM_ENTRIES = "DROP TABLE IF EXISTS "
			+ Alarm.TABLE_NAME;
	
	public static final String SQL_DELETE_PARTNER_ALARM_ENTRIES = "DROP TABLE IF EXISTS "
			+ AlarmInstance.PARTNER_TABLE_NAME;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ALARM_ENTRIES);
		db.execSQL(SQL_CREATE_PARTNER_ALARM_ENTRIES);
		db.execSQL(SQL_CREATE_MY_ALARM_INSTANCES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}
