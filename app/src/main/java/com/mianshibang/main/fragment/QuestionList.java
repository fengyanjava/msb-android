package com.mianshibang.main.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;

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
import com.mianshibang.main.api.QuestionApis;
import com.mianshibang.main.model.MData;
import com.mianshibang.main.model.dto.Questions;

public class QuestionList extends Base implements OnLoadMoreListener, OnRefreshListener<ListView> {

	private static final String ClassifyId = "cid";

	private String mClassifyId;

	private PullToRefreshListView mListView;
	private RequestAdapter mAdapter;

	public static Fragment newInstance(String classifyId) {
		Fragment fragment = new QuestionList();
		Bundle args = new Bundle();
		args.putString(ClassifyId, classifyId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getContentLayout() {
		return R.layout.question_list;
	}

	@Override
	public void findViews(View view) {
		mListView = (PullToRefreshListView) view;
	}

	@Override
	public void setListeners() {
		mListView.setOnLoadMoreListener(this);
		mListView.setOnRefreshListener(this);
	}

	@Override
	public void process() {
		mClassifyId = getStringArgument(ClassifyId);

		initListView();
		mAdapter.loadData();
	}

	private void initListView() {
		mAdapter = new RequestAdapter(mLoader, new QuestionViewProvider(getActivity()));
		mListView.setAdapter(mAdapter);
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
		protected void loadDataDone(ArrayList<? extends MData> downloaded) {
			if (getActivity() == null) {
				return;
			}
			mAdapter.notifyDataSetChanged();
		}

		@Override
		public void requestRefresh(LoaderListener listener) {
			QuestionApis.requestQuestions(mClassifyId, null, getPageIndex(), new Delegate(listener));
		}

		@Override
		protected void refreshDone(ArrayList<? extends MData> downloaded) {
			loadDataDone(downloaded);
		}

		@Override
		public void requestLoadMore(LoaderListener listener, String beforeId) {
			QuestionApis.requestQuestions(mClassifyId, null, getPageIndex(), new Delegate(listener));
		}

		@Override
		protected void loadMoreDone(ArrayList<? extends MData> downloaded) {
			loadDataDone(downloaded);
		}
	};

	private class Delegate extends ArrayDelegate<Questions> {

		public Delegate(LoaderListener listener) {
			super(listener);
		}
		
		@Override
		public void onFinish() {
			mListView.onRefreshComplete();
		}

		@Override
		protected ArrayList<? extends MData> getArray(Questions data) {
			return data != null && data.isSucceeded() ? data.questions : null;
		}

	}

}
