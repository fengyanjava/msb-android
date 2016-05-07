package com.mianshibang.main.fragment;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mianshibang.main.R;
import com.mianshibang.main.adapter.RequestAdapter;
import com.mianshibang.main.adapter.RequestAdapter.ArrayDelegate;
import com.mianshibang.main.adapter.RequestAdapter.Loader;
import com.mianshibang.main.adapter.RequestAdapter.LoaderListener;
import com.mianshibang.main.adapter.RequestAdapter.ViewProvider;
import com.mianshibang.main.api.CurrentUserApis;
import com.mianshibang.main.api.FavoriteApis;
import com.mianshibang.main.model.MData;
import com.mianshibang.main.model.MFavoriteFolder;
import com.mianshibang.main.model.dto.FavoriteFolders;
import com.mianshibang.main.utils.EventBusUtils;
import com.mianshibang.main.utils.UiNavigation;
import com.mianshibang.main.widget.FavoriteFolderItemView;
import com.mianshibang.main.widget.PageView;
import com.mianshibang.main.widget.TitleBar;

public class FavoriteFolder extends Base implements OnRefreshListener<ListView>, View.OnClickListener {

	private TitleBar mTitleBar;

	private PageView mPageView;
	private View mLogin;

	private PullToRefreshListView mListView;
	private RequestAdapter mAdapter;

	private Boolean mIsLogined;

	@Override
	public int getContentLayout() {
		return R.layout.favorite_folder;
	}

	@Override
	public void findViews(View view) {
		mTitleBar = (TitleBar) view.findViewById(R.id.title_bar);
		mListView = (PullToRefreshListView) view.findViewById(R.id.list);

		mPageView = (PageView) view.findViewById(R.id.page_view);
		mPageView.setContentView(mListView);
		mPageView.setEmptyIcon(R.drawable.icon_favorite_folder_empty);
		mPageView.setEmptyText(R.string.favorite_folder_empty);
		mPageView.setOther(R.id.unlogin);

		mLogin = view.findViewById(R.id.login);
	}

	@Override
	public void setListeners() {
		mListView.setOnRefreshListener(this);
		mLogin.setOnClickListener(this);
		mPageView.subscribRefreshEvent(this);
	}

	@Override
	public void process() {
		setTitleBarStyle();
		initListView();

		EventBusUtils.subscribeFolderEvent(this);

		if (CurrentUserApis.isLogin()) {
			mAdapter.loadData();
		} else {
			mPageView.showOther();
		}
	}

	private void initListView() {
		mAdapter = new RequestAdapter(mLoader, mViewProvider);
		mListView.setAdapter(mAdapter);
	}

	private void setTitleBarStyle() {
		mTitleBar.hideLeftButton();
		mTitleBar.setRightButtonIcon(R.drawable.icon_title_bar_search);
		mTitleBar.setTitleText(R.string.favorite);
	}

	@Override
	public void onPause() {
		super.onPause();
		mIsLogined = CurrentUserApis.isLogin();
	}

	@Override
	public void onResume() {
		super.onResume();

		if (mIsLogined == null) {
			return;
		}

		if (mIsLogined != CurrentUserApis.isLogin()) { // 登录状态有变化
			if (CurrentUserApis.isLogin()) {
				mAdapter.loadData();
			} else {
				mPageView.showOther();
			}
		} else if (!mAdapter.isEmpty()) {
			mAdapter.notifyDataSetChanged();
		} else if (CurrentUserApis.isLogin()) {
			mAdapter.loadData();
		}

	}

	public void onEventErrorRefresh() {
		mAdapter.loadData();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		mAdapter.refresh();
	}

	public void onEventFolderUpdate() {
		ArrayList<MData> list = mLoader.getArray();
		for (int i = list.size() - 1; i >= 0; i--) {
			MFavoriteFolder folder = (MFavoriteFolder) list.get(i);
			if (folder.isDeleted) {
				list.remove(i);
			}
		}
		mAdapter.notifyDataSetChanged();
		showEmptyIfNeed();
	}

	public void onEventFolderDeleted(String folderId) {
		ArrayList<MData> list = mLoader.getArray();
		for (int i = list.size() - 1; i >= 0; i--) {
			MFavoriteFolder folder = (MFavoriteFolder) list.get(i);
			if (folder.id.equals(folderId)) {
				list.remove(i);
			}
		}
		mAdapter.notifyDataSetChanged();
		showEmptyIfNeed();
	}

	private void showEmptyIfNeed() {
		mPageView.showEmpty(mAdapter.isEmpty());
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
			mPageView.showEmpty(downloaded == null || downloaded.isEmpty());
			mPageView.showContent(downloaded != null && !downloaded.isEmpty());
			mAdapter.notifyDataSetChanged();
		}

		@Override
		public void requestRefresh(LoaderListener listener) {
			FavoriteApis.requestFavoriteFolders(CurrentUserApis.getId(), new Delegate(listener));
		}

		@Override
		protected void refreshDone(ArrayList<? extends MData> downloaded) {
			loadDataDone(downloaded);
		}

		@Override
		public void requestLoadMore(LoaderListener listener, String beforeId) {

		}

		@Override
		protected void handleFailure(Throwable e) {
			if (mAdapter.isEmpty()) {
				mPageView.showNetworkError();
			}
		}

		@Override
		protected void handleFinish() {
			mListView.onRefreshComplete();
			mPageView.hideLoading();
		}

	};

	private ViewProvider mViewProvider = new ViewProvider() {

		@Override
		public View getView(MData data, int position, boolean isFirstItem, boolean isLastItem, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = new FavoriteFolderItemView(getActivity());
			}
			FavoriteFolderItemView itemView = (FavoriteFolderItemView) convertView;
			itemView.subscribUpdateEvent(FavoriteFolder.this);
			itemView.show((MFavoriteFolder) data, isLastItem);
			return convertView;
		}
	};

	private class Delegate extends ArrayDelegate<FavoriteFolders> {

		public Delegate(LoaderListener listener) {
			super(listener);
		}

		@Override
		protected ArrayList<? extends MData> getArray(FavoriteFolders data) {
			return data != null && data.isSucceeded() ? data.folders : null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			UiNavigation.startLoginRegister(getActivity());
			break;

		default:
			break;
		}
	}

	@Override
	public void scrollToTop() {
		mListView.getRefreshableView().setSelection(0);
	}

}
