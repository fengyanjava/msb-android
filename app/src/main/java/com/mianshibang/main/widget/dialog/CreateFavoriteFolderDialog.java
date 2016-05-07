package com.mianshibang.main.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.mianshibang.main.R;
import com.mianshibang.main.api.FavoriteApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.MFavoriteFolder;
import com.mianshibang.main.model.MFavoriteFolderSimple;
import com.mianshibang.main.model.dto.BaseDTO;
import com.mianshibang.main.model.dto.IdResult;
import com.mianshibang.main.utils.EventBusUtils;
import com.mianshibang.main.utils.ToastUtils;

public class CreateFavoriteFolderDialog extends BaseDialog implements View.OnClickListener {
	
	private static final String Event_Folder_Create = "onEventFolderCreate";
	private static final String Event_Folder_Update = "onEventFolderUpdate";
	
	private MFavoriteFolder mFolder;
	
	private EditText mName;
	private EditText mIntro;
	
	private Object mSubscriber;
	
	private ProgressDialog mProgressDialog;
	
	public CreateFavoriteFolderDialog(Context context) {
		this(context, null);
	}
	
	public CreateFavoriteFolderDialog(Context context, MFavoriteFolder folder) {
		super(context);
		
		int title = folder == null ? R.string.create_favorite_folder : R.string.edit_favorite_folder;
		setTitle(title);
		setContentView(R.layout.dialog_create_folder);
		
		mName = (EditText) findViewById(R.id.name);
		mIntro = (EditText) findViewById(R.id.intro);
		
		mFolder = folder;
		
		if (mFolder != null) {
			mName.getText().append(mFolder.name);
			mIntro.getText().append(mFolder.intro);
		}
		
		setNegativeButton(true, null, null);
		
		setPositiveButton(false, null, this);
	}
	
	public void subscribeEvent(Object subscriber) {
		mSubscriber = subscriber;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case Positive_Button_Id:
			onPositiveClick();
			break;

		default:
			break;
		}
	}
	
	private void onPositiveClick() {
		String name = mName.getText().toString().trim();
		String intro = mIntro.getText().toString().trim();
		
		if (TextUtils.isEmpty(name)) {
			ToastUtils.show(R.string.create_folder_name_empty);
			return;
		}
		
		if (mFolder == null) {
			createFolder(name, intro);
		} else {
			updateFolder(name, intro);
		}
	}
	
	private void updateFolder(final String name, final String intro) {
		if (name.equals(mFolder.name) && intro.equals(mFolder.intro)) {
			ToastUtils.show("信息未变更");
			dismiss();
			return;
		}
		
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getContext());
		}
		
		mProgressDialog.show("修改收藏夹");
		
		MFavoriteFolder folder = new MFavoriteFolder();
		folder.id = mFolder.id;
		folder.name = name;
		folder.intro = intro;
		
		FavoriteApis.updateFavoriteFolder(folder, new ResponseHandler<BaseDTO>() {
			@Override
			public void onSuccess(BaseDTO data) {
				if (data.isSucceeded()) {
					mFolder.name = name;
					mFolder.intro = intro;
					EventBusUtils.postEvent(mSubscriber, Event_Folder_Update);
					ToastUtils.show("修改成功");
					dismiss();
				}
			}
			
			@Override
			public void onFinish() {
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
			}
		});
	}
	
	private void createFolder(final String name, String intro) {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(getContext());
		}
		
		mProgressDialog.show(R.string.create_favorite_folder);
		
		FavoriteApis.addFavoriteFolder(name, intro, new ResponseHandler<IdResult>() {
			@Override
			public void onSuccess(IdResult data) {
				if (data.isSucceeded()) {
					MFavoriteFolderSimple folder = new MFavoriteFolderSimple();
					folder.id = ((IdResult) data).data.id;
					folder.name = name;
					
					EventBusUtils.postEvent(mSubscriber, Event_Folder_Create, folder);
					
					dismiss();
				}
			}
			
			@Override
			public void onFinish() {
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
			}
		});
	}
	
}
