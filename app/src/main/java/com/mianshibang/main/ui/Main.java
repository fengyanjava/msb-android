package com.mianshibang.main.ui;

import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.api.ClassifyApis;
import com.mianshibang.main.api.PlatformApis;
import com.mianshibang.main.api.UserApis;
import com.mianshibang.main.fragment.Category;
import com.mianshibang.main.fragment.FavoriteFolder;
import com.mianshibang.main.fragment.Home;
import com.mianshibang.main.fragment.Me;
import com.mianshibang.main.push.Push;
import com.mianshibang.main.utils.ToastUtils;
import com.mianshibang.main.widget.TabHost;
import com.mianshibang.main.widget.TabHost.OnTabUnchangeListener;

public class Main extends Base implements OnTabUnchangeListener {

	private long mBackPressTime;
	private TabHost mTabHost;

	@Override
	public int getContentLayout() {
		return R.layout.main;
	}

	@Override
	public void findViews() {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
	}

	@Override
	public void setListeners() {

	}

	@Override
	public void process() {
		View tabHome = getTabItemView(Tab.Home);
		View tabCategory = getTabItemView(Tab.Category);
		View tabFavorite = getTabItemView(Tab.Favorite);
		View tabMe = getTabItemView(Tab.Me);

		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

		mTabHost.addTab(mTabHost.newTabSpec(Tab.Home.name()).setIndicator(tabHome), Home.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(Tab.Category.name()).setIndicator(tabCategory), Category.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(Tab.Favorite.name()).setIndicator(tabFavorite), FavoriteFolder.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(Tab.Me.name()).setIndicator(tabMe), Me.class, null);

		mTabHost.setOnTabUnchangeListener(this);

		ClassifyApis.updateClassify();
		PlatformApis.checkUpdate(this, true);
		UserApis.autoLogin();

		Push.init();
		Push.openOrStop();
	}

	@Override
	public void onTabUnchange(int index) {
		Fragment fragment = mTabHost.getFragment(index);
		if (fragment == null) {
			return;
		}
		if (fragment instanceof com.mianshibang.main.fragment.Base) {
			((com.mianshibang.main.fragment.Base) fragment).scrollToTop();
		}
	}

	private View getTabItemView(Tab tab) {
		View view = (View) getLayoutInflater().inflate(R.layout.tab_item, null);
		TextView text = (TextView) view.findViewById(R.id.tab_item_text);
		text.setText(tab.getTextResId());
		text.setCompoundDrawablesWithIntrinsicBounds(0, tab.getIconResId(), 0, 0);
		return view;
	}

	@Override
	public void onBackPressed() {
		long now = SystemClock.elapsedRealtime();
		if (now - mBackPressTime <= 1500) {
			super.onBackPressed();
		} else {
			mBackPressTime = now;
			ToastUtils.show(R.string.back_press_exit);
		}
	}

	private static enum Tab {

		Home, Category, Favorite, Me;

		private static int StringHome = R.string.tab_home;
		private static int StringCategory = R.string.tab_category;
		private static int StringFavorite = R.string.tab_favorite;
		private static int StringMe = R.string.tab_me;

		private static int IconHome = R.drawable.icon_tab_home;
		private static int IconCategory = R.drawable.icon_tab_category;
		private static int IconFavorite = R.drawable.icon_tab_favorite;
		private static int IconMe = R.drawable.icon_tab_me;

		private static int[] String_Ids = new int[] { StringHome, StringCategory, StringFavorite, StringMe };
		private static int[] Icon_Ids = new int[] { IconHome, IconCategory, IconFavorite, IconMe };

		public int getTextResId() {
			return String_Ids[ordinal()];
		}

		public int getIconResId() {
			return Icon_Ids[ordinal()];
		}

	}

}
