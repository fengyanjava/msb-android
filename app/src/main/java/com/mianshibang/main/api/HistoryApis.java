package com.mianshibang.main.api;

import java.util.ArrayList;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mianshibang.main.storage.BrowseHistoryStorage;
import com.mianshibang.main.storage.SearchHistoryStorage;
import com.mianshibang.main.utils.AppConfig;

public class HistoryApis extends BaseApis {
	
	public static void addBrowseHistory(String qid) {
		add(HistoryType.Browse, qid);
	}
	
	public static ArrayList<String> getBrowseHistory() {
		return getHistory(HistoryType.Browse);
	}
	
	public static void clearBrowseHistory() {
		BrowseHistoryStorage.clearBrowseHistory();
	}
	
	public static void addSearchHistory(String keyword) {
		add(HistoryType.Search, keyword);
	}
	
	public static ArrayList<String> getSearchHistory() {
		return getHistory(HistoryType.Search);
	}
	
	public static void clearSearchHistory() {
		SearchHistoryStorage.clearSearchHistory();
	}
	
	private static void add(HistoryType type, String history) {
		String json = type.getHistory();
		LinkedList<String> histories = null;
		if (json == null) {
			histories = new LinkedList<String>();
		} else {
			histories = new Gson().fromJson(json, new TypeToken<LinkedList<String>>(){}.getType());
		}
		histories.remove(history);
		histories.addFirst(history);
		
		int max = type.getMax();
		while (histories.size() > max) {
			histories.removeLast();
		}
		
		json = new Gson().toJson(histories);
		type.saveHistory(json);
	}
	
	private static ArrayList<String> getHistory(HistoryType type) {
		String json = type.getHistory();
		if (json == null) {
			return new ArrayList<String>();
		}
		return new Gson().fromJson(json, new TypeToken<ArrayList<String>>(){}.getType());
	}
	
	private static enum HistoryType {
		Browse, Search;
		
		public String getHistory() {
			if (this == Browse) {
				return BrowseHistoryStorage.getBrowseHistory();
			}
			if (this == Search) {
				return SearchHistoryStorage.getSearchHistory();
			}
			return null;
		}
		
		public void saveHistory(String json) {
			if (this == Browse) {
				BrowseHistoryStorage.putBrowseHistory(json);
			}
			if (this == Search) {
				SearchHistoryStorage.putSearchHistory(json);
			}
		}
		
		public int getMax() {
			if (this == Browse) {
				return AppConfig.Browse_History_Max;
			}
			if (this == Search) {
				return AppConfig.Search_History_Max;
			}
			return 0;
		}
	}
}
