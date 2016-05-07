package com.mianshibang.main.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

public abstract class Base extends Fragment {
	
	private View mContentView;
	
	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mContentView == null) {
			mContentView = inflater.inflate(getContentLayout(), container, false);
			findViews(mContentView);
			setListeners();
			process();
		}
		if (mContentView.getParent() != null) {
			((ViewGroup) mContentView.getParent()).removeView(mContentView);
		}
		return mContentView;
	}
	
	protected String getStringArgument(String key) {
		Bundle arguments = getArguments();
		return arguments != null ? arguments.getString(key) : null;
	}
	
	public abstract int getContentLayout();

	public abstract void findViews(View view);
	
	public abstract void setListeners();
	
	public abstract void process();
	
	public void scrollToTop() {};
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getClass().getSimpleName());
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(getClass().getSimpleName());
	}
	
	protected void showProgress() {
		com.mianshibang.main.ui.Base activity = (com.mianshibang.main.ui.Base) getActivity();
		activity.showProgress();
	}
	
	protected void showProgress(int resId) {
		com.mianshibang.main.ui.Base activity = (com.mianshibang.main.ui.Base) getActivity();
		activity.showProgress(resId);
	}
	
	protected void showProgress(String text) {
		com.mianshibang.main.ui.Base activity = (com.mianshibang.main.ui.Base) getActivity();
		activity.showProgress(text);
	}
	
	protected void dismissProgress() {
		com.mianshibang.main.ui.Base activity = (com.mianshibang.main.ui.Base) getActivity();
		activity.dismissProgress();
	}
	
}
