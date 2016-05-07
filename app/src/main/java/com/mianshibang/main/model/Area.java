package com.mianshibang.main.model;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Area {

	@SerializedName("cn")
	public String name;
	
	@SerializedName("sub")
	public ArrayList<Area> cities;
}
