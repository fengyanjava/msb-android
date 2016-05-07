package com.mianshibang.main.ui;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.api.HistoryApis;
import com.mianshibang.main.api.QuestionApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.MQuestion;
import com.mianshibang.main.model.dto.QuestionDetail;
import com.mianshibang.main.storage.GenericSettingStorage;
import com.mianshibang.main.utils.ViewUtils;
import com.mianshibang.main.widget.PageView;
import com.mianshibang.main.widget.PostDetailActionBar;
import com.mianshibang.main.widget.QuestionDetailActionBar;
import com.mianshibang.main.widget.QuestionHeaderView;
import com.mianshibang.main.widget.QuestionTagsView;
import com.mianshibang.main.widget.TitleBar;

public class Detail extends Base implements OnClickListener {

	private TitleBar mTitleBar;
	private PageView mPageView;

	private QuestionHeaderView mHeaderView;
	private QuestionTagsView mTags;
	private TextView mDescription;
	private TextView mAnswer;
	private Button mSeeAnswer;

	private View mThinkingView;
	private View mAnswerView;

	private QuestionDetailActionBar mActionBar;
	private PostDetailActionBar mPostActionBar;

	private MQuestion mQuestion;

	@Override
	public int getContentLayout() {
		return R.layout.detail;
	}

	@Override
	public void findViews() {
		mTitleBar = (TitleBar) findViewById(R.id.title_bar);

		mHeaderView = (QuestionHeaderView) findViewById(R.id.question_header);
		mTags = (QuestionTagsView) findViewById(R.id.tags);
		mDescription = (TextView) findViewById(R.id.description);
		mAnswer = (TextView) findViewById(R.id.answer);
		mSeeAnswer = (Button) findViewById(R.id.see_answer);

		mThinkingView = findViewById(R.id.answer_thinking);
		mAnswerView = findViewById(R.id.answer_view);

		mActionBar = (QuestionDetailActionBar) findViewById(R.id.action_bar);
		mPostActionBar = (PostDetailActionBar) findViewById(R.id.post_action_bar);

		View content = findViewById(R.id.content);
		mPageView = (PageView) findViewById(R.id.page_view);
		mPageView.setContentView(content);
	}

	@Override
	public void setListeners() {
		mSeeAnswer.setOnClickListener(this);
		mPageView.subscribRefreshEvent(this);
	}

	@Override
	public void process() {
		setTitleBarStyle();
		loadDetail();
	}

	private void showAnswerIfNeed() {
		if (GenericSettingStorage.isDirectShowAnswer()) {
			seeAnswer();
		}
	}

	public void onEventErrorRefresh() {
		loadDetail();
	}

	protected void requestDetail(String id, ResponseHandler<QuestionDetail> handler) {
		QuestionApis.requestDetail(id, handler);
	}

	protected void addToBrowseHistory() {
		HistoryApis.addBrowseHistory(mQuestion.id);
	}

	private void loadDetail() {
		String id = null;
		Uri data = getIntent().getData();

		if (data != null) {
			id = data.getQueryParameter("id");
		} else {
			id = getIntent().getStringExtra("id");
		}

		if (TextUtils.isEmpty(id)) {
			finish();
			return;
		}

		mPageView.showLoading();

		ResponseHandler<QuestionDetail> handler = new ResponseHandler<QuestionDetail>() {
			@Override
			public void onSuccess(QuestionDetail data) {
				if (data.isSucceeded()) {
					mQuestion = data.question;

					addToBrowseHistory();
					fillDetail();
					showAnswerIfNeed();

					mPageView.showContent();
				} else {
					mPageView.showEmpty();
				}
			}

			@Override
			public void onFailure(Exception e) {
				mPageView.showNetworkError();
			}

			@Override
			public void onFinish() {
				mPageView.hideLoading();
			}
		};

		requestDetail(id, handler);
	}

	private void fillDetail() {
		if (mQuestion == null) {
			return;
		}

		mHeaderView.show(mQuestion);
		mTags.showTags(mQuestion.tags);
		mDescription.setText(mQuestion.description);
		mAnswer.setText(mQuestion.findBestAnswer().answer);

		mActionBar.setData(mQuestion);
		mPostActionBar.setData(mQuestion.audit);

		ViewUtils.setTextViewSizeWithConfig(mDescription);
		ViewUtils.setTextViewSizeWithConfig(mAnswer);
	}

	private void setTitleBarStyle() {
		mTitleBar.hideRightButton();
		mTitleBar.setTitleText(R.string.detail_title);
	}

	private void seeAnswer() {
		mThinkingView.setVisibility(View.GONE);
		mAnswerView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.see_answer:
			seeAnswer();
			break;

		default:
			break;
		}
	}

}
