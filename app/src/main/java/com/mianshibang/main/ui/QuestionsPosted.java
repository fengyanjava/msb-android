package com.mianshibang.main.ui;

import com.mianshibang.main.R;
import com.mianshibang.main.api.PostApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.dto.Questions;
import com.mianshibang.main.widget.PageView;

public class QuestionsPosted extends QuestionList {

	@Override
	public void process() {
		super.process();
		mTitleBar.hideRightButton();
	}

	@Override
	protected String getTitleString() {
		return getString(R.string.post_question_title);
	}

	@Override
	protected void setPageViewStyle(PageView pageView) {
		pageView.setEmptyIcon(R.drawable.icon_post_empty);
		pageView.setEmptyText(R.string.post_list_empty);
	}

	@Override
	protected void onRequestRefresh(String beforeId, int pageIndex, ResponseHandler<Questions> handler) {
		PostApis.requestQuestions(pageIndex, handler);
	}

	@Override
	protected void onRequestLoadMore(String beforeId, int pageIndex, ResponseHandler<Questions> handler) {
		PostApis.requestQuestions(pageIndex, handler);
	}

}
