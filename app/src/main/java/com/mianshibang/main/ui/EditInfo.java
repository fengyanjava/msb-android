package com.mianshibang.main.ui;

import android.content.Intent;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.utils.ToastUtils;
import com.mianshibang.main.widget.TitleBar;

public class EditInfo extends Base implements View.OnClickListener {
	
	public static final String EXTRA_NAME = "name";
	public static final String EXTRA_VALUE = "value";
	public static final String EXTRA_HINT = "hint";
	public static final String EXTRA_MAX_LENGTH = "max_length";
	public static final String EXTRA_ALLOW_EMPTY = "allow_empty";
	
	private TitleBar mTitleBar;
	private EditText mEditText;
	private TextView mHintText;
	
	private Intent mIntent;
	
	private String mEditName;
	private boolean mAllowEmpty;
	
	@Override
	public int getContentLayout() {
		return R.layout.edit_info;
	}

	@Override
	public void findViews() {
		mTitleBar = (TitleBar) findViewById(R.id.title_bar);
		mEditText = (EditText) findViewById(R.id.edit_text);
		mHintText = (TextView) findViewById(R.id.hint_text);
	}

	@Override
	public void setListeners() {

	}

	@Override
	public void process() {
		mIntent = getIntent();
		
		mEditName = mIntent.getStringExtra(EXTRA_NAME);
		mAllowEmpty = mIntent.getBooleanExtra(EXTRA_ALLOW_EMPTY, true);
		
		int maxLength = mIntent.getIntExtra(EXTRA_MAX_LENGTH, 0);
		if (maxLength > 0) {
			mEditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
		}
		
		mEditText.setText(mIntent.getStringExtra(EXTRA_VALUE));
		mEditText.setSelectAllOnFocus(true);
		mHintText.setText(mIntent.getStringExtra(EXTRA_HINT));
		
		setTitleBarStyle();
	}
	
	private void setTitleBarStyle() {
		mTitleBar.setTitleText("编辑" + mEditName);
		
		mTitleBar.setRightButtonIcon(R.drawable.icon_title_bar_ok);
		mTitleBar.setRightButtonClick(this);
	}
	
	private void onOkClick() {
		String text = mEditText.getText().toString().trim();
		
		if (!mAllowEmpty && TextUtils.isEmpty(text)) {
			ToastUtils.show(mEditName + "不能为空");
			return;
		}
		
		Intent data = new Intent();
		data.putExtra("data", text);
		setResult(RESULT_OK, data);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case TitleBar.RIGHT_BUTTON_ID:
			onOkClick();
			break;

		default:
			break;
		}
	}

}
