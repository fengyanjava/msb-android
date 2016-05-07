package com.mianshibang.main.model;

public class MUser extends MData {

	public String email;
	public String avatar;
	public String nickname;
	public String gender;
	public String perfession;
	public String lastLoginTime;
	public String dateline;
	public String token;
	public int experience;
	public int score;
	
	public transient String password;
	
	public MUserDetail detail;
}
