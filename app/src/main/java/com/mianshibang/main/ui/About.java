package com.mianshibang.main.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.api.PlatformApis;
import com.mianshibang.main.utils.AppConfig;
import com.mianshibang.main.utils.SystemUtils;
import com.mianshibang.main.utils.ToastUtils;
import com.mianshibang.main.utils.UIUtils;
import com.mianshibang.main.utils.UiNavigation;
import com.mianshibang.main.widget.TitleBar;

public class About extends Base implements OnClickListener {

	private TitleBar mTitleBar;

	private TextView mVersion;
	private View mEvaluation;
	private View mFeedback;
	private View mCheckUpdate;
	
	private View mWebsiteOfficial;
	private View mWebsiteWeibo;

	@Override
	public int getContentLayout() {
		return R.layout.about;
	}

	@Override
	public void findViews() {
		mTitleBar = (TitleBar) findViewById(R.id.title_bar);

		mVersion = (TextView) findViewById(R.id.version);
		mEvaluation = (View) findViewById(R.id.evaluation);
		mFeedback = (View) findViewById(R.id.feedback);
		mCheckUpdate = (View) findViewById(R.id.check_update);
		
		mWebsiteOfficial = findViewById(R.id.website_official);
		mWebsiteWeibo = findViewById(R.id.website_weibo);
	}

	@Override
	public void setListeners() {
		mEvaluation.setOnClickListener(this);
		mFeedback.setOnClickListener(this);
		mCheckUpdate.setOnClickListener(this);
		
		mWebsiteOfficial.setOnClickListener(this);
		mWebsiteWeibo.setOnClickListener(this);
	}

	@Override
	public void process() {
		setTitleBarStyle();
		setAppVersion();
	}

	private void setAppVersion() {
		String version = UIUtils.getString(R.string.about_version, SystemUtils.getVersionName());
		mVersion.setText(version);
	}

	private void setTitleBarStyle() {
		mTitleBar.hideRightButton();
		mTitleBar.setTitleText(R.string.about_title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.evaluation:
			startMarketDetail();
			break;

		case R.id.feedback:
			UiNavigation.startFeedback(this);
			break;

		case R.id.check_update:
			PlatformApis.checkUpdate(this, false);
			break;
			
		case R.id.website_official:
			UiNavigation.startWeb(this, AppConfig.App_Website_Official);
			break;
			
		case R.id.website_weibo:
			startOfficialWeibo();
			break;

		default:
			break;
		}
	}
	
	private void startOfficialWeibo() {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			Uri data = Uri.parse("sinaweibo://userinfo?nick=" + AppConfig.App_Weibo_Nickname);
			intent.setData(data);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			UiNavigation.startWeb(this, AppConfig.App_Website_Weibo);
		}
	}

	private void startMarketDetail() {
		try {
			String uriString = "market://details?id=" + getPackageName();
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriString));
			startActivity(intent);
		} catch (Exception e) {
			ToastUtils.show("请安装应用市场后再评分");
		}
	}

}
