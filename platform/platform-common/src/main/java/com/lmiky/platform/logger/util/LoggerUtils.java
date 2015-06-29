package com.lmiky.platform.logger.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lmiky.platform.constants.Constants;
import com.lmiky.platform.util.BundleUtils;

/**
 * 日志工具
 * @author lmiky
 * @date 2013-5-10
 */
public class LoggerUtils {
	private static final String CONFIGNAME_PREFIX_OPETYPE = "opeType_";
	private static final String CONFIGNAME_PREFIX_POJONAME= "opePojo_";
	private static int exceptionStackLogNum = Constants.SYSTEM_EXCEPTION_STACKLOGNUM;
	private static final Log log = LogFactory.getLog("com.lmiky");

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @return
	 */
	public static boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @return
	 */
	public static boolean isErrorEnabled() {
		return log.isErrorEnabled();
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @return
	 */
	public static boolean isFatalEnabled() {
		return log.isFatalEnabled();
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @return
	 */
	public static boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @return
	 */
	public static boolean isTraceEnabled() {
		return log.isTraceEnabled();
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @return
	 */
	public static boolean isWarnEnabled() {
		return log.isWarnEnabled();
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @param obj
	 */
	public static void debug(Object obj) {
		log.debug(obj);
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @param obj
	 * @param t
	 */
	public static void debug(Object obj, Throwable t) {
		log.debug(obj, t);
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @param obj
	 */
	public static void error(Object obj) {
		log.error(obj);
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @param obj
	 * @param t
	 */
	public static void error(Object obj, Throwable t) {
		log.error(obj, t);
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @param obj
	 */
	public static void fatal(Object obj) {
		log.fatal(obj);
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @param obj
	 * @param t
	 */
	public static void fatal(Object obj, Throwable t) {
		log.fatal(obj, t);
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @param obj
	 */
	public static void info(Object obj) {
		log.info(obj);
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @param obj
	 * @param t
	 */
	public static void info(Object obj, Throwable t) {
		log.info(obj, t);
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @param obj
	 */
	public static void trace(Object obj) {
		log.trace(obj);
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @param obj
	 * @param t
	 */
	public static void trace(Object obj, Throwable t) {
		log.trace(obj, t);
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @param obj
	 */
	public static void warn(Object obj) {
		log.warn(obj);
	}

	/**
	 * @author lmiky
	 * @date 2013-5-10
	 * @param obj
	 * @param t
	 */
	public static void warn(Object obj, Throwable t) {
		log.warn(obj, t);
	}

	/**
	 * 处理异常日志
	 * @author lmiky
	 * @date 2013-5-31
	 * @param e
	 */
	private static void detailLogException(Throwable e) {
		// 记录异常对象
		error(e);
		StackTraceElement[] stackTraceElements = e.getStackTrace();
		int logNum = exceptionStackLogNum;
		if (exceptionStackLogNum > stackTraceElements.length) {
			logNum = stackTraceElements.length;
		}
		// 记录异常跟踪信息
		StackTraceElement stackTraceElement = null;
		for (int i = 0; i < logNum; i++) {
			stackTraceElement = stackTraceElements[i];
			if(stackTraceElement == null) {
				break;
			}
			error(String.format("Caused by: %s", stackTraceElement));
		}
	}
	
	/**
	 * 记录异常
	 * @author lmiky
	 * @date 2014年11月13日 上午11:25:02
	 * @param exceptionDesc 错误说明
	 * @param e
	 */
	public static void logException(String exceptionDesc, Throwable e) {
		// 记录异常对象
		error(String.format(exceptionDesc + ": %s", e.getMessage()));
		detailLogException(e);
	}
	
	/**
	 * 记录异常
	 * @author lmiky
	 * @date 2013-5-31
	 * @param e
	 */
	public static void logException(Throwable e) {
		// 记录异常对象
		error(e.getMessage());
		detailLogException(e);
	}

	
	/**
	 * 获取操作动作名称
	 * @author lmiky
	 * @date 2014-7-16
	 * @param opeType
	 * @return
	 */
	public static String getOpeName(String opeType) {
		try {
			return BundleUtils.getStringValue(Constants.PROPERTIES_KEY_OPERATENAME_FILE, CONFIGNAME_PREFIX_OPETYPE + opeType);
		} catch(Exception e) {
			logException(e);
			return "";
		}
	}
	
	/**
	 * 获取操作对象名称
	 * @author lmiky
	 * @date 2014-7-16
	 * @param pojoClassName
	 * @return
	 */
	public static String getPojoName(String pojoClassName) {
		try {
			return BundleUtils.getStringValue(Constants.PROPERTIES_KEY_OPERATENAME_FILE, CONFIGNAME_PREFIX_POJONAME + pojoClassName);
		} catch(Exception e) {
			logException(e);
			return "";
		}
	}
}
