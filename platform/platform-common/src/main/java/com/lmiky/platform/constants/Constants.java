package com.lmiky.platform.constants;

import com.lmiky.platform.util.BundleUtils;

/**
 * 常量
 * @author lmiky
 * @date 2013-4-16
 */
public class Constants {
	
	
	// ****************************************properties文件****************************************//
	// 配置文件
	public static final String PROPERTIES_KEY_PLATFORM_FILE = "config/platform";	//核心配置
	public static final String PROPERTIES_KEY_CONTEXT_FILE = "config/context";
	public static final String PROPERTIES_KEY_OPERATENAME_FILE = "config/operateName";
	public static final String PROPERTIES_KEY_CODE_MSG_FILE = "config/codeMsg";
	
	//系统
	public static final String SYSTEM_ENCODE =  BundleUtils.getStringValue(PROPERTIES_KEY_PLATFORM_FILE, "system.encode"); 	//系统编码
	public static final int SYSTEM_EXCEPTION_STACKLOGNUM = BundleUtils.getIntValue(PROPERTIES_KEY_PLATFORM_FILE, "system.exceptionStackLogNum");	//错误日志行数

	// 格式
	public static final String CONTEXT_KEY_FORMAT_DATE = "format.date"; // 日期
	public static final String CONTEXT_KEY_FORMAT_DATETIME = "format.dateTime"; // 日期时间
	public static final String CONTEXT_KEY_FORMAT_TIME = "format.time"; // 时间
	//日期格式值
	public static final String CONTEXT_KEY_FORMAT_DATE_VALUE = BundleUtils.getStringValue(PROPERTIES_KEY_PLATFORM_FILE, CONTEXT_KEY_FORMAT_DATE); // 日期
	public static final String CONTEXT_KEY_FORMAT_DATETIME_VALUE = BundleUtils.getStringValue(PROPERTIES_KEY_PLATFORM_FILE, CONTEXT_KEY_FORMAT_DATETIME); // 日期时间
	public static final String CONTEXT_KEY_FORMAT_TIME_VALUE = BundleUtils.getStringValue(PROPERTIES_KEY_PLATFORM_FILE, CONTEXT_KEY_FORMAT_TIME);  // 时间

	//文件上传路径
	//临时目录
	public static final String SYSTEM_FILE_UPLOAD_PATH_TEMP = BundleUtils.getStringContextValue("system.file.upload.path.temp");
	public static final String SYSTEM_FILE_PATH = BundleUtils.getStringContextValue("system.file.path");
	
	// ****************************************properties文件****************************************//
}