package com.mianshibang.main.api;

import java.util.ArrayList;
import java.util.List;

import com.mianshibang.main.http.MParameter;
import com.mianshibang.main.http.MVolley;
import com.mianshibang.main.http.ResponseDecorator;
import com.mianshibang.main.http.ResponseHandler;
import com.mianshibang.main.http.SilentRespHandler;
import com.mianshibang.main.model.MUser;
import com.mianshibang.main.model.MUserDetail;
import com.mianshibang.main.model.dto.BaseDTO;
import com.mianshibang.main.model.dto.UserLogin;
import com.mianshibang.main.utils.EventBusUtils;

public class UserApis extends BaseApis {
	
	public static void autoLogin() {
		if (CurrentUserApis.isUnLogin()) {
			return;
		}
		login(null, null, new SilentRespHandler<UserLogin>());
	}

	public static void login(String account, String password, ResponseHandler<UserLogin> handler) {
		MVolley.sendRequest(login, UserLogin.class, getUserLoginDecorator(handler), 
				new MParameter("account", account), new MParameter("password", password));
	}

	public static void register(String nickname, String email, String password, ResponseHandler<UserLogin> handler) {
		MVolley.sendRequest(register, UserLogin.class, getUserLoginDecorator(handler), 
				new MParameter("nickname", nickname), new MParameter("email", email), new MParameter("password", password));
	}
	
	public static void updateUser(MUser user, ResponseHandler<BaseDTO> handler) {
		if (user == null) {
			return;
		}
		
		List<MParameter> params = new ArrayList<MParameter>();
		
		if (user.nickname != null) {
			params.add(new MParameter("nickname", user.nickname));
		}
		if (user.gender != null) {
			params.add(new MParameter("gender", user.gender));
		}
		if (user.password != null) {
			params.add(new MParameter("password", user.password));
		}
		if (user.perfession != null) {
			params.add(new MParameter("perfession", user.perfession));
		}
		if (user.detail.intro != null) {
			params.add(new MParameter("intro", user.detail.intro));
		}
		if (user.detail.birthday != null) {
			params.add(new MParameter("birthday", user.detail.birthday));
		}
		if (user.detail.province != null) {
			params.add(new MParameter("province", user.detail.province));
		}
		if (user.detail.city != null) {
			params.add(new MParameter("city", user.detail.city));
		}
		if (user.detail.weibo != null) {
			params.add(new MParameter("weibo", user.detail.weibo));
		}
		if (user.detail.blog != null) {
			params.add(new MParameter("blog", user.detail.blog));
		}
		if (user.detail.qq != null) {
			params.add(new MParameter("qq", user.detail.qq));
		}
		
		if (params.isEmpty()) {
			return;
		}
		
		MParameter[] args = params.toArray(new MParameter[0]);
		
		MVolley.sendRequest(update_user, BaseDTO.class, getUpdateUserDecorator(user, handler), args);
	}
	
	private static ResponseDecorator<BaseDTO> getUpdateUserDecorator(final MUser user, ResponseHandler<BaseDTO> handler) {
		return new ResponseDecorator<BaseDTO>(handler) {
			@Override
			public void onSuccess(BaseDTO data) {
				if (data.isSucceeded()) {
					updateCurrentUser(user);
				}
				super.onSuccess(data);
			}
		};
	}
	
	private static void updateCurrentUser(MUser user) {
		MUser current = CurrentUserApis.get();
		current.detail = current.detail == null ? new MUserDetail() : current.detail;
		
		if (user.nickname != null) {
			current.nickname = user.nickname;
		}
		if (user.gender != null) {
			current.gender = user.gender;
		}
		if (user.password != null) {
			current.password = user.password;
		}
		if (user.perfession != null) {
			current.perfession = user.perfession;
		}
		if (user.detail.intro != null) {
			current.detail.intro = user.detail.intro;
		}
		if (user.detail.birthday != null) {
			current.detail.birthday = user.detail.birthday;
		}
		if (user.detail.province != null) {
			current.detail.province = user.detail.province;
		}
		if (user.detail.city != null) {
			current.detail.city = user.detail.city;
		}
		if (user.detail.weibo != null) {
			current.detail.weibo = user.detail.weibo;
		}
		if (user.detail.blog != null) {
			current.detail.blog = user.detail.blog;
		}
		if (user.detail.qq != null) {
			current.detail.qq = user.detail.qq;
		}
		
		CurrentUserApis.set(current);
	}
	
	private static ResponseDecorator<UserLogin> getUserLoginDecorator(ResponseHandler<UserLogin> handler) {
		return new ResponseDecorator<UserLogin>(handler){
			@Override
			public void onSuccess(UserLogin data) {
				if (data.isSucceeded()) {
					CurrentUserApis.set(data.user);
					EventBusUtils.postLoginEvent();
				}
				
				super.onSuccess(data);
			}
		};
	}
}
