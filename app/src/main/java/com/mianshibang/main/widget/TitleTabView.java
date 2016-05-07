package com.mianshibang.main.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mianshibang.main.R;

public class TitleTabView extends LinearLayout {

	public static final int LEFT_ID = R.id.tab_left;
	public static final int RIGHT_ID = R.id.tab_right;

	private Button mLeft;
	private Button mRight;

	public TitleTabView(Context context) {
		this(context, null);
	}

	public TitleTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View.inflate(context, R.layout.title_tab, this);

		mLeft = (Button) findViewById(LEFT_ID);
		mRight = (Button) findViewById(RIGHT_ID);

		switchToLeft();
	}

	public void setLeftButton(int resid, OnClickListener listener) {
		setButton(mLeft, resid, listener);
	}

	public void setRightButton(int resid, OnClickListener listener) {
		setButton(mRight, resid, listener);
	}

	private static void setButton(Button button, int resid, OnClickListener listener) {
		button.setText(resid);
		button.setOnClickListener(listener);
	}

	public void switchToLeft() {
		switchButton(true);
	}

	public void switchToRight() {
		switchButton(false);
	}

	public void switchButton(boolean left) {
		mLeft.setSelected(left);
		mRight.setSelected(!left);
	}
}
