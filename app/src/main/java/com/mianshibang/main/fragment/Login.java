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

public class Login extends Base implements OnClickListener {
	
	private EditText mAccount;
	private EditText mPassword;
	private View mViewPassword;
	private Button mLogin;
	
	private View mGoRegister;
	
	public static Login newInstance() {
		return new Login();
	}

	@Override
	public int getContentLayout() {
		return R.layout.login;
	}

	@Override
	public void findViews(View view) {
		mAccount = (EditText) view.findViewById(R.id.account);
		mPassword = (EditText) view.findViewById(R.id.password);
		mViewPassword = view.findViewById(R.id.view_password);
		mLogin = (Button) view.findViewById(R.id.login);
		
		mGoRegister = view.findViewById(R.id.go_register);
	}

	@Override
	public void setListeners() {
		mLogin.setOnClickListener(this);
		mViewPassword.setOnClickListener(this);
		mGoRegister.setOnClickListener(this);
	}

	@Override
	public void process() {

	}
	
	private void doLogin() {
		String account = mAccount.getText().toString().trim();
		String password = mPassword.getText().toString().trim();
		
		if (TextUtils.isEmpty(account)) {
			ToastUtils.show(R.string.login_account_empty);
			return;
		}
		
		if (TextUtils.isEmpty(password)) {
			ToastUtils.show(R.string.login_password_empty);
			return;
		}
		
		showProgress("登录");
		mLogin.setEnabled(false);
		
		UserApis.login(account, password, new ResponseHandler<UserLogin>() {
			@Override
			public void onSuccess(UserLogin data) {
				if (data.isSucceeded()) {
					getActivity().finish();
				}
			}
			
			@Override
			public void onFinish() {
				mLogin.setEnabled(true);
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
		case R.id.login:
			doLogin();
			break;

		case R.id.view_password:
			togglePasswordVisible();
			break;

		case R.id.go_register:
			EventBusUtils.postEvent(getActivity(), LoginRegister.Event_Go_Register);
			break;

		default:
			break;
		}
	}

}
