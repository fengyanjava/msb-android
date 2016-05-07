package com.mianshibang.main.model;

import com.mianshibang.main.R;
import com.mianshibang.main.MApplication;

public enum FontSize {
	
	Small(R.string.font_size_small, 12), 
	Medium(R.string.font_size_medium, 15), 
	Large(R.string.font_size_large, 18), 
	XLarge(R.string.font_size_xlarge, 20);
	
	private int mTextId;
	private int mSizeDP;
	
	private FontSize(int resId, int sizeDP) {
		mTextId = resId;
		mSizeDP = sizeDP;
	}
	
	public static FontSize fromOrdinal(int ordinal) {
		for (FontSize e : values()) {
			if (e.ordinal() == ordinal) {
				return e;
			}
		}
		return getDefault();
	}
	
	public int getTextSizeDP() {
		return mSizeDP;
	}
	
	@Override
	public String toString() {
		return MApplication.getApplication().getString(mTextId);
	}
	
	private static FontSize getDefault() {
		return Medium;
	}
}
