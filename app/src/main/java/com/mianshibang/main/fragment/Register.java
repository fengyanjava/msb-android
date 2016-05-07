package com.mianshibang.main.fragment;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mianshibang.main.R;
import com.mianshibang.main.api.UserApis;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.model.dto.UserLogin;
import com.mianshibang.main.ui.LoginRegister;
import com.mianshibang.main.utils.EventBusUtils;
import com.mianshibang.main.utils.ToastUtils;
import com.mianshibang.main.utils.UIUtils;

public class Register extends Base implements OnClickListener {
	
	private EditText mNickname;
	private EditText mEmail;
	private EditText mPassword;
	private View mViewPassword;
	private Button mRegister;
	
	private View mGoLogin;
	
	public static Register newInstance() {
		return new Register();
	}

	@Override
	public int getContentLayout() {
		return R.layout.register;
	}

	@Override
	public void findViews(View view) {
		mNickname = (EditText) view.findViewById(R.id.nickname);
		mEmail = (EditText) view.findViewById(R.id.email);
		mPassword = (EditText) view.findViewById(R.id.password);
		mViewPassword = view.findViewById(R.id.view_password);
		mRegister = (Button) view.findViewById(R.id.register);
		
		mGoLogin = view.findViewById(R.id.go_login);
	}

	@Override
	public void setListeners() {
		mRegister.setOnClickListener(this);
		mViewPassword.setOnClickListener(this);
		mGoLogin.setOnClickListener(this);
	}

	@Override
	public void process() {
		togglePasswordVisible();
	}
	
	private void doRegister() {
		String nickname = mNickname.getText().toString().trim();
		String email = mEmail.getText().toString().trim();
		String password = mPassword.getText().toString().trim();
		
		if (TextUtils.isEmpty(nickname)) {
			ToastUtils.show(R.string.register_nickname_empty);
			return;
		}
		
		if (TextUtils.isEmpty(email)) {
			ToastUtils.show(R.string.register_email_empty);
			return;
		}
		
		if (TextUtils.isEmpty(password)) {
			ToastUtils.show(R.string.register_password_empty);
			return;
		}
		
		showProgress("注册");
		mRegister.setEnabled(false);
		
		UserApis.register(nickname, email, password, new ResponseHandler<UserLogin>() {
			@Override
			public void onSuccess(UserLogin data) {
				if (data.isSucceeded()) {
					ToastUtils.show("注册成功");
					getActivity().finish();
				}
			}
			
			@Override
			public void onFinish() {
				mRegister.setEnabled(true);
				dismissProgress();
			}
		});
	}
	
	private void togglePasswordVisible() {
		UIUtils.setPasswordVisible(mPassword, !mViewPassword.isSelected());
		mViewPassword.setSelected(!mViewPassword.isSelected());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register:
			doRegister();
			break;

		case R.id.view_password:
			togglePasswordVisible();
			break;

		case R.id.go_login:
			EventBusUtils.postEvent(getActivity(), LoginRegister.Event_Go_Login);
			break;

		default:
			break;
		}
	}

}
