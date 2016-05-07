package com.mianshibang.main.widget;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mianshibang.main.R;
import com.mianshibang.main.api.PlatformApis;
import com.mianshibang.main.http.SilentRespHandler;
import com.mianshibang.main.image.MPicasso;
import com.mianshibang.main.model.BannerType;
import com.mianshibang.main.model.MBanner;
import com.mianshibang.main.model.dto.Banners;
import com.mianshibang.main.utils.DimensionUtils;
import com.mianshibang.main.utils.UiNavigation;

public class BannerView extends FrameLayout implements Callback {

	private static final int WIDTH_DEFAULT = 720;
	private static final int HEIGHT_DEFAULT = 200;
	private static final int PLAY_INTERVAL = 3000;

	private BannerType mType = BannerType.getDefault();

	private ArrayList<MBanner> mBanners;

	private View mBannerContent;
	private BannerAdapter mAdapter;
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;

	private Handler mHandler = new Handler(this);

	public BannerView(Context context) {
		super(context);

		mBannerContent = LayoutInflater.from(context).inflate(R.layout.banner, this, false);
		addView(mBannerContent);
		mBannerContent.setVisibility(View.GONE);

		mAdapter = new BannerAdapter();

		mPager = (ViewPager) findViewById(R.id.view_pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			onPause();
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			onResume();
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	public void setBannerType(BannerType type) {
		mType = type;
	}

	@Override
	public boolean handleMessage(Message msg) {
		int item = mPager.getCurrentItem() + 1;
		if (item >= 0 && item < mAdapter.getCount()) {
			mPager.setCurrentItem(item, true);
		}
		mHandler.sendEmptyMessageDelayed(0, PLAY_INTERVAL);
		return true;
	}

	public void onResume() {
		onPause();
		if (mBanners != null && mBanners.size() > 1) {
			mHandler.sendEmptyMessageDelayed(0, 3000);
		}
	}

	public void onPause() {
		mHandler.removeMessages(0);
	}

	public void refresh() {
		PlatformApis.requestBanner(mType, new SilentRespHandler<Banners>() {
			@Override
			public void onSuccess(Banners data) {
				if (!data.isSucceeded()) {
					return;
				}
				mBanners = data.banners;
				onRefreshSuccess();
			}
		});
	}

	private void onRefreshSuccess() {
		boolean shouldShow = mBanners != null && !mBanners.isEmpty();
		mBannerContent.setVisibility(shouldShow ? View.VISIBLE : View.GONE);

		mPager.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();

		adjustHeight();
		mIndicator.setVisibility(mBanners.size() > 1 ? View.VISIBLE : View.GONE);

		onResume();
	}

	private void adjustHeight() {
		if (mBanners == null || mBanners.isEmpty()) {
			return;
		}

		double defaultRatio = 1.0 * WIDTH_DEFAULT / HEIGHT_DEFAULT;

		int imageWidth = 0;
		int imageHeight = 0;

		for (MBanner banner : mBanners) {
			if (imageWidth == 0 || imageHeight == 0) {
				imageWidth = banner.imageWidth;
				imageHeight = banner.imageHeight;
				continue;
			}

			double current = 1.0 * imageWidth / imageHeight;
			double ratio = 1.0 * banner.imageWidth / banner.imageHeight;

			if (Math.abs(defaultRatio - ratio) < Math.abs(defaultRatio - current)) {
				imageWidth = banner.imageWidth;
				imageHeight = banner.imageHeight;
			}
		}

		int height = (int) (1.0 * imageHeight * DimensionUtils.getScreenWidth() / imageWidth);
		mBannerContent.getLayoutParams().height = height;
		mBannerContent.requestLayout();
	}

	private class BannerAdapter extends LoopPagerAdapter implements OnClickListener {

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			position %= getRealCount();
			ImageView imageView = new ImageView(getContext());
			imageView.setTag(position);
			imageView.setOnClickListener(this);
			MPicasso.load(mBanners.get(position).imageUrl, R.drawable.shape_default_banner, imageView);
			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getRealCount() {
			return mBanners == null ? 0 : mBanners.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void onClick(View v) {
			Integer position = (Integer) v.getTag();

			if (position == null) {
				return;
			}

			if (mBanners == null || position < 0 || position >= mBanners.size()) {
				return;
			}

			MBanner banner = mBanners.get(position.intValue());
			String url = banner.url;

			if (TextUtils.isEmpty(url)) {
				return;
			}

			UiNavigation.startWithUri(getContext(), url);
		}

	}

}
