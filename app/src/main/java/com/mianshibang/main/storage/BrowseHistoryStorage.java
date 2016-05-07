package com.mianshibang.main.storage;

public class BrowseHistoryStorage extends PrefsManager {

	public synchronized static String getBrowseHistory() {
		return PrefsManager.get(Key.BrowseHistory, null);
	}

	public synchronized static void putBrowseHistory(String json) {
		PrefsManager.put(Key.BrowseHistory, json);
	}

	public synchronized static void clearBrowseHistory() {
		PrefsManager.remove(Key.BrowseHistory);
	}

}
