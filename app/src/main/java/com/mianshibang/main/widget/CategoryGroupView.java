package com.mianshibang.main.widget;

import com.mianshibang.main.R;
import com.mianshibang.main.model.MClassify;
import com.mianshibang.main.model.MClassifyGroup;
import com.mianshibang.main.model.MClassifyMapping;
import com.mianshibang.main.utils.ViewUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class CategoryGroupView extends LinearLayout {
	
	public CategoryGroupView(Context context) {
		this(context, null);
	}

	public CategoryGroupView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initStyle();
	}
	
	private void initStyle() {
		setBackgroundColor(getResources().getColor(android.R.color.white));
		setOrientation(VERTICAL);
	}
	
	public void show(MClassifyGroup group) {
		ViewUtils.addLineToLinearLayout(this, 0);
		for (MClassifyMapping mapping : group.mappings) {
			boolean isLastItem = group.mappings.indexOf(mapping) == (group.mappings.size() - 1);
			addClassify(mapping.classify, isLastItem);
		}
		ViewUtils.addLineToLinearLayout(this, 0);
	}
	
	private void addClassify(MClassify classify, boolean isLastItem) {
		CategoryGroupItemView itemView = createItemView(classify);
		int height = getResources().getDimensionPixelSize(R.dimen.category_item_height);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
		addView(itemView, params);
		
		if (!isLastItem) {
			int leftMargin = getResources().getDimensionPixelSize(R.dimen.category_item_line_margin);
			ViewUtils.addLineToLinearLayout(this, leftMargin);
		}
	}
	
	private CategoryGroupItemView createItemView(MClassify classify) {
		CategoryGroupItemView item = new CategoryGroupItemView(getContext());
		item.show(classify);
		return item;
	}
	
}
