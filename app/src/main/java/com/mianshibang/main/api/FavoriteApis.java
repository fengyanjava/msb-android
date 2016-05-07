package com.mianshibang.main.api;

import java.util.ArrayList;
import java.util.List;

import com.mianshibang.main.http.MParameter;
import com.mianshibang.main.http.MVolley;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.MFavoriteFolder;
import com.mianshibang.main.model.MQuestion;
import com.mianshibang.main.model.dto.BaseDTO;
import com.mianshibang.main.model.dto.FavoriteFolderDetail;
import com.mianshibang.main.model.dto.FavoriteFolders;
import com.mianshibang.main.model.dto.FoldersForSelect;
import com.mianshibang.main.model.dto.IdResult;

public class FavoriteApis extends BaseApis {
	
	public static void favorite(final MQuestion question, List<String> folderJson, ResponseHandler<BaseDTO> handler) {
		List<MParameter> args = new ArrayList<MParameter>();
		args.add(new MParameter("qid", question.id));
		for (String json : folderJson) {
			args.add(new MParameter("folder", json));
		}
		MParameter[] array = new MParameter[args.size()];
		args.toArray(array);
		MVolley.sendRequest(favorite, BaseDTO.class, handler, array);
	}

	public static void requestFavoriteFolders(String userId, ResponseHandler<FavoriteFolders> handler) {
		MVolley.sendRequest(favorite_folders, FavoriteFolders.class, handler, new MParameter("user_id", userId));
	}
	
	public static void requestFavoriteDetail(String folderId, ResponseHandler<FavoriteFolderDetail> handler) {
		MVolley.sendRequest(favorite_detail, FavoriteFolderDetail.class, handler, new MParameter("folder_id", folderId));
	}
	
	public static void requestFoldersForSelect(String questionId, ResponseHandler<FoldersForSelect> handler) {
		MVolley.sendRequest(favorite_folders_for_select, FoldersForSelect.class, handler, new MParameter("question_id", questionId));
	}
	
	public static void addFavoriteFolder(String name, String intro, ResponseHandler<IdResult> handler) {
		MVolley.sendRequest(add_favorite_folder, IdResult.class, handler, new MParameter("name", name), new MParameter("intro", intro));
	}
	
	public static void updateFavoriteFolder(MFavoriteFolder folder, ResponseHandler<BaseDTO> handler) {
		MVolley.sendRequest(update_favorite_folder, BaseDTO.class, handler, 
				new MParameter("id", folder.id), new MParameter("name", folder.name), new MParameter("intro", folder.intro));
	}
	
	public static void deleteFavoriteFolder(String folderId, ResponseHandler<IdResult> handler) {
		MVolley.sendRequest(delete_favorite_folder, IdResult.class, handler, new MParameter("id", folderId));
	}
	
}
