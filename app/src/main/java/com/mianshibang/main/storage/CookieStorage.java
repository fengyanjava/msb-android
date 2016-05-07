package com.mianshibang.main.storage;

public class CookieStorage extends PrefsManager {

	public synchronized static void putCookie(String cookie) {
		put(Key.Cookie, cookie);
	}
	
	public synchronized static String getCookie() {
		return get(Key.Cookie, null);
	}
}
