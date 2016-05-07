package com.mianshibang.main.widget;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;

public class TabHost extends FragmentTabHost {

	private OnTabUnchangeListener mListener;

	public TabHost(Context context) {
		this(context, null);
	}

	public TabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onAttachedToWindow() {
		try {
			super.onAttachedToWindow();
		} catch (Exception e) {
		}
	}

	@SuppressWarnings("unchecked")
	public Fragment getFragment(int index) {
		try {
			Field mTabsField = getClass().getSuperclass().getDeclaredField("mTabs");
			mTabsField.setAccessible(true);
			ArrayList<Object> mTabs = (ArrayList<Object>) mTabsField.get(this);

			Field tabInfo = mTabs.get(index).getClass().getDeclaredField("fragment");
			tabInfo.setAccessible(true);
			return (Fragment) tabInfo.get(mTabs.get(index));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setOnTabUnchangeListener(OnTabUnchangeListener listener) {
		mListener = listener;
	}

	@Override
	public void setCurrentTab(int index) {
		if (index == getCurrentTab()) {
			invokeOnTabUnchange(index);
		}
		super.setCurrentTab(index);
	}

	private void invokeOnTabUnchange(int index) {
		if (mListener != null) {
			mListener.onTabUnchange(index);
		}
	}

	public interface OnTabUnchangeListener {
		void onTabUnchange(int index);
	}

}
