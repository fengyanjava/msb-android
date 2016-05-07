package com.mianshibang.main.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.api.FavoriteApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.MFavoriteFolder;
import com.mianshibang.main.model.MQuestion;
import com.mianshibang.main.model.dto.IdResult;
import com.mianshibang.main.utils.EventBusUtils;
import com.mianshibang.main.utils.ToastUtils;
import com.mianshibang.main.utils.UiNavigation;
import com.mianshibang.main.widget.dialog.AlertDialog;
import com.mianshibang.main.widget.dialog.CreateFavoriteFolderDialog;
import com.mianshibang.main.widget.dialog.ProgressDialog;

public class FavoriteFolderItemView extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {
	
	
	private TextView mFolderName;
	private TextView mQuestionCount;
	private LinearLayout mFolderContainer;
	
	private View mBottom;
	private View mItem;
	
	private MFavoriteFolder mFavoriteFolder;
	
	private int mPadding;
	
	private ProgressDialog mProgressDialog;
	
	private Object mSubscriber;
	private static final String Event_Folder_Update = "onEventFolderUpdate";
	
	public FavoriteFolderItemView(Context context) {
		this(context, null);
	}

	public FavoriteFolderItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater.from(context).inflate(R.layout.favorite_folder_item, this, true);
		
		mPadding = getResources().getDimensionPixelSize(R.dimen.category_item_space);
		
		setLayoutStyle();
		findViews();
	}
	
	private void setLayoutStyle() {
		setOrientation(VERTICAL);
	}
	
	private void findViews() {
		mFolderName = (TextView) findViewById(R.id.folder_name);
		mQuestionCount = (TextView) findViewById(R.id.count);
		mFolderContainer = (LinearLayout) findViewById(R.id.folder_container);
		
		mBottom = findViewById(R.id.bottom);
		mItem = findViewById(R.id.item);
		
		mItem.setOnClickListener(this);
		mItem.setOnLongClickListener(this);
	}
	
	public void show(MFavoriteFolder folder, boolean isLastItem) {
		mFavoriteFolder = folder;
		
		mBottom.setVisibility(isLastItem ? View.VISIBLE : View.GONE);
		
		mFolderName.setText(folder.name);
		mQuestionCount.setText(folder.questionCount + "");
		mFolderContainer.removeAllViews();
		
		if (folder.questions == null || folder.questions.isEmpty()) {
			return;
		}
		
		for (int i = 0; i < folder.questions.size(); i++) {
			MQuestion q = folder.questions.get(i);
			boolean isLast = i == folder.questions.size() - 1;
			mFolderContainer.addView(createQuestionTextView(q, isLast));
		}
	}
	
	private TextView createQuestionTextView(MQuestion question, boolean isLastItem) {
		TextView textView = new TextView(getContext());
		textView.setMaxLines(3);
		textView.setEllipsize(TruncateAt.END);
		String description = removeBlankLine(question.description);
		textView.setText(description);
		
		int vPadding = getResources().getDimensionPixelSize(R.dimen.question_item_padding_v);
		textView.setPadding(mPadding, vPadding, mPadding, isLastItem ? vPadding : 0);
		
		int textColor = getResources().getColor(R.color.favorite_folder_question_desc);
		textView.setTextColor(textColor);
		
		int textSize = getResources().getDimensionPixelSize(R.dimen.favorite_folder_question_text_size);
		textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		
		return textView;
	}
	
	private static String removeBlankLine(String text) {
		if (text == null) {
			return null;
		}
		
		String[] strings = text.trim().split("\n+");
		StringBuilder result = new StringBuilder();
		
		for (String str : strings) {
			if (TextUtils.isEmpty(str.trim())) {
				continue;
			}
			if (str.trim().matches("^　+$")) {
				continue;
			}
			result.append("\n");
			result.append(str);
		}
		
		return result.toString().trim();
		
	}

	@Override
	public void onClick(View v) {
		if (mFavoriteFolder == null) {
			return;
		}
		UiNavigation.startFavoriteDetail(getContext(), mFavoriteFolder.id);
	}
	
	public void subscribUpdateEvent(Object subscriber) {
		mSubscriber = subscriber;
	}
	
	@Override
	public boolean onLongClick(View v) {
		if (mFavoriteFolder == null) {
			return false;
		}
		showOptionDialog();
		return true;
	}
	
	private void showOptionDialog() {
		AlertDialog dialog = new AlertDialog(getContext());
		dialog.setTitleEx(mFavoriteFolder.name);
		dialog.setItems(R.array.favorite_folder_option, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					showEditDialog();
				} else if (which == 1) {
					showDeleteHintDialog();
				}
			}
		});
		dialog.show();
	}
	
	private void showEditDialog() {
		CreateFavoriteFolderDialog dialog = new CreateFavoriteFolderDialog(getContext(), mFavoriteFolder);
		dialog.subscribeEvent(this);
		dialog.show();
	}
	
	public void onEventFolderUpdate() {
		EventBusUtils.postEvent(mSubscriber, Event_Folder_Update);
	}
	
	private void showDeleteHintDialog() {
		AlertDialog dialog = new AlertDialog(getContext());
		dialog.setTitleEx(R.string.delete_favorite_folder);
		dialog.setMessage(R.string.delete_favorite_folder_hint);
		dialog.setNegativeButton(true, null, null);
		dialog.setPositiveButton(true, R.string.delete, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				realDeleteFolder();
			}
		});
		dialog.show();
	}
	
	private void realDeleteFolder() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getContext());
		}
		
		mProgressDialog.show(R.string.delete_favorite_folder);
		
		FavoriteApis.deleteFavoriteFolder(mFavoriteFolder.id, new ResponseHandler<IdResult>() {
			@Override
			public void onSuccess(IdResult data) {
				if (data.isSucceeded()) {
					ToastUtils.show("删除成功");
					mFavoriteFolder.isDeleted = true;
					EventBusUtils.postEvent(mSubscriber, Event_Folder_Update);
				}
			}
			
			@Override
			public void onFinish() {
				mProgressDialog.dismiss();
			}
		});
	}

}
