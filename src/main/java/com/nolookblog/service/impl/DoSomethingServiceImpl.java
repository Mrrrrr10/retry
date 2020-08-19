package com.nolookblog.service.impl;

import com.nolookblog.annotation.Retry;
import com.nolookblog.common.em.ResponseEnum;
import com.nolookblog.common.exception.BusinessException;
import com.nolookblog.common.exception.RetryException;
import com.nolookblog.service.DoSomethingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mrrrrr10
 * @github https://github.com/Mrrrrr10
 * @blog https://nolookblog.com/
 * @description
 */

@Slf4j
@Service
public class DoSomethingServiceImpl implements DoSomethingService {

	private static AtomicLong times = new AtomicLong();

	/**
	 * doSomething
	 *
	 * @Retry(maxTimes = 4, interval = 2)
	 * @Retryable(maxAttempts = 4)：重试4次，默认3次
	 * @Retryable(value = RetryException.class)：特定异常重试，默认为空重试全部异常
	 * @Retryable(include = RetryException.class)：特定异常重试
	 * @Retryable(exclude = RetryException.class)：排除异常不重试
	 * @Retryable(maxAttempts = 10, backoff = @Backoff(value = 2000L, delay = 5000L, multiplier = 2, maxDelay = 11000L))：重试策略
	 * - value：等待时长，默认1秒
	 * - delay：延迟时长
	 * - multiplier：延迟倍数，例如：delay=5000L，multiplier=2，那么第一次等待5s，第二次等待10s，以此类推
	 * - maxDelay：最大延迟时长，当delay结合multiplier大于maxDelay时，等待时长变为maxDelay
	 * - random：是否启用随机退避策略，默认false。设置为true时启用退避策略，重试延迟时间将是delay和maxDelay间的一个随机数。
	 * 			 设置该参数的目的是重试的时候避免同时发起重试请求，造成Ddos攻击。
	 *
	 * @Recover：当全部尝试都失败时执行，修饰重试失败后处理的方法
	 * - 生效条件：1、被 @Retryable 标记的方法在同一个类中
	 * 			 2、异常一定要是 @Retryable 方法里抛出的异常
	 * 			 3、返回值类型必须和 @Retryable 修饰的方法一致
	 *
	 */
	@Retryable(maxAttempts = 2)
	@Override
	public void doSomething() {
		long doSomethingTimes = times.incrementAndGet();
		if (doSomethingTimes % 3 != 0){
			log.warn("发生异常, 第{}次, 时间:{}", doSomethingTimes, LocalDateTime.now());

			throw new RetryException("达到最大重试次数");
		}
		log.info("success");
	}

	@Recover
	public void recover(Exception e) {
		log.error("达到最大重试次数，记录日志：{}", e.getMessage());
	}


}
