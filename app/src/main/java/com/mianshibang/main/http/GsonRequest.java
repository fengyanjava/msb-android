package com.mianshibang.main.http;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.mianshibang.main.MApplication;
import com.mianshibang.main.api.CurrentUserApis;
import com.mianshibang.main.model.dto.BaseDTO;
import com.mianshibang.main.storage.CookieStorage;
import com.mianshibang.main.utils.MLog;
import com.mianshibang.main.utils.UiNavigation;

public class GsonRequest<T extends BaseDTO> extends Request<T> {

	private Class<T> cls;
	private Map<String, String> mParams;
	private Listener<T> mListener;

	public GsonRequest(String url, Map<String, String> params, Class<T> cls, ResponseHandler<T> handler) {
		super(Method.POST, url, new ErrorListenerEx<T>(handler));
		this.cls = cls;
		this.mParams = params;
		mListener = new ListenEx<T>(handler);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			if (response.headers != null) {
				String cookie = response.headers.get("Set-Cookie");
				if (!TextUtils.isEmpty(cookie)) {
					CookieStorage.putCookie(cookie);
				}
			}
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

			MLog.i("http_data", jsonString);
			T data = new Gson().fromJson(jsonString, cls);

			if (data != null && data.code < 0) { // 须重新登录
				CurrentUserApis.logout();
				UiNavigation.startMain(MApplication.getApplication());
			}

			return Response.success(data, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonParseException je) {
			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mParams;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		String cookie = CookieStorage.getCookie();
		if (cookie == null) {
			return super.getHeaders();
		}
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Cookie", cookie);
		return headers;
	}

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}

	private static class ListenEx<T extends BaseDTO> implements Listener<T> {

		private ResponseHandler<T> mHandler;

		public ListenEx(ResponseHandler<T> handler) {
			this.mHandler = handler;
		}

		@Override
		public void onResponse(T response) {
			if (mHandler != null) {
				mHandler.onFinish();
				mHandler.onDataParsed(response);
				mHandler.onRequestComplete(response);
			}
		}

	}

	private static class ErrorListenerEx<T extends BaseDTO> implements ErrorListener {

		private ResponseHandler<T> mHandler;

		public ErrorListenerEx(ResponseHandler<T> handler) {
			this.mHandler = handler;
		}

		@Override
		public void onErrorResponse(VolleyError error) {
			if (mHandler != null) {
				mHandler.onFinish();
				mHandler.onRequestFailure(error);
			}
		}

	}

}
