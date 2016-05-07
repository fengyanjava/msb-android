package com.mianshibang.main.ui;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mianshibang.main.R;
import com.mianshibang.main.utils.SystemUtils;
import com.mianshibang.main.widget.TitleBar;

public class Web extends Base implements OnClickListener {

	private TitleBar mTitleBar;
	private WebView mWebView;
	private ProgressBar mProgressBar;

	@Override
	public int getContentLayout() {
		return R.layout.web_view;
	}

	@Override
	public void findViews() {
		mTitleBar = (TitleBar) findViewById(R.id.title_bar);
		mWebView = (WebView) findViewById(R.id.webview);
		mProgressBar = (ProgressBar) findViewById(R.id.progress);
		mProgressBar.setMax(100);
	}

	@Override
	public void setListeners() {

	}

	@Override
	public void process() {
		setTitleBarStyle();
		initWebView();
		loadUrl();
	}

	private void loadUrl() {
		Uri uri = getIntent().getData();
		String url = uri != null ? uri.getQueryParameter("url") : getIntent().getStringExtra("url");
		if (!TextUtils.isEmpty(url)) {
			mWebView.loadUrl(url);
		} else {
			finish();
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView() {
		mWebView.setInitialScale(25);
		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setUseWideViewPort(true);
		hideZoomControls();

		String ua = mWebView.getSettings().getUserAgentString();
		ua += "/Mianshibang " + SystemUtils.getVersionName();
		mWebView.getSettings().setUserAgentString(ua);

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (TextUtils.isEmpty(url) || url.startsWith("http")) {
					view.loadUrl(url);
				} else {
					startIntent(url);
				}
				return true;
			}

			@Override
			public void onFormResubmission(WebView view, Message dontResend, Message resend) {
				resend.sendToTarget();
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedTitle(WebView view, String title) {
				mTitleBar.setTitleText(title);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				mProgressBar.setProgress(newProgress);
				mProgressBar.setVisibility(newProgress == 100 ? View.GONE : View.VISIBLE);
			}
		});

		mWebView.setDownloadListener(new DownloadListener() {

			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
				try {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					startActivity(intent);
				} catch (Exception e) {
				}
			}
		});
	}

	private void hideZoomControls() {
		try {
			Method method = mWebView.getSettings().getClass().getMethod("setDisplayZoomControls", Boolean.TYPE);
			method.invoke(mWebView.getSettings(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void startIntent(String url) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
		} catch (Exception e) {
		}
	}

	private void setTitleBarStyle() {
		mTitleBar.hideRightButton();
		mTitleBar.setTitleText(R.string.web_view_title);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			super.onBackPressed();
		}
	}

}
