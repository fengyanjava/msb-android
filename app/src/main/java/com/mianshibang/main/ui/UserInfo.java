package com.mianshibang.main.ui;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.api.CurrentUserApis;
import com.mianshibang.main.api.UserApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.MUser;
import com.mianshibang.main.model.MUserDetail;
import com.mianshibang.main.model.dto.BaseDTO;
import com.mianshibang.main.utils.MLog;
import com.mianshibang.main.utils.ToastUtils;
import com.mianshibang.main.utils.UiNavigation;
import com.mianshibang.main.widget.TitleBar;
import com.mianshibang.main.widget.dialog.AlertDialog;
import com.mianshibang.main.widget.dialog.SelectCityDialog;
import com.mianshibang.main.widget.dialog.SelectDateDialog;
import com.mianshibang.main.widget.dialog.SelectGenderDialog;
import com.mianshibang.main.widget.dialog.SelectCityDialog.OnCitySelectedListener;
import com.mianshibang.main.widget.dialog.SelectDateDialog.OnDateSelectListener;
import com.mianshibang.main.widget.dialog.SelectGenderDialog.OnGenderSelectListener;

public class UserInfo extends Base implements View.OnClickListener, OnGenderSelectListener, OnDateSelectListener, OnCitySelectedListener {

	private static final int REQUEST_CODE_NICKNAME = 1000;
	private static final int REQUEST_CODE_PERFESSION = 1001;
	private static final int REQUEST_CODE_INTRO = 1002;
	private static final int REQUEST_CODE_WEIBO = 1004;
	private static final int REQUEST_CODE_BLOG = 1005;
	private static final int REQUEST_CODE_QQ = 1006;

	private TitleBar mTitleBar;

	private TextView mNickname;
	private TextView mGender;
	private TextView mPerfession;
	private TextView mIntro;
	private TextView mLocation;
	private TextView mBirthday;
	private TextView mWeibo;
	private TextView mBlog;
	private TextView mQQ;

	private MUser mUser;

	@Override
	public int getContentLayout() {
		return R.layout.user_info;
	}

	@Override
	public void findViews() {
		mTitleBar = (TitleBar) findViewById(R.id.title_bar);

		mNickname = (TextView) findViewById(R.id.nickname);
		mGender = (TextView) findViewById(R.id.gender);
		mPerfession = (TextView) findViewById(R.id.perfession);
		mIntro = (TextView) findViewById(R.id.intro);
		mLocation = (TextView) findViewById(R.id.location);
		mBirthday = (TextView) findViewById(R.id.birthday);
		mWeibo = (TextView) findViewById(R.id.weibo);
		mBlog = (TextView) findViewById(R.id.blog);
		mQQ = (TextView) findViewById(R.id.qq);
	}

	@Override
	public void setListeners() {
		findViewById(R.id.view_nickname).setOnClickListener(this);
		findViewById(R.id.view_gender).setOnClickListener(this);
		findViewById(R.id.view_perfession).setOnClickListener(this);
		findViewById(R.id.view_intro).setOnClickListener(this);
		findViewById(R.id.view_location).setOnClickListener(this);
		findViewById(R.id.view_birthday).setOnClickListener(this);
		findViewById(R.id.view_weibo).setOnClickListener(this);
		findViewById(R.id.view_blog).setOnClickListener(this);
		findViewById(R.id.view_qq).setOnClickListener(this);
	}

	@Override
	public void process() {
		setTitleBarStyle();

		mUser = CurrentUserApis.get();
		mUser.detail = mUser.detail == null ? new MUserDetail() : mUser.detail;

		fillUserInfo();
	}

	private void fillUserInfo() {
		mNickname.setText(mUser.nickname);
		mGender.setText(mUser.gender);
		mPerfession.setText(mUser.perfession);
		mIntro.setText(mUser.detail.intro);
		mLocation.setText(mUser.detail.getLocation());
		mBirthday.setText(mUser.detail.birthday);
		mWeibo.setText(mUser.detail.weibo);
		mBlog.setText(mUser.detail.blog);
		mQQ.setText(mUser.detail.qq);
	}

	private void setTitleBarStyle() {
		mTitleBar.setTitleText(R.string.user_info_title);
		mTitleBar.setRightButtonIcon(R.drawable.icon_title_bar_ok);
		mTitleBar.setRightButtonClick(this);
	}

	private void realUpdateInfo(MUser user) {
		showProgress("正在保存");

		UserApis.updateUser(user, new ResponseHandler<BaseDTO>() {
			@Override
			public void onSuccess(BaseDTO data) {
				if (data.isSucceeded()) {
					ToastUtils.show("修改成功");
					dismissProgress();
					finish();
				}
			}

			@Override
			public void onFinish() {
				dismissProgress();
			}
		});
	}

	private void onUpdateInfo() {
		MUser changedUserInfo = getChangedUserInfo();

		if (changedUserInfo == null) {
			MLog.i("信息未变更");
			ToastUtils.show("信息未变更");
			finish();
			return;
		}

		realUpdateInfo(changedUserInfo);
	}

	@Override
	public void onClick(View v) {

		int requestCode = 0;
		String name = null;
		String value = null;
		String hint = null;
		int maxLength = 0;
		boolean allowEmpty = true;

		switch (v.getId()) {
		case TitleBar.RIGHT_BUTTON_ID:
			onUpdateInfo();
			return;

		case R.id.view_gender:
			SelectGenderDialog dialog = new SelectGenderDialog(this);
			dialog.setOnGenderSelectListener(this);
			dialog.show();
			return;

		case R.id.view_location:
			String location = mLocation.getText().toString().trim();
			String[] locations = parseLocation(location);
			SelectCityDialog cityDialog = SelectCityDialog.Factory.getDialog(this, locations[0], locations[1]);
			cityDialog.setOnCitySelectedListener(this);
			cityDialog.show();
			return;

		case R.id.view_birthday:
			String birthday = mBirthday.getText().toString().trim();
			SelectDateDialog dateDialog = new SelectDateDialog(this, birthday);
			dateDialog.setTitle("设置生日");
			dateDialog.setOnDateSelectListener(this);
			dateDialog.show();
			return;

		case R.id.view_nickname:
			requestCode = REQUEST_CODE_NICKNAME;
			name = getString(R.string.user_info_label_nickname);
			value = mNickname.getText().toString();
			hint = getString(R.string.eidt_user_info_hint_nickname);
			maxLength = 20;
			allowEmpty = false;
			break;

		case R.id.view_perfession:
			requestCode = REQUEST_CODE_PERFESSION;
			name = getString(R.string.user_info_label_perfession);
			value = mPerfession.getText().toString();
			hint = getString(R.string.eidt_user_info_hint_perfession);
			break;

		case R.id.view_intro:
			requestCode = REQUEST_CODE_INTRO;
			name = getString(R.string.user_info_label_intro);
			value = mIntro.getText().toString();
			hint = getString(R.string.eidt_user_info_hint_intro);
			maxLength = 100;
			break;

		case R.id.view_weibo:
			requestCode = REQUEST_CODE_WEIBO;
			name = getString(R.string.user_info_label_weibo);
			value = mWeibo.getText().toString();
			hint = getString(R.string.eidt_user_info_hint_weibo);
			maxLength = 70;
			break;

		case R.id.view_blog:
			requestCode = REQUEST_CODE_BLOG;
			name = getString(R.string.user_info_label_blog);
			value = mBlog.getText().toString();
			hint = getString(R.string.eidt_user_info_hint_blog);
			maxLength = 80;
			break;

		case R.id.view_qq:
			requestCode = REQUEST_CODE_QQ;
			name = getString(R.string.user_info_label_qq);
			value = mQQ.getText().toString();
			hint = getString(R.string.eidt_user_info_hint_qq);
			maxLength = 15;
			break;

		default:
			return;
		}

		UiNavigation.startEditInfo(requestCode, this, name, value, hint, maxLength, allowEmpty);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}

		String value = data.getStringExtra("data");

		switch (requestCode) {
		case REQUEST_CODE_NICKNAME:
			mNickname.setText(value);
			break;

		case REQUEST_CODE_PERFESSION:
			mPerfession.setText(value);
			break;

		case REQUEST_CODE_INTRO:
			mIntro.setText(value);
			break;

		case REQUEST_CODE_WEIBO:
			mWeibo.setText(value);
			break;

		case REQUEST_CODE_BLOG:
			mBlog.setText(value);
			break;

		case REQUEST_CODE_QQ:
			mQQ.setText(value);
			break;

		default:
			break;
		}
	}

	private MUser getChangedUserInfo() {
		String nickname = mNickname.getText().toString();
		String gender = mGender.getText().toString();
		String perfession = mPerfession.getText().toString();
		String intro = mIntro.getText().toString();

		String location = mLocation.getText().toString().trim();
		String[] locations = parseLocation(location);
		String province = locations[0];
		String city = locations[1];

		String birthday = mBirthday.getText().toString().trim();
		String weibo = mWeibo.getText().toString();
		String blog = mBlog.getText().toString();
		String qq = mQQ.getText().toString();

		MUser user = new MUser();
		user.detail = new MUserDetail();
		int count = 0;

		if (isChanged(mUser.nickname, nickname)) {
			MLog.i("nickname=" + nickname);
			user.nickname = nickname;
			count++;
		}

		if (isChanged(mUser.gender, gender)) {
			MLog.i("gender=" + gender);
			user.gender = gender;
			count++;
		}

		if (isChanged(mUser.perfession, perfession)) {
			MLog.i("perfession=" + perfession);
			user.perfession = perfession;
			count++;
		}

		if (isChanged(mUser.detail.intro, intro)) {
			MLog.i("intro=" + intro);
			user.detail.intro = intro;
			count++;
		}

		if (isChanged(mUser.detail.province, province)) {
			MLog.i("province=" + province);
			user.detail.province = province;
			count++;
		}

		if (isChanged(mUser.detail.city, city)) {
			MLog.i("city=" + city);
			user.detail.city = city;
			count++;
		}

		if (isChanged(mUser.detail.birthday, birthday)) {
			MLog.i("birthday=" + birthday);
			user.detail.birthday = birthday;
			count++;
		}

		if (isChanged(mUser.detail.weibo, weibo)) {
			MLog.i("weibo=" + weibo);
			user.detail.weibo = weibo;
			count++;
		}

		if (isChanged(mUser.detail.blog, blog)) {
			MLog.i("blog=" + blog);
			user.detail.blog = blog;
			count++;
		}

		if (isChanged(mUser.detail.qq, qq)) {
			MLog.i("qq=" + qq);
			user.detail.qq = qq;
			count++;
		}

		return count > 0 ? user : null;
	}

	private static boolean isChanged(String original, String newest) {
		if (TextUtils.isEmpty(original)) {
			return !TextUtils.isEmpty(newest);
		}
		return !original.equals(newest);
	}

	@Override
	public void onGenderSelect(String gender) {
		mGender.setText(gender);
	}

	@Override
	public void onDateSelect(String date) {
		mBirthday.setText(date);
	}

	@Override
	public void onCitySelected(String province, String city) {
		String location = province + " " + city;
		mLocation.setText(location);
	}

	private static String[] parseLocation(String location) {
		String[] split = location.split("\\ +");
		String province = split != null && split.length > 0 ? split[0] : null;
		String city = split != null && split.length > 1 ? split[1] : null;
		return new String[] { province, city };
	}

	@Override
	public void onBackPressed() {
		MUser changedUserInfo = getChangedUserInfo();
		if (changedUserInfo == null) {
			super.onBackPressed();
		} else {
			showUpdateUserInfoDialog(changedUserInfo);
		}
	}

	private void showUpdateUserInfoDialog(final MUser changedUserInfo) {
		AlertDialog dialog = new AlertDialog(this);
		dialog.setTitleEx(R.string.tip);
		dialog.setMessage(R.string.user_info_save_hint);
		dialog.setPositiveButton(true, R.string.save, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				realUpdateInfo(changedUserInfo);
			}
		});
		dialog.setNegativeButton(true, R.string.unsave, new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		dialog.show();
	}
}
