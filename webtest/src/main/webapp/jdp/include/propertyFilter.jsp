<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.lmiky.platform.database.pojo.BasePojo,com.lmiky.admin.util.PropertyFilterUtils,com.lmiky.platform.database.model.PropertyFilter,java.util.List,java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/tld/lmiky-lhtml.tld" prefix="lhtml" %>
<%
	String pojoClassName = request.getParameter("pojoClassName");
	Class<? extends BasePojo> pojoClass = (Class<? extends BasePojo>)Class.forName(pojoClassName);
	List<PropertyFilter> filters = PropertyFilterUtils.generateFromHttpRequest(request, pojoClass);
	for(PropertyFilter filter : filters) {
%>
		<lhtml:propertyFilter inputType="hidden" compareType="<%=filter.getCompareType().toString() %>" propertyName="<%=filter.getPropertyName().toString() %>" />
<%		
	}
%>