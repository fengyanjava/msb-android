package com.mianshibang.main.widget.dialog;

import com.mianshibang.main.R;
import com.mianshibang.main.utils.EventBusUtils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;

public class FolderMorePopupWindow extends PopupWindow implements OnClickListener {
	
	private Object mSubscriber;
	private static final String Event_Edit_Folder = "onEventEditFolder";
	private static final String Event_Delete_Folder = "onEventDeleteFolder";
	
	public FolderMorePopupWindow(Context context) {
		super(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		View contentView = View.inflate(context, R.layout.favorite_folder_more_popup, null);
		setContentView(contentView);

		setOutsideTouchable(true);
		setFocusable(true);
		setBackgroundDrawable(new ColorDrawable(0x00FFFFFF));
		
		contentView.findViewById(R.id.favorite_folder_edit).setOnClickListener(this);
		contentView.findViewById(R.id.favorite_folder_delete).setOnClickListener(this);
	}
	
	public void subscribeEvent(Object subscriber) {
		mSubscriber = subscriber;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.favorite_folder_edit:
			EventBusUtils.postEvent(mSubscriber, Event_Edit_Folder);
			break;
			
		case R.id.favorite_folder_delete:
			EventBusUtils.postEvent(mSubscriber, Event_Delete_Folder);
			break;

		default:
			break;
		}
		dismiss();
	}
	
	
}
