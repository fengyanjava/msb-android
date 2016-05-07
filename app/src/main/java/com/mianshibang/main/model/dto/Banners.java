package com.mianshibang.main.model.dto;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.mianshibang.main.model.MBanner;

public class Banners extends BaseDTO {

	@SerializedName("data")
	public ArrayList<MBanner> banners;
}
