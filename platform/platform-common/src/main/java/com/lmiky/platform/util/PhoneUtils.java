package com.lmiky.platform.util;

import org.apache.commons.lang3.StringUtils;

import com.lmiky.platform.constants.Constants;

/**
 * 手机
 * @author lmiky
 * @date 2015年5月11日 下午11:28:19
 */
public class PhoneUtils {
	//营运商
	public static final String OPERATOR_CM = "CM";	//中国移动
	public static final String OPERATOR_CU = "CU";		//中国联通
	public static final String OPERATOR_CTE = "CTE";	//中国电信
	public static final String OPERATOR_CTI = "CTI";	//中国铁通
	
	//营运商MNC值
	public static final String OPERATOR_CM_MNC = BundleUtils.getStringValue(Constants.PROPERTIES_KEY_PLATFORM_FILE, "imsi.mnc.cb");	//中国移动
	public static final String OPERATOR_CU_MNC = BundleUtils.getStringValue(Constants.PROPERTIES_KEY_PLATFORM_FILE, "imsi.mnc.cu");		//中国联通
	public static final String OPERATOR_CTE_MNC  = BundleUtils.getStringValue(Constants.PROPERTIES_KEY_PLATFORM_FILE, "imsi.mnc.cte");;//中国电信
	public static final String OPERATOR_CTI_MNC  = BundleUtils.getStringValue(Constants.PROPERTIES_KEY_PLATFORM_FILE, "imsi.mnc.cti");	//中国铁通
	
	/**
	 * 根据MNC获取营运商
	 * @author lmiky
	 * @date 2015年5月11日 下午11:35:02
	 * @param mnc
	 * @return
	 */
	public static String getOperateByMNC(String mnc) {
		if(!StringUtils.isBlank(mnc)) {
			if(OPERATOR_CM_MNC.indexOf(mnc) != -1) {
				return OPERATOR_CM;
			}
			if(OPERATOR_CU_MNC.indexOf(mnc) != -1) {
				return OPERATOR_CU;
			}
			if(OPERATOR_CTE_MNC.indexOf(mnc) != -1) {
				return OPERATOR_CTE;
			}
			if(OPERATOR_CTI_MNC.indexOf(mnc) != -1) {
				return OPERATOR_CTI;
			}
		}
		return "";
	}
	
}
