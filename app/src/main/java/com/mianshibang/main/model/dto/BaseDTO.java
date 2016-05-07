package com.mianshibang.main.model.dto;

public class BaseDTO {

	public int code;
	public String msg;
	public String cmd;
	
	public boolean isSucceeded() {
		return code == 0;
	}
}
