package com.mianshibang.main.utils;

import com.mianshibang.main.MApplication;

import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtils {

	public static void show(String text) {
		if (TextUtils.isEmpty(text)) {
			return;
		}
		Toast.makeText(MApplication.getApplication(), text, Toast.LENGTH_SHORT).show();
	}
	
	public static void show(int resId) {
		Toast.makeText(MApplication.getApplication(), resId, Toast.LENGTH_SHORT).show();
	}
}
