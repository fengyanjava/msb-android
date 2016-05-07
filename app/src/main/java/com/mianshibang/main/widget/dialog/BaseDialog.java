package com.mianshibang.main.widget.dialog;

import com.mianshibang.main.R;
import com.mianshibang.main.utils.DimensionUtils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class BaseDialog extends Dialog {
	
	public static final int Positive_Button_Id = R.id.dialog_positive;
	public static final int Negative_Button_Id = R.id.dialog_negative;
	public static final int Signle_Button_Id = R.id.dialog_single;
	
	private TextView mTitle;
	private FrameLayout mContent;
	
	private View mButtons;
	private Button mNegative;
	private Button mPositive;
	
	private View mSingleView;
	private Button mSingle;
	
	private boolean mCloseOnNegative = true;
	private boolean mCloseOnPositive = true;
	private boolean mCloseOnSingle = true;
	
	private View.OnClickListener mNegativeListener;
	private View.OnClickListener mPositiveListener;
	private View.OnClickListener mSingleListener;

	public BaseDialog(Context context) {
		super(context, R.style.msb_dialog);
		
		init();
		findViews();
		setListeners();
		adjustWidth();
		hideButtons();
	}
	
	@Override
	public void show() {
		try {
			super.show();
		} catch (Exception e) {
		}
	}
	
	private void init() {
        getWindow().setGravity(Gravity.CENTER);
        super.setContentView(R.layout.dialog);
        setCanceledOnTouchOutside(true);
    }
	
	private void findViews() {
		mTitle = (TextView) findViewById(R.id.dialog_title);
		mContent = (FrameLayout) findViewById(R.id.dialog_content);
		mButtons = (View) findViewById(R.id.dialog_buttons);
		mNegative = (Button) findViewById(R.id.dialog_negative);
		mPositive = (Button) findViewById(R.id.dialog_positive);
		
		mSingleView = (View) findViewById(R.id.dialog_single_view);
		mSingle = (Button) findViewById(R.id.dialog_single);
	}
	
	private void setListeners() {
		mNegative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onNegativeClick(v);
			}
		});
		
		mPositive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onPositiveClick(v);
			}
		});
		
		mSingle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onSingleClick(v);
			}
		});
	}
	
	public final BaseDialog showSingleButton() {
		mSingleView.setVisibility(View.VISIBLE);
		return this;
	}
	
	public final BaseDialog hideSingleButton() {
		mSingleView.setVisibility(View.GONE);
		return this;
	}
	
	public final BaseDialog showButtons() {
		mButtons.setVisibility(View.VISIBLE);
		return this;
	}
	
	public final BaseDialog hideButtons() {
		mButtons.setVisibility(View.GONE);
		return this;
	}
	
	public final BaseDialog hideTitle() {
		mTitle.setVisibility(View.GONE);
		return this;
	}
	
	public final void setTitle(CharSequence title) {
		mTitle.setText(title);
	}
	
	public final void setTitle(int resid) {
		mTitle.setText(resid);
	}
	
	public final void setSingleButtonText(String text) {
		mSingle.setText(text);
	}
	
	public final void setSingleButtonText(int resid) {
		mSingle.setText(resid);
	}
	
	public final void setPositiveButton(boolean autoClose, CharSequence text, View.OnClickListener listener) {
		showButtons();
		if (text != null) {
			mPositive.setText(text);
		}
		mCloseOnPositive = autoClose;
		mPositiveListener = listener;
	}
	
	public final void setPositiveButton(boolean autoClose, int resid, View.OnClickListener listener) {
		String text = resid == 0 ? null : getContext().getResources().getString(resid);
		setPositiveButton(autoClose, text, listener);
	}
	
	public final void setSingleButton(boolean autoClose, CharSequence text, View.OnClickListener listener) {
		showButtons();
		if (text != null) {
			mSingle.setText(text);
		}
		mCloseOnSingle = autoClose;
		mSingleListener = listener;
	}
	
	public final void setSingleButton(boolean autoClose, int resid, View.OnClickListener listener) {
		String text = resid == 0 ? null : getContext().getResources().getString(resid);
		setSingleButton(autoClose, text, listener);
	}
	
	public final void setNegativeButton(boolean autoClose, CharSequence text, View.OnClickListener listener) {
		showButtons();
		if (text != null) {
			mNegative.setText(text);
		}
		mCloseOnNegative = autoClose;
		mNegativeListener = listener;
	}
	
	public final void setNegativeButton(boolean autoClose, int resid, View.OnClickListener listener) {
		String text = resid == 0 ? null : getContext().getResources().getString(resid);
		setNegativeButton(autoClose, text, listener);
	}
	
	public final void setContentView(int layout) {
		LayoutInflater.from(getContext()).inflate(layout, mContent);
	}
	
	public final void setContentView(View view) {
		mContent.addView(view);
	}
	
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		mContent.addView(view, params);
	}
	
	private void adjustWidth() {
        int screenWidth = DimensionUtils.getScreenWidth();

        LayoutParams params = getWindow().getAttributes();
        int padding = DimensionUtils.getDimensionPixelSize(R.dimen.dialog_padding_horizontal) * 2;
        params.width = screenWidth - padding;
        getWindow().setAttributes(params);
    }
	
	protected void adjustHeight() {
		int screenHeight = DimensionUtils.getScreenHeight();
		
		LayoutParams params = getWindow().getAttributes();
		int padding = DimensionUtils.getDimensionPixelSize(R.dimen.dialog_padding_vertical) * 2;
		params.height = screenHeight - padding;
		getWindow().setAttributes(params);
	}
	
	private void onSingleClick(View v) {
		if (mSingleListener != null) {
			mSingleListener.onClick(v);
		}
		if (mCloseOnSingle) {
			dismiss();
		}
	}
	
	private void onPositiveClick(View v) {
		if (mPositiveListener != null) {
			mPositiveListener.onClick(v);
		}
		if (mCloseOnPositive) {
			dismiss();
		}
	}
	
	private void onNegativeClick(View v) {
		if (mNegativeListener != null) {
			mNegativeListener.onClick(v);
		}
		if (mCloseOnNegative) {
			dismiss();
		}
	}

}
