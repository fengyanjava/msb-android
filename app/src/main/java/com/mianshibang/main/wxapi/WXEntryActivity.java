package com.mianshibang.main.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.mianshibang.main.R;
import com.mianshibang.main.api.ShareApis;
import com.mianshibang.main.utils.ToastUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ShareApis.getWeiChatApi().handleIntent(getIntent(), this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			ToastUtils.show(R.string.share_success);
			break;

		case BaseResp.ErrCode.ERR_USER_CANCEL:
			break;

		default:
			ToastUtils.show(R.string.share_failure);
			break;
		}

		finish();
	}

}
