package com.mianshibang.main.model.dto;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.mianshibang.main.model.MClassifyGroup;

public class Classifies extends BaseDTO {

	@SerializedName("data")
	public ArrayList<MClassifyGroup> groups;
}
