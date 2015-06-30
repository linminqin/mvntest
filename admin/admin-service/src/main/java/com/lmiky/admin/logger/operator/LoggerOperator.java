package com.lmiky.admin.logger.operator;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.lmiky.admin.logger.pojo.Logger;
import com.lmiky.platform.constants.Constants;
import com.lmiky.platform.logger.util.LoggerUtils;
import com.lmiky.platform.service.BaseService;
import com.lmiky.platform.service.exception.ServiceException;
import com.lmiky.platform.util.BundleUtils;
import com.lmiky.platform.util.IPUtils;

/**
 * 日志操作
 * @author lmiky
 * @date 2015年6月22日 下午2:21:46
 */
public class LoggerOperator {
	private static final String CONFIGNAME_PREFIX_OPETYPE = "opeType_";
	private static final String CONFIGNAME_PREFIX_POJONAME= "opePojo_";

	/**
	 * 保存日志
	 * @author lmiky
	 * @date 2013-5-10
	 * @param request
	 * @param pojoName
	 * @param pojoId
	 * @param userId
	 * @param userName
	 * @param opeType
	 * @param opeClassName8
	 * @param logDesc
	 * @param service
	 * @throws ServiceException
	 */
	public static void save(HttpServletRequest request, String pojoName, Long pojoId, Long userId, String userName, String opeType, String opeClassName, String logDesc,
			BaseService service) throws ServiceException {
		Logger logger = new Logger();
		logger.setPojoName(pojoName);
		logger.setPojoId(pojoId);
		logger.setUserId(userId);
		logger.setUserName(userName);
		logger.setOpeType(opeType);
		logger.setOpeClassName(opeClassName);
		logger.setLogDesc(logDesc);
		logger.setLogTime(new Date());
		logger.setIp(IPUtils.getRealIP(request));
		service.add(logger);
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
			LoggerUtils.logException(e);
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
			LoggerUtils.logException(e);
			return "";
		}
	}
}
