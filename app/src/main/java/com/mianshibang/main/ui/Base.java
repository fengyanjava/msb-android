package com.mianshibang.main.ui;

import com.mianshibang.main.utils.SystemUtils;
import com.mianshibang.main.utils.UiNavigation;
import com.mianshibang.main.widget.dialog.ProgressDialog;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class Base extends FragmentActivity {

	private static final Class<?>[] SkipActivity = { Main.class };

	private ProgressDialog mProgressDialog;

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentLayout());
		findViews();
		setListeners();
		process();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	public abstract int getContentLayout();

	public abstract void findViews();

	public abstract void setListeners();

	public abstract void process();

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void finish() {
		Class<?> cls = getClass();
		for (int i = 0; i < SkipActivity.length; i++) {
			if (cls == SkipActivity[i]) {
				super.finish();
				return;
			}
		}
		if (SystemUtils.getActivityCount() <= 1) {
			UiNavigation.startMain(this);
		}
		super.finish();
	}

	public void showProgress() {
		showProgress(null);
	}

	public void showProgress(int resId) {
		String text = getString(resId);
		showProgress(text);
	}

	public void showProgress(String text) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		} else {
			mProgressDialog = new ProgressDialog(this);
		}
		mProgressDialog.show(text);
	}

	public void dismissProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

}
