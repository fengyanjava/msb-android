package com.mianshibang.main.model;

public class SearchHistory extends MData {

	public String keyword;
	
	public SearchHistory() {
		this(null);
	}

	public SearchHistory(String keyword) {
		this.keyword = keyword;
	}
	
}
