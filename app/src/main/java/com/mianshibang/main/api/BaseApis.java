package com.mianshibang.main.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;

import com.mianshibang.main.MApplication;


public abstract class BaseApis implements Command {

	public static String readStringFromAssets(String fileName) throws Exception {
		Context context = MApplication.getApplication();
		InputStream in = context.getAssets().open(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = reader.readLine();
		return line;
	}
}
