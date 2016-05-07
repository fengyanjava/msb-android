package com.mianshibang.main.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.api.CurrentUserApis;
import com.mianshibang.main.api.LikeApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.MQuestion;
import com.mianshibang.main.model.dto.BaseDTO;
import com.mianshibang.main.utils.EventBusUtils;
import com.mianshibang.main.utils.UiNavigation;
import com.mianshibang.main.widget.dialog.SelectFavoriteFolderDialog;
import com.mianshibang.main.widget.dialog.SelectFavoriteFolderDialog.OnFavoriteChangeListener;
import com.mianshibang.main.widget.dialog.ShareDialog;

public class QuestionActionBar extends FrameLayout implements View.OnClickListener, OnFavoriteChangeListener {

	private static final int REQ_LIKE = 0;
	private static final int REQ_FAVORITE = 1;

	private MQuestion mQuestion;

	private View mLike;
	private View mFavorite;
	private View mMore;

	private TextView mLikeText;
	private TextView mFavoriteText;

	public QuestionActionBar(Context context) {
		this(context, null);
	}

	public QuestionActionBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public QuestionActionBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		int resource = getLayoutResIdByStyle();
		LayoutInflater.from(context).inflate(resource, this);

		findViews();
	}

	private void findViews() {
		mLike = this.findViewById(R.id.like);
		mFavorite = this.findViewById(R.id.favorite);
		mMore = this.findViewById(R.id.more);

		mLikeText = (TextView) this.findViewById(R.id.like_text);
		mFavoriteText = (TextView) this.findViewById(R.id.favorite_text);

		mLike.setOnClickListener(this);
		mFavorite.setOnClickListener(this);
		mMore.setOnClickListener(this);
	}

	public void setData(MQuestion question) {
		mQuestion = question;
		refreshView();
	}

	private void refreshView() {
		if (mQuestion == null) {
			return;
		}
		
		setVisibility(mQuestion.isPostByUser() ? View.GONE : View.VISIBLE);
		
		if (!mQuestion.isPostByUser()) {
			updateLikeAndFavoriteCount();
		}
	}

	private void updateLikeAndFavoriteCount() {
		long voteCount = mQuestion.voteCount;
		String likeText = voteCount > 0 ? String.valueOf(voteCount) : getResources().getString(R.string.like);
		mLikeText.setText(likeText);

		long favoriteCount = mQuestion.favoriteCount;
		String favoriteText = favoriteCount > 0 ? String.valueOf(favoriteCount) : getResources().getString(R.string.favorite);
		mFavoriteText.setText(favoriteText);

		mLike.setSelected(mQuestion.isVotedByMe());
		mFavorite.setSelected(mQuestion.isFavoritedByMe());
	}

	@Override
	public void onClick(View v) {
		if (mQuestion == null) {
			return;
		}
		switch (v.getId()) {
		case R.id.like:
			onLike();
			break;

		case R.id.favorite:
			onFavorite();
			break;

		case R.id.more:
			onShare();
			break;

		default:
			break;
		}
	}

	private void onShare() {
		ShareDialog dialog = new ShareDialog(getContext(), mQuestion);
		dialog.show();
	}

	public void onEventLogin(Integer requestCode) {
		if (requestCode == null) {
			return;
		}
		if (REQ_LIKE == requestCode.intValue()) {
			onLike();
		}
		if (REQ_FAVORITE == requestCode.intValue()) {
			onFavorite();
		}
	}

	private void onFavorite() {
		if (mQuestion == null) {
			return;
		}

		if (CurrentUserApis.isUnLogin()) {
			EventBusUtils.subscribeLoginEvent(this, REQ_FAVORITE);
			UiNavigation.startLoginRegister(getContext());
			return;
		}

		SelectFavoriteFolderDialog dialog = new SelectFavoriteFolderDialog(getContext(), mQuestion);
		dialog.setOnFavoriteChangedListener(this);
		dialog.show();
	}

	private void onLike() {
		if (mQuestion == null) {
			return;
		}

		if (CurrentUserApis.isUnLogin()) {
			EventBusUtils.subscribeLoginEvent(this, REQ_LIKE);
			UiNavigation.startLoginRegister(getContext());
			return;
		}

		if (mQuestion.isVotedByMe) {
			doDislike();
		} else {
			doLike();
		}
	}

	private void doLike() {
		LikeApis.likeQuestion(mQuestion, new ResponseHandler<BaseDTO>() {
			@Override
			public void onSuccess(BaseDTO data) {
				updateLikeAndFavoriteCount();
			}
		});
	}

	private void doDislike() {
		LikeApis.dislikeQuestion(mQuestion, new ResponseHandler<BaseDTO>() {
			@Override
			public void onSuccess(BaseDTO data) {
				updateLikeAndFavoriteCount();
			}
		});
	}

	@Override
	public void onFavoriteStateChange() {
		refreshView();
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
				return R.layout.question_action_bar_item;
			}
			if (this == Detail) {
				return R.layout.question_action_bar_detail;
			}
			return 0;
		}

		public static ActionBarStyle getDefault() {
			return Item;
		}
	}

}
