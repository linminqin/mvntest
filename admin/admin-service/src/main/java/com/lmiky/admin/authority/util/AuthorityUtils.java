package com.lmiky.admin.authority.util;

import com.lmiky.admin.authority.service.AuthorityService;
import com.lmiky.admin.session.model.SessionInfo;
import com.lmiky.platform.service.exception.ServiceException;

/**
 * 权限工具
 * @author lmiky
 * @date 2015-6-30
 */
public class AuthorityUtils {
	
	/**
	 * 检查权限
	 * @author lmiky
	 * @date 2015-6-30
	 * @param authorityService
	 * @param sessionInfo
	 * @param authorityCode
	 * @return
	 * @throws ServiceException
	 */
	public static boolean checkAuthority(AuthorityService authorityService, SessionInfo sessionInfo, String authorityCode) throws ServiceException {
		//先从session缓存中找
		Boolean hasAuthority = sessionInfo.isAuthority(authorityCode);
		if(hasAuthority == null) {
			hasAuthority = authorityService.checkAuthority(authorityCode, sessionInfo.getUserId());
		}
		if(!hasAuthority) {
			sessionInfo.setAuthority(authorityCode, false);
		} else {
			sessionInfo.setAuthority(authorityCode, true);
		}
		return hasAuthority;
	}
}
