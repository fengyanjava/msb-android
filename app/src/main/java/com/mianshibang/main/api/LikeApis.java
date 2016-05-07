package com.mianshibang.main.api;

import com.mianshibang.main.http.MParameter;
import com.mianshibang.main.http.MVolley;
import com.mianshibang.main.http.ResponseDecorator;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.MQuestion;
import com.mianshibang.main.model.dto.BaseDTO;
import com.mianshibang.main.model.dto.Questions;

public class LikeApis extends BaseApis {
	
	public static void requestLikedQuestions(int page, ResponseHandler<Questions> handler) {
		MVolley.sendRequest(liked_questions, Questions.class, handler, new MParameter("page", page));
	}

	public static void likeQuestion(MQuestion question, ResponseHandler<BaseDTO> handler) {
		Decorator decorator = new Decorator(handler, question, true);
		MVolley.sendRequest(like_question, BaseDTO.class, decorator, new MParameter("qid", question.id));
	}
	
	public static void dislikeQuestion(MQuestion question, ResponseHandler<BaseDTO> handler) {
		Decorator decorator = new Decorator(handler, question, false);
		MVolley.sendRequest(dislike_question, BaseDTO.class, decorator, new MParameter("qid", question.id));
	}
	
	private static class Decorator extends ResponseDecorator<BaseDTO> {
		
		private boolean mIsLikeQuestion;
		private MQuestion mQuestion;

		public Decorator(ResponseHandler<BaseDTO> mResponseHandler, MQuestion q, boolean isLike) {
			super(mResponseHandler);
			mQuestion = q;
			mIsLikeQuestion = isLike;
		}
		
		@Override
		public void onSuccess(BaseDTO data) {
			if (data.isSucceeded() && mQuestion != null) {
				mQuestion.voteCount += (mIsLikeQuestion ? 1 : -1);
				mQuestion.isVotedByMe = mIsLikeQuestion;
			}
			
			super.onSuccess(data);
		}
		
	}
}
