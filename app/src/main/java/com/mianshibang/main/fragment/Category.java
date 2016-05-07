package com.mianshibang.main.fragment;

import java.util.ArrayList;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.mianshibang.main.R;
import com.mianshibang.main.api.ClassifyApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.MClassifyGroup;
import com.mianshibang.main.model.dto.Classifies;
import com.mianshibang.main.widget.CategoryGroupView;
import com.mianshibang.main.widget.TitleBar;

public class Category extends Base {
	
	private TitleBar mTitleBar;
	
	private ScrollView mCategoryScrollView;
	private LinearLayout mClassifyContainer;

	@Override
	public int getContentLayout() {
		return R.layout.category;
	}

	@Override
	public void findViews(View view) {
		mTitleBar = (TitleBar) view.findViewById(R.id.title_bar);
		mClassifyContainer = (LinearLayout) view.findViewById(R.id.classifies);
		mCategoryScrollView = (ScrollView) view.findViewById(R.id.category_scroll);
	}

	@Override
	public void setListeners() {

	}

	@Override
	public void process() {
		setTitleBarStyle();
		showDefaultClassify();
		requestClassify();
	}
	
	private void showDefaultClassify() {
		ArrayList<MClassifyGroup> groups = ClassifyApis.readDefaultClassify();
		showClassifies(groups);
	}
	
	private void requestClassify() {
		ClassifyApis.requestClassify(new ResponseHandler<Classifies>() {
			@Override
			public void onSuccess(Classifies data) {
				if (getActivity() == null) {
					return;
				}
				if (data.isSucceeded()) {
					showClassifies(data.groups);
				}
			}
		});
	}
	
	private void showClassifies(ArrayList<MClassifyGroup> groups) {
		if (groups == null || groups.isEmpty()) {
			// show empty view
			return;
		}
		
		mClassifyContainer.removeAllViews();
		
		for (MClassifyGroup group : groups) {
			boolean isLastItem = groups.indexOf(group) == (groups.size() - 1);
			addGroupView(group, isLastItem);
		}
	}
	
	private void addGroupView(MClassifyGroup group, boolean isLastItem) {
		CategoryGroupView groupView = new CategoryGroupView(getActivity());
		groupView.show(group);
		
		int margin = getResources().getDimensionPixelSize(R.dimen.category_item_space);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
		params.topMargin = margin;
		params.bottomMargin = isLastItem ? margin : 0;
		
		mClassifyContainer.addView(groupView, params);
	}
	
	private void setTitleBarStyle() {
		mTitleBar.hideLeftButton();
		mTitleBar.setRightButtonIcon(R.drawable.icon_title_bar_search);
		mTitleBar.setTitleText(R.string.category);
	}
	
	@Override
	public void scrollToTop() {
		try {
			mCategoryScrollView.fullScroll(View.FOCUS_UP);
		} catch (Exception e) {
		}
	}

}
