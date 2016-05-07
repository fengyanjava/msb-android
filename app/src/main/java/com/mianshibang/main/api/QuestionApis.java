package com.mianshibang.main.api;

import com.mianshibang.main.http.MParameter;
import com.mianshibang.main.http.MVolley;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.dto.QuestionDetail;
import com.mianshibang.main.model.dto.Questions;

public class QuestionApis extends BaseApis {
	
	public static void requestRecommendations(int page, ResponseHandler<Questions> handler) {
		MVolley.sendRequest(recommendations, Questions.class, handler, new MParameter("page", page));
	}
	
	public static void requestFeeds(String beforeId, ResponseHandler<Questions> handler) {
		MVolley.sendRequest(feeds, Questions.class, handler, new MParameter("before_id", beforeId));
	}
	
	// page base 0
	public static void requestQuestions(String classifyId, String keyword, int page, ResponseHandler<Questions> handler) {
		MVolley.sendRequest(questions, Questions.class, handler, new MParameter("cid", classifyId), new MParameter("keyword", keyword), new MParameter("page", page));
	}
	
	public static void requestDetail(String id, ResponseHandler<QuestionDetail> handler) {
		MVolley.sendRequest(detail, QuestionDetail.class, handler, new MParameter("id", id));
	}
	
	public static void requestQuestionWithIds(String ids, ResponseHandler<Questions> handler) {
		MVolley.sendRequest(question_with_ids, Questions.class, handler, new MParameter("ids", ids));
	}

}
