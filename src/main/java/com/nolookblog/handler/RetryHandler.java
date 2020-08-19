package com.nolookblog.handler;

import com.nolookblog.annotation.Retry;
import com.nolookblog.common.em.ResponseEnum;
import com.nolookblog.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @author Mrrrrr10
 * @github https://github.com/Mrrrrr10
 * @blog https://nolookblog.com/
 * @description
 */

@Aspect
@Component
@Slf4j
public class RetryHandler {

	/**
	 * 切入点, 对注解进行拦截
	 */
	@Pointcut("@annotation(com.nolookblog.annotation.Retry)")
	public void retry() {

	}

	/**
	 * 环绕目标对象方法执行, 拦截到了做什么
	 *
	 * @param proceedingJoinPoint
	 * @param retry
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(retry)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint, Retry retry) throws Throwable {
		if (log.isDebugEnabled()) {
			log.info("RetryHandler开始执行重试操作");
		}

		// 方法标记对象
		Signature signature = proceedingJoinPoint.getSignature();
		if (!(signature instanceof MethodSignature)) {
			throw new IllegalArgumentException("the Annotation @Retry must used on method!");
		}

		// 获取注解参数
		int maxTimes = retry.maxTimes();
		int interval = retry.interval();

		for (int retryTimes = 1; retryTimes <= maxTimes; retryTimes++){
			try {
				return proceedingJoinPoint.proceed();
			} catch (Throwable throwable) {

			}

			Thread.sleep(interval * 1000);
		}

		throw new BusinessException(ResponseEnum.E_100101);
	}

}
