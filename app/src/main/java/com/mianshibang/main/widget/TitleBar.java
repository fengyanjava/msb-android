package com.mianshibang.main.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.utils.UiNavigation;

public class TitleBar extends RelativeLayout {

	public static final int LEFT_BUTTON_ID = R.id.title_bar_left;
	public static final int RIGHT_BUTTON_ID = R.id.title_bar_right;

	private ImageButton mLeftButton;
	private ImageButton mRightButton;
	private TextView mText;
	private FrameLayout mTitleContainer;

	public TitleBar(Context context) {
		this(context, null);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setBackgroundResource(R.color.app_color);
		LayoutInflater.from(context).inflate(R.layout.title_bar, this, true);

		mLeftButton = (ImageButton) findViewById(R.id.title_bar_left);
		mRightButton = (ImageButton) findViewById(R.id.title_bar_right);
		mText = (TextView) findViewById(R.id.title_bar_text);
		mTitleContainer = (FrameLayout) findViewById(R.id.title_bar_title_container);
		
		setLeftButtonClick(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (getContext() instanceof Activity) {
					((Activity) getContext()).finish();
				}
			}
		});

		setRightButtonClick(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UiNavigation.startSearch(getContext());
			}
		});
	}

	public void setLeftButtonClick(OnClickListener listener) {
		if (mLeftButton != null) {
			mLeftButton.setOnClickListener(listener);
		}
	}

	public void setRightButtonClick(OnClickListener listener) {
		if (mRightButton != null) {
			mRightButton.setOnClickListener(listener);
		}
	}

	public void setLeftButtonIcon(int resid) {
		if (mLeftButton != null) {
			mLeftButton.setImageResource(resid);
		}
	}

	public void setRightButtonIcon(int resid) {
		if (mRightButton != null) {
			mRightButton.setImageResource(resid);
		}
	}

	public void setTitleText(int resid) {
		if (mText != null) {
			mText.setText(resid);
		}
	}

	public void setTitleText(String title) {
		if (mText != null) {
			mText.setText(title);
		}
	}

	public void hideLeftButton() {
		if (mLeftButton != null) {
			mLeftButton.setVisibility(View.GONE);
		}
	}

	public void hideRightButton() {
		hideRightButton(true);
	}

	public void hideRightButton(boolean hide) {
		if (mRightButton != null) {
			mRightButton.setVisibility(hide ? View.GONE : View.VISIBLE);
		}
	}

	public void addViewToTitleContainer(View view) {
		if (view == null) {
			return;
		}
		mTitleContainer.removeAllViews();
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-2, -2);
		params.gravity = Gravity.CENTER;
		mTitleContainer.addView(view, params);
	}

}
