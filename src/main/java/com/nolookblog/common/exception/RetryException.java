package com.nolookblog.common.exception;

/**
 * @author Mrrrrr10
 * @github https://github.com/Mrrrrr10
 * @blog https://nolookblog.com/
 * @description
 */

public class RetryException extends RuntimeException {

	private String message;

	public RetryException(String message) {
		this.message = message;
	}

}
