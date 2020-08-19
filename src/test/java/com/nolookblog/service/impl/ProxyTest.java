package com.nolookblog.service.impl;

import com.github.rholder.retry.*;
import com.google.common.base.Predicates;
import com.nolookblog.Application;
import com.nolookblog.proxy.CGLibProxy;
import com.nolookblog.proxy.DynamicProxy;
import com.nolookblog.service.DoSomethingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Mrrrrr10
 * @github https://github.com/Mrrrrr10
 * @blog https://nolookblog.com/
 * @description
 */

@Slf4j
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class ProxyTest {

	// @Autowired
	// private DoSomethingService staticProxy;

	@Autowired
	private DoSomethingService doSomethingService;

	@Test
	public void test() {
		// do1();
		// do2();
		// do3();
		// do4();
		// do5();
		do6();
	}

	/**
	 * 静态代理模式
	 *
	 * @return
	 */
	private void do1() {
		// staticProxy.doSomething();
	}

	/**
	 * 动态代理模式
	 */
	private void do2() {
		DoSomethingServiceImpl target = new DoSomethingServiceImpl();
		DoSomethingService dynamicProxy = (DoSomethingService) DynamicProxy.getProxy(target);

		dynamicProxy.doSomething();
	}

	/**
	 * 动态代理模式
	 */
	private void do3() {
		DoSomethingServiceImpl target = new DoSomethingServiceImpl();
		CGLibProxy cgLibProxy = new CGLibProxy();
		DoSomethingService cglibProxy = (DoSomethingService) cgLibProxy.getCglibProxy(target);
		cglibProxy.doSomething();
	}

	/**
	 * aop
	 */
	private void do4() {
		doSomethingService.doSomething();
	}

	/**
	 * spring retry
	 */
	private void do5() {
		doSomethingService.doSomething();
	}

	/**
	 * guava retrying
	 */
	private void do6() {
		// 定义重试机制
		Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
				// retryIf表示如果遇到异常重试源, 返回结果为false, 那么就重试

				// 抛出 runtime 异常、checked 异常时都会重试，但是抛出 error 不会重试
				.retryIfException()

				// 只会在抛 runtime 异常的时候才重试，checked 异常和error 都不重试
				.retryIfRuntimeException()

				// 只在发生特定异常的时候才重试
				// 比如NullPointerException 和 IllegalStateException 都属于 runtime 异常，也包括自定义的error
				.retryIfExceptionOfType(Exception.class)
				.retryIfException(Predicates.equalTo(new Exception()))
				.retryIfResult(Predicates.equalTo(false))

				// 等待策略: 每次请求(每次重试) 间隔5s
				.withWaitStrategy(WaitStrategies.fixedWait(5, TimeUnit.SECONDS))

				// 停止策略: 尝试请求6次
				.withStopStrategy(StopStrategies.stopAfterAttempt(6))

				// 时间限制: 某次请求不得超过2s, 类似: TimeLimiter timeLimiter = new SimpleTimeLimiter();
				.withAttemptTimeLimiter(AttemptTimeLimiters.fixedTimeLimit(2, TimeUnit.SECONDS))

				.build();

		// 定义请求实现
		Callable<Boolean> callable = new Callable<Boolean>() {
			int times = 0;

			/**
			 * 业务处理代码
			 *
			 * @return
			 * @throws Exception
			 */
			@Override
			public Boolean call() throws Exception {
				times++;

				if (times == 1) {
					log.error("第{}次模拟调用api接口失败", times);
					throw new ArrayIndexOutOfBoundsException();
				} else if (times == 2) {
					log.error("第{}次模拟调用api接口失败", times);
					throw new NullPointerException();
				} else if (times == 3) {
					log.error("第{}次模拟调用api接口失败", times);
					throw new Exception();
				} else if (times == 4) {
					log.error("第{}次模拟调用api接口失败", times);
					throw new RuntimeException();
				} else if (times == 5) {
					log.error("第{}次模拟调用api接口失败", times);
					return false;
				} else {
					log.info("第{}次模拟调用api接口成功", times);
					return true;
				}
			}
		};

		// 利用重试器调用请求, 并且获取结果
		Boolean result = null;
		try {
			// 拿到返回值
			result = retryer.call(callable);
		} catch (ExecutionException e) {
			log.error("error", e);
		} catch (RetryException e) {
			log.error("error", e);
		}

		log.info("调用成功输出结果: {}", result);
	}
}