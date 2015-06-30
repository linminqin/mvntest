<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/jdp/common/common.jsp" %>
<div class="control-group">
<label class="control-label read-control-label">游戏名称：</label>
	<div class="controls">
		${pojo.game.name}
	</div>
	<label class="control-label read-control-label">编号：</label>
	<div class="controls">
		${pojo.code}
	</div>
	<label class="control-label read-control-label">名称：</label>
	<div class="controls">
		${pojo.name}
	</div>
	<label class="control-label read-control-label">价钱（分）：</label>
	<div class="controls">
		 ${pojo.price}
	</div>
</div>
<div class="control-group">
	<label class="control-label read-control-label">创建时间：</label>
	<div class="controls">
		<fmt:formatDate value="${pojo.createTime}" pattern="${defaultDateTimeFormater }"/>
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
		onclick="back('/basedata/gameitem/list.shtml')">返回</button>
</div>