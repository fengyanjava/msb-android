package com.mianshibang.main.storage;

import com.mianshibang.main.model.FontSize;

public class GenericSettingStorage extends PrefsManager {

	public synchronized static boolean isDirectShowAnswer() {
		return PrefsManager.get(Key.ShowAnswer, false);
	}

	public synchronized static void putDirectShowAnswer(boolean value) {
		PrefsManager.put(Key.ShowAnswer, value);
	}

	public synchronized static boolean isReceivePush() {
		return PrefsManager.get(Key.ReceivePush, true);
	}

	public synchronized static void putReceivePush(boolean value) {
		PrefsManager.put(Key.ReceivePush, value);
	}

	public synchronized static FontSize getFontSize() {
		int ordinal = PrefsManager.get(Key.FontSize, -1);
		return FontSize.fromOrdinal(ordinal);
	}

	public synchronized static void putFontSize(FontSize fontSize) {
		PrefsManager.put(Key.FontSize, fontSize.ordinal());
	}

}
