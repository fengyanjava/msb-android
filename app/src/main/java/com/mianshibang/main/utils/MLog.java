package com.mianshibang.main.utils;

import android.util.Log;

public class MLog {
	
	private static final String TAG = "MLog";

	public static void i(String tag, String msg) {
		if (AppConfig.Debut_Mode) {
			Log.i(tag, msg);
		}
	}
	
	public static void i(String msg) {
		i(TAG, msg);
	}
	
	public static void e(String msg) {
		Log.e(TAG, msg);
	}
}
