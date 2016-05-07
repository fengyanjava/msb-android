package com.mianshibang.main.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.model.FontSize;
import com.mianshibang.main.storage.GenericSettingStorage;

public class FontSizeDialog extends BaseDialog implements View.OnClickListener {
	
	private static final int[] mViewIds = new int[] {R.id.font_size_small, 
		R.id.font_size_medium, R.id.font_size_large, R.id.font_size_xlarge};
	
	private TextView[] mSizeViews;
	
	private OnFontSizeChangedListener mListener;

	public FontSizeDialog(Context context) {
		super(context);
		
		setTitle(R.string.font_size_title);
		setContentView(R.layout.dialog_font_size);
		
		FontSize fontSize = GenericSettingStorage.getFontSize();
		
		mSizeViews = new TextView[mViewIds.length];
		for (int i = 0; i < mViewIds.length; i++) {
			mSizeViews[i] = (TextView) findViewById(mViewIds[i]);
			mSizeViews[i].setOnClickListener(this);
			
			if (fontSize.ordinal() == i) {
				setItemChecked(mSizeViews[i]);
			}
		}
	}
	
	private void setItemChecked(TextView item) {
		item.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkbox_checked, 0);
	}

	@Override
	public void onClick(View v) {
		int ordinal = indexOf(v.getId());
		FontSize fontSize = FontSize.fromOrdinal(ordinal);
		GenericSettingStorage.putFontSize(fontSize);
		onFontSizeChanged(fontSize);
		dismiss();
	}
	
	private static int indexOf(int id) {
		for (int i = 0; i < mViewIds.length; i++) {
			if (id == mViewIds[i]) {
				return i;
			}
		}
		return -1;
	}
	
	private void onFontSizeChanged(FontSize fontSize) {
		if (mListener != null) {
			mListener.onFontSizeChanged(fontSize);
		}
	}
	
	public void setOnFontSizeChangedListener(OnFontSizeChangedListener listener) {
		mListener = listener;
	}
	
	public static interface OnFontSizeChangedListener {
		void onFontSizeChanged(FontSize fontSize); 
	}

}
