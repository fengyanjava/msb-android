package com.mianshibang.main.model.dto;

import com.google.gson.annotations.SerializedName;
import com.mianshibang.main.model.MQuestion;

public class QuestionDetail extends BaseDTO {

	@SerializedName("data")
	public MQuestion question;
}
