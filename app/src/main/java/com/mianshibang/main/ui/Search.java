package com.mianshibang.main.ui;

import java.util.ArrayList;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.handmark.pulltorefresh.library.OnLoadMoreListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mianshibang.main.adapter.QuestionViewProvider;
import com.mianshibang.main.adapter.RequestAdapter;
import com.mianshibang.main.adapter.RequestAdapter.ArrayDelegate;
import com.mianshibang.main.adapter.RequestAdapter.Loader;
import com.mianshibang.main.adapter.RequestAdapter.LoaderListener;
import com.mianshibang.main.adapter.RequestAdapter.ViewProvider;
import com.mianshibang.main.api.HistoryApis;
import com.mianshibang.main.api.QuestionApis;
import com.mianshibang.main.model.MData;
import com.mianshibang.main.model.SearchHistory;
import com.mianshibang.main.model.dto.Questions;
import com.mianshibang.main.utils.InputMethodUtils;
import com.mianshibang.main.utils.ToastUtils;
import com.mianshibang.main.utils.UIUtils;
import com.mianshibang.main.widget.PageView;
import com.mianshibang.main.widget.SearchHistoryHeaderView;
import com.mianshibang.main.widget.SearchHistoryItemView;

public class Search extends Base implements OnLoadMoreListener, OnRefreshListener<ListView>, TextView.OnEditorActionListener, View.OnClickListener {

	public static final String Keyword = "keyword";

	private PageView mPageView;

	private PullToRefreshListView mListView;
	private RequestAdapter mAdapter;
	private QuestionViewProvider mQuestionProvider;

	private Button mCancel;
	private EditText mKeyword;
	private String mKeywordText;

	@Override
	public int getContentLayout() {
		return R.layout.search;
	}

	@Override
	public void findViews() {
		mCancel = (Button) findViewById(R.id.cancel);
		mKeyword = (EditText) findViewById(R.id.keyword);
		mListView = (PullToRefreshListView) findViewById(R.id.list);

		mPageView = (PageView) findViewById(R.id.page_view);
		mPageView.setContentView(mListView);
		mPageView.subscribRefreshEvent(this);
	}

	@Override
	public void setListeners() {
		mListView.setOnLoadMoreListener(this);
		mListView.setOnRefreshListener(this);

		mCancel.setOnClickListener(this);
		mKeyword.setOnEditorActionListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			onCancel();
			break;

		default:
			break;
		}
	}

	private void onCancel() {
		if (TextUtils.isEmpty(mKeywordText)) {
			finish();
			return;
		}
		mKeywordText = null;
		mKeyword.getText().clear();

		mAdapter.setViewProvider(mHistoryProvider);
		mAdapter.setLoader(mHistoryLoader);
		mAdapter.loadData();
	}

	@Override
	public void onBackPressed() {
		onCancel();
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (event == null || event.getAction() == KeyEvent.ACTION_UP) {
			onSearch();
		}
		return true;
	}

	private void onSearch() {
		String keyword = mKeyword.getText().toString().trim();
		if (TextUtils.isEmpty(keyword)) {
			ToastUtils.show(R.string.search_hint);
			return;
		}
		realSearch(keyword);
	}

	private void realSearch(String keyword) {
		InputMethodUtils.hide(mKeyword);
		if (mLoader.isInProgress()) {
			return;
		}
		mKeywordText = keyword;
		mAdapter.setLoader(mLoader);
		mAdapter.setViewProvider(mQuestionProvider);
		mAdapter.loadData();
		HistoryApis.addSearchHistory(keyword);
	}

	@Override
	public void process() {
		initListView();
		mPageView.showLoading();
		mAdapter.loadData();

		String keyword = getIntent().getStringExtra(Keyword);
		if (getIntent().getData() != null) {
			keyword = getIntent().getData().getQueryParameter("keyword");
		}
		if (!TextUtils.isEmpty(keyword)) {
			mKeyword.getText().append(keyword);
			onSearch();
		}
	}

	private void initListView() {
		mQuestionProvider = new QuestionViewProvider(this);
		mAdapter = new RequestAdapter(mHistoryLoader, mHistoryProvider);
		mListView.setAdapter(mAdapter);
	}

	public void onEventErrorRefresh() {
		mAdapter.loadData();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		mAdapter.refresh();
	}

	@Override
	public void onLoadMore() {
		mAdapter.loadMore();
	}

	private Loader mHistoryLoader = new Loader() {

		@Override
		public void requestRefresh(LoaderListener listener) {
			ArrayList<String> searchHistory = HistoryApis.getSearchHistory();
			ArrayList<SearchHistory> history = convert(searchHistory);
			if (!history.isEmpty()) {
				history.add(0, new SearchHistory());
			}
			listener.onLoadComplete(history);
		}

		@Override
		protected void refreshDone(ArrayList<? extends MData> downloaded) {
			mAdapter.notifyDataSetChanged();

			mListView.postDelayed(new Runnable() {
				@Override
				public void run() {
					mListView.onRefreshComplete();
				}
			}, 200);
		}

		@Override
		protected void loadDataDone(ArrayList<? extends MData> downloaded) {
			mListView.onRefreshComplete();
			mPageView.hideLoading();
			mPageView.showContent(!isEmpty());
			mAdapter.notifyDataSetChanged();
		}

		private ArrayList<SearchHistory> convert(ArrayList<String> searchHistory) {
			ArrayList<SearchHistory> histories = new ArrayList<SearchHistory>();
			for (String history : searchHistory) {
				histories.add(new SearchHistory(history));
			}
			return histories;
		}

		@Override
		public void requestLoadMore(LoaderListener listener, String beforeId) {
			listener.onLoadComplete(null);
		}
	};

	public void onEventSearch(String keyword) {
		mKeyword.getText().clear();
		mKeyword.getText().append(keyword);
		realSearch(keyword);
	}

	public void onEventClearHistory() {
		HistoryApis.clearSearchHistory();
		mAdapter.refresh();
	}

	private ViewProvider mHistoryProvider = new ViewProvider() {

		@Override
		public View getView(MData data, int position, boolean isFirstItem, boolean isLastItem, View convertView, ViewGroup parent) {
			if (convertView == null || (position == 0 && !(convertView instanceof SearchHistoryHeaderView))) {
				convertView = new SearchHistoryHeaderView(Search.this);
			}
			if (convertView == null || (position != 0 && !(convertView instanceof SearchHistoryItemView))) {
				convertView = new SearchHistoryItemView(Search.this);
			}

			if (convertView instanceof SearchHistoryItemView) {
				SearchHistoryItemView itemView = (SearchHistoryItemView) convertView;
				itemView.setData((SearchHistory) data);
				itemView.subscribe(Search.this);
			} else if (convertView instanceof SearchHistoryHeaderView) {
				SearchHistoryHeaderView headerView = (SearchHistoryHeaderView) convertView;
				headerView.subscribeClearEvent(Search.this);
			}

			return convertView;
		}

		@Override
		public int getItemViewType(int position) {
			return position == 0 ? 0 : 1;
		};

		@Override
		public int getViewTypeCount() {
			return 2;
		};
	};

	private Loader mLoader = new Loader() {
		@Override
		protected void loadDataDone(ArrayList<? extends MData> downloaded) {
			mAdapter.notifyDataSetChanged();
			String empty = UIUtils.getString(R.string.search_result_empty, mKeywordText);
			mPageView.setEmptyText(empty);
			mPageView.showEmpty(isEmpty());
			mPageView.showContent(!isEmpty());
		}

		@Override
		protected void beforeLoadData() {
			mPageView.showLoading();
		}

		@Override
		public void requestRefresh(LoaderListener listener) {
			QuestionApis.requestQuestions(null, mKeywordText, getPageIndex(), new Delegate(listener));
		}

		@Override
		protected void refreshDone(ArrayList<? extends MData> downloaded) {
			loadDataDone(downloaded);
		}

		@Override
		public void requestLoadMore(LoaderListener listener, String beforeId) {
			QuestionApis.requestQuestions(null, mKeywordText, getPageIndex(), new Delegate(listener));
		}

		@Override
		protected void loadMoreDone(ArrayList<? extends MData> downloaded) {
			mAdapter.notifyDataSetChanged();
		}

		@Override
		protected void handleFinish() {
			mListView.onRefreshComplete();
		}

		@Override
		protected void handleFailure(Throwable e) {
			if (isEmpty()) {
				mPageView.showNetworkError();
			}
		}
	};

	private class Delegate extends ArrayDelegate<Questions> {

		public Delegate(LoaderListener listener) {
			super(listener);
		}

		@Override
		protected ArrayList<? extends MData> getArray(Questions data) {
			return data != null && data.isSucceeded() ? data.questions : null;
		}

	}

}
