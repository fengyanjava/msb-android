package com.mianshibang.main.widget.dialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.mianshibang.main.R;
import com.mianshibang.main.model.MVersion;
import com.mianshibang.main.utils.ToastUtils;
import com.mianshibang.main.utils.UIUtils;
import com.mianshibang.main.utils.UiNavigation;

public class NewVersionDialog extends BaseDialog implements View.OnClickListener {
	
	private MVersion mVersion;
	private UpdateTask mUpdateTask;

	public NewVersionDialog(Context context, MVersion version) {
		super(context);
		
		mVersion = version;
		
		setTitle("发现新版本-" + mVersion.name);
		setContentView(R.layout.dialog_new_version);
		initView();
		
		setNegativeButton(true, "以后再说", null);
		setPositiveButton(false, "立即更新", this);
		setSingleButton(false, null, this);
		setCancelable(false);
		setCanceledOnTouchOutside(false);
	}
	
	private void initView() {
		TextView description = (TextView) findViewById(R.id.description);
		TextView fileSize = (TextView) findViewById(R.id.file_size);
		
		description.setText(mVersion.description);
		fileSize.setText("安装包大小: " + mVersion.sizeString);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case Positive_Button_Id:
			onUpdateClick();
			break;
			
		case Signle_Button_Id:
			cancelUpdateTask();
			break;

		default:
			break;
		}
	}
	
	private void cancelUpdateTask() {
		if (mUpdateTask != null) {
			mUpdateTask.cancel();
		}
	}
	
	private void onUpdateClick() {
		hideButtons();
		showSingleButton();
		mUpdateTask = new UpdateTask();
		mUpdateTask.execute();
	}
	
	private void onDownloadSuccess(String path) {
		dismiss();
		UiNavigation.installAPK(getContext(), path);
	}
	
	private void onDownloadFail() {
		ToastUtils.show("下载更新未完成");
		showButtons();
		hideSingleButton();
	}
	
	private class UpdateTask extends AsyncTask<Void, Integer, String> {
		
		private boolean mCancel;
		
		public void cancel() {
			mCancel = true;
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				HttpURLConnection conn = (HttpURLConnection) new URL(mVersion.path).openConnection();
				conn.setConnectTimeout(10000);
				conn.setReadTimeout(20000);
				conn.setUseCaches(false);
				
				BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
				
				File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "mianshibang_" + mVersion.version + ".apk");
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
				
				byte[] buffer = new byte[1024 * 10];
				int length = 0;
				int count = 0;
				
				while(!mCancel && (length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
					count += length;
					
					publishProgress(count);
				}
				
				in.close();
				conn.disconnect();
				out.flush();
				out.close();
				
				if (mCancel) {
					if (file.exists()) {
						file.delete();
					}
					return null;
				}
				
				return file.getAbsolutePath();
			} catch (Exception e) {
				return null;
			}
		}
		
		@Override
		protected void onPreExecute() {
			onProgressUpdate(0);
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			int count = values[0];
			int progress = (int) (1.0 * count / mVersion.size * 100);
			String text = UIUtils.getString(R.string.download_progress, progress);
			setSingleButtonText(text);
		}
		
		@Override
		protected void onPostExecute(String path) {
			if (path == null) {
				onDownloadFail();
			} else {
				onDownloadSuccess(path);
			}
		}
		
	}

}
