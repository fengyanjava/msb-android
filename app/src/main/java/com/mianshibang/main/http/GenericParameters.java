package com.mianshibang.main.http;

import java.util.HashMap;

import com.mianshibang.main.utils.AppConfig;
import com.mianshibang.main.utils.SystemUtils;

public class GenericParameters {

	public static HashMap<String, String> params;
	
	static {
		params = new HashMap<String, String>();
		
		params.put("g_device", "android");
		params.put("g_api_version", AppConfig.Api_Version);
		params.put("g_device_id", SystemUtils.getDeviceId());
		
	}
}
