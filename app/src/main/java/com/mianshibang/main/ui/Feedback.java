package com.mianshibang.main.ui;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.mianshibang.main.R;
import com.mianshibang.main.api.PlatformApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.dto.BaseDTO;
import com.mianshibang.main.utils.ToastUtils;
import com.mianshibang.main.widget.TitleBar;

public class Feedback extends Base implements OnClickListener {
	
	private TitleBar mTitleBar;
	
	private EditText mContent;
	private EditText mEmail;

	@Override
	public int getContentLayout() {
		return R.layout.feedback;
	}

	@Override
	public void findViews() {
		mTitleBar = (TitleBar) findViewById(R.id.title_bar);
		
		mEmail = (EditText) findViewById(R.id.email);
		mContent = (EditText) findViewById(R.id.content);
	}

	@Override
	public void setListeners() {

	}

	@Override
	public void process() {
		setTitleBarStyle();
	}
	
	private void setTitleBarStyle() {
		mTitleBar.setTitleText(R.string.feedback_title);
		mTitleBar.setRightButtonIcon(R.drawable.icon_title_bar_send);
		mTitleBar.setRightButtonClick(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case TitleBar.RIGHT_BUTTON_ID:
			onSubmitFeedback();
			break;

		default:
			break;
		}
	}
	
	private void onSubmitFeedback() {
		String content = mContent.getText().toString().trim();
		String email = mEmail.getText().toString().trim();
		
		if (TextUtils.isEmpty(content)) {
			ToastUtils.show("反馈内容不能为空");
			return;
		}
		
		showProgress();
		
		PlatformApis.submitFeedback(content, email, new ResponseHandler<BaseDTO>() {
			@Override
			public void onSuccess(BaseDTO data) {
				if (data.isSucceeded()) {
					ToastUtils.show(data.msg);
					finish();
				}
			}
			
			@Override
			public void onFinish() {
				dismissProgress();
			}
		});
	}

}
