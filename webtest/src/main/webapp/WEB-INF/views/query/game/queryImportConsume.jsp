<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jdp/common/htmlDoctype.jsp"%>
<%@ include file="/jdp/common/common.jsp"%>
<html>
	<head>
		<title>${subMenu.label }</title>
		<%@ include file="/jdp/common/header.jsp"%>
		<%@ include file="/jdp/view/header.jsp"%>
	</head>
	<body>
		<%@ include file="/jdp/page/topMenu.jsp"%>
		<div class="container_content">
			<%@ include file="/jdp/page/leftMenu.jsp"%>
			<div id="content">
				<%@ include file="/jdp/page/location.jsp"%>
				<div class="container-fluid">
					<form id="mainForm" action="<c:url value="/query/game/queryimportconsume.shtml"/>" method="get" class="form-vertical">
					<%@ include file="/jdp/view/field.jsp"%>
					<div class="row-fluid">
						<div class="span12">
							<div class="widget-box border-radius">
								<div class="nopadding control-group">
										<c:choose>
											<c:when test="${not empty operatorChannel}">
												<!-- <label class="control-label">${operatorChannel.name }</label> -->
											</c:when>
											<c:otherwise>
												<label class="control-label"  style="width: 70px;">渠道：</label>
												<lhtml:propertyFilter inputType="select" compareType="EQ" propertyName="channel.code"  styleClass="bian medium"  style="width: 144px !important;">
					            					<option value="">全部</option>
						            				<c:if test="${not empty channels }">
						            					<c:forEach items="${channels }" var="channel">
						            						<option value="${channel.code }">${channel.name }</option>
						            					</c:forEach>
						            				</c:if>
					            				</lhtml:propertyFilter>
												&nbsp;
											</c:otherwise>
										</c:choose>
										<label class="control-label"  style="width: 70px;">游戏：</label>
										<lhtml:propertyFilter inputType="select" compareType="EQ" propertyName="gameVersion.code"  styleClass="bian medium" style="width: 144px !important;">
			            					<option value="">全部</option>
				            				<c:if test="${not empty gameVersions }">
				            					<c:forEach items="${gameVersions }" var="gameVersion">
				            						<option value="${gameVersion.code }">${gameVersion.game.name } ${gameVersion.version }</option>
				            					</c:forEach>
				            				</c:if>
			            				</lhtml:propertyFilter>
								</div>
								<div class="nopadding control-group">
										<label class="control-label"  style="width: 70px;">统计日期：</label>
										<lhtml:propertyFilter inputType="date" compareType="GE" propertyName="stateDate" styleClass="medium" dateFormater="yyyyMMdd"/>
										&nbsp;
										<label style="width: 70px; text-align: center;" class="control-label" >-</label>
										<lhtml:propertyFilter inputType="date" compareType="LE" propertyName="stateDate" styleClass="medium" dateFormater="yyyyMMdd"/>
										&nbsp;
										<button type="submit" name="query" class="btn action-button">查询</button>
								</div>
							</div>						
						</div>
					</div>
					<div class="row-fluid">
						<lauthority:checkAuthority authorityCode="query_game_importconsume_import">
								<button type="button" class="btn" onClick="redirectPage('<c:url value="/" />query/game/toconsumeimport.shtml?modulePath=${modulePath }&menuFrom=${param.menuFrom }&subMenuId=${param.subMenuId }')"><i class="icon-plus"></i> 导入</button>
						</lauthority:checkAuthority>
						<jsp:include page="/jdp/include/favoriteMenu.jsp">
							<jsp:param value="query_game_importconsume_load" name="menuId"/>
						</jsp:include>
					</div>
					<div class="row-fluid">
					<table class="listContent table table-bordered table-striped with-check table-hover">
						<thead>
							<tr>
								<th class="list_index">&nbsp;</th>
								<th>统计日期</th>
								<c:if test="${empty operatorChannel}">
									<th>渠道</th>
								</c:if>
								<th>游戏</th>
								<th>金额（元）</th>
								<th>上传时间</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${page.items}" varStatus="status">
									<tr>
										<td>${status.count + (page.currentPage - 1) * page.pageSize}</td>
										<td>${item.stateDate}</td>
										<c:if test="${empty operatorChannel}">
											<td>${item.channel.name}</td>
										</c:if>
										<td>${item.gameVersion.game.name} ${item.gameVersion.version}</td>
										<td><fmt:formatNumber value="${item.amount/100}" pattern="###,###,###,###,##0.00#"/></td>
										<td><fmt:formatDate value="${item.importTime}" pattern="${defaultDateTimeFormater }"/></td>
									</tr>
								</c:forEach>
									<tr>
										<th>合计</th>
										<th>&nbsp;</th>
										<c:if test="${empty operatorChannel}">
											<th>&nbsp;</th>
										</c:if>
										<th>&nbsp;</th>
										<th>
											<%
												java.util.HashMap<String, Object> data = (java.util.HashMap<String, Object>)request.getAttribute("countData");
												long amountCountValue = Long.parseLong(data.get("amountCount").toString());
												pageContext.setAttribute("amountCountValue", amountCountValue);
											%>
											<fmt:formatNumber value="${amountCountValue / 100 }" pattern="###,###,###,###,##0.00#"/>
										</th>
										<th>&nbsp;</th>
									</tr>
									<tr>
										<td colspan="
										<c:choose>
											<c:when test="${not empty operatorChannel}">
												5
											</c:when>
											<c:otherwise>
												6
											</c:otherwise>
										</c:choose>
										" class="pageColumn"><%@ include file="/jdp/view/page.jsp" %></td>
									</tr>
							</tbody>
						</table>
						<%@ include file="/jdp/view/sort.jsp" %>
					</div>
				</form>
				</div>
			</div>
			<%@ include file="/jdp/page/footer.jsp"%>
		</div>
		<%@ include file="/jdp/common/scripts.jsp"%>
		<%@ include file="/jdp/view/scripts.jsp"%>
		<%@ include file="/jdp/common/date.jsp"%>
	</body>
</html>