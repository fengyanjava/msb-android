package com.mianshibang.main.push;

import android.app.Notification;
import android.content.Context;

import com.mianshibang.main.MApplication;
import com.mianshibang.main.storage.GenericSettingStorage;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.entity.UMessage;

public class Push {

	public static void init() {
		Context context = MApplication.getApplication();
		PushAgent pushAgent = PushAgent.getInstance(context);
		pushAgent.setDebugMode(false);

		UmengMessageHandler messageHandler = new UmengMessageHandler() {

			@Override
			public Notification getNotification(Context context, UMessage msg) {
				switch (msg.builder_id) {
				default:
					return super.getNotification(context, msg);
				}
			}
		};
		pushAgent.setMessageHandler(messageHandler);

		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {

			@Override
			public void openActivity(Context context, UMessage msg) {
				super.openActivity(context, msg);
			}
		};
		pushAgent.setNotificationClickHandler(notificationClickHandler);

	}

	public static void openOrStop() {
		if (GenericSettingStorage.isReceivePush()) {
			enable();
		} else {
			disable();
		}
	}

	private static void enable() {
		Context context = MApplication.getApplication();
		PushAgent.getInstance(context).enable(new IUmengRegisterCallback() {

			@Override
			public void onRegistered(String token) {
				System.out.println("token:" + token);
			}
		});
	}

	private static void disable() {
		Context context = MApplication.getApplication();
		PushAgent.getInstance(context).disable();
	}

	public static String getDeviceToken() {
		Context context = MApplication.getApplication();
		String token = UmengRegistrar.getRegistrationId(context);
		return token == null ? "" : token;
	}
}
