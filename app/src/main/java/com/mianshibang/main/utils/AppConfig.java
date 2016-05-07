package com.mianshibang.main.utils;

public interface AppConfig {
	
	boolean Debut_Mode = false;
	String Web_Scheme = "msbapp";
	
	String App_Weibo_Nickname = "面试帮APP";
	String App_Website_Official = "http://mianshibang.duapp.com/touch/?from=android_app";
	String App_Website_Weibo = "http://weibo.cn/msbapp";
	
	String WeiboAppKey = "3675915988";
	String WechatAppId = "wx81de5b71411c726b";
	String TencentAppId = "1102349525";
	
	String Api_Version = "1";
	String Api_Secret = Web_Scheme + WeiboAppKey;
	
	String Api_Url_Local = "http://192.168.1.101/md/api/";
	String Api_Url_Online = "http://mianshibang.duapp.com/api/";

	int Browse_History_Max = 50;
	int Browse_History_Page_Count = 10;
	
	int Search_History_Max = 10;
	
}
