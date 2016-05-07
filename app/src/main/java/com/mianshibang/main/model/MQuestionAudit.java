package com.mianshibang.main.model;

import com.google.gson.annotations.SerializedName;
import com.mianshibang.main.R;

public class MQuestionAudit extends MData {

	public int questionId;
	@SerializedName("result")
	public State state;
	public String cause;

	public static enum State {
		@SerializedName("0")
		Success(R.string.post_question_audit_success, R.string.post_question_view_question),
		
		@SerializedName("1")
		Fail(R.string.post_question_audit_fail, R.string.post_question_view_cause),
		
		@SerializedName("2")
		Waiting(R.string.post_question_audit_waiting, R.string.app_name);
		
		public int text;
		public int action;
		
		private State(int text, int action) {
			this.text = text;
			this.action = action;
		}
		
		public boolean hasAction() {
			return this == Success || this == Fail;
		}
	}
}
