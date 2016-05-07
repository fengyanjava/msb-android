package com.mianshibang.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.mianshibang.main.adapter.RequestAdapter.ViewProvider;
import com.mianshibang.main.model.MData;
import com.mianshibang.main.model.MQuestion;
import com.mianshibang.main.widget.QuestionItemView;

public class QuestionViewProvider extends ViewProvider {
	
	private Context mContext;
	
	public QuestionViewProvider(Context context) {
		mContext = context;
	}
	
	@Override
	public View getView(MData data, int position, boolean isFirstItem, boolean isLastItem, View convertView, ViewGroup parent) {
		if (convertView == null || !(convertView instanceof QuestionItemView)) {
			convertView = new QuestionItemView(mContext);
		}
		QuestionItemView itemView = (QuestionItemView) convertView;
		itemView.setData((MQuestion) data, isLastItem);
		return convertView;
	}
	
}
