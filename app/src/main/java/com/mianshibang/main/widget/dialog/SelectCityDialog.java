package com.mianshibang.main.widget.dialog;

import java.util.ArrayList;

import android.content.Context;
import android.os.Build;
import android.view.View;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.WheelVerticalView;
import antistatic.spinnerwheel.adapters.ArrayWheelAdapter;

import com.mianshibang.main.R;
import com.mianshibang.main.api.AreaApis;
import com.mianshibang.main.model.Area;

public class SelectCityDialog extends BaseDialog implements View.OnClickListener, OnWheelChangedListener {

	protected String mProvince;
	protected String mCity;

	private WheelVerticalView mProvinceWheel;
	private WheelVerticalView mCityWheel;

	protected ArrayList<Area> mAreas;
	protected String[] mProvinceNames;

	private OnCitySelectedListener mListener;

	protected SelectCityDialog(Context context, String province, String city) {
		super(context);

		mProvince = province;
		mCity = city;

		setTitle(R.string.select_city_title);
		setContentView(R.layout.dialog_select_city);

		mAreas = AreaApis.getProvinces();
		initProvinceNames();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			initPicker();
		} else {
			initWheel();
		}

		setPositiveButton(true, null, this);
	}

	private void initWheel() {
		mProvinceWheel = (WheelVerticalView) findViewById(R.id.province);
		mCityWheel = (WheelVerticalView) findViewById(R.id.city);
		
		MyWheelAdapter provinceAdapter = new MyWheelAdapter(getContext(), mProvinceNames);
		mProvinceWheel.setViewAdapter(provinceAdapter);

		int provinceIndex = getDefaultProvinceIndex(mProvince);
		mProvinceWheel.setCurrentItem(provinceIndex);

		updateCityWheel(provinceIndex);
		int cityIndex = getDefaultCityIndex(provinceIndex, mCity);
		mCityWheel.setCurrentItem(cityIndex);
		
		mProvinceWheel.addChangingListener(this);
	}

	protected void initPicker() {
	}

	private void initProvinceNames() {
		mProvinceNames = new String[mAreas.size()];
		for (int i = 0; i < mProvinceNames.length; i++) {
			mProvinceNames[i] = mAreas.get(i).name;
		}
	}

	protected int getDefaultProvinceIndex(String province) {
		for (int i = 0; i < mAreas.size(); i++) {
			if (mAreas.get(i).name.equals(province)) {
				return i;
			}
		}
		return 0;
	}

	protected int getDefaultCityIndex(int provinceIndex, String city) {
		for (int i = 0; i < mAreas.get(provinceIndex).cities.size(); i++) {
			if (mAreas.get(provinceIndex).cities.get(i).name.equals(city)) {
				return i;
			}
		}
		return 0;
	}
	

	@Override
	public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
		updateCityWheel(newValue);
		mCityWheel.setCurrentItem(0);
	}

	

	private void updateCityWheel(int provinceIndex) {
		Area province = mAreas.get(provinceIndex);
		String[] cityNames = getCityNames(province);
		
		MyWheelAdapter cityAdapter = new MyWheelAdapter(getContext(), cityNames);
		mCityWheel.setViewAdapter(cityAdapter);
	}

	protected String[] getCityNames(Area province) {
		String[] cityNames = new String[province.cities.size()];
		for (int i = 0; i < cityNames.length; i++) {
			cityNames[i] = province.cities.get(i).name;
		}

		return cityNames;
	}
	
	protected int getSelectProvinceIndex() {
		return mProvinceWheel.getCurrentItem();
	}
	
	protected int getSelectCityIndex() {
		return mCityWheel.getCurrentItem();
	}

	@Override
	public void onClick(View v) {
		int provinceIndex = getSelectProvinceIndex();
		int cityIndex = getSelectCityIndex();

		String province = mAreas.get(provinceIndex).name;
		String city = mAreas.get(provinceIndex).cities.get(cityIndex).name;

		if (mListener != null) {
			mListener.onCitySelected(province, city);
		}
	}

	public void setOnCitySelectedListener(OnCitySelectedListener listener) {
		mListener = listener;
	}

	public static interface OnCitySelectedListener {
		void onCitySelected(String province, String city);
	}
	
	private static class MyWheelAdapter extends ArrayWheelAdapter<String> {

		public MyWheelAdapter(Context context, String[] items) {
			super(context, items);
			setItemResource(R.layout.dialog_select_city_item);
		}
		
	}
	
	public static class Factory {
		public static SelectCityDialog getDialog(Context context, String province, String city) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				return new SelectCityDialogV11(context, province, city);
			} else {
				return new SelectCityDialog(context, province, city);
			}
		}
	}

}
