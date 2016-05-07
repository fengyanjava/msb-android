package com.mianshibang.main.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.api.CurrentUserApis;
import com.mianshibang.main.model.MUser;
import com.mianshibang.main.utils.EventBusUtils;
import com.mianshibang.main.utils.UiNavigation;
import com.mianshibang.main.widget.TitleBar;

public class Me extends Base implements OnClickListener {

	private static final int REQ_PROFILE = 0;
	private static final int REQ_LIKEED = 1;
	private static final int REQ_POSTED = 2;

	private TitleBar mTitleBar;

	private MUser mUser;
	private TextView mNickname;
	private TextView mScore;

	private View mProfile;
	private View mLiked;
	private View mPosted;
	private View mHistory;
	private View mPost;
	private View mSettings;

	@Override
	public int getContentLayout() {
		return R.layout.me;
	}

	@Override
	public void findViews(View view) {
		mTitleBar = (TitleBar) view.findViewById(R.id.title_bar);

		mNickname = (TextView) view.findViewById(R.id.nickname);
		mScore = (TextView) view.findViewById(R.id.score);

		mProfile = view.findViewById(R.id.me_profile);
		mLiked = view.findViewById(R.id.me_liked);
		mPosted = view.findViewById(R.id.me_posted);
		mHistory = view.findViewById(R.id.me_history);
		mPost = view.findViewById(R.id.me_post);
		mSettings = view.findViewById(R.id.me_settings);
	}

	@Override
	public void setListeners() {
		mProfile.setOnClickListener(this);
		mLiked.setOnClickListener(this);
		mPosted.setOnClickListener(this);
		mHistory.setOnClickListener(this);
		mPost.setOnClickListener(this);
		mSettings.setOnClickListener(this);
	}

	@Override
	public void process() {
		setTitleBarStyle();
	}

	private void setTitleBarStyle() {
		mTitleBar.hideLeftButton();
		mTitleBar.hideRightButton();
		mTitleBar.setTitleText(R.string.me);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.me_profile:
			if (CurrentUserApis.isUnLogin()) {
				UiNavigation.startLoginRegister(getActivity());
			} else {
				UiNavigation.startUserInfo(getActivity());
			}
			break;

		case R.id.me_liked:
			if (CurrentUserApis.isUnLogin()) {
				EventBusUtils.subscribeLoginEvent(this, REQ_LIKEED);
				UiNavigation.startLoginRegister(getActivity());
			} else {
				UiNavigation.startQuestionsLiked(getActivity());
			}
			break;

		case R.id.me_posted:
			if (CurrentUserApis.isUnLogin()) {
				EventBusUtils.subscribeLoginEvent(this, REQ_POSTED);
				UiNavigation.startLoginRegister(getActivity());
			} else {
				UiNavigation.startQuestionsPosted(getActivity());
			}
			break;

		case R.id.me_history:
			UiNavigation.startBrowseHistory(getActivity());
			break;
			
		case R.id.me_post:
			UiNavigation.startPost(getActivity());
			break;

		case R.id.me_settings:
			UiNavigation.startSettings(getActivity());
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		refreshView();
	}

	private void refreshView() {
		mUser = CurrentUserApis.get();

		refreshUserInfo();
	}

	private void refreshUserInfo() {
		String nickname = mUser == null ? getString(R.string.me_nickname_default) : mUser.nickname;
		mNickname.setText(nickname);

		String scoreUnlogin = getString(R.string.me_score_unlogin);
		int score = mUser == null ? 0 : mUser.score;
		String scoreText = score > 0 ? getString(R.string.me_score, score) : getString(R.string.me_score_default);
		mScore.setText(mUser == null ? scoreUnlogin : scoreText);
	}

	public void onEventLogin(Integer requestCode) {
		if (requestCode == null) {
			return;
		}
		switch (requestCode.intValue()) {
		case REQ_PROFILE:
			UiNavigation.startUserInfo(getActivity());
			break;
			
		case REQ_LIKEED:
			UiNavigation.startQuestionsLiked(getActivity());
			break;
			
		case REQ_POSTED:
			UiNavigation.startQuestionsPosted(getActivity());
			break;

		default:
			break;
		}
	}
}
