package com.nolookblog.annotation;

import java.lang.annotation.*;

/**
 * @author Mrrrrr10
 * @github https://github.com/Mrrrrr10
 * @blog https://nolookblog.com/
 * @description
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Retry {

	/**
	 * 最大重试次数
	 *
	 * @return
	 */
	int maxTimes() default 3;

	/**
	 * 重试间隔
	 *
	 * @return
	 */
	int interval() default 1;
}
