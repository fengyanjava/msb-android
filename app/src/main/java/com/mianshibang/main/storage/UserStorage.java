package com.mianshibang.main.storage;

public class UserStorage extends PrefsManager {

	public synchronized static String getCurrentUser() {
		return PrefsManager.get(Key.CurrentUser, null);
	}

	public synchronized static void putCurrentUser(String json) {
		PrefsManager.put(Key.CurrentUser, json);
	}

	public synchronized static void removeCurrentUser() {
		PrefsManager.remove(Key.CurrentUser);
	}

}
