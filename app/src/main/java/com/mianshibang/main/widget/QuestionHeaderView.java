package com.mianshibang.main.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.image.MPicasso;
import com.mianshibang.main.model.MClassify;
import com.mianshibang.main.model.MQuestion;
import com.mianshibang.main.utils.UiNavigation;

public class QuestionHeaderView extends LinearLayout implements View.OnClickListener {
	
	private ImageView mCategoryIcon;
	private TextView mCategroyName;
	private TextView mDateline;
	private TextView mFrom;
	
	private MQuestion mQuestion;

	public QuestionHeaderView(Context context) {
		this(context, null);
	}

	public QuestionHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setOrientation(HORIZONTAL);
		LayoutInflater.from(context).inflate(R.layout.question_header, this);
		
		mCategoryIcon = (ImageView) this.findViewById(R.id.category_icon);
		mCategroyName = (TextView) this.findViewById(R.id.category_name);
		mDateline = (TextView) this.findViewById(R.id.dateline);
		mFrom = (TextView) this.findViewById(R.id.from);
		
		mCategoryIcon.setOnClickListener(this);
	}
	
	public void show(MQuestion question) {
		mQuestion = question;
		
		MPicasso.load(question.classify.smallIcon, R.drawable.icon_category_default, mCategoryIcon);
		mCategroyName.setText(mQuestion.classify.name);
		mDateline.setText(mQuestion.getDateline());
		mFrom.setText("来自：" + mQuestion.from);
	}

	@Override
	public void onClick(View v) {
		MClassify c = mQuestion.classify;
		UiNavigation.startQuestionsClassify(getContext(), c.name, c.id);
	}

}
