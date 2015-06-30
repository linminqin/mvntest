<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jdp/common/htmlDoctype.jsp"%>
<%@ include file="/jdp/common/common.jsp"%>
<html>
	<head>
		<title>${subMenu.label }</title>
		<%@ include file="/jdp/common/header.jsp"%>
		<%@ include file="/jdp/view/header.jsp"%>
	</head>
	<%@ include file="/jdp/page/topMenu.jsp"%>
		<div class="container_content">
			<%@ include file="/jdp/page/leftMenu.jsp"%>
			<div id="content">
				<%@ include file="/jdp/page/location.jsp"%>
				<div class="container-fluid">
					<form id="mainForm" action="<c:url value="/query/game/querydownloadcount.shtml"/>" class="form-vertical">
					<%@ include file="/jdp/view/field.jsp"%>
					<div class="row-fluid">
						<div class="span12">
							<div class="widget-box border-radius">
								<div class="nopadding control-group">
										<c:if test="${empty operatorChannel}">
											<label class="control-label"  style="width: 70px;">渠道：</label>
											<select name="channelCode" style="width: 144px !important;">
												<option value=""  >所有</option>
												<c:forEach items="${channels }" var="channel">
													<option value="${channel.code }" 
														<c:if test="${channel.code == channelCode}">selected="selected"</c:if>
													>${channel.name }</option>
												</c:forEach>
											</select>
											&nbsp;
										</c:if>
										<label class="control-label"  style="width: 70px;">游戏：</label>
										<select name="gameCode" style="width: 144px !important;">
											<option value=""  >所有</option>
											<c:forEach items="${games }" var="game">
												<option value="${game.code }" 
													<c:if test="${game.code == param.gameCode}">selected="selected"</c:if>
												>${game.game.name } ${game.version }</option>
											</c:forEach>
										</select>
								</div>
								<div class="nopadding control-group">
										<label class="control-label"  style="width: 70px;">统计日期：</label>
										<input type="text" name="beginDate" class="medium" value="${param.beginDate }" onFocus="WdatePicker({readOnly:true, dateFmt:'yyyyMMdd'})"/>
										&nbsp;
										<label style="width: 70px; text-align: center;" class="control-label" >-</label>
										<input type="text" name="endDate" class="medium" value="${param.endDate }" onFocus="WdatePicker({readOnly:true, dateFmt:'yyyyMMdd'})"/>
										&nbsp;
										<label style="width: 50px; text-align: center;" class="control-label" >&nbsp;</label>
										<button type="submit" class="btn action-button">查询</button>
								</div>
							</div>						
						</div>
					</div>
					<div class="row-fluid">
						<jsp:include page="/jdp/include/favoriteMenu.jsp">
							<jsp:param value="query_game_downloadcount_load" name="menuId"/>
						</jsp:include>
					</div>
					<div class="row-fluid">
					<table class="listContent table table-bordered table-striped with-check table-hover">
						<thead>
							<tr>
								<th>&nbsp;</th>
								<th>统计日期</th>
								<th>渠道</th>
								<th>游戏</th>
								<th>下载量</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="data" items="${datas}" varStatus="status">
								<tr>
								<c:choose>
									<c:when test="${status.last }">
										<th>${status.count}</th>
										<th>总计</th>
										<th></th>
										<th></th>
										<th>${data.downloadCount}</th>
									</c:when>
									<c:otherwise>
										<td>${status.count}</td>
										<td>${data.stateDate}</td>
										<td>${data.channelName}</td>
										<td>${data.gameName} ${data.gameVersion}</td>
										<td>${data.downloadCount}</td>
									</c:otherwise>
								</c:choose>
								</tr>
							</c:forEach>
						</tbody>
					</table>
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