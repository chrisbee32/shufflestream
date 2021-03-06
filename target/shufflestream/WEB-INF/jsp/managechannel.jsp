<%@ include file="/WEB-INF/jsp/header.jsp" %>

<h2>Channel Manager</h2>

<select name="Channel" id="channelMgr" >
    <option value="">Choose a channel:</option>
	<c:forEach var="channel" items="${channels}">
		<option value="${pageContext.request.contextPath}/admin/managechannel?channel=${channel}">${channel}</option>
	</c:forEach>
</select>

<div class="row">
	<c:forEach items="${content}" var="item" varStatus="loop">
		<div class="span3">
			<form method="POST" action="${pageContext.request.contextPath}/admin/updateorder">
				<img src="${item.assetUrl_orig}" class="img" alt="loupe" />
				<br>Title: ${item.title} <br> 
				Artist:	${item.artist} <br> 
				<a href="${pageContext.request.contextPath}/admin/editcontent?id=${item.id}">Edit Content</a> <br>
		       	<input type="text" class="ordervalue" id="ordervalue" name="ordervalue" value="${item.channels[param.channel]}" /> <br>
				<input type="hidden" name="id" value="${item.id}" />
				<input type="hidden" name="channelParam" value="${param.channel}" />  
				<input type="submit" value="Update Order"> 
			</form>
			<form method="POST" action="${pageContext.request.contextPath}/admin/makecover">
			    <input type="submit" value="Make Cover">
			    <input type="hidden" name="id" value="${item.id}" />
                <input type="hidden" name="channelParam" value="${param.channel}" />  
			</form>
			
		</div>
	</c:forEach>
</div>
<!-- closing div from header -->
</div>
</body>
</html>