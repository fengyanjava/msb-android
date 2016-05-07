package com.mianshibang.main.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mianshibang.main.R;
import com.mianshibang.main.utils.EventBusUtils;

public class SearchHistoryHeaderView extends LinearLayout implements View.OnClickListener {
	
	private Button mClearHistory;
	
	private Object mSubscriber;
	private static final String mMethodName = "onEventClearHistory";

	public SearchHistoryHeaderView(Context context) {
		super(context);
		setOrientation(HORIZONTAL);
		setBackgroundColor(0xFFFFFFFF);
		LayoutInflater.from(context).inflate(R.layout.search_history_header, this);
		
		mClearHistory = (Button) findViewById(R.id.clear_history);
		mClearHistory.setOnClickListener(this);
	}
	
	public void subscribeClearEvent(Object subscriber) {
		mSubscriber = subscriber;
	}

	@Override
	public void onClick(View v) {
		EventBusUtils.postEvent(mSubscriber, mMethodName);
	}

}
