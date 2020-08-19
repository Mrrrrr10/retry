package com.nolookblog.common.entity;

import com.nolookblog.common.em.ResponseEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Mrrrrr10
 * @description 统一响应类(构造器私有, 外部不可以直接创建, 只可以调用统一返回类的静态方法返回对象)
 */

@Data
public class CommonResponse<T> implements Serializable {

	/**
	 * 响应状态码
	 */
	private Integer code;

	/**
	 * 响应信息
	 */
	private String message;

	/**
	 * 响应数据
	 */
	private T data;

	/**
	 * 无参构造器
	 */
	private CommonResponse() {
		this.code = ResponseEnum.C_SUCCESS.getCode();
		this.message = ResponseEnum.C_SUCCESS.getMessage();
	}

	/**
	 * 有参构造器
	 *
	 * @param data
	 */
	private CommonResponse(T data) {
		this.code = ResponseEnum.C_SUCCESS.getCode();
		this.message = ResponseEnum.C_SUCCESS.getMessage();
		this.data = data;
	}

	/**
	 * 有参构造器
	 *
	 * @param responseEnum
	 */
	private CommonResponse(ResponseEnum responseEnum) {
		this.code = responseEnum.getCode();
		this.message = responseEnum.getMessage();
	}

	/**
	 * 通用返回成功的静态方法（没有返回结果）
	 *
	 * @return
	 */
	public static CommonResponse success() {
		return new CommonResponse();
	}

	/**
	 * 通用返回成功的静态方法（有返回结果）
	 *
	 * @param data
	 * @param <T>
	 * @return
	 */
	public static <T> CommonResponse<T> success(T data) {
		return new CommonResponse<>(data);
	}

	/**
	 * 通用返回失败的静态方法
	 *
	 * @param responseEnum
	 * @param <T>
	 * @return
	 */
	public static <T> CommonResponse<T> failure(ResponseEnum responseEnum) {
		return new CommonResponse<>(responseEnum);
	}
}

