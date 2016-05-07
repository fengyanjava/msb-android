package com.mianshibang.main.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.model.MFavoriteFolder;

public class FavoriteDetailHeaderView extends LinearLayout {
	
	private TextView mFolderName;
	private TextView mFolderIntro;
	private TextView mQuestionCount;
	
	public FavoriteDetailHeaderView(Context context) {
		this(context, null);
	}

	public FavoriteDetailHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater.from(context).inflate(R.layout.favorite_detail_header, this, true);
		
		setStyle();
		findViews();
	}
	
	private void findViews() {
		mFolderName = (TextView) findViewById(R.id.folder_name);
		mFolderIntro = (TextView) findViewById(R.id.folder_intro);
		mQuestionCount = (TextView) findViewById(R.id.count);
	}
	
	private void setStyle() {
		setOrientation(VERTICAL);
		setBackgroundColor(getResources().getColor(android.R.color.white));
	}
	
	public void show(MFavoriteFolder folder) {
		if (folder == null) {
			return;
		}
		mFolderName.setText(folder.name);
		mFolderIntro.setText(folder.intro);
		mQuestionCount.setText("" + folder.questionCount);
		mFolderIntro.setVisibility(TextUtils.isEmpty(folder.intro) ? View.GONE : View.VISIBLE);
	}

}
