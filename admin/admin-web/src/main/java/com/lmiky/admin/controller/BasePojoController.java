package com.lmiky.admin.controller;

import java.lang.reflect.ParameterizedType;

import com.lmiky.platform.database.pojo.BasePojo;

/**
 * @author lmiky
 * @date 2014-1-2
 */
public abstract class BasePojoController<T extends BasePojo> extends BaseWebController {
	protected Class<T> pojoClass;
	
	@SuppressWarnings("unchecked")
	public BasePojoController() {
		this.pojoClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
