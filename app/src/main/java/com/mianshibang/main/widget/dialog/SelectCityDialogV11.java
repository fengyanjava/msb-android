package com.mianshibang.main.widget.dialog;

import com.mianshibang.main.R;
import com.mianshibang.main.model.Area;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SelectCityDialogV11 extends SelectCityDialog implements OnValueChangeListener {
	
	private NumberPicker mProvincePicker;
	private NumberPicker mCityPicker;

	protected SelectCityDialogV11(Context context, String province, String city) {
		super(context, province, city);
	}
	
	@Override
	protected void initPicker() {
		mProvincePicker = (NumberPicker) findViewById(R.id.province);
		mCityPicker = (NumberPicker) findViewById(R.id.city);

		mProvincePicker.setDisplayedValues(mProvinceNames);
		mProvincePicker.setMinValue(0);
		mProvincePicker.setMaxValue(mAreas.size() - 1);
		mProvincePicker.setOnValueChangedListener(this);

		int provinceIndex = getDefaultProvinceIndex(mProvince);
		mProvincePicker.setValue(provinceIndex);

		updateCityPicker(provinceIndex);

		int cityIndex = getDefaultCityIndex(provinceIndex, mCity);
		mCityPicker.setValue(cityIndex);
	}
	
	private void updateCityPicker(int provinceIndex) {
		Area province = mAreas.get(provinceIndex);
		String[] cityNames = getCityNames(province);

		mCityPicker.setDisplayedValues(null);
		mCityPicker.setMinValue(0);
		mCityPicker.setMaxValue(province.cities.size() - 1);
		mCityPicker.setDisplayedValues(cityNames);
	}
	
	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		updateCityPicker(newVal);
	}
	
	protected int getSelectProvinceIndex() {
		return mProvincePicker.getValue();
	}
	
	protected int getSelectCityIndex() {
		return mCityPicker.getValue();
	}

}
