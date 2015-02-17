<%@ include file="/WEB-INF/jsp/header.jsp" %>
<body>
<h2 class="title">Create Channel</h2>

<div class="content">
<form action="${pageContext.request.contextPath}/createchannel" method="POST">
  <label for="channel">Channel Name: </label><input name="channel" type="text" /> <br />
  <label for="channel">Channel Description: </label><input name="description" type="text" />
  <input type="submit" value="Create" /> 
</form>

<h3 class="title">All Channels</h3>
 <c:forEach var="channel" items="${channels}">
	        <li><a href="${pageContext.request.contextPath}/managechannel?channel=${channel}">${channel}</a></li>
	</c:forEach>
</div>


</body>
</html>