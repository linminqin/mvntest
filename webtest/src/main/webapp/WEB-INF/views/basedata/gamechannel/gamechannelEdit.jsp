<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jdp/common/common.jsp"%>
<div class="control-group">
	<label class="control-label read-control-label">游戏 <span class="req">*</span></label>
	<div class="controls">
		<select name="gameversionId"  size="15" style="width: 50%">
				<c:forEach items="${gameVersions }" var="gameVersion">
					<option  value="${gameVersion.id }" 
						<c:if test="${gameVersion.id == pojo.gameVersion.id || gameVersion.id == param.gameversionId}">selected="selected"</c:if>
					>${gameVersion.game.name } ${gameVersion.version }</option>
				</c:forEach>
		</select>
	</div>
</div>
<div class="control-group">
	<label class="control-label">渠道 <span class="req">*</span></label>
	<div class="controls">
		<select name="channelId" size="10" style="width: 50%">
				<c:forEach items="${channels }" var="channel">
					<option value="${channel.id }" 
						<c:if test="${channel.id == pojo.channel.id || channel.id == param.channelId}">selected="selected"</c:if>
					>${channel.name }</option>
				</c:forEach>
		</select>
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
		onclick="back('/basedata/gamechannel/list.shtml')">返回</button>
</div>