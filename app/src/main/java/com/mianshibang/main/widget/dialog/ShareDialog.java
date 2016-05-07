package com.mianshibang.main.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.mianshibang.main.R;
import com.mianshibang.main.api.ShareApis;
import com.mianshibang.main.model.MQuestion;

public class ShareDialog extends Dialog implements View.OnClickListener {
	
	private Context mContext;
	private MQuestion mQuestion;
	
	public ShareDialog(Context context, MQuestion question) {
		super(context, R.style.msb_dialog_share);

		mContext = context;
		mQuestion = question;
		
		init();
		setListeners();
	}
	
	private void init() {
        getWindow().setGravity(Gravity.BOTTOM);
        super.setContentView(R.layout.dialog_share);
        setCanceledOnTouchOutside(true);
    }
	
	private void setListeners() {
		View wechat = findViewById(R.id.share_to_wechat);
		View moments = findViewById(R.id.share_to_moments);
		View weibo = findViewById(R.id.share_to_weibo);
		View qq = findViewById(R.id.share_to_qq);
		View cancel = findViewById(R.id.share_cancel);
		
		wechat.setOnClickListener(this);
		moments.setOnClickListener(this);
		weibo.setOnClickListener(this);
		qq.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.share_to_wechat:
			ShareApis.shareToWechat((Activity) mContext, mQuestion, false);
			break;
			
		case R.id.share_to_moments:
			ShareApis.shareToWechat((Activity) mContext, mQuestion, true);
			break;
			
		case R.id.share_to_weibo:
			ShareApis.shareToWeibo((Activity) mContext, mQuestion);
			break;
			
		case R.id.share_to_qq:
			ShareApis.shareToQQ((Activity) mContext, mQuestion);
			break;
			
		case R.id.share_cancel:
			break;

		default:
			break;
		}
		
		dismiss();
	}

}
