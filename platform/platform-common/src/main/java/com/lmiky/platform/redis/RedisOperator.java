package com.lmiky.platform.redis;

import java.util.Map;

/**
 * Redis操作工具
 * @author lmiky
 * @date 2015年5月12日 下午1:01:41
 */
public interface RedisOperator {
	/**
	 * @author lmiky
	 * @date 2015年5月12日 下午1:38:52
	 * @param key
	 * @param field
	 * @param value
	 */
	public void hset(String key, String field, Object value);
	
	/**
	 * @author lmiky
	 * @date 2015年5月12日 下午1:48:41
	 * @param key
	 * @param field
	 * @return
	 */
	public Object hget(String key, String field);
	
	/**
	 * 获取全部
	 * @author lmiky
	 * @date 2015年5月12日 下午3:34:23
	 * @param key
	 * @return
	 */
	public Map<Object, Object> hgetAll(String key);
	
	/**
	 * 删除
	 * @author lmiky
	 * @date 2015年5月12日 下午4:46:29
	 * @param key
	 * @param field
	 */
	public void hdel(String key, String field);
}
