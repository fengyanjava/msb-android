package com.mianshibang.main.utils;

import com.mianshibang.main.MApplication;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class DimensionUtils {

	private static DisplayMetrics getDisplayMetrics() {
		return getResources().getDisplayMetrics();
	}

	public static int getScreenWidth() {
		return getDisplayMetrics().widthPixels;
	}

	public static int getScreenHeight() {
		return getDisplayMetrics().heightPixels;
	}

	public static int px2dp(float px) {
		return (int) (px / getDisplayMetrics().density + 0.5f);
	}

	public static int dp2px(float dp) {
		return (int) (dp * getDisplayMetrics().density + 0.5f);
	}

	public static int px2sp(float pxValue) {
		return (int) (pxValue / getDisplayMetrics().scaledDensity + 0.5f);
	}

	public static int sp2px(float spValue) {
		return (int) (spValue * getDisplayMetrics().scaledDensity + 0.5f);
	}
	
	public static int getDimensionPixelSize(int id) {
		return getResources().getDimensionPixelSize(id);
	}
	
	private static Context getContext() {
		return MApplication.getApplication();
	}
	
	private static Resources getResources() {
		return getContext().getResources();
	}

}
