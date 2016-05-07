package com.mianshibang.main.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;

import com.mianshibang.main.MApplication;
import com.mianshibang.main.R;
import com.mianshibang.main.model.MQuestion;
import com.mianshibang.main.utils.AppConfig;
import com.mianshibang.main.utils.ToastUtils;
import com.mianshibang.main.utils.UIUtils;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.connect.auth.QQAuth;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

public class ShareApis extends BaseApis {
	
	private static IWXAPI sWechatApi;
	private static IWeiboShareAPI sWeiboApi;

	public static void shareToWeibo(Activity activity, MQuestion question) {
		sWeiboApi = WeiboShareSDK.createWeiboAPI(activity, AppConfig.WeiboAppKey);
		
		if (!sWeiboApi.isWeiboAppInstalled()) {
			ToastUtils.show(R.string.share_weibo_not_installed);
			return;
		}
		
		if (!sWeiboApi.isWeiboAppSupportAPI()) {
			ToastUtils.show(R.string.share_weibo_not_support_api);
			return;
		}
		
		sWeiboApi.registerApp();

		String text = UIUtils.getString(R.string.share_weibo, AppConfig.App_Weibo_Nickname, AppConfig.App_Website_Official);
		int length = 140 - text.length(); // 剩余字数
		int descLength = question.description.length();
		String questionTitle = descLength <= length ? question.description : question.description.substring(0, length - 1).concat("…");

		TextObject textObject = new TextObject();
		textObject.text = questionTitle + text;

		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		weiboMessage.textObject = textObject;

		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;

		sWeiboApi.sendRequest(request);
	}
	
	public static IWeiboShareAPI getWeiboApi() {
		return sWeiboApi;
	}

	public static void shareToQQ(Activity activity, MQuestion question) {
		QQToken qqToken = QQAuth.createInstance(AppConfig.TencentAppId, activity).getQQToken();
		QQShare qqShare = new QQShare(activity, qqToken);

		Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, UIUtils.getString(R.string.share_qq_title));
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, question.description);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, question.webUrl);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, copyShareImage());
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME, UIUtils.getString(R.string.app_name));
		params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);

		qqShare.shareToQQ(activity, params, new IUiListener() {

			@Override
			public void onError(UiError arg0) {

			}

			@Override
			public void onComplete(Object arg0) {

			}

			@Override
			public void onCancel() {

			}
		});

	}
	
	public static void initWechatShare() {
		sWechatApi = WXAPIFactory.createWXAPI(MApplication.getApplication(), AppConfig.WechatAppId);
		sWechatApi.registerApp(AppConfig.WechatAppId);
	}
	
	public static IWXAPI getWeiChatApi() {
		return sWechatApi;
	}
	
	public static void shareToWechat(Activity activity, MQuestion question, boolean toMoments) {
		if (sWechatApi == null) {
			return;
		}
		
		if (!sWechatApi.isWXAppInstalled()) {
			ToastUtils.show(R.string.share_wechat_not_installed);
			return;
		}
		
		if (!sWechatApi.isWXAppSupportAPI()) {
			ToastUtils.show(R.string.share_wechat_not_support_api);
			return;
		}
		
		WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = question.webUrl;
		
		WXMediaMessage msg = new WXMediaMessage();
		msg.title = UIUtils.getString(R.string.share_wechat_title);
        msg.description = question.description;
        
        try {
        	Bitmap bm = BitmapFactory.decodeStream(getShareImageStream());
        	ByteArrayOutputStream bout = new ByteArrayOutputStream();
        	bm.compress(Bitmap.CompressFormat.PNG, 100, bout);
        	msg.thumbData = bout.toByteArray();
        	bm.recycle();
		} catch (Exception e) {
		}
        
        msg.mediaObject = webpageObject;
    	
    	SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = toMoments ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        sWechatApi.sendReq(req);
	}
	
	private static InputStream getShareImageStream() throws Exception {
		return UIUtils.getContext().getAssets().open("logo_share.png");
	}

	private static String copyShareImage() {
		try {
			File file = new File(Environment.getExternalStorageDirectory(), "mianshibang/logo.png");
			file.getParentFile().mkdirs();

			BufferedInputStream in = new BufferedInputStream(getShareImageStream());
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
			
			int length = 0;
			byte[] buffer = new byte[1024 * 100];
			
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			
			in.close();
			out.close();
			
			return file.getAbsolutePath();
		} catch (Exception e) {
			return null;
		}
	}
}
