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
					<form id="mainForm" action="<c:url value="/query/game/querydownload.shtml"/>" method="get" class="form-vertical">
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
										<label class="control-label"  style="width: 70px;">下载时间：</label>
										<lhtml:propertyFilter inputType="dateTime" compareType="GE" propertyName="activateTime" styleClass="medium"/>
										&nbsp;
										<label style="width: 70px; text-align: center;" class="control-label" >-</label>
										<lhtml:propertyFilter inputType="dateTime" compareType="LE" propertyName="activateTime" styleClass="medium"/>
										&nbsp;
										<label class="control-label"  style="width: 70px;">运营商：</label>
										<lhtml:propertyFilter inputType="select" compareType="EQ" propertyName="player.operator"  styleClass="bian medium" style="width: 144px !important;">
			            					<option value="">全部</option>
				            				<option value="CM">中国移动</option>
				            				<option value="CU">中国联通</option>
				            				<option value="CTE">中国电信</option>
			            				</lhtml:propertyFilter>
								</div>
								<div class="nopadding control-group">
										<label class="control-label"  style="width: 70px;">省份：</label>
			            				<select style="width: 144px !important;" class="bian medium" name="provinceCode" onchange="changeProvince(this.value)">
			            					<option value="">全部</option>
				            				<c:if test="${not empty provinces }">
				            					<c:forEach items="${provinces }" var="province">
				            						<option value="${province.code }"
				            						<c:if test="${param.provinceCode == province.code}">
				            							selected="selected"
				            						</c:if>
				            						>${province.name }</option>
				            					</c:forEach>
				            				</c:if>
				            			</select>
			            				&nbsp;
										<label class="control-label"  style="width: 70px;">城市：</label>
										<lhtml:propertyFilter inputType="select" compareType="EQ" id="citySelect" propertyName="player.region"  styleClass="bian medium" style="width: 144px !important;">
			            					<option value="">全部</option>
				            				<c:if test="${not empty citys }">
				            					<c:forEach items="${citys }" var="city">
				            						<option value="${city.code }">${city.name }</option>
				            					</c:forEach>
				            				</c:if>
			            				</lhtml:propertyFilter>
										<label class="control-label"  style="width: 70px;">&nbsp;</label>
										&nbsp;
										<button type="submit" name="query" class="btn action-button">查询</button>
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
							<jsp:param value="query_game_download_load" name="menuId"/>
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
								<th>省份</th>
								<th>城市</th>
								<th>IMSI</th>
								<th>运营商</th>
								<th>下载时间</th>
								<th>最后活跃时间</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="item" items="${page.items}" varStatus="status">
									<tr>
										<td>${status.count + (page.currentPage - 1) * page.pageSize}</td>
										<td>${item.playerCode}</td>
										<td>${item.channel.name}</td>
										<td>${item.gameVersion.game.name} ${item.gameVersion.version}</td>
										<td>${item.player.city.province.name}</td>
										<td>${item.player.city.name}</td>
										<td>${item.player.imsi}</td>
										<td>
											<c:choose>
												<c:when test="${item.player.operator == 'CM'}">中国移动</c:when>
												<c:when test="${item.player.operator == 'CU'}">中国联通</c:when>
												<c:when test="${item.player.operator == 'CTE'}">中国电信</c:when>
												<c:when test="${item.player.operator == 'CTI'}">中国铁通</c:when>
											</c:choose>
										</td>
										<td><fmt:formatDate value="${item.activateTime}" pattern="${defaultDateTimeFormater }"/></td>
										<td><fmt:formatDate value="${item.player.lastActiveTime}" pattern="${defaultDateTimeFormater }"/></td>
									</tr>
								</c:forEach>
									<tr>
										<td colspan="10" class="pageColumn"><%@ include file="/jdp/view/page.jsp" %></td>
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
		<script type="text/javascript">
			function changeProvince(provinceCode) {
				if(provinceCode == '') {
					$('#citySelect').html('<option value="">全部</option>');
					return;
				}
				$.get('${ctx }/city/ajaxList.shtml', 
					{provinceCode: provinceCode},
					function(json) {
						var cityOptions = '<option value="">全部</option>';
						$.each(json.data.citys, function(i, item) {
				            cityOptions += '<option value="' + item.code + '">' + item.name + '</option>';		
						});
						$('#citySelect').html(cityOptions);
					},
					'json'
				);
			}
		</script>
	</body>
</html>