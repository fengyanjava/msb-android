package com.mianshibang.main.utils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.storage.GenericSettingStorage;

public class ViewUtils {

	private static View createLine(Context context) {
		View line = new View(context);
		line.setBackgroundColor(context.getResources().getColor(R.color.line_gray));
		return line;
	}
	
	public static void addLineToLinearLayout(LinearLayout layout, int leftMargin) {
		Context context= layout.getContext();
		View line = createLine(context);
		int lineHeight = context.getResources().getDimensionPixelSize(R.dimen.line_height);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, lineHeight);
		params.leftMargin = leftMargin;
		layout.addView(line, params);
	}
	
	public static void setTextViewSizeWithConfig(TextView textView) {
		textView.setTextSize(GenericSettingStorage.getFontSize().getTextSizeDP());
	}
}
