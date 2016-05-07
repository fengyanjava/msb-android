package com.mianshibang.main.api;

import com.google.gson.Gson;
import com.mianshibang.main.model.MUser;
import com.mianshibang.main.storage.UserStorage;

public class CurrentUserApis {
	
	private static MUser sUser;
	
	static {
		String json = UserStorage.getCurrentUser();
		if (json != null) {
			sUser = new Gson().fromJson(json, MUser.class);
		}
	}
	
	public synchronized static MUser get() {
		return sUser;
	}
	
	public synchronized static boolean isLogin() {
		return get() != null;
	}
	
	public synchronized static boolean isUnLogin() {
		return !isLogin();
	}
	
	public synchronized static String getId() {
		if (isLogin()) {
			return sUser.id;
		}
		return null;
	}
	
	public synchronized static String getToken() {
		if (isLogin()) {
			return sUser.token;
		}
		return null;
	}

	public synchronized static void set(MUser user) {
		sUser = user;
		save();
	}
	
	public synchronized static void logout() {
		set(null);
	}
	
	private synchronized static void save() {
		if (sUser == null) {
			UserStorage.removeCurrentUser();
		} else {
			String json = new Gson().toJson(sUser);
			UserStorage.putCurrentUser(json);
		}
	}
}
