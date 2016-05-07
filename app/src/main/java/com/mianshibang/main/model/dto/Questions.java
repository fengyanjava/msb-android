package com.mianshibang.main.model.dto;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.mianshibang.main.model.MQuestion;

public class Questions extends BaseDTO {

	@SerializedName("data")
	public ArrayList<MQuestion> questions;
}
