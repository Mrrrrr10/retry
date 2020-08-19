package com.nolookblog.proxy;

import com.nolookblog.common.constant.RetryConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.time.LocalTime;

/**
 * @author Mrrrrr10
 * @github https://github.com/Mrrrrr10
 * @blog https://nolookblog.com/
 * @description CGLIB动态代理：也叫作子类代理 如果目标对象没有实现接口, 如何对目标对象进行扩展？
 *
 * 上面的静态代理和动态代理模式，都是要求：目标对象必须是实现一个接口的目标对象。
 * 但是有时候目标对象只是一个单独的对象，并没有实现任何的接口，这个时候就可以使用以目标对象子类的方式，实现代理，这种方法就叫做：Cglib 代理。
 *
 * Cglib 代理，也叫作子类代理，它是在内存中构建一个子类对象从而实现对目标对象功能的扩展。
 *
 * Cglib 是一个强大的高性能的代码生成包，它可以在运行期扩展 java 类与实现 java 接口，
 * 它广泛的被许多 AOP 的框架使用，例如 Spring AOP 和 synaop，为他们提供方法的 interception（拦截）
 *
 * JDK 动态代理和 CGLIB 字节码生成代理的区别？
 * - JDK 动态代理只能对接口或实现了接口的类生成代理，而不能针对类
 * - CGLIB 是针对类实现代理，主要是对指定的类生成一个子类，覆盖其中的方法。因为是继承，所以该类或方法不能声明成 final
 *
 * 小结：
 * - cglib代理，也叫做子类代理，运行时期对目标对象动态生成子类。
 * - 要求：目标对象不能为final，否则报错。因为生成不了子类。
 * - 目标对象的方法如果为final，static， 就不会执行事件处理程序，就不能扩展。会直接执行目标对象的方法。
 */

@Slf4j
public class CGLibProxy implements MethodInterceptor {

	/**
	 * 需要代理的目标对象
	 */
	private Object target;

	/**
	 * 拦截
	 *
	 * @param obj
	 * @param method
	 * @param arr
	 * @param proxy
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object intercept(Object obj, Method method, Object[] arr, MethodProxy proxy) throws Throwable {
		int times = 0;

		while (times < RetryConstants.MAX_TIMES) {
			try {
				return method.invoke(target, arr);
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
	 * 获取代理对象
	 *
	 * @param target
	 * @return
	 */
	public Object getCglibProxy(Object target) {
		this.target = target;
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(target.getClass());
		enhancer.setCallback(this);

		return enhancer.create();
	}
}
