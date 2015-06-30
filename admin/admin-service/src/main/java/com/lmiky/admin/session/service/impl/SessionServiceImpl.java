package com.lmiky.admin.session.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.lmiky.admin.constants.Constants;
import com.lmiky.admin.session.exception.SessionException;
import com.lmiky.admin.session.model.SessionInfo;
import com.lmiky.admin.session.service.SessionService;
import com.lmiky.admin.user.pojo.User;

/**
 * @author lmiky
 * @date 2013-4-24
 */
@Service("sessionService")
public class SessionServiceImpl implements SessionService {

	/* (non-Javadoc)
	 * @see com.lmiky.admin.session.service.SessionService#generateSessionInfo(javax.servlet.http.HttpServletRequest, com.lmiky.admin.user.pojo.User)
	 */
	public SessionInfo generateSessionInfo(HttpServletRequest request, User user) throws SessionException {
		if(user == null) {
			throw new SessionException(SessionException.USER_NULL);
		}
		SessionInfo sessionInfo = new SessionInfo();
		sessionInfo.setUser(user);
		sessionInfo.setSessionId(request.getSession().getId());
		return sessionInfo;
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.admin.session.service.SessionService#getSessionInfo(javax.servlet.http.HttpServletRequest)
	 */
	public SessionInfo getSessionInfo(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (SessionInfo)session.getAttribute(Constants.SESSION_ATTR_SESSIONINFO);
	}
	
	/* (non-Javadoc)
	 * @see com.lmiky.admin.session.service.SessionService#removeSessionInfo(javax.servlet.http.HttpServletRequest)
	 */
	public void removeSessionInfo(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(Constants.SESSION_ATTR_SESSIONINFO, null);
		session.removeAttribute(Constants.SESSION_ATTR_SESSIONINFO);
	}

}
