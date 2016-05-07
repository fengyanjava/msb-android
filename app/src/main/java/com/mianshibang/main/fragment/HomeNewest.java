package com.mianshibang.main.fragment;

import java.util.ArrayList;

import android.view.View;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.OnLoadMoreListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mianshibang.main.R;
import com.mianshibang.main.adapter.QuestionViewProvider;
import com.mianshibang.main.adapter.RequestAdapter;
import com.mianshibang.main.adapter.RequestAdapter.ArrayDelegate;
import com.mianshibang.main.adapter.RequestAdapter.Loader;
import com.mianshibang.main.adapter.RequestAdapter.LoaderListener;
import com.mianshibang.main.api.QuestionApis;
import com.mianshibang.main.model.MData;
import com.mianshibang.main.model.dto.Questions;
import com.mianshibang.main.widget.BannerView;
import com.mianshibang.main.widget.PageView;

public class HomeNewest extends Base implements OnLoadMoreListener, OnRefreshListener<ListView> {

	private PageView mPageView;
	
	private PullToRefreshListView mListView;
	private RequestAdapter mAdapter;
	
	private BannerView mBannerView;

	@Override
	public int getContentLayout() {
		return R.layout.home_questions;
	}

	@Override
	public void findViews(View view) {
		mListView = (PullToRefreshListView) view.findViewById(R.id.list);
		mPageView = (PageView) view.findViewById(R.id.page_view);
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
		initListView();
		mAdapter.loadData();
	}

	private void initListView() {
		mBannerView = createBannerView();
		if (mBannerView != null) {
			mListView.getRefreshableView().addHeaderView(mBannerView);
		}

		mAdapter = new RequestAdapter(mLoader, new QuestionViewProvider(getActivity()));
		mListView.setAdapter(mAdapter);
	}
	
	protected BannerView createBannerView() {
		return null;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mBannerView != null) {
			mBannerView.onResume();
		}
		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (mBannerView != null) {
			mBannerView.onPause();
		}
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
			if (getActivity() == null) {
				return;
			}
			mPageView.showContent();
			mAdapter.notifyDataSetChanged();
		}

		@Override
		public void requestRefresh(LoaderListener listener) {
			if (mBannerView != null) {
				mBannerView.refresh();
			}
			doRefresh(listener);
		}

		@Override
		protected void refreshDone(ArrayList<? extends MData> downloaded) {
			loadDataDone(downloaded);
		}

		@Override
		public void requestLoadMore(LoaderListener listener, String beforeId) {
			doLoadMore(listener, beforeId, getPageIndex());
		}

		@Override
		protected void loadMoreDone(ArrayList<? extends MData> downloaded) {
			loadDataDone(downloaded);
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

	protected class Delegate extends ArrayDelegate<Questions> {

		public Delegate(LoaderListener listener) {
			super(listener);
		}
		
		@Override
		protected ArrayList<? extends MData> getArray(Questions data) {
			return data != null && data.isSucceeded() ? data.questions : null;
		}

	}
	
	protected void doRefresh(LoaderListener listener) {
		
		QuestionApis.requestFeeds(null, new Delegate(listener));
	}
	
	protected void doLoadMore(LoaderListener listener, String beforeId, int page) {
		QuestionApis.requestFeeds(beforeId, new Delegate(listener));
	}
	
	@Override
	public void scrollToTop() {
		mListView.getRefreshableView().setSelection(0);
	}

}
