package com.mianshibang.main.ui;

import java.util.ArrayList;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mianshibang.main.R;
import com.mianshibang.main.adapter.QuestionViewProvider;
import com.mianshibang.main.adapter.RequestAdapter;
import com.mianshibang.main.adapter.RequestAdapter.ArrayDelegate;
import com.mianshibang.main.adapter.RequestAdapter.Loader;
import com.mianshibang.main.adapter.RequestAdapter.LoaderListener;
import com.mianshibang.main.api.FavoriteApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.MData;
import com.mianshibang.main.model.MFavoriteFolder;
import com.mianshibang.main.model.dto.FavoriteFolderDetail;
import com.mianshibang.main.model.dto.IdResult;
import com.mianshibang.main.utils.EventBusUtils;
import com.mianshibang.main.utils.ToastUtils;
import com.mianshibang.main.utils.UIUtils;
import com.mianshibang.main.widget.FavoriteDetailHeaderView;
import com.mianshibang.main.widget.PageView;
import com.mianshibang.main.widget.TitleBar;
import com.mianshibang.main.widget.dialog.AlertDialog;
import com.mianshibang.main.widget.dialog.CreateFavoriteFolderDialog;
import com.mianshibang.main.widget.dialog.FolderMorePopupWindow;
import com.mianshibang.main.widget.dialog.ProgressDialog;

public class FavoriteDetail extends Base implements OnClickListener, OnRefreshListener<ListView> {

	public static final String FolderId = "folder_id";

	private TitleBar mTitleBar;
	private PageView mPageView;
	private FavoriteDetailHeaderView mDetailHeader;

	private PullToRefreshListView mListView;
	private RequestAdapter mAdapter;

	private String mFolderId;
	private MFavoriteFolder mFolder;
	
	private ProgressDialog mProgressDialog;

	@Override
	public int getContentLayout() {
		return R.layout.favorite_detail;
	}

	@Override
	public void findViews() {
		mTitleBar = (TitleBar) findViewById(R.id.title_bar);
		mListView = (PullToRefreshListView) findViewById(R.id.list);
		mPageView = (PageView) findViewById(R.id.page_view);
		mPageView.setContentView(mListView);
	}

	@Override
	public void setListeners() {
		mListView.setOnRefreshListener(this);
		mPageView.subscribRefreshEvent(this);
	}

	@Override
	public void process() {
		setTitleBarStyle();

		mFolderId = getIntent().getStringExtra(FolderId);
		checkParameter();

		initListView();
		mAdapter.loadData();
	}

	private void initListView() {
		mDetailHeader = new FavoriteDetailHeaderView(this);
		mListView.getRefreshableView().addHeaderView(mDetailHeader);

		mAdapter = new RequestAdapter(mLoader, new QuestionViewProvider(this));
		mListView.setAdapter(mAdapter);
	}

	private void checkParameter() {
		if (TextUtils.isEmpty(mFolderId)) {
			throw new IllegalArgumentException("Come to .ui.FavoriteDetail but folder_id is null");
		}
	}

	private void setTitleBarStyle() {
		mTitleBar.setTitleText(R.string.favorite_detail_title);
		mTitleBar.setRightButtonIcon(R.drawable.icon_title_bar_favorite_more);
		mTitleBar.setRightButtonClick(this);
	}

	public void onEventErrorRefresh() {
		mAdapter.refresh();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		mAdapter.refresh();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case TitleBar.RIGHT_BUTTON_ID:
			showMorePopupWindow(v);
			break;

		default:
			break;
		}
	}

	public void onEventEditFolder() { // 点击编辑
		if (mFolder == null) {
			return;
		}
		
		CreateFavoriteFolderDialog dialog = new CreateFavoriteFolderDialog(this, mFolder);
		dialog.subscribeEvent(this);
		dialog.show();
	}
	
	public void onEventFolderUpdate() { // 完成编辑
		refreshHeaderView(mFolder);
	}

	public void onEventDeleteFolder() { // 点击删除
		showDeleteHintDialog();
	}
	
	private void showDeleteHintDialog() {
		AlertDialog dialog = new AlertDialog(this);
		dialog.setTitleEx(R.string.delete_favorite_folder);
		dialog.setMessage(R.string.delete_favorite_folder_hint);
		dialog.setNegativeButton(true, null, null);
		dialog.setPositiveButton(true, R.string.delete, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				realDeleteFolder();
			}
		});
		dialog.show();
	}
	
	private void realDeleteFolder() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
		}
		
		mProgressDialog.show(R.string.delete_favorite_folder);
		
		FavoriteApis.deleteFavoriteFolder(mFolderId, new ResponseHandler<IdResult>() {
			@Override
			public void onSuccess(IdResult data) {
				if (data.isSucceeded()) {
					mProgressDialog.dismiss();
					ToastUtils.show("删除成功");
					EventBusUtils.postFolderEvent(mFolderId);
					finish();
				}
			}
			
			@Override
			public void onFinish() {
				mProgressDialog.dismiss();
			}
		});
	}

	private void showMorePopupWindow(View anchor) {
		FolderMorePopupWindow popupWindow = new FolderMorePopupWindow(this);
		popupWindow.subscribeEvent(this);
		int yOffset = UIUtils.dip2px(12);
		popupWindow.showAsDropDown(anchor, 0, -yOffset);
	}

	private void refreshHeaderView(MFavoriteFolder folder) {
		mDetailHeader.show(folder);
	}

	private Loader mLoader = new Loader() {

		@Override
		protected void beforeLoadData() {
			mPageView.showLoading();
		}

		@Override
		protected void loadDataDone(ArrayList<? extends MData> downloaded) {
			mPageView.showContent();
			mAdapter.notifyDataSetChanged();
		}

		@Override
		public void requestRefresh(LoaderListener listener) {
			FavoriteApis.requestFavoriteDetail(mFolderId, new Delegate(listener));
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
		public void handleFinish() {
			mPageView.hideLoading();
			mListView.onRefreshComplete();
		}

	};

	private class Delegate extends ArrayDelegate<FavoriteFolderDetail> {

		public Delegate(LoaderListener listener) {
			super(listener);
		}

		@Override
		protected ArrayList<? extends MData> getArray(FavoriteFolderDetail data) {
			mFolder = data.folder;
			refreshHeaderView(mFolder);
			return data != null && data.isSucceeded() ? data.folder.questions : null;
		}

	}

}
