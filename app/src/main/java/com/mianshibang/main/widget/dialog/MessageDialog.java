package com.mianshibang.main.widget.dialog;

import android.content.Context;
import android.widget.TextView;

import com.mianshibang.main.R;

public class MessageDialog extends BaseDialog {
	
	private TextView mMessage;

	public MessageDialog(Context context) {
		super(context);
		
		hideTitle();
		setContentView(R.layout.dialog_message_confirm);
		
		mMessage = (TextView) findViewById(R.id.dialog_message);
	}
	
	public void setMessage(String message) {
		mMessage.setText(message);
	}
	
	public void setMessage(int resid) {
		mMessage.setText(resid);
	}
	
}
