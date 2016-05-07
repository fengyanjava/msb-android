package com.mianshibang.main.model;

public class MUserDetail extends MData {
	public int userId;
	public String birthday;
	public String intro;
	public String province;
	public String city;
	public String weibo;
	public String blog;
	public String qq;
	
	public String getLocation() {
		String location = (province != null ? province : "") + " " + (city != null ? city : "");
		return location.trim();
	}
}
