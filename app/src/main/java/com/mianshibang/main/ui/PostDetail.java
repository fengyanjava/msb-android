package com.mianshibang.main.ui;

import com.mianshibang.main.api.PostApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.dto.QuestionDetail;

public class PostDetail extends Detail {

	@Override
	protected void requestDetail(String id, ResponseHandler<QuestionDetail> handler) {
		PostApis.requestDetail(id, handler);
	}
	
	@Override
	protected void addToBrowseHistory() {
		// do nothing:用户上传的问题不记入浏览记录
	}
}
