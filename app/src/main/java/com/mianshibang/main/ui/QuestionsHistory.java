package com.mianshibang.main.ui;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

import com.google.gson.Gson;
import com.mianshibang.main.R;
import com.mianshibang.main.api.HistoryApis;
import com.mianshibang.main.api.QuestionApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.dto.Questions;
import com.mianshibang.main.utils.AppConfig;
import com.mianshibang.main.widget.PageView;
import com.mianshibang.main.widget.TitleBar;
import com.mianshibang.main.widget.dialog.BaseDialog;
import com.mianshibang.main.widget.dialog.MessageDialog;

public class QuestionsHistory extends QuestionList implements View.OnClickListener {

	private static final int PAGE_COUNT = AppConfig.Browse_History_Page_Count;

	private ArrayList<String> mHistory;

	@Override
	protected String getTitleString() {
		return getString(R.string.me_browse_history);
	}

	@Override
	protected void setPageViewStyle(PageView pageView) {
		pageView.setEmptyIcon(R.drawable.icon_browse_history_empty);
		pageView.setEmptyText(R.string.browse_history_empty);
	}

	@Override
	public void process() {
		mHistory = HistoryApis.getBrowseHistory();
		super.process();

		mTitleBar.setRightButtonIcon(R.drawable.icon_title_bar_delete);
		mTitleBar.setRightButtonClick(this);
		mTitleBar.hideRightButton(mHistory.isEmpty());
	}

	private void onClearClick() {
		MessageDialog dialog = new MessageDialog(this);
		dialog.setMessage(R.string.browse_history_clear);
		dialog.setPositiveButton(true, null, this);
		dialog.show();
	}

	private void realClearHistory() {
		HistoryApis.clearBrowseHistory();
		mHistory.clear();
		mPageView.showEmpty();
		mTitleBar.hideRightButton();
	}

	@Override
	protected void onRequestRefresh(String beforeId, int pageIndex, ResponseHandler<Questions> handler) {
		String ids = getIdsByPage(pageIndex);
		if (ids == null) {
			handler.onSuccess(null);
			return;
		}
		QuestionApis.requestQuestionWithIds(ids, handler);
	}

	@Override
	protected void onRequestLoadMore(String beforeId, int pageIndex, ResponseHandler<Questions> handler) {
		String ids = getIdsByPage(pageIndex);
		QuestionApis.requestQuestionWithIds(ids, handler);
	}

	private String getIdsByPage(int pageIndex) {
		int start = pageIndex * PAGE_COUNT;
		if (start >= mHistory.size()) {
			return null;
		}
		int pageCount = Math.min(PAGE_COUNT, mHistory.size() - (pageIndex * PAGE_COUNT));
		List<String> idList = mHistory.subList(start, start + pageCount);
		return idList == null ? null : new Gson().toJson(idList);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case TitleBar.RIGHT_BUTTON_ID:
			onClearClick();
			break;

		case BaseDialog.Positive_Button_Id:
			realClearHistory();
			break;

		default:
			break;
		}
	}

}
