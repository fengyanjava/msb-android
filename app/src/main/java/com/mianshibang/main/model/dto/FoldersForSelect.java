package com.mianshibang.main.model.dto;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;
import com.mianshibang.main.model.MFavoriteFolderSimple;

public class FoldersForSelect extends BaseDTO {

	@SerializedName("data")
	public ArrayList<MFavoriteFolderSimple> folders;
}
