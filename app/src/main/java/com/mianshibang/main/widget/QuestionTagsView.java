package com.mianshibang.main.widget;

import java.util.ArrayList;
import java.util.List;

import com.mianshibang.main.R;
import com.mianshibang.main.utils.UiNavigation;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

public class QuestionTagsView extends FlowLayout implements View.OnClickListener {
	
	public QuestionTagsView(Context context) {
		this(context, null);
	}
	
	public QuestionTagsView(Context context, AttributeSet attributeSet) {
		this(context, attributeSet, 0);
	}

	public QuestionTagsView(Context context, AttributeSet attributeSet, int defStyle) {
		super(context, attributeSet, defStyle);
		
		ImageView tagIcon = new ImageView(context);
		tagIcon.setImageResource(R.drawable.icon_tags);
		addView(tagIcon);
	}
	
	public void showTags(String tags) {
		List<String> tagList = convertToTagList(tags);
		if (tagList.isEmpty()) {
			setVisibility(GONE);
			return;
		}
		
		setVisibility(VISIBLE);
		if (getChildCount() > 1) {
			removeViews(1, getChildCount() - 1);
		}
		
		addTags(tagList);
	}
	
	private void addTags(List<String> tagList) {
		for (String tagName : tagList) {
			TextView tag = generateTagView(tagName);
			addView(tag);
		}
	}
	
	private TextView generateTagView(String tagName) {
		TextView tag = new TextView(getContext());
		tag.setTag(tagName);
		tag.setOnClickListener(this);
		tag.setGravity(Gravity.CENTER);
		tag.setSingleLine();
		tag.setEllipsize(TextUtils.TruncateAt.END);
		tag.setText(tagName);
		tag.setTextColor(getResources().getColor(R.color.tag_text_color));
		int textSizePx = getResources().getDimensionPixelSize(R.dimen.tag_text_size);
		tag.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx);
		tag.setBackgroundResource(R.drawable.bg_tag);
		int padding = getResources().getDimensionPixelSize(R.dimen.tag_padding);
		tag.setPadding(padding, 0, padding, 0);
		return tag;
	}
	
	private List<String> convertToTagList(String tags) {
		List<String> tagList = new ArrayList<String>();
		if (TextUtils.isEmpty(tags)) {
			return tagList;
		}
		
		String[] tagArray = tags.split(",+");
		if (tagArray != null && tagArray.length > 0) {
			for (String tag : tagArray) {
				tag = tag.trim();
				if (!TextUtils.isEmpty(tag)) {
					tagList.add(tag);
				}
			}
		}
		
		return tagList;
	}

	@Override
	public void onClick(View v) {
		String tag = (String) v.getTag();
		UiNavigation.startSearch(getContext(), tag);
	}

}
