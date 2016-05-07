package com.mianshibang.main.model;

import java.util.ArrayList;

public class MFavoriteFolder extends MData {

	public String name;
	public String intro;
	public int isPublic;
	public int userId;
	public long dateline;

	public long questionCount;
	public ArrayList<MQuestion> questions;
	
	public transient boolean isDeleted;

}
