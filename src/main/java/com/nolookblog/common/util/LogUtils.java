package com.nolookblog.common.util;

/**
 * @author Mrrrrr10
 * @description
 */

public class LogUtils {

	/**
	 * 格式化异常错误信息
	 *
	 * @param t
	 * @return
	 */
	public static String formatExceptionMsg(Throwable t) {
		StringBuffer sb = new StringBuffer();
		sb.append(t.getClass().getName()).append(":").append(t.getMessage());
		for (StackTraceElement stackTraceElement : t.getStackTrace()) {
			sb.append("\n").append("    ").append(stackTraceElement.toString());
		}

		return sb.toString();
	}
}
