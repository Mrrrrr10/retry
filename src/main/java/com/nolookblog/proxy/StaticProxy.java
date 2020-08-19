package com.nolookblog.proxy;

import com.nolookblog.common.constant.RetryConstants;
import com.nolookblog.common.em.ResponseEnum;
import com.nolookblog.common.exception.BusinessException;
import com.nolookblog.service.DoSomethingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mrrrrr10
 * @github https://github.com/Mrrrrr10
 * @blog https://nolookblog.com/
 * @description 静态代理
 *
 * 1、代理对象需要实现与目标对象一样的接口
 * 2、代理对象需要维护一个目标对象
 * 3、代理对象一定会调用目标对象
 *
 * 优点：
 * - 在不修改原有代码的基础上，对指定的目标对象进行扩展！
 *
 * 缺点：
 * - 不方便维护：如果目标对象的接口添加了方法，代理对象也要修改
 * - 拓展性差：如果目标对象的接口方法有100个，而我们只需要扩展或修改其中一个，但是对于代理对象来说，所有的方法都要重写！
 */

@Slf4j
//@Service
public class StaticProxy implements DoSomethingService {

	private static AtomicLong times = new AtomicLong();

	/**
	 * doSomething
	 *
	 * @return
	 */
	@Override
	public void doSomething() {
		for (int retry = 1; retry <= RetryConstants.MAX_TIMES; retry++) {
			try {
				long doSomethingTimes = times.incrementAndGet();
				if (doSomethingTimes % 4 != 0){
					log.warn("发生异常, 第{}次", doSomethingTimes);
					throw new BusinessException(ResponseEnum.C_SERVER_ERROR);
				}
				log.info("success");
			} catch (BusinessException e) {

			}
		}
	}
}
