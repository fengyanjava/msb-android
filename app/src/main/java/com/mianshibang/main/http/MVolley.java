package com.mianshibang.main.http;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.TreeSet;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mianshibang.main.MApplication;
import com.mianshibang.main.api.CurrentUserApis;
import com.mianshibang.main.model.dto.BaseDTO;
import com.mianshibang.main.utils.AppConfig;
import com.mianshibang.main.utils.MLog;
import com.mianshibang.main.utils.Md5;

public class MVolley implements AppConfig {
	
	private static final String API_URL = Debut_Mode ? Api_Url_Local : Api_Url_Online;

	private static RequestQueue requestQueue;

	public static void initVolley(Context context) {
		if (requestQueue == null) {
			requestQueue = Volley.newRequestQueue(context);
			requestQueue.start();
		}
	}
	
	public static <T extends BaseDTO> void sendRequest(
			String command, Class<T> cls, ResponseHandler<T> handler, MParameter... parameters) {
		
		check();
		
		String url = buildURL(command);
		Map<String, String> params = new IdentityHashMap<String, String>();
		
		for (MParameter param : parameters) {
			if (param.isValidated()) {
				params.put(param.key, param.value);
			}
		}
		
		if (CurrentUserApis.isLogin()) {
			params.put("token", CurrentUserApis.getToken());
		}
		
		if (GenericParameters.params != null) {
			params.putAll(GenericParameters.params);
		}
		
		params.put("sign", calculateSign(params));
		
		printRequestAddress(url, params);
		
		requestQueue.add(new GsonRequest<T>(url, params, cls, handler));
	}
	
	private static String calculateSign(Map<String, String> params) {
		TreeSet<String> set = new TreeSet<String>();
		set.add("as=" + Api_Secret);
		
		for (Map.Entry<String, String> entry : params.entrySet()) {
			String keyValue = entry.getKey() + "=" + entry.getValue();
			set.add(keyValue);
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (String keyValue : set) {
			sb.append(sb.length() > 0 ? "&" : "");
			sb.append(keyValue);
		}
		
		return Md5.md5(sb.toString());
	}
	
	private static String buildURL(String command) {
		return API_URL.concat(command);
	}
	
	private static void check() {
		if (requestQueue == null) {
			initVolley(MApplication.getApplication());
		}
	}
	
	private static void printRequestAddress(String url, Map<String, String> params) {
		StringBuilder sb = new StringBuilder(url);
		sb.append("?");
		if (params != null) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
		}
		
		MLog.i("http_request", sb.toString());
	}
	
}
