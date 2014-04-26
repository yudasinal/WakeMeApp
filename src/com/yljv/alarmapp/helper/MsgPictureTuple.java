package com.yljv.alarmapp.helper;

public class MsgPictureTuple {

	private byte[] picData;
	private String msg;

	public MsgPictureTuple(String msg, byte[] data) {
		picData = data;
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public byte[] getPicData() {
		return picData;
	}
}