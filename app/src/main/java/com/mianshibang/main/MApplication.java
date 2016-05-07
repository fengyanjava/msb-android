package com.mianshibang.main;

import android.app.Application;

import com.mianshibang.main.api.AreaApis;
import com.mianshibang.main.api.ShareApis;
import com.mianshibang.main.http.MVolley;

public class MApplication extends Application {
	
	private static MApplication application;

	@Override
	public void onCreate() {
		super.onCreate();
		
		application = this;
		
		MVolley.initVolley(this);
		AreaApis.loadAreas();
		ShareApis.initWechatShare();
	}
	
	public static MApplication getApplication() {
		return application;
	}
}
