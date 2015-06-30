<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jdp/common/htmlDoctype.jsp"%>
<%@ include file="/jdp/common/common.jsp"%>
<html>
	<head>
		<title>${subMenu.label }</title>
		<%@ include file="/jdp/common/header.jsp"%>
		<%@ include file="/jdp/form/header.jsp"%>
	</head>
	<body>
		<%@ include file="/jdp/page/topMenu.jsp"%>
		<div class="container_content">
			<%@ include file="/jdp/page/leftMenu.jsp"%>
			<div id="content">
				<%@ include file="/jdp/page/location.jsp"%>
				<div class="container-fluid">
					<div class="row-fluid">
					<form:form id="mainForm" action="${ctx}/query/game/activateimport.shtml" method="post" 
						commandName="pojo" class="form-horizontal" enctype="multipart/form-data">
					<%@ include file="/jdp/form/field.jsp" %>
					<div class="span12 nomargin">
						<div class="widget-box form-table">
							<div class="widget-title">
								<span class="icon"><i class="icon-align-justify"></i></span>
								<h5>下载清单导入</h5>
							</div>
							<div class="nopadding">
									<%@ include file="/jdp/form/messages.jsp" %>
									<div class="row-fluid">
										<div class="messageDiv">
											<c:if test="${not empty errorFile }">
												<div class="alert alert-error">
													<button class="close" data-dismiss="alert">×</button>
													<a target="_blank" class="error" href="${ctx}${errorFile}">点击查看（或右键另存）错误文件</a>
												</div>
											</c:if>
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">统计日期 <span class="req">*</span></label>
										<div class="controls">
											<input onfocus="WdatePicker({readOnly:true, dateFmt:'yyyyMMdd'})" type="text" class="medium" name="stateDate" readonly="readonly" value="${param.stateDate }">
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">文件 <span class="req">*</span></label>
										<div class="controls">
											<input type="file" name="file" id="file">
										</div>
									</div>
									<div class="control-group">
										<label class="control-label">说明 <span class="req">*</span></label>
										<div class="controls" style="  padding-top: 15px;">
											Excel文件，每行的字段顺序如下：渠道编号，游戏编号，下载量；从第一行开始（无需标题行）。
										</div>
									</div>
									<div class="form-actions">
										<button type="submit" class="btn btn-primary" onclick="setImportState()" id="importBtn">导入</button>
										&nbsp;&nbsp;
										<button type="button" class="btn btn-primary"
											onclick="back('/query/game/queryimportdownload.shtml')">返回</button>
									</div>
							</div>
						</div>						
					</div>
					</form:form>
				</div>
				</div>
			</div>
			<%@ include file="/jdp/page/footer.jsp"%>
		</div>
		<%@ include file="/jdp/common/scripts.jsp"%>
		<%@ include file="/jdp/common/date.jsp"%>
	</body>
	<script type="text/javascript">
		function setImportState() {
			$('#importBtn').addClass('disabled');
			$('#importBtn').text('导入中...');
		}
	</script>
</html>