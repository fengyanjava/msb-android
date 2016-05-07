package com.mianshibang.main.model.dto;

import com.google.gson.annotations.SerializedName;
import com.mianshibang.main.model.MFavoriteFolder;

public class FavoriteFolderDetail extends BaseDTO {

	@SerializedName("data")
	public MFavoriteFolder folder;
}
