package both.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import sun.reflect.Reflection;
import cn.com.bankit.phoenix.session.Session;
import cn.com.bankit.phoenix.session.SessionManager;

/**
 * Logger工具类
 * 
 */
public class LoggerUtil {

	/**
	 * logger
	 */
	private static Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

	/**
	 * 柜员日志格式
	 */
	private static String TellerLogger_style2 = "tellerlog";

	/**
	 * 格式
	 */
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	/**
	 * 构造哈思楠
	 */
	public LoggerUtil() {
		// do nothing
	}

	/**
	 * 获取当前日志记录对象
	 * 
	 * @return
	 */
	private static Logger getLogger() {

		// 获取session
		Session session = SessionManager.getInstance()
				.getSessionFromCurrentThread();

		String tellerNo = "";
		String orgNo = "";
		try {
			tellerNo = (String) session.getData("tellerId");
			orgNo = (String) session.getData("orgId");
		} catch (Exception e) {
			tellerNo = "";
			orgNo = "";
		}

		// 如果柜员号和网点号有一个为空或没有则指定两个都为unkown
		StringBuilder sb = new StringBuilder();
		sb.append("teller/");
		sb.append(sdf.format(new Date()));
		sb.append("/");
		if (tellerNo == null || orgNo == null || "".equals(tellerNo)
				|| "".equals(orgNo)) {
			tellerNo = "unknow";
			orgNo = "unknow";
			sb.append("unknow");
		} else {
			sb.append(orgNo);
			sb.append("/");
			sb.append(tellerNo);
		}

		// sb.append(".log");
		MDC.put(TellerLogger_style2, sb.toString());
		return logger;
	}

	/**
	 * 记录debug日志
	 * 
	 * @param message
	 *            自定义异常信息
	 * @param e
	 *            异常
	 */
	public static void debug(String message, Exception e) {
		String clazzName = Reflection.getCallerClass(2).getName();
		Logger logger = getLogger();
		if (logger != null) {
			logger.debug(message);
			if (e != null) {
				logger.debug(e.toString());
				StackTraceElement[] stackTraceElements = e.getStackTrace();
				if (stackTraceElements != null && stackTraceElements.length > 0) {
					for (StackTraceElement ste : stackTraceElements)
						logger.debug("[{}] - {}", clazzName, ste.toString());
				}
			}
		}
	}

	/**
	 * 记录debug日志
	 * 
	 * @param message
	 */
	public static void debug(String message) {
		Logger logger = getLogger();
		if (logger != null) {
			String clazzName = Reflection.getCallerClass(2).getName();
			logger.debug("[{}] - {}", clazzName, message);
		}
	}

	/**
	 * 记录info日志
	 * 
	 * @param message
	 *            自定义异常信息
	 * @param e
	 *            异常
	 */
	public static void info(String message, Exception e) {
		String clazzName = Reflection.getCallerClass(2).getName();
		Logger logger = getLogger();
		if (logger != null) {
			logger.info(message);
			if (e != null) {
				logger.info(e.toString());
				StackTraceElement[] stackTraceElements = e.getStackTrace();
				if (stackTraceElements != null && stackTraceElements.length > 0) {
					for (StackTraceElement ste : stackTraceElements)
						logger.info("[{}] - {}", clazzName, ste.toString());
				}
			}
		}
	}

	/**
	 * 记录info日志
	 * 
	 * @param message
	 */
	public static void info(String message) {
		String clazzName = Reflection.getCallerClass(2).getName();
		Logger logger = getLogger();
		if (logger != null) {
			logger.info("[{}] - {}", clazzName, message);
		}
	}

	/**
	 * 记录error日志
	 * 
	 * @param message
	 *            自定义异常信息
	 * @param e
	 *            异常
	 */
	public static void error(String message, Throwable e) {
		String clazzName = Reflection.getCallerClass(2).getName();
		Logger logger = getLogger();
		if (logger != null) {
			logger.error(message);
			if (e != null) {
				logger.error(e.toString());
				StackTraceElement[] stackTraceElements = e.getStackTrace();
				if (stackTraceElements != null && stackTraceElements.length > 0) {
					for (StackTraceElement ste : stackTraceElements)
						logger.error("[{}] - {}", clazzName, ste.toString());
				}
			}
			e.printStackTrace();
		}
	}

	/**
	 * 记录error日志
	 * 
	 * @param message
	 *            自定义异常信息
	 * @param e
	 *            异常
	 */
	public static void error(String message, Exception e) {
		String clazzName = Reflection.getCallerClass(2).getName();
		Logger logger = getLogger();
		if (logger != null) {
			logger.error(message);
			if (e != null) {
				logger.error(e.toString());
				StackTraceElement[] stackTraceElements = e.getStackTrace();
				if (stackTraceElements != null && stackTraceElements.length > 0) {
					for (StackTraceElement ste : stackTraceElements)
						logger.error("[{}] - {}", clazzName, ste.toString());
				}
			}
			e.printStackTrace();
		}
	}

	/**
	 * 记录error日志
	 * 
	 * @param message
	 */
	public static void error(String message) {
		String clazzName = Reflection.getCallerClass(2).getName();
		Logger logger = getLogger();
		if (logger != null) {
			logger.error("[{}] - {}", clazzName, message);
		}
	}

	/**
	 * 记录warn日志
	 * 
	 * @param message
	 */
	public static void warn(String message) {
		String clazzName = Reflection.getCallerClass(2).getName();
		Logger logger = getLogger();
		if (logger != null) {
			logger.warn("[{}] - {}", clazzName, message);
		}
	}

	/**
	 * 记录warn日志
	 * 
	 * @param message
	 *            自定义异常信息
	 * @param e
	 *            异常
	 */
	public static void warn(String message, Exception e) {
		String clazzName = Reflection.getCallerClass(2).getName();
		Logger logger = getLogger();
		if (logger != null) {
			logger.warn(message);
			if (e != null) {
				logger.warn(e.toString());
				StackTraceElement[] stackTraceElements = e.getStackTrace();
				if (stackTraceElements != null && stackTraceElements.length > 0) {
					for (StackTraceElement ste : stackTraceElements)
						logger.warn("[{}] - {}", clazzName, ste.toString());
				}
			}
		}
	}
}
