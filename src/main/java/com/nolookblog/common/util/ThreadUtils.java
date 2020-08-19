package com.nolookblog.common.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mrrrrr10
 * @github https://github.com/Mrrrrr10
 * @blog https://nolookblog.com/
 * @description
 */

public class ThreadUtils {

	/**
	 * 测试方法
	 *
	 * @param serverNums 模拟请求数量
	 * @param per        每台服务请求
	 */
	public static void test(int serverNums, int per) {
		// 倒计数器：用于模拟高并发 juc CountDownLatch 主线分布式锁，线程的阻塞和唤醒jdk5 juc编程提供并发编程类
		CountDownLatch countDownLatch = new CountDownLatch(1);

		// 线程集合
		List<Thread> threads = new ArrayList<>();

		//模拟服务器的数量
		for (int i = 0; i < serverNums; i++) {
			// 模拟每台服务器发起请求的数量
			for (int i1 = 0; i1 < per; i1++) {
				Thread thread = new Thread(() -> {
					try {
						// 等待countdownlatch值为0，也就是其他线程就绪后，在运行后续的代码
						countDownLatch.await();

						// 执行动作
						post();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				});

				// 添加线程到集合中
				threads.add(thread);
				// 启动线程
				thread.start();
			}
		}

		// 并发执行所有请求
		countDownLatch.countDown();
		threads.forEach((e) -> {
			try {
				e.join();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
	}

	public static void main(String[] args) {
		test(10, 200);
	}

	private static void post() {
//		String tokenUrl = "http://localhost:8080/idempotent/token";
//		String testUrl = "http://localhost:8080/idempotent/test";
//
////		for (int i = 0; i < 100; i++) {
////			ResponseEntity<String> responseEntity = RestTemplateUtils.get(tokenUrl, String.class);
////			JSONObject response = JSONObject.parseObject(responseEntity.getBody());
////			String token = response.getString("data");
//
//		// 请求头
//		HttpHeaders requestHeaders = new HttpHeaders();
//		requestHeaders.add("RequestToken", "9813277f9f9844bcafc7a823400d9cb6");
//
//		// 请求
//		ResponseEntity<String> responseEntity2 = RestTemplateUtils.get(testUrl, requestHeaders, String.class);
//		System.out.println("响应: " + responseEntity2.getBody());
////		}
	}

}
