<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jdp/common/common.jsp"%>
<div class="control-group">
	<label class="control-label read-control-label">游戏 <span class="req">*</span></label>
	<div class="controls">
		<select name="gameId" style="width: 50%">
				<c:forEach items="${games }" var="game">
					<option value="${game.id }" 
						<c:if test="${game.id == pojo.gameId}">selected="selected"</c:if>
					>${game.name }</option>
				</c:forEach>
		</select>
	</div>
</div>
<div class="control-group">
	<label class="control-label">版本 <span class="req">*</span></label>
	<div class="controls">
		<input name="version" type="text" value="${pojo.version}" />
	</div>
</div>
<div class="control-group">
	<label class="control-label">编号 <span class="req">*</span></label>
	<div class="controls">
		<input name="code" type="text" value="${pojo.code}" />
	</div>
</div>
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
		onclick="back('/basedata/gameversion/list.shtml')">返回</button>
</div>