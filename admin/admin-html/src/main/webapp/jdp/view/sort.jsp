<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.lmiky.admin.constants.Constants" %>
<div id="sortDiv" style="display: none;">
	<c:forEach var="sort" items="${sorts }">
		<c:set var="sortPropertyName" value="${sort.propertyName}"/>
		<c:if test="${sort.sortClass.name != pojoClassName }">
			<c:set var="sortClassName" value="${sort.sortClass.simpleName}"/>
			<c:set var="sortClassNameFirstLettle" value="${fn:substring(sort.sortClass.simpleName, 0, 1)}"/>
			<c:set var="sortPropertyName" value="${fn:toLowerCase(sortClassNameFirstLettle)}${fn:substring(sort.sortClass.simpleName, 1, -1)}.${sort.propertyName}"/>
		</c:if>
		<input type="hidden" name="<%=Constants.HTTP_PARAM_SORT_ORDERBY_NAME %>" value="${sortPropertyName}"/>
		<input type="hidden" name="<%=Constants.HTTP_PARAM_SORT_TYPE_NAME_PREFIX %>${sortPropertyName}" value="${sort.sortType}" />
	</c:forEach>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		<c:forEach var="sort" items="${sorts }">
			<c:set var="sortPropertyName" value="${sort.propertyName}"/>
			<c:if test="${sort.sortClass.name != pojoClassName }">
				<c:set var="sortClassName" value="${sort.sortClass.simpleName}"/>
				<c:set var="sortClassNameFirstLettle" value="${fn:substring(sort.sortClass.simpleName, 0, 1)}"/>
				<c:set var="sortPropertyName" value="${fn:toLowerCase(sortClassNameFirstLettle)}${fn:substring(sort.sortClass.simpleName, 1, -1)}_${sort.propertyName}"/>
			</c:if>
			var sortObj = $(".sorted_${sortPropertyName}");
			$(sortObj).addClass("order-${sort.sortType}");
		</c:forEach>
	});

	function pageSort(sortItem) {
		var sortType ="";
		var sortValue ="";
		var sortObjs = $("input[name='<%=Constants.HTTP_PARAM_SORT_ORDERBY_NAME%>'][value='" + sortItem + "']");
		if(sortObjs.length > 0) {
			sortType = $("input[name='<%=Constants.HTTP_PARAM_SORT_TYPE_NAME_PREFIX%>" + sortItem + "']").val();
		}
		if(sortType == "") {
			sortValue = "asc";
		} else if(sortType == "asc") {
			sortValue = "desc";
		} else if(sortType == "desc") {
			sortValue = "";
		}
		
		var orderHtml = '<input type="hidden" name="<%=Constants.HTTP_PARAM_SORT_ORDERBY_NAME %>" value="' + sortItem + '"/>' +
		'<input type="hidden" name="<%=Constants.HTTP_PARAM_SORT_TYPE_NAME_PREFIX %>' + sortItem + '" value="' + sortValue + '" />';
		$("#sortDiv").html(orderHtml);
		submitForm();
	}
</script>