package com.mianshibang.main.ui;

import java.util.List;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.api.ClassifyApis;
import com.mianshibang.main.api.CurrentUserApis;
import com.mianshibang.main.api.PostApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.MClassify;
import com.mianshibang.main.model.dto.BaseDTO;
import com.mianshibang.main.utils.EventBusUtils;
import com.mianshibang.main.utils.SystemUtils;
import com.mianshibang.main.utils.ToastUtils;
import com.mianshibang.main.utils.UiNavigation;
import com.mianshibang.main.widget.TitleBar;
import com.mianshibang.main.widget.dialog.AlertDialog;

public class Post extends Base implements OnClickListener {

	private TitleBar mTitleBar;

	private TextView mClassify;
	private EditText mFrom;
	private EditText mDescription;
	private EditText mAnswer;
	private EditText mTags;
	private Button mSubmit;
	
	private View mOpenUrl;
	private View mCopyUrl;

	private String[] mClassifyNames;
	private List<MClassify> mClassifyList;

	@Override
	public int getContentLayout() {
		return R.layout.post;
	}

	@Override
	public void findViews() {
		mTitleBar = (TitleBar) findViewById(R.id.title_bar);

		mClassify = (TextView) findViewById(R.id.classify);
		mFrom = (EditText) findViewById(R.id.from);
		mDescription = (EditText) findViewById(R.id.description);
		mAnswer = (EditText) findViewById(R.id.answer);
		mTags = (EditText) findViewById(R.id.tags);
		mSubmit = (Button) findViewById(R.id.submit);
		
		mOpenUrl = findViewById(R.id.post_web_open);
		mCopyUrl = findViewById(R.id.post_web_copy);
	}

	@Override
	public void setListeners() {
		mClassify.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		mOpenUrl.setOnClickListener(this);
		mCopyUrl.setOnClickListener(this);
	}

	@Override
	public void process() {
		setTitleBarStyle();

		mClassifyList = ClassifyApis.getClassifyList();
		buildClassifyNames();
	}

	private void buildClassifyNames() {
		if (mClassifyList == null) {
			return;
		}
		mClassifyNames = new String[mClassifyList.size()];
		for (int i = 0; i < mClassifyNames.length; i++) {
			mClassifyNames[i] = mClassifyList.get(i).name;
		}
	}

	private void setTitleBarStyle() {
		mTitleBar.setTitleText(R.string.post_title);
		mTitleBar.hideRightButton();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.classify:
			onClassifyClick();
			break;

		case R.id.submit:
			onSubmit();
			break;
			
		case R.id.post_web_open:
			openPostUrl();
			break;
			
		case R.id.post_web_copy:
			copyPostUrl();
			break;

		default:
			break;
		}
	}
	
	private void openPostUrl() {
		String url = getString(R.string.post_web_url);
		UiNavigation.startWeb(this, url + "?from=post_android_app");
	}
	
	private void copyPostUrl() {
		String url = getString(R.string.post_web_url);
		SystemUtils.copyToClipboard(url);
		ToastUtils.show("已复制到剪贴板");
	}

	private void onClassifyClick() {
		if (mClassifyList == null || mClassifyList.isEmpty()) {
			return;
		}

		AlertDialog dialog = new AlertDialog(this);
		dialog.setTitleEx(mClassify.getHint().toString());
		dialog.setItems(mClassifyNames, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which < 0 || which >= mClassifyList.size()) {
					return;
				}
				MClassify classify = mClassifyList.get(which);
				mClassify.setText(classify.name);
				mClassify.setTag(classify.id);
			}
		});
		dialog.show();
	}

	private void onSubmit() {
		String classifyId = (String) mClassify.getTag();
		String from = mFrom.getText().toString().trim();
		String description = mDescription.getText().toString().trim();
		String answer = mAnswer.getText().toString().trim();
		String tags = mTags.getText().toString().trim();

		if (TextUtils.isEmpty(classifyId)) {
			onClassifyClick();
			return;
		}

		if (TextUtils.isEmpty(from)) {
			mFrom.requestFocus();
			ToastUtils.show(mFrom.getHint().toString());
			return;
		}

		if (TextUtils.isEmpty(description)) {
			mDescription.requestFocus();
			ToastUtils.show(mDescription.getHint().toString());
			return;
		}

		if (TextUtils.isEmpty(answer)) {
			mAnswer.requestFocus();
			ToastUtils.show(mAnswer.getHint().toString());
			return;
		}

		if (TextUtils.isEmpty(tags)) {
			mTags.requestFocus();
			ToastUtils.show(mTags.getHint().toString());
			return;
		}

		if (CurrentUserApis.isUnLogin()) {
			EventBusUtils.subscribeLoginEvent(this);
			UiNavigation.startLoginRegister(this);
			return;
		}

		realSubmit(classifyId, from, description, answer, tags);
	}

	private void realSubmit(String classifyId, String from, String description, String answer, String tags) {
		showProgress(R.string.post_submiting);
		mSubmit.setEnabled(false);

		PostApis.post(classifyId, from, description, answer, tags, new ResponseHandler<BaseDTO>() {
			@Override
			public void onFinish() {
				mSubmit.setEnabled(true);
				dismissProgress();
			}

			@Override
			public void onSuccess(BaseDTO data) {
				if (data.isSucceeded()) {
					ToastUtils.show(R.string.post_submit_success);
					onSubmitSuccess();
				}
			}
		});
	}

	private void onSubmitSuccess() {
		mDescription.getText().clear();
		mAnswer.getText().clear();
		mTags.getText().clear();
	}

	public void onEventLogin() {
		onSubmit();
	}

}
