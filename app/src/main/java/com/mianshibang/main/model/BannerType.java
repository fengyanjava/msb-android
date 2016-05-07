package com.mianshibang.main.model;

public enum BannerType {

	Feed(1), QuestionList(2);
	
	private int type;
	
	private BannerType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public static BannerType getDefault() {
		return Feed;
	}
	
}
