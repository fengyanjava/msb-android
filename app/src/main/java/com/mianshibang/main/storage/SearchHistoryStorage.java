package com.mianshibang.main.storage;

public class SearchHistoryStorage extends PrefsManager {

	public synchronized static String getSearchHistory() {
		return PrefsManager.get(Key.SearchHistory, null);
	}

	public synchronized static void putSearchHistory(String json) {
		PrefsManager.put(Key.SearchHistory, json);
	}

	public synchronized static void clearSearchHistory() {
		PrefsManager.remove(Key.SearchHistory);
	}

}
