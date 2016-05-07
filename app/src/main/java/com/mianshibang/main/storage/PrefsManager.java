package com.mianshibang.main.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.mianshibang.main.MApplication;

public class PrefsManager {
	
	private static final String PrefName = "msb_app";
	
	protected interface Key {
		String Classify = "classify";
		
		String CurrentUser = "current_user";
		
		String BrowseHistory = "browse_history";
		String SearchHistory = "search_history";
		
		String ShowAnswer = "config_show_answer";
		String ReceivePush = "config_receive_push";
		String FontSize = "config_font_size";
		
		String Cookie = "cookie";
	}
	
	protected synchronized static boolean get(String key, boolean defValue) {
		SharedPreferences prefs = getPrefs();
		return prefs.getBoolean(key, defValue);
	}
	
	protected synchronized static void put(String key, boolean value) {
		SharedPreferences prefs = getPrefs();
		prefs.edit().putBoolean(key, value).commit();
	}
	
	protected synchronized static String get(String key, String defValue) {
		SharedPreferences prefs = getPrefs();
		return prefs.getString(key, defValue);
	}
	
	protected synchronized static void put(String key, String value) {
		SharedPreferences prefs = getPrefs();
		prefs.edit().putString(key, value).commit();
	}
	
	protected synchronized static int get(String key, int defValue) {
		SharedPreferences prefs = getPrefs();
		return prefs.getInt(key, defValue);
	}
	
	protected synchronized static void put(String key, int value) {
		SharedPreferences prefs = getPrefs();
		prefs.edit().putInt(key, value).commit();
	}
	
	protected synchronized static void remove(String key) {
		getPrefs().edit().remove(key).commit();
	}
	
	protected static SharedPreferences getPrefs() {
		return MApplication.getApplication().getSharedPreferences(PrefName, Context.MODE_PRIVATE);
	}
}
