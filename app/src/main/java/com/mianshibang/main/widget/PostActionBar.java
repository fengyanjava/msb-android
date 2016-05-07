package com.mianshibang.main.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.model.MQuestionAudit;
import com.mianshibang.main.model.MQuestionAudit.State;
import com.mianshibang.main.utils.UiNavigation;
import com.mianshibang.main.widget.dialog.AlertDialog;

public class PostActionBar extends FrameLayout implements View.OnClickListener {

	private MQuestionAudit mAudit;

	private TextView mStateText;
	private TextView mActionText;
	private View mLine;

	public PostActionBar(Context context) {
		this(context, null);
	}

	public PostActionBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PostActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		int resource = getLayoutResIdByStyle();
		LayoutInflater.from(context).inflate(resource, this);

		findViews();
	}

	private void findViews() {
		mStateText = (TextView) findViewById(R.id.post_state_text);
		mActionText = (TextView) findViewById(R.id.post_action_text);
		mLine = findViewById(R.id.post_line);

		mActionText.setOnClickListener(this);
	}

	public void setData(MQuestionAudit audit) {
		mAudit = audit;
		refreshView();
	}

	private void refreshView() {
		setVisibility(mAudit != null ? View.VISIBLE : View.GONE);
		
		if (mAudit == null) {
			return;
		}

		mStateText.setText(mAudit.state.text);
		mStateText.setSelected(mAudit.state == State.Success);
		mStateText.setEnabled(mAudit.state == State.Success || mAudit.state == State.Waiting);

		boolean showAction = mAudit.state.hasAction();
		mLine.setVisibility(showAction ? View.VISIBLE : View.GONE);
		mActionText.setVisibility(showAction ? View.VISIBLE : View.GONE);
		mActionText.setText(mAudit.state.action);
	}

	@Override
	public void onClick(View v) {
		if (mAudit == null) {
			return;
		}

		if (mAudit.state == State.Success) {
			UiNavigation.startDetail(getContext(), String.valueOf(mAudit.questionId));
		} else if (mAudit.state == State.Fail) {
			AlertDialog dialog = new AlertDialog(getContext());
			dialog.setTitleEx(R.string.post_question_fail_cause);
			dialog.setMessage(mAudit.cause);
			dialog.setSingleButton(true, R.string.ok, null);
			dialog.hideButtons().showSingleButton();
			dialog.show();
		}
	}

	protected ActionBarStyle getStyle() {
		return ActionBarStyle.getDefault();
	}

	private int getLayoutResIdByStyle() {
		ActionBarStyle style = getStyle();
		return style.getLayoutResId();
	}

	protected static enum ActionBarStyle {
		Item, Detail;

		public int getLayoutResId() {
			if (this == Item) {
				return R.layout.post_action_bar_item;
			}
			if (this == Detail) {
				return R.layout.post_action_bar_detail;
			}
			return 0;
		}

		public static ActionBarStyle getDefault() {
			return Item;
		}
	}

}
