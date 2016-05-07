package com.mianshibang.main.api;

import java.util.ArrayList;
import java.util.List;

import com.mianshibang.main.http.MParameter;
import com.mianshibang.main.http.MVolley;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.dto.BaseDTO;
import com.mianshibang.main.model.dto.QuestionDetail;
import com.mianshibang.main.model.dto.Questions;

public class PostApis extends BaseApis {

	public static void post(String cid, String from, String description, String answer, String tags, ResponseHandler<BaseDTO> handler) {
		List<MParameter> args = new ArrayList<MParameter>();
		args.add(new MParameter("cid", cid));
		args.add(new MParameter("from", from));
		args.add(new MParameter("desc", description));
		args.add(new MParameter("answer", answer));
		args.add(new MParameter("tags", tags));
		
		MParameter[] array = new MParameter[args.size()];
		args.toArray(array);
		
		MVolley.sendRequest(post_question, BaseDTO.class, handler, array);
	}

	// page base 0
	public static void requestQuestions(int page, ResponseHandler<Questions> handler) {
		MVolley.sendRequest(post_questions, Questions.class, handler, new MParameter("page", page));
	}

	public static void requestDetail(String id, ResponseHandler<QuestionDetail> handler) {
		MVolley.sendRequest(post_detail, QuestionDetail.class, handler, new MParameter("id", id));
	}
}
