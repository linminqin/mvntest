<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jdp/common/common.jsp" %>
<div class="control-group">
	<label class="control-label read-control-label">游戏名称：</label>
	<div class="controls">
		${pojo.gameVersion.game.name}
	</div>
</div>
<div class="control-group">
	<label class="control-label read-control-label">游戏版本：</label>
	<div class="controls">
		${pojo.gameVersion.version}
	</div>
</div>
<c:if test="${empty operatorChannelCode }">
	<div class="control-group">
		<label class="control-label read-control-label">游戏编号：</label>
		<div class="controls">
			${pojo.gameVersion.code}
		</div>
	</div>
	<div class="control-group">
		<label class="control-label read-control-label">渠道名称：</label>
		<div class="controls">
			${pojo.channel.name}
		</div>
	</div>
	<div class="control-group">
		<label class="control-label read-control-label">渠道编号：</label>
		<div class="controls">
			${pojo.channel.code}
		</div>
	</div>
</c:if>
<div class="control-group">
	<label class="control-label read-control-label">推广时间时间：</label>
	<div class="controls">
		<fmt:formatDate value="${pojo.mapTime}" pattern="${defaultDateTimeFormater }"/>
	</div>
</div>
<div class="control-group">
	<label class="control-label read-control-label">说明：</label>
	<div class="controls">
		${pojo.summary}
	</div>
</div>
<div class="form-actions">
	<button type="button" class="btn btn-primary"
		onclick="back('/basedata/gamechannel/list.shtml')">返回</button>
</div>