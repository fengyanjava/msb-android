package com.mianshibang.main.ui;

import com.mianshibang.main.R;
import com.mianshibang.main.api.LikeApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.dto.Questions;
import com.mianshibang.main.widget.PageView;

public class QuestionsLiked extends QuestionList {

	@Override
	protected String getTitleString() {
		return getString(R.string.me_have_liked);
	}

	@Override
	public void process() {
		super.process();
		mTitleBar.hideRightButton();
	}

	@Override
	protected void setPageViewStyle(PageView pageView) {
		pageView.setEmptyIcon(R.drawable.icon_like_empty);
		pageView.setEmptyText(R.string.liked_list_empty);
	}

	@Override
	protected void onRequestRefresh(String beforeId, int pageIndex, ResponseHandler<Questions> handler) {
		LikeApis.requestLikedQuestions(pageIndex, handler);
	}

	@Override
	protected void onRequestLoadMore(String beforeId, int pageIndex, ResponseHandler<Questions> handler) {
		LikeApis.requestLikedQuestions(pageIndex, handler);
	}

}
