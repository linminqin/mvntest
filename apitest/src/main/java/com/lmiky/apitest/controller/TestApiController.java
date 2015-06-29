package com.lmiky.apitest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lmiky.platform.controller.BaseApiController;
import com.lmiky.platform.controller.view.BaseCodeDataListView;
import com.lmiky.platform.controller.view.BaseCodeView;
import com.lmiky.platform.controller.view.BaseJsonView;
import com.lmiky.platform.logger.pojo.Logger;
import com.lmiky.platform.logger.util.LoggerUtils;
import com.lmiky.platform.service.BaseService;
import com.lmiky.platform.util.Environment;
import com.lmiky.platform.util.IPUtils;

/**
 * @author lmiky
 * @date 2015年5月11日 下午2:58:27
 */
@Controller
@RequestMapping("/api/test")
public class TestApiController extends BaseApiController {
	private BaseService baseService;

	/**
	 * 激活
	 * @author lmiky
	 * @date 2015年5月11日 下午5:24:25
	 * @param modelMap
	 * @param request
	 * @param response
	 * @param playerCode 15为IEMI+15位IMSI(3位MCC+2位MNC+10位MSIN)+12位MAC地址
	 * @param gameCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/test.shtml")
	public String test(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			LoggerUtils.info(String.format("IP[%s]进入到测试接口中", IPUtils.getRealIP(request)));
			System.out.println(baseService);
			System.out.println(Environment.getBean("baseService"));
		} catch (Exception e) {
			transactException(modelMap, request, response, e);
		}
		return BaseJsonView.getViewName(BaseCodeView.class);
	}
	
	/**
	 * 激活
	 * @author lmiky
	 * @date 2015年5月11日 下午5:24:25
	 * @param modelMap
	 * @param request
	 * @param response
	 * @param playerCode 15为IEMI+15位IMSI(3位MCC+2位MNC+10位MSIN)+12位MAC地址
	 * @param gameCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/testList.shtml")
	public String testList(ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			LoggerUtils.info(String.format("IP[%s]进入到测试接口中", IPUtils.getRealIP(request)));
			List<Logger> loggers = baseService.list(Logger.class);
			Map<String, Object> data = new HashMap<>();
			data.put("loggers", loggers);
			modelMap.put(BaseCodeDataListView.KEY_NAME_DATA, data);
		} catch (Exception e) {
			transactException(modelMap, request, response, e);
		}
		return BaseJsonView.getViewName(BaseCodeDataListView.class);
	}

	/**
	 * @return the baseService
	 */
	public BaseService getBaseService() {
		return baseService;
	}

	/**
	 * @param baseService the baseService to set
	 */
	@Resource(name="baseService")
	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}
	
}
