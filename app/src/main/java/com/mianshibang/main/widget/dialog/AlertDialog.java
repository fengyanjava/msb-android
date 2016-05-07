package com.mianshibang.main.widget.dialog;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.utils.UIUtils;

public class AlertDialog extends BaseDialog implements OnItemClickListener {
	
	private TextView mTitleEx;
	private View mLine;
	private TextView mMessage;
	private ListView mListView;
	private OnClickListener mOnClickListener;

	public AlertDialog(Context context) {
		super(context);
		
		hideTitle();
		setContentView(R.layout.dialog_alert);
		
		mTitleEx = (TextView) findViewById(R.id.dialog_title_ex);
		mLine = (View) findViewById(R.id.dialog_line);
		mMessage = (TextView) findViewById(R.id.dialog_message);
		mListView = (ListView) findViewById(R.id.dialog_list);
		
		mListView.setOnItemClickListener(this);
		
		hideView(mTitleEx);
		hideView(mLine);
		hideView(mMessage);
		hideView(mListView);
	}
	
	public AlertDialog setTitleEx(String title) {
		mTitleEx.setText(title);
		showView(mTitleEx);
		showView(mLine);
		return this;
	}
	
	public AlertDialog setTitleEx(int resId) {
		String title = UIUtils.getString(resId);
		return setTitleEx(title);
	}
	
	public AlertDialog setMessage(String message) {
		mMessage.setText(message);
		showView(mMessage);
		return this;
	}
	
	public AlertDialog setMessage(int resId) {
		String message = UIUtils.getString(resId);
		return setMessage(message);
	}
	
	public AlertDialog setItems(int itemsId, final OnClickListener listener) {
		CharSequence[] items = getContext().getResources().getTextArray(itemsId);
		return setItems(items, listener);
	}
	
	public AlertDialog setItems(CharSequence[] items, final OnClickListener listener) {
		showView(mListView);
		mOnClickListener = listener;
		mListView.setAdapter(new DialogAdapter(items));
		return this;
	}
	
	private static void hideView(View view) {
		view.setVisibility(View.GONE);
	}
	
	private static void showView(View view) {
		view.setVisibility(View.VISIBLE);
	}
	
	private class DialogAdapter extends BaseAdapter {
		
		private CharSequence[] items;
		
		public DialogAdapter(CharSequence[] items) {
			this.items = items;
		}

		@Override
		public int getCount() {
			return items == null ? 0 : items.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new ItemView(getContext());
			}
			ItemView itemView = (ItemView) convertView;
			itemView.setText(items[position]);
			return itemView;
		}
		
	}
	
	private static class ItemView extends TextView {

		public ItemView(Context context) {
			super(context);
			int size = UIUtils.getDimens(R.dimen.dialog_alert_text_size);
			setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
			setTextColor(0xff000000);
			int paddingTop = UIUtils.dip2px(20);
			int paddingLeft = UIUtils.dip2px(10);
			setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		dismiss();
		if (mOnClickListener != null) {
			mOnClickListener.onClick(this, position);
		}
	}
}
