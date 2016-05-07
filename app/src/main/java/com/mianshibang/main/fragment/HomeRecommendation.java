package com.mianshibang.main.fragment;

import com.mianshibang.main.adapter.RequestAdapter.LoaderListener;
import com.mianshibang.main.api.QuestionApis;
import com.mianshibang.main.widget.BannerView;

public class HomeRecommendation extends HomeNewest {

	@Override
	protected BannerView createBannerView() {
		return new BannerView(getActivity());
	}

	@Override
	protected void doRefresh(LoaderListener listener) {
		doLoadMore(listener, null, 0);
	}

	@Override
	protected void doLoadMore(LoaderListener listener, String beforeId, int page) {
		QuestionApis.requestRecommendations(page, new Delegate(listener));
	}

}
