package com.mianshibang.main.widget.dialog;

import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.view.View;

import com.mianshibang.main.R;
import com.mianshibang.main.utils.MLog;

public class SelectDateDialog extends BaseDialog implements View.OnClickListener {
	
	private DatePicker mDatePicker;
	private OnDateSelectListener mListener;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public SelectDateDialog(Context context, String date) {
		super(context);
		
		setContentView(R.layout.dialog_select_date);
		
		mDatePicker = (DatePicker) findViewById(R.id.date_picker);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			mDatePicker.setCalendarViewShown(false);
		}
		
		if (!TextUtils.isEmpty(date)) {
			initDatePicker(date);
		}
		
		setPositiveButton(true, null, this);
	}
	
	private void initDatePicker(String date) {
		try {
			String[] split = date.trim().split("\\-");
			int year = Integer.parseInt(split[0]);
			int month = Integer.parseInt(split[1]) - 1;
			int day = Integer.parseInt(split[2]);
			mDatePicker.init(year, month, day, null);
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View v) {
		int year = mDatePicker.getYear();
		int month = mDatePicker.getMonth() + 1;
		int day = mDatePicker.getDayOfMonth();
		String date = String.format(Locale.CHINA, "%d-%02d-%02d", year, month, day);
		MLog.i(date);
		
		if (mListener != null) {
			mListener.onDateSelect(date);
		}
	}
	
	public void setOnDateSelectListener(OnDateSelectListener listener) {
		mListener = listener;
	}
	
	public static interface OnDateSelectListener {
		void onDateSelect(String date);
	}

}
