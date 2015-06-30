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
					<form id="mainForm" action="<c:url value="/basedata/gameversion/list.shtml"/>" method="post" class="form-vertical">
					<%@ include file="/jdp/view/field.jsp"%>
					<div class="row-fluid">
						<div class="span12">
							<div class="widget-box border-radius">
								<div class="nopadding control-group">
										<label class="control-label" style="width: 80px;">游戏名称：</label>
										<lhtml:propertyFilter inputType="text" compareType="LIKE" propertyName="game.name" styleClass="medium"/>
										&nbsp;
										<label class="control-label" style="width: 80px;">游戏版本：</label>
										<lhtml:propertyFilter inputType="text" compareType="EQ" propertyName="version" styleClass="medium"/>
										&nbsp;
										<label class="control-label" style="width: 80px;">游戏编号：</label>
										<lhtml:propertyFilter inputType="text" compareType="EQ" propertyName="code" styleClass="medium"/>
										&nbsp;
										<button type="submit" class="btn action-button">查询</button>
								</div>
							</div>						
						</div>
					</div>
					<div class="row-fluid">
						<jsp:include page="/jdp/include/addMenu.jsp">
							<jsp:param value="basedata_gameversion_add" name="authorityCode"/>
							<jsp:param value="/basedata/gameversion/load.shtml?${httpParamOpenMode }=${createOpenMode }&modulePath=${modulePath }&menuFrom=${param.menuFrom }&subMenuId=${param.subMenuId }" name="addUrl"/>
						</jsp:include>
						<jsp:include page="/jdp/include/batchDeleteMenu.jsp">
							<jsp:param value="basedata_gameversion_delete" name="authorityCode"/>
							<jsp:param value="basedata/gameversion/batchDelete.shtml" name="deleteUrl"/>
						</jsp:include>
						<jsp:include page="/jdp/include/favoriteMenu.jsp">
							<jsp:param value="basedata_gameversion_load" name="menuId"/>
						</jsp:include>
					</div>
					<div class="row-fluid">
					<table class="listContent table table-bordered table-striped with-check table-hover">
						<thead>
							<tr>
								<th class="list_index">&nbsp;</th>
								<th class="sortable sorted_game_name"><a href="javascript:pageSort('game.name')">游戏名称</a></th>
								<th style="width: 110px;" class="sortable sorted_version"><a href="javascript:pageSort('version')">游戏版本</a></th>
								<th style="width: 80px;" class="sortable sorted_code"><a href="javascript:pageSort('code')">游戏编号</a></th>
								<th style="width: 110px;" class="sortable sorted_createTime"><a href="javascript:pageSort('createTime')">创建时间</a></th>
								<th style="width: 170px;">操作</th>
								<c:set var="colCount" value="6"/>
								<lauthority:checkAuthority authorityCode="basedata_gameversion_delete">
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
										<td>${item.game.name}</td>
										<td>${item.version}</td>
										<td>${item.code}</td>
										<td><fmt:formatDate value="${item.createTime}" pattern="${defaultDateTimeFormater }"/></td>
										<td>
											<lauthority:checkAuthority authorityCode="basedata_gameversion_load">
												<a href="javascript:void(0)" class="td_2" onclick="redirectPage('<c:url value="/basedata/gameversion/load.shtml?id=${item.id}&${httpParamOpenMode }=${readOpenMode }"/>&modulePath=${modulePath }&menuFrom=${param.menuFrom }&subMenuId=${param.subMenuId }', 800, 600)">
													<i class="icon icon-circle-arrow-up"></i> 查看
												</a>
											</lauthority:checkAuthority>
											<lauthority:checkAuthority authorityCode="basedata_gameversion_modify">
												&nbsp;
												<a href="javascript:void(0)" class="td_2" onclick="redirectPage('<c:url value="/basedata/gameversion/load.shtml?id=${item.id}&${httpParamOpenMode }=${editOpenMode }"/>&modulePath=${modulePath }&menuFrom=${param.menuFrom }&subMenuId=${param.subMenuId }', 800, 600)">
													<i class="icon icon-edit"></i> 修改
												</a>
											</lauthority:checkAuthority>
											<lauthority:checkAuthority authorityCode="basedata_gameversion_delete">
												&nbsp;
												<a href="javascript:void(0)" onclick="deletePojo('<c:url value="/basedata/gameversion/delete.shtml?id=${item.id}"/>')" class="td_2">
													<i class="icon icon-trash"></i> 删除
												</a>
											</lauthority:checkAuthority>
										</td>
										<lauthority:checkAuthority authorityCode="basedata_gameversion_delete">
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