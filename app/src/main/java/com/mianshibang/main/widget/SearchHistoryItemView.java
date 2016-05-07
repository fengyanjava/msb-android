package com.mianshibang.main.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.model.SearchHistory;
import com.mianshibang.main.utils.EventBusUtils;

public class SearchHistoryItemView extends LinearLayout {
	
	private TextView mText;
	
	private SearchHistory mHistory;
	
	private Object mSubscriber;
	private static final String mMethodName = "onEventSearch";

	public SearchHistoryItemView(Context context) {
		super(context);
		
		setOrientation(VERTICAL);
		setBackgroundResource(R.drawable.bg_white_item);
		LayoutInflater.from(context).inflate(R.layout.search_history_item, this);

		mText = (TextView) findViewById(R.id.text);
		
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EventBusUtils.postEvent(mSubscriber, mMethodName, mHistory.keyword);
			}
		});
		
	}
	
	public void setData(SearchHistory history) {
		mHistory = history;
		refreshView();
	}
	
	private void refreshView() {
		mText.setText(mHistory.keyword);
	}
	
	public void subscribe(Object subscriber) {
		mSubscriber = subscriber;
	}
	
	

}
