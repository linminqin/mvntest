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
<c:if test="${openMode == editOpenMode }">
	<div class="control-group">
		<label class="control-label read-control-label">编号</label>
		<div class="controls">
			${pojo.code}
		</div>
	</div>
</c:if>

<div class="control-group">
	<label class="control-label">名称 <span class="req">*</span></label>
	<div class="controls">
		<input name="name" type="text" value="${pojo.name}" />
	</div>
</div>
<div class="control-group">
	<label class="control-label">价钱（分）<span class="req">*</span></label>
	<div class="controls">
		<input name="price" type="text" class="priceCheck" value="${pojo.price}"/>
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
		onclick="back('/basedata/gameitem/list.shtml')">返回</button>
</div>


