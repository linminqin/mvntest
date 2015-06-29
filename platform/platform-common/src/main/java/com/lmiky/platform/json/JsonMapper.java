package com.lmiky.platform.json;
/**
 * JSON映射
 * @author lmiky
 * @date 2013-5-19
 */
public interface JsonMapper {
	
	/**
	 * 初始化
	 * @author lmiky
	 * @date 2013-5-19
	 */
	public void init();
	
	/**
	 * 将对象转为json格式
	 * @author lmiky
	 * @date 2013-5-19
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public String toJson(Object object) throws Exception;	
	
	/**
	 * 将json转为对象
	 * @author lmiky
	 * @date 2013-5-19
	 * @param json
	 * @param objectClass
	 * @return
	 * @throws Exception
	 */
	public <T> T fromJson(String json, Class<T> objectClass) throws Exception;	
	
	/**
	 * 将json转为对象
	 * @author lmiky
	 * @date 2015年1月28日 上午11:18:27
	 * @param json
	 * @param objectClass 泛型类
	 * @param parameterClasses 泛型内参数类
	 * @return
	 * @throws Exception
	 */
	public <T> T fromJson(String json, Class<T> objectClass, Class<?>... parameterClasses) throws Exception;	
	
	/**
	 * 销毁
	 * @author lmiky
	 * @date 2013-5-19
	 */
	public void destory();
}
