<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.lmiky.admin.constants.Constants,com.lmiky.admin.form.controller.FormController"%>
<%@ include file="/jdp/common/taglibs.jsp" %>
<c:set var="httpParamSubMenuId" value="<%=Constants.HTTP_PARAM_SUBMENU_ID %>"/>
<c:set var="httpParamTopMenuId" value="<%=Constants.HTTP_PARAM_TOPMENU_ID %>"/>
<c:set var="httpParamMenuFrom" value="<%=Constants.HTTP_PARAM_MENU_FROM %>"/>
<c:set var="httpParamOpenMode" value="<%=FormController.HTTP_PARAM_FORM_OEPNMODE %>"/>
<c:set var="readOpenMode" value="<%=FormController.OPEN_MODE_READ %>"/>
<c:set var="editOpenMode" value="<%=FormController.OPEN_MODE_EDIT %>"/>
<c:set var="createOpenMode" value="<%=FormController.OPEN_MODE_CTEATE %>"/>
<c:set var="defaultDateFormater" value="<%=Constants.CONTEXT_KEY_FORMAT_DATE_VALUE %>"/>
<c:set var="defaultDateTimeFormater" value="<%=Constants.CONTEXT_KEY_FORMAT_DATETIME_VALUE %>"/>