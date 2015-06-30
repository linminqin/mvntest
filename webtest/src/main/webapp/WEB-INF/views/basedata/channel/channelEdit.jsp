<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jdp/common/common.jsp"%>
<div class="control-group">
	<label class="control-label">名称 <span class="req">*</span></label>
	<div class="controls">
		<input name="name" type="text" value="${pojo.name}" />
	</div>
</div>
<c:if test="${openMode == editOpenMode }">
	<div class="control-group">
		<label class="control-label">渠道编号</label>
		<div class="controls">
			${pojo.code}
		</div>
	</div>
</c:if>
<div class="control-group">
	<label class="control-label">说明</label>
	<div class="controls">
		<textarea name="summary" maxlength="512" rows="5">${pojo.summary}</textarea>
	</div>
</div>
<div class="form-actions">
	<button type="submit" class="btn btn-primary">提交</button>
	&nbsp;&nbsp;
	<button type="button" class="btn btn-primary"
		onclick="back('/basedata/channel/list.shtml')">返回</button>
</div>