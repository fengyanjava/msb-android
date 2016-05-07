package com.mianshibang.main.http;

import com.android.volley.NetworkError;
import com.mianshibang.main.R;
import com.mianshibang.main.model.dto.BaseDTO;
import com.mianshibang.main.utils.ToastUtils;

public class ResponseHandler<T extends BaseDTO> {
	
	public final void onRequestComplete(T data) {
		if (!data.isSucceeded() && isShowError()) {
			ToastUtils.show(data.msg);
		}
		onSuccess(data);
	}
	
	public final void onRequestFailure(Exception e) {
		if (isShowError() && e != null) {
			showErrorToast(e);
		}
		onFailure(e);
	}
	
	private void showErrorToast(Exception e) {
		if (e instanceof NetworkError) {
			ToastUtils.show(R.string.network_error);
		} else {
			ToastUtils.show(e.getMessage());
		}
	}
	
	public boolean isShowError() {
		return true;
	}
	
	public void onDataParsed(T data) {
		
	}

	
	public void onFailure(Exception e) {
		
	}
	
	public void onFinish() {
		
	}
	
	public void onSuccess(T data) {
		
	}
	
}
