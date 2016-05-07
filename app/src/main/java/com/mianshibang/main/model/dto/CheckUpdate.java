package com.mianshibang.main.model.dto;

import com.google.gson.annotations.SerializedName;
import com.mianshibang.main.model.MVersion;

public class CheckUpdate extends BaseDTO {

	@SerializedName("data")
	public MVersion version;
}
