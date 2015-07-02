package com.lmiky.platform.json.jackson;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.type.JavaType;
import org.springframework.stereotype.Component;

import com.lmiky.platform.json.JsonMapper;
import com.lmiky.platform.logger.util.LoggerUtils;

/**
 * jackson插件
 * @author lmiky
 * @date 2013-5-19
 */
@Component("jacksonJsonMapperImpl")
public class JsonMapperImpl implements JsonMapper {
	private ObjectMapper mapper;
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.json.JsonMapper#init()
	 */
	@PostConstruct
	public void init() {
		mapper = new ObjectMapper();
		//设置输出时包含属性的风格：所有属性
		mapper.setSerializationInclusion(Inclusion.ALWAYS);
		//设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//禁止使用int代表Enum的order()來反序列化Enum,非常危險
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.json.JsonMapper#toJson(java.lang.Object)
	 */
	public String toJson(Object object) throws Exception {
		try {
			return mapper.writeValueAsString(object);
		} catch (Exception e) {
			LoggerUtils.error(String.format("将对象转为json字符串错误: %s", e));
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.json.JsonMapper#fromJson(java.lang.String, java.lang.Class)
	 */
	public <T> T fromJson(String json, Class<T> objectClass) throws Exception {
		if(StringUtils.isBlank(json)) {
			return null;
		}
		try {
			return mapper.readValue(json, objectClass);
		} catch (Exception e) {
			LoggerUtils.warn(String.format("将json字符串转为对象错误: %s", e));
			throw e;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.platform.json.JsonMapper#fromJson(java.lang.String, java.lang.Class, java.lang.Class[])
	 */
	public <T> T fromJson(String json, Class<T> objectClass, Class<?>... parameterClasses) throws Exception {
		if(StringUtils.isBlank(json)) {
			return null;
		}
		try {
			JavaType javaType = mapper.getTypeFactory().constructParametricType(objectClass, parameterClasses);
			return mapper.readValue(json, javaType);
		} catch (Exception e) {
			LoggerUtils.warn(String.format("将json字符串转为对象错误: %s", e));
			throw e;
		}
	}

	/* (non-Javadoc)
	 * @see com.lmiky.platform.json.JsonMapper#destory()
	 */
	@PreDestroy
	public void destory() {
		mapper = null;
	}

}
