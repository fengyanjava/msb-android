package com.mianshibang.main.widget;

import android.content.Context;
import android.util.AttributeSet;

public class QuestionDetailActionBar extends QuestionActionBar {
	
	public QuestionDetailActionBar(Context context) {
		this(context, null);
	}
	
	public QuestionDetailActionBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public QuestionDetailActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected ActionBarStyle getStyle() {
		return ActionBarStyle.Detail;
	}

}
