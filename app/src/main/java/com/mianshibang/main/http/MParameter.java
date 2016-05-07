package com.mianshibang.main.http;

public class MParameter {

	public String key;
	public String value;
	
	public MParameter(String key, String value) {
		this.key = new String(key);
		this.value = value;
	}
	
	public MParameter(String key, Object value) {
		this(key, String.valueOf(value));
	}
	
	public boolean isValidated() {
		return key != null && value != null;
	}
	
}
