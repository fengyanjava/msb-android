package com.mianshibang.main.ui;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.mianshibang.main.api.QuestionApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.BannerType;
import com.mianshibang.main.model.dto.Questions;
import com.mianshibang.main.widget.BannerView;

public class QuestionsClassify extends QuestionList {
	
	public static final String TitleString = "title";
	public static final String ClassifyId = "classifyId";
	
	private String mTitleString;
	private String mClassifyId;
	
	private BannerView mBannerView;

	@Override
	protected String getTitleString() {
		return mTitleString;
	}
	
	@Override
	protected View getHeaderView() {
		mBannerView = new BannerView(this);
		mBannerView.setBannerType(BannerType.QuestionList);
		return mBannerView;
	}
	
	@Override
	public void process() {
		mTitleString = getIntent().getStringExtra(TitleString);
		
		mClassifyId = null;
		Uri data = getIntent().getData();
		
		if (data != null) {
			mClassifyId = data.getQueryParameter("id");
			mTitleString = data.getQueryParameter("title");
		} else {
			mClassifyId = getIntent().getStringExtra(ClassifyId);
		}
		
		if (TextUtils.isEmpty(mClassifyId)) {
			finish();
			return;
		}
		
		super.process();
	}

	@Override
	protected void onRequestRefresh(String beforeId, int pageIndex, ResponseHandler<Questions> handler) {
		mBannerView.refresh();
		QuestionApis.requestQuestions(mClassifyId, null, pageIndex, handler);
	}

	@Override
	protected void onRequestLoadMore(String beforeId, int pageIndex, ResponseHandler<Questions> handler) {
		QuestionApis.requestQuestions(mClassifyId, null, pageIndex, handler);
	}

}
