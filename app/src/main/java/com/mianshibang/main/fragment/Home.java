package com.mianshibang.main.fragment;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.view.View;

import com.mianshibang.main.R;
import com.mianshibang.main.model.HomeTab;
import com.mianshibang.main.utils.UIUtils;
import com.mianshibang.main.widget.TitleBar;
import com.mianshibang.main.widget.TitleTabView;

public class Home extends Base implements View.OnClickListener {

	private TitleBar mTitleBar;
	private TitleTabView mTabView;
	private ViewPager mViewPager;
	private HomePagerAdapter mAdapter;

	@Override
	public int getContentLayout() {
		return R.layout.home;
	}

	@Override
	public void findViews(View view) {
		mTitleBar = (TitleBar) view.findViewById(R.id.title_bar);
		mViewPager = (ViewPager) view.findViewById(R.id.view_pager);

		mTabView = new TitleTabView(getActivity());
		mTabView.setLeftButton(R.string.home_tab_recommendation, this);
		mTabView.setRightButton(R.string.home_tab_newest, this);
	}

	@Override
	public void setListeners() {
	}

	@Override
	public void process() {
		initTitleBar();
		initViewPager();
	}

	private void initViewPager() {
		mViewPager.setPageMargin(UIUtils.dip2px(10));
		mViewPager.setPageMarginDrawable(new ColorDrawable(0x00FFFFFF));

		mAdapter = new HomePagerAdapter(getChildFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mTabView.switchButton(position == 0);
			}
		});
	}

	private void initTitleBar() {
		mTitleBar.hideLeftButton();
		mTitleBar.setRightButtonIcon(R.drawable.icon_title_bar_search);
		mTitleBar.addViewToTitleContainer(mTabView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case TitleTabView.LEFT_ID:
			mTabView.switchToLeft();
			mViewPager.setCurrentItem(HomeTab.Recommendation.ordinal(), false);
			break;

		case TitleTabView.RIGHT_ID:
			mTabView.switchToRight();
			mViewPager.setCurrentItem(HomeTab.Newest.ordinal(), false);
			break;

		default:
			break;
		}
	}

	@Override
	public void scrollToTop() {
		mAdapter.getItem(mViewPager.getCurrentItem()).scrollToTop();
	}

	private static class HomePagerAdapter extends FragmentPagerAdapter {

		private Base mRecommendation;
		private Base mNewest;

		public HomePagerAdapter(FragmentManager fm) {
			super(fm);
			mNewest = new HomeNewest();
			mRecommendation = new HomeRecommendation();
		}

		@Override
		public Base getItem(int position) {
			return position == 0 ? mRecommendation : mNewest;
		}

		@Override
		public int getCount() {
			return HomeTab.values().length;
		}

	}

}
