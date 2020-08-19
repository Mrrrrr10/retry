package com.nolookblog.common.em;

/**
 * @author Mrrrrr10
 * @description 响应枚举, 前三位: 服务标识; 后三位: 异常标识
 */

public enum ResponseEnum {

	//////////////////////////////////// 通用编码 ////////////////////////////////////

	C_SUCCESS(200, "success"),
	C_SERVER_ERROR(500, "系统繁忙, 请稍后再试"),
	E_100101(100101, "重试次数耗尽"),

	;

	/**
	 * 响应状态码
	 */
	private Integer code;

	/**
	 * 响应信息
	 */
	private String message;

	ResponseEnum(Integer code, String msg) {
		this.code = code;
		this.message = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
