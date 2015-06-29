package com.lmiky.platform.redis.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.lmiky.platform.redis.RedisOperator;

/**
 * Redis操作工具
 * @author lmiky
 * @date 2015年5月12日 下午1:01:41
 */
@Component("redisOperator")
public class RedisOperatorImpl implements RedisOperator {
	private RedisTemplate<String, Object> redisTemplate;

	/* (non-Javadoc)
	 * @see com.lmiky.platform.redis.RedisOperator#hset(java.lang.String, java.lang.String, java.lang.Object)
	 */
	public void hset(String key, String field, Object value) {
		redisTemplate.boundHashOps(key).put(field, value);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.redis.RedisOperator#hget(java.lang.String, java.lang.String)
	 */
	@Override
	public Object hget(String key, String field) {
		return redisTemplate.boundHashOps(key).get(field);
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.redis.RedisOperator#hgetAll(java.lang.String)
	 */
	@Override
	public Map<Object, Object> hgetAll(String key) {
		return redisTemplate.boundHashOps(key).entries();
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.redis.RedisOperator#hdel(java.lang.String, java.lang.String)
	 */
	@Override
	public void hdel(String key, String field) {
		redisTemplate.boundHashOps(key).delete(field);
		
	}
	
	/**
	 * @return the redisTemplate
	 */
	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	/**
	 * @param redisTemplate the redisTemplate to set
	 */
	@Autowired
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
}
