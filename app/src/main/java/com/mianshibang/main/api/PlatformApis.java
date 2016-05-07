package com.mianshibang.main.api;

import android.app.Activity;

import com.mianshibang.main.http.MParameter;
import com.mianshibang.main.http.MVolley;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.BannerType;
import com.mianshibang.main.model.dto.Banners;
import com.mianshibang.main.model.dto.BaseDTO;
import com.mianshibang.main.model.dto.CheckUpdate;
import com.mianshibang.main.utils.SystemUtils;
import com.mianshibang.main.utils.ToastUtils;
import com.mianshibang.main.widget.dialog.NewVersionDialog;

public class PlatformApis extends BaseApis {
	
	public static void submitFeedback(String content, String email, ResponseHandler<BaseDTO> handler) {
		MVolley.sendRequest(feedback, BaseDTO.class, handler, new MParameter("content", content), new MParameter("email", email));
	}
	
	public static void checkUpdate(final Activity activity, final boolean isSilent) {
		ResponseHandler<CheckUpdate> handler = new ResponseHandler<CheckUpdate>() {
			
			@Override
			public void onSuccess(CheckUpdate data) {
				onCheckUpdateSuccess(activity, data, isSilent);
			}
			
			@Override
			public boolean isShowError() {
				return !isSilent;
			}
		};
		MVolley.sendRequest(checkUpdate, CheckUpdate.class, handler, new MParameter("version", SystemUtils.getVersionCode()));
	}
	
	private static void onCheckUpdateSuccess(Activity activity, CheckUpdate data, boolean isSilent) {
		if (data.version == null) {
			if (!isSilent) {
				ToastUtils.show(data.msg);
			}
			return;
		}
		NewVersionDialog dialog = new NewVersionDialog(activity, data.version);
		dialog.show();
	}
	
	public static void requestBanner(BannerType type, ResponseHandler<Banners> handler) {
		MVolley.sendRequest(banner, Banners.class, handler, new MParameter("type", type.getType()));
	}
}
