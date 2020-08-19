package com.nolookblog.proxy;

import com.nolookblog.common.constant.RetryConstants;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalTime;

/**
 * @author Mrrrrr10
 * @github https://github.com/Mrrrrr10
 * @blog https://nolookblog.com/
 * @description 代理对象：jdk动态代理，程序运行时创建代理 代理类在程序运行时（内存）创建的代理方式，被成为动态代理。
 *
 * JDK 动态代理有以下特点:
 *
 * 1、代理对象，不需要实现接口
 * 2、目标对象，一定要实现接口，所以也叫做接口代理
 * 3、运行时期，动态生成字节码对象
 *
 * 小结：
 *
 * JDK 动态代理也叫作接口代理。
 * JDK 动态代理，要求目标对象一定要实现接口，否则不能用动态代理。
 * 如果目标对象就是没有实现接口，还想对目标对象某个方法进行扩展，怎么办？使用CGLIB 子类代理
 */

@Slf4j
public class DynamicProxy implements InvocationHandler {

	private final Object subject;

	public DynamicProxy(Object subject) {
		this.subject = subject;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		int times = 0;

		while (times < RetryConstants.MAX_TIMES) {
			try {
				return method.invoke(subject, args);
			} catch (Exception e) {
				times++;
				if (times >= RetryConstants.MAX_TIMES) {
					throw new RuntimeException(e);
				}
			}

			// 延时一秒
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 获取动态代理对象
	 *
	 * @param target 目标对象
	 */
	public static Object getProxy(Object target) {
		InvocationHandler handler = new DynamicProxy(target);
		return Proxy.newProxyInstance(
				handler.getClass().getClassLoader(),	// 类加载器（使用默认的类加载器）
				target.getClass().getInterfaces(), 		// 接口类型数组
				handler									// 事件处理程序
		);
	}

}
