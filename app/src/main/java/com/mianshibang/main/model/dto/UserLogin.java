package com.mianshibang.main.model.dto;

import com.google.gson.annotations.SerializedName;
import com.mianshibang.main.model.MUser;

public class UserLogin extends BaseDTO {

	@SerializedName("data")
	public MUser user;
	
}
