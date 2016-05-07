package com.mianshibang.main.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.mianshibang.main.R;

public class ProgressDialog extends Dialog {

	private TextView mText;

	public ProgressDialog(Context context) {
		super(context, R.style.msb_progress_dialog);

		getWindow().setGravity(Gravity.CENTER);
		super.setContentView(R.layout.dialog_progress);
		setCanceledOnTouchOutside(false);

		mText = (TextView) findViewById(R.id.progress_dialog_text);
	}
	
	@Override
	public void show() {
		show(null);
	}
	
	public void show(int resId) {
		show(getContext().getString(resId));
	}

	public void show(String text) {
		try {
			mText.setText(text);
			mText.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
			super.show();
		} catch (Exception e) {
		}
	}

}
