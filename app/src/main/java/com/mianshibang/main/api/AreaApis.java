package com.mianshibang.main.api;

import java.util.ArrayList;

import android.os.SystemClock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mianshibang.main.model.Area;
import com.mianshibang.main.utils.MLog;

public class AreaApis extends BaseApis {
	
	private static ArrayList<Area> provinces;

	public static void loadAreas() {
		try {
			long start = SystemClock.elapsedRealtime();
			
			String json = readStringFromAssets("area.json");
			provinces = new Gson().fromJson(json, new TypeToken<ArrayList<Area>>(){}.getType());
			
			long end = SystemClock.elapsedRealtime();
			long spend = end - start;
			MLog.i("load area spend:" + spend + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Area> getProvinces() {
		return provinces;
	}
}
