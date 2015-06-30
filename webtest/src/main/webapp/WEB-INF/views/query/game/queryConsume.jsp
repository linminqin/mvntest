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
					<form id="mainForm" action="<c:url value="/query/game/queryconsume.shtml"/>" method="get" class="form-vertical">
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
												<lhtml:propertyFilter inputType="select" compareType="EQ" propertyName="channel.code"  styleClass="bian medium" style="width: 144px !important;">
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
										<label class="control-label"  style="width: 70px;">玩家：</label>
										<lhtml:propertyFilter inputType="text" compareType="EQ" propertyName="playerCode" styleClass="medium"/>
										&nbsp;
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
										<label class="control-label"  style="width: 70px;">消费时间：</label>
										<lhtml:propertyFilter inputType="dateTime" compareType="GE" propertyName="consumeTime" styleClass="medium"/>
										&nbsp;
										<label style="width: 70px; text-align: center;" class="control-label" >-</label>
										<lhtml:propertyFilter inputType="dateTime" compareType="LE" propertyName="consumeTime" styleClass="medium"/>
										&nbsp;
										<label class="control-label"  style="width: 70px;">&nbsp;</label>
										<button type="submit" class="btn action-button">查询</button>
										&nbsp;
										<button type="submit" name="export" class="btn action-button">导出</button>
										&nbsp;
										<button type="submit" name="exportCount" class="btn action-button">导出统计</button>
								</div>
							</div>						
						</div>
					</div>
					<div class="row-fluid">
						<jsp:include page="/jdp/include/favoriteMenu.jsp">
							<jsp:param value="query_game_consume_load" name="menuId"/>
						</jsp:include>
					</div>
					<div class="row-fluid">
					<table class="listContent table table-bordered table-striped with-check table-hover">
						<thead>
							<tr>
								<th class="list_index">&nbsp;</th>
								<th>玩家</th>
								<th>渠道</th>
								<th>游戏</th>
								<th>道具</th>
								<th>消费时间</th>
								<th>消费金额（元）</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${page.items}" varStatus="status">
									<tr>
										<td>${status.count + (page.currentPage - 1) * page.pageSize}</td>
										<td>${item.playerCode}</td>
										<td>${item.channel.name}</td>
										<td>${item.gameVersion.game.name} ${item.gameVersion.version}</td>
										<td>${item.gameItem.name}</td>
										<td><fmt:formatDate value="${item.consumeTime}" pattern="${defaultDateTimeFormater }"/></td>
										<td><fmt:formatNumber value="${item.amount/100}" pattern="###,###,##0.00#"/></td>
									</tr>
								</c:forEach>
									<tr>
										<td colspan="7" class="pageColumn"><%@ include file="/jdp/view/page.jsp" %></td>
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