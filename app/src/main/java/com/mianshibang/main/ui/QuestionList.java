package com.mianshibang.main.ui;

import java.util.ArrayList;

import android.view.View;
import android.widget.ListView;

import com.mianshibang.main.R;
import com.handmark.pulltorefresh.library.OnLoadMoreListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.mianshibang.main.adapter.QuestionViewProvider;
import com.mianshibang.main.adapter.RequestAdapter;
import com.mianshibang.main.adapter.RequestAdapter.ArrayDelegate;
import com.mianshibang.main.adapter.RequestAdapter.Loader;
import com.mianshibang.main.adapter.RequestAdapter.LoaderListener;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.MData;
import com.mianshibang.main.model.dto.Questions;
import com.mianshibang.main.widget.PageView;
import com.mianshibang.main.widget.TitleBar;

public abstract class QuestionList extends Base implements OnLoadMoreListener, OnRefreshListener<ListView> {

	protected TitleBar mTitleBar;
	protected PageView mPageView;

	private PullToRefreshListView mListView;
	private RequestAdapter mAdapter;

	@Override
	public int getContentLayout() {
		return R.layout.questions;
	}

	@Override
	public void findViews() {
		mTitleBar = (TitleBar) this.findViewById(R.id.title_bar);
		mListView = (PullToRefreshListView) this.findViewById(R.id.list);
		mPageView = (PageView) this.findViewById(R.id.page_view);
		mPageView.setContentView(mListView);
	}

	@Override
	public void setListeners() {
		mListView.setOnLoadMoreListener(this);
		mListView.setOnRefreshListener(this);
		mPageView.subscribRefreshEvent(this);
	}

	@Override
	public void process() {
		setTitleBarStyle();
		setPageViewStyle(mPageView);
		initListView();
		mAdapter.loadData();
	}

	protected void setPageViewStyle(PageView pageView) {

	}

	private void setTitleBarStyle() {
		mTitleBar.setLeftButtonClick(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mTitleBar.setRightButtonIcon(R.drawable.icon_title_bar_search);
		mTitleBar.setTitleText(getTitleString());
	}

	private void initListView() {
		mAdapter = new RequestAdapter(mLoader, new QuestionViewProvider(this));
		View headerView = getHeaderView();
		if (headerView != null) {
			mListView.getRefreshableView().addHeaderView(headerView);
		}
		mListView.setAdapter(mAdapter);
	}

	public void onEventErrorRefresh() {
		mAdapter.refresh();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		mAdapter.refresh();
	}

	@Override
	public void onLoadMore() {
		mAdapter.loadMore();
	}

	private Loader mLoader = new Loader() {

		@Override
		protected void beforeLoadData() {
			mPageView.showLoading();
		}

		@Override
		protected void loadDataDone(ArrayList<? extends MData> downloaded) {
			mAdapter.notifyDataSetChanged();
			mPageView.showContent(!mAdapter.isEmpty());
			mPageView.showEmpty(mAdapter.isEmpty());
		}

		@Override
		public void requestRefresh(LoaderListener listener) {
			onRequestRefresh(null, getPageIndex(), new Delegate(listener));
		}

		@Override
		protected void refreshDone(ArrayList<? extends MData> downloaded) {
			loadDataDone(downloaded);
		}

		@Override
		public void requestLoadMore(LoaderListener listener, String beforeId) {
			onRequestLoadMore(beforeId, getPageIndex(), new Delegate(listener));
		}

		@Override
		protected void loadMoreDone(ArrayList<? extends MData> downloaded) {
			mAdapter.notifyDataSetChanged();
		}

		@Override
		protected void handleFailure(Throwable e) {
			if (mAdapter.isEmpty()) {
				mPageView.showNetworkError();
			}
		}

		@Override
		public void handleFinish() {
			mPageView.hideLoading();
			mListView.onRefreshComplete();
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

	protected View getHeaderView() {
		return null;
	}

	protected abstract String getTitleString();

	protected abstract void onRequestRefresh(String beforeId, int pageIndex, ResponseHandler<Questions> handler);

	protected abstract void onRequestLoadMore(String beforeId, int pageIndex, ResponseHandler<Questions> handler);

}
