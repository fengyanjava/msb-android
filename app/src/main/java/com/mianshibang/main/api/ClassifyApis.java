package com.mianshibang.main.api;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mianshibang.main.http.MVolley;
import com.mianshibang.main.http.ResponseDecorator;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.MClassify;
import com.mianshibang.main.model.MClassifyGroup;
import com.mianshibang.main.model.MClassifyMapping;
import com.mianshibang.main.model.dto.Classifies;
import com.mianshibang.main.storage.ClassifyStorage;

public class ClassifyApis extends BaseApis {
	
	public static void updateClassify() {
		requestClassify(new ResponseHandler<Classifies>());
	}

	public static void requestClassify(ResponseHandler<Classifies> handler) {
		ResponseDecorator<Classifies> decorator = new ResponseDecorator<Classifies>(handler) {
			@Override
			public void onDataParsed(Classifies data) {
				if (data != null && data.groups != null) {
					String json = new Gson().toJson(data.groups);
					ClassifyStorage.putClassify(json);
				}
				super.onDataParsed(data);
			}
		};
		MVolley.sendRequest(classify, Classifies.class, decorator);
	}
	
	public static ArrayList<MClassifyGroup> readDefaultClassify() {
		try {
			String json = ClassifyStorage.getClassify();
			if (json == null) {
				json = readStringFromAssets("classify.json");
				ClassifyStorage.putClassify(json);
			}
			ArrayList<MClassifyGroup> groups = new Gson().fromJson(json, new TypeToken<ArrayList<MClassifyGroup>>(){}.getType());
			return groups;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static List<MClassify> getClassifyList() {
		List<MClassify> classifies = new ArrayList<MClassify>();
		ArrayList<MClassifyGroup> groups= ClassifyApis.readDefaultClassify();
		
		if (groups == null) {
			return classifies;
		}
		
		for (MClassifyGroup group : groups) {
			if (group.mappings == null) {
				continue;
			}
			for (MClassifyMapping mapping : group.mappings) {
				if (mapping.classify == null) {
					continue;
				}
				classifies.add(mapping.classify);		
			}
		}
		return classifies;
	}
}
