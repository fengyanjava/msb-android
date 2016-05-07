package com.mianshibang.main.widget.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.model.MFavoriteFolderSimple;

public class SelectFavoriteFolderItemView extends LinearLayout implements OnClickListener {
	
	private TextView mName;
	private MFavoriteFolderSimple mFolder;

	public SelectFavoriteFolderItemView(Context context) {
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.dialog_select_folder_item, this, true);
		
		initStyle();
		initView();
	}
	
	public void setData(MFavoriteFolderSimple folder) {
		mFolder =folder;
		refreshView();
	}
	
	public void refreshView() {
		mName.setText(mFolder.name);
		mName.setSelected(mFolder.isFavoritedByMe);
	}
	
	private void initView() {
		mName = (TextView) findViewById(R.id.name);
		mName.setOnClickListener(this);
	}
	
	private void initStyle() {
		setOrientation(VERTICAL);
	}

	@Override
	public void onClick(View v) {
		mFolder.isFavoritedByMe = !mFolder.isFavoritedByMe;
		refreshView();
	}

}
