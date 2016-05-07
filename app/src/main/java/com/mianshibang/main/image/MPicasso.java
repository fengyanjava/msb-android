package com.mianshibang.main.image;

import android.widget.ImageView;

import com.mianshibang.main.MApplication;
import com.squareup.picasso.Picasso;

public class MPicasso {

	public static void load(String url, int placeholder, ImageView view) {
		Picasso picasso = Picasso.with(MApplication.getApplication());
		picasso.setLoggingEnabled(false);
		//picasso.setIndicatorsEnabled(true);
		picasso.load(url).placeholder(placeholder).error(placeholder).into(view);
	}
	
	public static void load(int resourceId, int placeholder, ImageView view) {
		Picasso.with(MApplication.getApplication()).load(resourceId).placeholder(placeholder).error(placeholder).fit().into(view);
	}
}
