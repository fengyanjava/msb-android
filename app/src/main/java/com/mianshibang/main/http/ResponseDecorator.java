package com.mianshibang.main.http;

import com.mianshibang.main.model.dto.BaseDTO;

public class ResponseDecorator<T extends BaseDTO> extends ResponseHandler<T> {

	private ResponseHandler<T> mResponseHandler;

	public ResponseDecorator(ResponseHandler<T> mResponseHandler) {
		this.mResponseHandler = mResponseHandler;
	}
	
	@Override
	public void onSuccess(T data) {
		if (mResponseHandler != null) {
			mResponseHandler.onSuccess(data);
		}
	}
	
	@Override
	public void onDataParsed(T data) {
		if (mResponseHandler != null) {
			mResponseHandler.onDataParsed(data);
		}
	}
	
	@Override
	public void onFinish() {
		if (mResponseHandler != null) {
			mResponseHandler.onFinish();
		}
	}
	
	@Override
	public void onFailure(Exception e) {
		if (mResponseHandler != null) {
			mResponseHandler.onFailure(e);
		}
	}
}
