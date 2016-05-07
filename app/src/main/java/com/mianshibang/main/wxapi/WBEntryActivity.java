package com.mianshibang.main.wxapi;

import com.mianshibang.main.R;
import com.mianshibang.main.api.ShareApis;
import com.mianshibang.main.utils.ToastUtils;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.constant.WBConstants;

import android.app.Activity;
import android.os.Bundle;

public class WBEntryActivity extends Activity implements Response {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ShareApis.getWeiboApi().handleWeiboResponse(getIntent(), this);
	}

	@Override
	public void onResponse(BaseResponse response) {
		switch (response.errCode) {
		case WBConstants.ErrorCode.ERR_OK:
			ToastUtils.show(R.string.share_success);
			break;

		case WBConstants.ErrorCode.ERR_FAIL:
			ToastUtils.show(R.string.share_failure);
			break;
			
		case WBConstants.ErrorCode.ERR_CANCEL:
			break;
		}
		finish();
	}
}
