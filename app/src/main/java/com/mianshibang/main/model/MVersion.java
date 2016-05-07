package com.mianshibang.main.model;

public class MVersion extends MData {

	public String name;
	public String description;
	public String path;
	public int version;
	public long size;
	public String sizeString;
	public int isForce;
	
	public boolean isForce() {
		return isForce != 0;
	}
}
