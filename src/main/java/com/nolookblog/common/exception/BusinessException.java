package com.nolookblog.common.exception;


import com.nolookblog.common.em.ResponseEnum;

/**
 * @author Mrrrrr10
 * @description 业务异常类
 */

public class BusinessException extends RuntimeException {

	private ResponseEnum responseEnum;

	public BusinessException(ResponseEnum responseEnum) {
		super();
		this.responseEnum = responseEnum;
	}

	public ResponseEnum getResponseEnum() {
		return responseEnum;
	}

	public void setResponseEnum(ResponseEnum responseEnum) {
		this.responseEnum = responseEnum;
	}
}
