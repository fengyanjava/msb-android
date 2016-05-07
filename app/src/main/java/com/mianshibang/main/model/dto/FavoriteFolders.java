package com.mianshibang.main.model.dto;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.mianshibang.main.model.MFavoriteFolder;

public class FavoriteFolders extends BaseDTO {
	
	@SerializedName("data")
	public ArrayList<MFavoriteFolder> folders;
}
