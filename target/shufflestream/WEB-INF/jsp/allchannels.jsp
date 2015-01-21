<%@ include file="/WEB-INF/jsp/header.jsp" %>
<body>
<h2>Channel Manager</h2>

<h3>Create New Channel</h3>
<form action="${pageContext.request.contextPath}/createchannel" method="POST">
  <label for="channel">Channel Name: </label><input name="channel" type="text" />
  <input type="submit" value="Create" /> 
</form>

<h3>Channel List</h3>
<ul>
	<c:forEach var="channel" items="${channels}">
	    <li><a href="${pageContext.request.contextPath}/managechannel?channel=${channel}">${channel}</a></li>
    </c:forEach> 
</ul> 

</body>
</html>