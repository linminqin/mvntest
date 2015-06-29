package com.lmiky.platform.json.util;

import com.lmiky.platform.json.JsonMapper;
import com.lmiky.platform.json.jackson.JsonMapperImpl;

/**
 * json工具类
 * @author lmiky
 * @date 2013-5-19
 */
public class JsonUtils {
	//private static JsonMapper jsonMapper = (JsonMapper)Environment.getBean(PropertiesUtils.getStringContextValue("json.jsonMapperName"));
	private static JsonMapper jsonMapper = new JsonMapperImpl();
	static {
		jsonMapper.init();
	}
	
	/**
	 * 将对象转为json字符串
	 * @author lmiky
	 * @date 2013-5-19
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object object) throws Exception {
		return jsonMapper.toJson(object);
	}
	
	/**
	 * 将json转为对象
	 * @author lmiky
	 * @date 2013-5-19
	 * @param json
	 * @param objectClass
	 * @return
	 * @throws Exception
	 */
	public static <T> T fromJson(String json, Class<T> objectClass) throws Exception {
		return jsonMapper.fromJson(json, objectClass);
	}
	
	/**
	 * 方法说明
	 * @author lmiky
	 * @date 2015年1月28日 上午11:21:33
	 * @param json
	 * @param objectClass 泛型类
	 * @param parameterClasses 泛型内参数类
	 * @return
	 * @throws Exception
	 */
	public static <T> T fromJson(String json, Class<T> objectClass, Class<?>... parameterClasses) throws Exception {
		return jsonMapper.fromJson(json, objectClass, parameterClasses);
	}
}
