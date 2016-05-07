package com.mianshibang.main.utils;

import java.lang.reflect.Method;

public class EventBusUtils {

	private static Object SubscriberFolderEvent;
	private static final String EventFolderDeleted = "onEventFolderDeleted";

	private static Object SubscriberLoginEvent;
	private static Integer RequestCodeLoginEvent;
	private static final String EventLogin = "onEventLogin";

	public static void subscribeFolderEvent(Object subscriber) {
		SubscriberFolderEvent = subscriber;
	}

	public static void postFolderEvent(String folderId) {
		postEvent(SubscriberFolderEvent, EventFolderDeleted, folderId);
	}

	public static void subscribeLoginEvent(Object subscriber) {
		subscribeLoginEvent(subscriber, null);
	}

	public static void subscribeLoginEvent(Object subscriber, Integer requestCode) {
		RequestCodeLoginEvent = requestCode;
		SubscriberLoginEvent = subscriber;
	}

	public static void removeLoginEventSubscriber() {
		SubscriberLoginEvent = null;
	}

	public static void postLoginEvent() {
		if (RequestCodeLoginEvent != null) {
			postEvent(SubscriberLoginEvent, EventLogin, RequestCodeLoginEvent.intValue());
		} else {
			postEvent(SubscriberLoginEvent, EventLogin);
		}
		removeLoginEventSubscriber();
	}

	public static void postEvent(Object subscriber, String eventName, Object... args) {
		try {
			if (subscriber == null || eventName == null) {
				return;
			}

			Class<?>[] parameterTypes = new Class<?>[args.length];
			for (int i = 0; i < args.length; i++) {
				parameterTypes[i] = args[i].getClass();
			}

			Method method = null;
			Class<?> clazz = subscriber.getClass();

			while (method == null && clazz != null) {
				try {
					method = clazz.getDeclaredMethod(eventName, parameterTypes);
				} catch (Exception e) {
				}
				clazz = clazz.getSuperclass();
			}
			method.setAccessible(true);
			method.invoke(subscriber, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
