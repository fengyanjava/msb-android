package com.mianshibang.main.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mianshibang.main.R;

public class SelectGenderDialog extends BaseDialog implements View.OnClickListener {
	
	private OnGenderSelectListener mListener;

	public SelectGenderDialog(Context context) {
		super(context);

		hideTitle().hideButtons();
		setContentView(R.layout.dialog_select_gender);

		initView();
	}

	private void initView() {
		View male = findViewById(R.id.gender_male);
		View female = findViewById(R.id.gender_female);
		View secret = findViewById(R.id.gender_secret);

		male.setOnClickListener(this);
		female.setOnClickListener(this);
		secret.setOnClickListener(this);
	}
	
	public void setOnGenderSelectListener(OnGenderSelectListener listener) {
		mListener = listener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gender_male:
		case R.id.gender_female:
		case R.id.gender_secret:
			String gender = ((TextView) v).getText().toString();
			onGenderSelect(gender);
			dismiss();
			break;

		default:
			break;
		}
	}
	
	private void onGenderSelect(String gender) {
		if (mListener != null) {
			mListener.onGenderSelect(gender);
		}
	}
	
	public static interface OnGenderSelectListener {
		void onGenderSelect(String gender);
	}

}
