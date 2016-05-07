package com.mianshibang.main.http;

import com.mianshibang.main.model.dto.BaseDTO;

public class SilentRespHandler<T extends BaseDTO> extends ResponseHandler<T> {

	@Override
	public boolean isShowError() {
		return false;
	}
}
