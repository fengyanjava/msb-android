package com.mianshibang.main.ui;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mianshibang.main.R;
import com.mianshibang.main.fragment.Login;
import com.mianshibang.main.fragment.Register;
import com.mianshibang.main.utils.EventBusUtils;
import com.mianshibang.main.utils.UIUtils;

public class LoginRegister extends Base implements View.OnClickListener {

	public static final String Event_Go_Login = "onEventLoginSelected";
	public static final String Event_Go_Register = "onEventRegisterSelected";

	private ViewPager mViewPager;
	private View mClose;

	@Override
	public int getContentLayout() {
		return R.layout.login_register;
	}

	@Override
	public void findViews() {
		mClose = (View) findViewById(R.id.close);
		mViewPager = (ViewPager) findViewById(R.id.view_pager);

		mViewPager.setPageMargin(UIUtils.dip2px(10));
		mViewPager.setPageMarginDrawable(new ColorDrawable(0x00FFFFFF));
	}

	@Override
	public void setListeners() {
		mClose.setOnClickListener(this);
	}

	@Override
	public void process() {
		mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
	}

	public void onEventLoginSelected() {
		mViewPager.setCurrentItem(0, true);
	}

	public void onEventRegisterSelected() {
		mViewPager.setCurrentItem(1, true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.close:
			finish();
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBusUtils.removeLoginEventSubscriber();
	}

	private static class FragmentAdapter extends FragmentPagerAdapter {

		public FragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int index) {
			return index == 0 ? Login.newInstance() : Register.newInstance();
		}

		@Override
		public int getCount() {
			return 2;
		}

	}

}
