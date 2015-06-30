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
					<form id="mainForm" action="<c:url value="/basedata/gamechannel/list.shtml"/>" method="post" class="form-vertical">
					<%@ include file="/jdp/view/field.jsp"%>
					<div class="row-fluid">
						<div class="span12">
							<div class="widget-box border-radius">
								<div class="nopadding control-group">
										<label class="control-label" style="width: 80px;">游戏名称：</label>
										<lhtml:propertyFilter inputType="text" compareType="LIKE" propertyName="gameName" styleClass="medium"/>
										&nbsp;
										<label class="control-label" style="width: 80px;">游戏版本：</label>
										<lhtml:propertyFilter inputType="text" compareType="EQ" propertyName="gameVersion.version" styleClass="medium"/>
										&nbsp;
										<label class="control-label" style="width: 80px;">游戏编号：</label>
										<lhtml:propertyFilter inputType="text" compareType="EQ" propertyName="gameVersion.code" styleClass="medium"/>
										<c:if test="${not empty operatorChannelCode }">
											&nbsp;
											<button type="submit" class="btn action-button">查询</button>
										</c:if>
								</div>
								<c:if test="${empty operatorChannelCode }">
									<div class="nopadding control-group">
											<label class="control-label" style="width: 80px;">渠道名称：</label>
											<lhtml:propertyFilter inputType="text" compareType="LIKE" propertyName="channel.name" styleClass="medium"/>
											&nbsp;
											<label class="control-label" style="width: 80px;">渠道编号：</label>
											<lhtml:propertyFilter inputType="text" compareType="EQ" propertyName="channel.code" styleClass="medium"/>
											&nbsp;
											<label style="width: 80px; text-align: center;" class="control-label" >&nbsp;</label>
											<button type="submit" class="btn action-button">查询</button>
									</div>
								</c:if>
							</div>						
						</div>
					</div>
					<div class="row-fluid">
						<jsp:include page="/jdp/include/addMenu.jsp">
							<jsp:param value="basedata_gamechannel_add" name="authorityCode"/>
							<jsp:param value="/basedata/gamechannel/load.shtml?${httpParamOpenMode }=${createOpenMode }&modulePath=${modulePath }&menuFrom=${param.menuFrom }&subMenuId=${param.subMenuId }" name="addUrl"/>
						</jsp:include>
						<jsp:include page="/jdp/include/batchDeleteMenu.jsp">
							<jsp:param value="basedata_gamechannel_delete" name="authorityCode"/>
							<jsp:param value="basedata/gamechannel/batchDelete.shtml" name="deleteUrl"/>
						</jsp:include>
						<jsp:include page="/jdp/include/favoriteMenu.jsp">
							<jsp:param value="basedata_gamechannel_load" name="menuId"/>
						</jsp:include>
					</div>
					<div class="row-fluid">
					<table class="listContent table table-bordered table-striped with-check table-hover">
						<thead>
							<tr>
								<th class="list_index">&nbsp;</th>
								<th class="sortable sorted_game_name"><a href="javascript:pageSort('game.name')">游戏名称</a></th>
								<th class="sortable sorted_gameVersion_version"><a href="javascript:pageSort('gameVersion.version')">游戏版本</a></th>
								<c:if test="${empty operatorChannelCode }">
									<th class="sortable sorted_gameVersion_code"><a href="javascript:pageSort('gameVersion.code')">游戏编号</a></th>
									<th class="sortable sorted_channel_name"><a href="javascript:pageSort('channel.name')">渠道名称</a></th>
									<th class="sortable sorted_channel_code"><a href="javascript:pageSort('channel.code')">渠道编号</a></th>
								</c:if>
								<th class="sortable sorted_mapTime"><a href="javascript:pageSort('mapTime')">推广时间</a></th>
								<th style="width: 170px;">操作</th>
								<c:set var="colCount" value="8"/>
								<lauthority:checkAuthority authorityCode="basedata_gamechannel_delete">
									<c:set var="colCount" value="${colCount + 1 }"/>
									<th class="simpleCheckbox">
										<input type="checkbox" name="batctSelectDelete"  id="batctSelectDelete" value="" onclick="batchSelectDelete()"/>
									</th>
								</lauthority:checkAuthority>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${page.items}" varStatus="status">
									<tr>
										<td>${status.count + (page.currentPage - 1) * page.pageSize}</td>
										<td>${item.gameVersion.game.name}</td>
										<td>${item.gameVersion.version}</td>
										<c:if test="${empty operatorChannelCode }">
											<td>${item.gameVersion.code}</td>
											<td>${item.channel.name}</td>
											<td>${item.channel.code}</td>
										</c:if>
										<td><fmt:formatDate value="${item.mapTime}" pattern="${defaultDateTimeFormater }"/></td>
										<td>
											<lauthority:checkAuthority authorityCode="basedata_gamechannel_load">
												<a href="javascript:void(0)" class="td_2" onclick="redirectPage('<c:url value="/basedata/gamechannel/load.shtml?id=${item.id}&${httpParamOpenMode }=${readOpenMode }"/>&modulePath=${modulePath }&menuFrom=${param.menuFrom }&subMenuId=${param.subMenuId }', 800, 600)">
													<i class="icon icon-circle-arrow-up"></i> 查看
												</a>
											</lauthority:checkAuthority>
											<lauthority:checkAuthority authorityCode="basedata_gamechannel_modify">
												&nbsp;
												<a href="javascript:void(0)" class="td_2" onclick="redirectPage('<c:url value="/basedata/gamechannel/load.shtml?id=${item.id}&${httpParamOpenMode }=${editOpenMode }"/>&modulePath=${modulePath }&menuFrom=${param.menuFrom }&subMenuId=${param.subMenuId }', 800, 600)">
													<i class="icon icon-edit"></i> 修改
												</a>
											</lauthority:checkAuthority>
											<lauthority:checkAuthority authorityCode="basedata_gamechannel_delete">
												&nbsp;
												<a href="javascript:void(0)" onclick="deletePojo('<c:url value="/basedata/gamechannel/delete.shtml?id=${item.id}"/>')" class="td_2">
													<i class="icon icon-trash"></i> 删除
												</a>
											</lauthority:checkAuthority>
										</td>
										<lauthority:checkAuthority authorityCode="basedata_gamechannel_delete">
											<td>
												<input type="checkbox" name="batchDeleteId" value="${item.id}" />
											</td>
										</lauthority:checkAuthority>
									</tr>
								</c:forEach>
									<tr>
										<td colspan="${colCount }" class="pageColumn"><%@ include file="/jdp/view/page.jsp" %></td>
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
	</body>
</html>