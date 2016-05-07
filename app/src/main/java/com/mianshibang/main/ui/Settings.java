package com.mianshibang.main.ui;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mianshibang.main.R;
import com.mianshibang.main.api.CurrentUserApis;
import com.mianshibang.main.api.PlatformApis;
import com.mianshibang.main.model.FontSize;
import com.mianshibang.main.push.Push;
import com.mianshibang.main.storage.GenericSettingStorage;
import com.mianshibang.main.utils.UiNavigation;
import com.mianshibang.main.widget.TitleBar;
import com.mianshibang.main.widget.dialog.BaseDialog;
import com.mianshibang.main.widget.dialog.FontSizeDialog;
import com.mianshibang.main.widget.dialog.FontSizeDialog.OnFontSizeChangedListener;
import com.mianshibang.main.widget.dialog.MessageDialog;

public class Settings extends Base implements OnClickListener, OnCheckedChangeListener, OnFontSizeChangedListener {

	private TitleBar mTitleBar;

	private ToggleButton mDirectShowAnswer;
	private ToggleButton mReceivePushMessage;

	private View mFontSize;
	private TextView mFontSizeValue;

	private View mFeedback;
	private View mCheckUpdate;
	private View mAboutApp;
	private View mLogout;

	@Override
	public int getContentLayout() {
		return R.layout.settings;
	}

	@Override
	public void findViews() {
		mTitleBar = (TitleBar) findViewById(R.id.title_bar);

		mDirectShowAnswer = (ToggleButton) findViewById(R.id.direct_show_answer);
		mReceivePushMessage = (ToggleButton) findViewById(R.id.receive_push_toggle);

		mFontSize = (View) findViewById(R.id.font_size);
		mFontSizeValue = (TextView) findViewById(R.id.font_size_value);

		mFeedback = (View) findViewById(R.id.feedback);
		mCheckUpdate = findViewById(R.id.check_update);
		mAboutApp = findViewById(R.id.about_app);
		mLogout = (View) findViewById(R.id.logout);
	}

	@Override
	public void setListeners() {
		mDirectShowAnswer.setOnCheckedChangeListener(this);
		mReceivePushMessage.setOnCheckedChangeListener(this);
		
		mFontSize.setOnClickListener(this);

		mFeedback.setOnClickListener(this);
		mCheckUpdate.setOnClickListener(this);
		mAboutApp.setOnClickListener(this);
		mLogout.setOnClickListener(this);
	}

	@Override
	public void process() {
		setTitleBarStyle();

		setShowAnswerChecked(GenericSettingStorage.isDirectShowAnswer());
		setReceivePushChecked(GenericSettingStorage.isReceivePush());
		setFontSizeValue(GenericSettingStorage.getFontSize());
	}

	private void setShowAnswerChecked(boolean checked) {
		mDirectShowAnswer.setChecked(checked);
	}

	private void setReceivePushChecked(boolean checked) {
		mReceivePushMessage.setChecked(checked);
	}

	private void setFontSizeValue(FontSize fontSize) {
		mFontSizeValue.setText(fontSize.toString());
	}

	private void setTitleBarStyle() {
		mTitleBar.setTitleText(R.string.setting_title);
		mTitleBar.hideRightButton();
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshLogoutButton();
	}

	private void refreshLogoutButton() {
		boolean isLogin = CurrentUserApis.isLogin();
		mLogout.setVisibility(isLogin ? View.VISIBLE : View.GONE);
	}

	private void onLogoutClick() {
		MessageDialog dialog = new MessageDialog(this);
		dialog.setMessage(R.string.logout_confirm_dialog_message);
		dialog.setPositiveButton(true, null, this);
		dialog.show();
	}

	private void onFontSizeClick() {
		FontSizeDialog dialog = new FontSizeDialog(this);
		dialog.setOnFontSizeChangedListener(this);
		dialog.hideButtons().show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.font_size:
			onFontSizeClick();
			break;

		case R.id.feedback:
			UiNavigation.startFeedback(this);
			break;

		case R.id.check_update:
			PlatformApis.checkUpdate(this, false);
			break;

		case R.id.about_app:
			UiNavigation.startAbout(this);
			break;

		case R.id.logout:
			onLogoutClick();
			break;

		case BaseDialog.Positive_Button_Id:
			CurrentUserApis.logout();
			finish();
			// refreshLogoutButton();
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.direct_show_answer:
			GenericSettingStorage.putDirectShowAnswer(isChecked);
			break;

		case R.id.receive_push_toggle:
			GenericSettingStorage.putReceivePush(isChecked);
			Push.openOrStop();
			break;

		default:
			break;
		}
	}

	@Override
	public void onFontSizeChanged(FontSize fontSize) {
		setFontSizeValue(fontSize);
	}

}
