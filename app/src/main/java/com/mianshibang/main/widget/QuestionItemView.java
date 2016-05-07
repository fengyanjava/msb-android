package com.mianshibang.main.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.model.MQuestion;
import com.mianshibang.main.utils.UiNavigation;
import com.mianshibang.main.utils.ViewUtils;

public class QuestionItemView extends LinearLayout implements View.OnClickListener {

	private QuestionHeaderView mHeader;
	private TextView mDescription;
	private PostActionBar mPostActionBar;
	private QuestionActionBar mActionBar;
	
	private MQuestion mQuestion;
	private boolean mIsLastItem;
	
	private View mItem;
	private View mBottom;

	public QuestionItemView(Context context) {
		this(context, null);
	}

	public QuestionItemView(Context context, AttributeSet attrs) {
		super(context, attrs);

		LayoutInflater.from(context).inflate(R.layout.question_item, this, true);

		setStyle();
		findViews();
	}

	private void findViews() {
		mHeader = (QuestionHeaderView) this.findViewById(R.id.question_header);
		mDescription = (TextView) this.findViewById(R.id.description);
		mActionBar = (QuestionActionBar) this.findViewById(R.id.question_action_bar);
		mPostActionBar = (PostActionBar) this.findViewById(R.id.post_action_bar);
		
		mItem = this.findViewById(R.id.item);
		mBottom = this.findViewById(R.id.bottom);
		
		mItem.setOnClickListener(this);
	}

	private void setStyle() {
		setOrientation(VERTICAL);
		setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
	}
	
	public void setData(MQuestion question, boolean isLastItem) {
		mQuestion = question;
		mIsLastItem = isLastItem;
		refreshView();
	}

	private void refreshView() {
		if (mQuestion == null) {
			return;
		}
		
		mHeader.show(mQuestion);
		ViewUtils.setTextViewSizeWithConfig(mDescription);
		mDescription.setText(mQuestion.description);

		mActionBar.setData(mQuestion);
		mActionBar.setVisibility(mQuestion.isPostByUser() ? View.GONE : View.VISIBLE);
		
		mPostActionBar.setData(mQuestion.audit);
		mPostActionBar.setVisibility(mQuestion.isPostByUser() ? View.VISIBLE : View.GONE);
		
		mBottom.setVisibility(mIsLastItem ? View.VISIBLE : View.GONE);
	}
	
	@Override
	public void onClick(View v) {
		if (mQuestion == null) {
			return;
		}
		switch (v.getId()) {
		case R.id.item:
			if (mQuestion.isPostByUser()) {
				UiNavigation.startPostDetail(getContext(), mQuestion.id);
			} else {
				UiNavigation.startDetail(getContext(), mQuestion.id);
			}
			break;

		default:
			break;
		}
	}
	
	

}
