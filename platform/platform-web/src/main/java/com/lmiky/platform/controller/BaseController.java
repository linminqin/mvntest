package com.lmiky.platform.controller;

import javax.annotation.Resource;

import com.lmiky.platform.service.BaseService;

/**
 * @author lmiky
 * @date 2015年5月11日 上午9:43:07
 */
public abstract class BaseController {
	protected BaseService service;
	
	/**
	 * @return the service
	 */
	public BaseService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	@Resource(name="baseService")
	public void setService(BaseService service) {
		this.service = service;
	}
}
