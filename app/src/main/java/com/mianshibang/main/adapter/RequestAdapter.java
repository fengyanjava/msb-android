package com.mianshibang.main.adapter;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.MData;
import com.mianshibang.main.model.dto.BaseDTO;

public class RequestAdapter extends BaseAdapter {

	protected Loader mLoader;
	protected ViewProvider mViewProvider;

	public RequestAdapter(Loader loader) {
		this(loader, null);
	}

	public RequestAdapter(Loader loader, ViewProvider viewProvider) {
		mLoader = loader;
		mViewProvider = viewProvider;
	}

	public void setViewProvider(ViewProvider viewProvider) {
		mViewProvider = viewProvider;
	}

	public Loader getLoader() {
		return mLoader;
	}

	public void setLoader(Loader loader) {
		mLoader = loader;
	}

	public static enum ArrayLoadStyle {
		REFRESH, LOAD_MORE, LOAD_DATA
	}

	public static abstract class ArrayDelegate<T extends BaseDTO> extends ResponseHandler<T> {
		final private ArrayLoadStyle mStyle;
		final private LoaderListener mListener;
		
		public ArrayDelegate(LoaderListener listener) {
			this(null, listener);
		}

		public ArrayDelegate(ArrayLoadStyle style, LoaderListener listener) {
			mStyle = style;
			mListener = listener;
		}

		protected boolean isRefresh() {
			return (mStyle == ArrayLoadStyle.REFRESH);
		}

		protected boolean isLoadMore() {
			return (mStyle == ArrayLoadStyle.LOAD_MORE);
		}

		protected boolean isLoadData() {
			return (mStyle == ArrayLoadStyle.LOAD_DATA);
		}

		protected abstract ArrayList<? extends MData> getArray(T data);

		@Override
		public void onDataParsed(T result) {
			if (result.isSucceeded()) {
			}
		}

		@Override
		public final void onFailure(Exception error) {
			mListener.onFailure(error);
		};

		@Override
		public final void onSuccess(T data) {
			mListener.onLoadComplete(getArray(data));
		}
		
		@Override
		public void onFinish() {
			mListener.onFinish();
		}
	}

	public static interface LoaderListener {
		public void onLoadComplete(ArrayList<? extends MData> downloaded);

		public void onFailure(Throwable error);
		
		public void onFinish();
	}

	public static abstract class ViewProvider {
		
		public int getViewTypeCount() {
			return 1;
		};

		public int getItemViewType(int position) {
			return 0;
		};
		
		public abstract View getView(MData data, int position, boolean isFirstItem, boolean isLastItem, View convertView, ViewGroup parent);
	}

	public static abstract class Loader {
		protected ArrayList<MData> mData;
		private boolean mIsInProgress;
		private boolean mNoMoreData;
		private int mPage;

		public Loader() {
			mData = new ArrayList<MData>();
		}
		
		public boolean isEmpty() {
			return mData.isEmpty();
		}

		protected String getDataId(MData data) {
			return data != null ? data.id : null;
		}

		private void appendDataForLoadMore(ArrayList<? extends MData> downloaded) {
			if (downloaded != null) {
				mData.addAll(downloaded);
			}
		}
		
		public void addDataToHead(MData data) {
			mData.add(0, data);
		}
		
		public void addDataToTail(MData data) {
			mData.add(data);
		}
		
		public int getCount() {
			return mData.size();
		}
		
		public int getPageIndex() {
			return mPage;
		}
		
		private void resetPageIndex() {
			mPage = 0;
		}
		
		private void increasePage() {
			mPage++;
		}

		public boolean isInProgress() {
			return mIsInProgress;
		}

		private void setInProgress(boolean isInProgress) {
			mIsInProgress = isInProgress;
		}
		
		private boolean isNoMoreData() {
			return mNoMoreData;
		}
		
		private void setNoMoreData(boolean isNoMoreData) {
			mNoMoreData = isNoMoreData;
		}
		
		private void increasePage(ArrayList<? extends MData> downloaded) {
			if (downloaded != null && !downloaded.isEmpty()) {
				increasePage();
			}
		}

		public final LoaderListener createListenerForRefresh() {
			LoaderListener listener = new LoaderListener() {
				@Override
				public void onLoadComplete(ArrayList<? extends MData> downloaded) {
					increasePage(downloaded);
					replace(downloaded);
					refreshDone(downloaded);
					setInProgress(false);
				}

				@Override
				public void onFailure(Throwable error) {
					setInProgress(false);
					if (shouldCallDoneOnFailure()) {
						refreshDone(null);
					}
					handleFailure(error);
				}
				
				@Override
				public void onFinish() {
					handleFinish();
				}
			};

			return listener;
		}

		public final LoaderListener createListenerForLoadMore() {
			LoaderListener listener = new LoaderListener() {
				@Override
				public void onLoadComplete(ArrayList<? extends MData> downloaded) {
					increasePage(downloaded);
					appendDataForLoadMore(downloaded);
					loadMoreDone(downloaded);
					setNoMoreData(downloaded == null || downloaded.isEmpty());
					setInProgress(false);
				}

				@Override
				public void onFailure(Throwable error) {
					setInProgress(false);
					if (shouldCallDoneOnFailure()) {
						loadMoreDone(null);
					}
					handleFailure(error);
				}
				
				@Override
				public void onFinish() {
					handleFinish();
				}
			};
			return listener;
		}

		protected void replace(ArrayList<? extends MData> downloaded) {
			mData.clear();
			if (downloaded != null) {
				mData.addAll(downloaded);
			}
		}

		public final LoaderListener createListenerForLoadData() {

			LoaderListener listener = new LoaderListener() {
				@Override
				public void onLoadComplete(ArrayList<? extends MData> downloaded) {
					increasePage(downloaded);
					replace(downloaded);
					loadDataDone(downloaded);
					setInProgress(false);
				}

				@Override
				public void onFailure(Throwable error) {
					setInProgress(false);
					if (shouldCallDoneOnFailure()) {
						loadDataDone(null);
					}
					handleFailure(error);
				}
				
				@Override
				public void onFinish() {
					handleFinish();
				}
			};
			return listener;
		}

		public String queryBeforeId() {
			if (mData == null || mData.isEmpty()) {
				return null;
			}

			MData data = mData.get(mData.size() - 1);
			return getDataId(data);
		}

		protected boolean shouldCallDoneOnFailure() {
			return false;
		}

		protected void handleFailure(Throwable e) {
		}
		
		protected void handleFinish() {
		}

		protected void beforeRefresh() {
		}

		protected void beforeLoadData() {
		}

		protected void beforeLoadMore() {
		}

		protected void inProgressCancel() {
		}

		protected void refreshDone(ArrayList<? extends MData> downloaded) {
		}

		protected void loadMoreDone(ArrayList<? extends MData> downloaded) {
		}

		protected void loadDataDone(ArrayList<? extends MData> downloaded) {
		}

		public abstract void requestRefresh(final LoaderListener listener);

		public abstract void requestLoadMore(final LoaderListener listener, String beforeId);

		public void requestLoadData(LoaderListener listener) {
			requestRefresh(listener);
		}

		public ArrayList<MData> getArray() {
			return mData;
		}

		public void setData(ArrayList<? extends MData> data) {
			replace(data);
		}
	}

	public void addData(int pos, MData data) {
		mLoader.getArray().add(pos, data);
	}

	public void addData(MData data) {
		mLoader.getArray().add(data);
	}

	public void addDataToHead(MData data) {
		mLoader.getArray().add(0, data);
	}

	public void replace(ArrayList<? extends MData> downloaded) {
		mLoader.replace(downloaded);
	}

	public void clear() {
		mLoader.getArray().clear();
	}

	@Override
	public int getCount() {
		return mLoader.getArray().size();
	}

	@Override
	public Object getItem(int position) {
		if ((position < 0) || (position >= mLoader.getArray().size())) {
			return null;
		}
		return mLoader.getArray().get(position);
	}

	public void removeItem(Object object) {
		mLoader.getArray().remove(object);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return (mViewProvider == null) ? super.getViewTypeCount() : mViewProvider.getViewTypeCount();
	}

	@Override
	public int getItemViewType(int position) {
		return (mViewProvider == null) ? super.getItemViewType(position) : mViewProvider.getItemViewType(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		boolean isFirstItem = position == 0;
		boolean isLastItem = position == getCount() - 1;
		return (mViewProvider == null) ? null : mViewProvider.getView((MData) getItem(position), position, isFirstItem, isLastItem, convertView, parent);
	}

	public final void loadMore() {
		if (mLoader.isInProgress()) {
			return;
		}
		
		if (mLoader.isNoMoreData()) {
			return;
		}
		
		mLoader.setInProgress(true);
		mLoader.beforeLoadMore();
		LoaderListener listener = mLoader.createListenerForLoadMore();
		mLoader.requestLoadMore(listener, mLoader.queryBeforeId());
	}

	public final void refresh() {
		if (mLoader.isInProgress()) {
			return;
		}
		mLoader.setInProgress(true);
		mLoader.setNoMoreData(false);
		mLoader.resetPageIndex();
		mLoader.beforeRefresh();

		LoaderListener listener = mLoader.createListenerForRefresh();
		mLoader.requestRefresh(listener);
	}

	public final void loadData() {
		if (mLoader.isInProgress()) {
			mLoader.inProgressCancel();
			return;
		}
		mLoader.setInProgress(true);
		mLoader.setNoMoreData(false);
		mLoader.resetPageIndex();
		mLoader.beforeLoadData();
		LoaderListener listener = mLoader.createListenerForLoadData();
		mLoader.requestLoadData(listener);
	}
}
