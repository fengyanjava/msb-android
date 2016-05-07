package com.mianshibang.main.storage;


public class ClassifyStorage extends PrefsManager {

	public synchronized static void putClassify(String json) {
		put(Key.Classify, json);
	}
	
	public synchronized static String getClassify() {
		return get(Key.Classify, null);
	}
}
