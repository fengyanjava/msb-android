package com.mianshibang.main.widget;

import android.content.Context;
import android.util.AttributeSet;

public class PostDetailActionBar extends PostActionBar {
	
	public PostDetailActionBar(Context context) {
		this(context, null);
	}

	public PostDetailActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected ActionBarStyle getStyle() {
		return ActionBarStyle.Detail;
	}

}
