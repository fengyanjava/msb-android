package com.mianshibang.main.widget;

import android.support.v4.view.PagerAdapter;

public abstract class LoopPagerAdapter extends PagerAdapter {

	@Override
	public int getCount() {
		int count = getRealCount();
		return count <= 1 ? count : Integer.MAX_VALUE;
	}

	public abstract int getRealCount();

}
