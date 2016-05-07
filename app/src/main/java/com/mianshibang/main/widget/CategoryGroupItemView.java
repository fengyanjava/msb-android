package com.mianshibang.main.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.image.MPicasso;
import com.mianshibang.main.model.MClassify;
import com.mianshibang.main.utils.MLog;
import com.mianshibang.main.utils.UiNavigation;

public class CategoryGroupItemView extends LinearLayout implements View.OnClickListener {

	private ImageView mClassifyIcon;
	private TextView mClassifyName;

	private MClassify mClassify;

	public CategoryGroupItemView(Context context) {
		this(context, null);
	}

	public CategoryGroupItemView(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater.from(context).inflate(R.layout.category_group_item, this, true);

		initStyle();
		process();
	}

	private void process() {
		mClassifyIcon = (ImageView) findViewById(R.id.classify_icon);
		mClassifyName = (TextView) findViewById(R.id.classify_name);

		this.setOnClickListener(this);
	}

	private void initStyle() {
		setOrientation(HORIZONTAL);
		setBackgroundResource(R.drawable.bg_common_item);
		int padding = getResources().getDimensionPixelSize(R.dimen.category_item_padding);
		setPadding(padding, 0, padding, 0);
		setGravity(Gravity.CENTER_VERTICAL);
	}

	public void show(MClassify classify) {
		mClassify = classify;
		mClassifyName.setText(classify.name);
		MPicasso.load(classify.bigIcon, R.drawable.icon_category_default, mClassifyIcon);
	}

	@Override
	public void onClick(View v) {
		MLog.i("classify is clicked. id=" + mClassify.id + ", name=" + mClassify.name);
		UiNavigation.startQuestionsClassify(getContext(), mClassify.name, mClassify.id);
	}

}
