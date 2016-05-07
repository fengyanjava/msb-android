package com.mianshibang.main.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputMethodUtils {

	public static void show(View view) {
		if (view == null) {
			return;
		}
		Context context = UIUtils.getContext();
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
	}
	
	public static void hide(View view) {
		if (view == null) {
			return;
		}
		Context context = UIUtils.getContext();
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
	}
}
