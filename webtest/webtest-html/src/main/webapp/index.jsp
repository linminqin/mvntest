<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.lmiky.admin.util.WebUtils"%>
<%@page import="com.lmiky.admin.session.model.SessionInfo"%>
<%@page import="com.lmiky.platform.util.BundleUtils"%>
<%@page import="com.lmiky.admin.constants.Constants"%>
<%
	SessionInfo sessionInfo = WebUtils.getSessionInfo(request);
	if(sessionInfo == null) {
		response.sendRedirect(request.getContextPath() + BundleUtils.getStringValue(Constants.PROPERTIES_KEY_ADMIN_FILE, "system.loginUrl"));
	} else {
		request.getRequestDispatcher("/sso/system/menu/load.shtml").forward(request, response);
		//response.sendRedirect(request.getContextPath() + "/sso/system/menu/load.shtml");		
	}
%>