package com.mianshibang.main.widget.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.JsonObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mianshibang.main.R;
import com.mianshibang.main.adapter.RequestAdapter;
import com.mianshibang.main.adapter.RequestAdapter.ArrayDelegate;
import com.mianshibang.main.adapter.RequestAdapter.Loader;
import com.mianshibang.main.adapter.RequestAdapter.LoaderListener;
import com.mianshibang.main.adapter.RequestAdapter.ViewProvider;
import com.mianshibang.main.api.FavoriteApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.MData;
import com.mianshibang.main.model.MFavoriteFolderSimple;
import com.mianshibang.main.model.MQuestion;
import com.mianshibang.main.model.dto.BaseDTO;
import com.mianshibang.main.model.dto.FoldersForSelect;
import com.mianshibang.main.utils.MLog;

public class SelectFavoriteFolderDialog extends BaseDialog 
		implements OnRefreshListener<ListView>, View.OnClickListener {
	
	private MQuestion mQuestion;
	
	private PullToRefreshListView mListView;
	private RequestAdapter mAdapter;
	
	private Map<String, Boolean> mOriginalState = new HashMap<String, Boolean>();
	
	private OnFavoriteChangeListener mFavoriteChangeListener;
	
	private ProgressDialog mProgressDialog;

	public SelectFavoriteFolderDialog(Context context, MQuestion question) {
		super(context);
		
		if (question == null) {
			throw new IllegalArgumentException("question is null");
		}
		
		mQuestion = question;
		
		setTitle(R.string.select_favorite_folder);
		setContentView(R.layout.dialog_select_folder);
		adjustHeight();
		setPositiveButton(false, null, this);
		
		mListView = (PullToRefreshListView) findViewById(R.id.list);
		mListView.setOnRefreshListener(this);
		
		View header = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_folder_header, null);
		header.setOnClickListener(this);
		mListView.getRefreshableView().addHeaderView(header);
		
		mAdapter = new RequestAdapter(mLoader, mViewProvider);
		mListView.setAdapter(mAdapter);
	}
	
	public void setOnFavoriteChangedListener(OnFavoriteChangeListener listener) {
		mFavoriteChangeListener = listener;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case Positive_Button_Id:
			onPositiveClick();
			break;

		default:
			CreateFavoriteFolderDialog dialog = new CreateFavoriteFolderDialog(getContext());
			dialog.subscribeEvent(this);
			dialog.show();
			break;
		}
	}
	
	private void onPositiveClick() {
		Map<String, Boolean> changedState = getChangedState();
		
		if (changedState.isEmpty()) {
			MLog.i("selected not changed");
			dismiss();
			return;
		}
		
		MLog.i("change state:" + changedState);
		
		List<String> args = new ArrayList<String>();
		for (Map.Entry<String, Boolean> entry : changedState.entrySet()) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("fid", entry.getKey());
			jsonObject.addProperty("op", entry.getValue());
			args.add(jsonObject.toString());
		}
		
		realFavorite(args);
	}
	
	private void realFavorite(List<String> args) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getContext());
		}
		
		mProgressDialog.show("处理中");
		
		FavoriteApis.favorite(mQuestion, args, new ResponseHandler<BaseDTO>() {
			@Override
			public void onSuccess(BaseDTO data) {
				if (data.isSucceeded()) {
					onFavoriteOperateSuccess();
					dismiss();
				}
			}
			
			@Override
			public void onFinish() {
				mProgressDialog.dismiss();
			}
		});
	}
	
	private void onFavoriteOperateSuccess() {
		boolean favoriteState = getNewFavoriteState();
		if (mQuestion.isFavoritedByMe == favoriteState) {
			MLog.i("favorite state not changed, state=" + favoriteState);
			return;
		}
		
		MLog.i("favorite state changed, old state=" + mQuestion.isFavoritedByMe + " new state=" + favoriteState);
		mQuestion.favoriteCount += (favoriteState ? 1 : -1);
		mQuestion.isFavoritedByMe = favoriteState;
		
		if (mFavoriteChangeListener != null) {
			mFavoriteChangeListener.onFavoriteStateChange();
		}
	}
	
	private Map<String, Boolean> getChangedState() {
		ArrayList<MData> datas = mLoader.getArray();
		Map<String, Boolean> changed = new HashMap<String, Boolean>();
		
		for (MData data : datas) {
			MFavoriteFolderSimple folder = (MFavoriteFolderSimple) data;
			if (folder.isFavoritedByMe != mOriginalState.get(folder.id)) {
				changed.put(folder.id, folder.isFavoritedByMe);
			}
		}
		
		return changed;
	}
	
	private boolean getNewFavoriteState() {
		ArrayList<MData> datas = mLoader.getArray();
		for (MData data : datas) {
			MFavoriteFolderSimple folder = (MFavoriteFolderSimple) data;
			if (folder.isFavoritedByMe) {
				return true;
			}
		}
		return false;
	}

	public void onEventFolderCreate(MFavoriteFolderSimple folder) {
		if (folder != null) {
			addOriginalState(folder);
			folder.isFavoritedByMe = true;
			mLoader.addDataToTail(folder);
			mAdapter.notifyDataSetChanged();
			
			scrollToBottom();
		}
	}
	
	private void scrollToBottom() {
		mListView.getRefreshableView().post(new Runnable() {
			@Override
			public void run() {
				ListView listView = mListView.getRefreshableView();
				int position = listView.getHeaderViewsCount() + mLoader.getCount() - 1;
				listView.setSelection(position);
			}
		});
	}
	
	public final void show() {
		super.show();
		mAdapter.loadData();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		mAdapter.refresh();
	}
	
	private void addOriginalState(MFavoriteFolderSimple folder) {
		mOriginalState.put(folder.id, folder.isFavoritedByMe);
	}
	
	private void saveOriginalState(ArrayList<? extends MData> datas) {
		mOriginalState.clear();
		if (datas == null || datas.isEmpty()) {
			return;
		}
		for (MData data : datas) {
			addOriginalState((MFavoriteFolderSimple) data);
		}
	}
	
	private Loader mLoader = new Loader() {
		
		@Override
		protected void loadDataDone(ArrayList<? extends MData> downloaded) {
			mAdapter.notifyDataSetChanged();
			saveOriginalState(downloaded);
		}
		
		@Override
		public void requestRefresh(LoaderListener listener) {
			FavoriteApis.requestFoldersForSelect(mQuestion.id, new Delegate(listener));
		}
		
		@Override
		protected void refreshDone(ArrayList<? extends MData> downloaded) {
			mAdapter.notifyDataSetChanged();
			saveOriginalState(downloaded);
		}
		
		@Override
		public void requestLoadMore(LoaderListener listener, String beforeId) {
			
		}
	};
	
	private ViewProvider mViewProvider = new ViewProvider() {

		@Override
		public View getView(MData data, int position, boolean isFirstItem, boolean isLastItem, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new SelectFavoriteFolderItemView(getContext());
			}
			SelectFavoriteFolderItemView itemView = (SelectFavoriteFolderItemView) convertView;
			itemView.setData((MFavoriteFolderSimple) data);
			return convertView;
		}
	};
	
	private class Delegate extends ArrayDelegate<FoldersForSelect> {

		public Delegate(LoaderListener listener) {
			super(listener);
		}
		
		@Override
		public void onFinish() {
			mListView.onRefreshComplete();
		}

		@Override
		protected ArrayList<? extends MData> getArray(FoldersForSelect data) {
			return data != null && data.isSucceeded() ? data.folders : null;
		}

	}
	
	public static interface OnFavoriteChangeListener {
		void onFavoriteStateChange();
	}

}
