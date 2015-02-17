<%@ include file="/WEB-INF/jsp/header.jsp" %>
<body>
<h2 class="title">Loupe Channel Manager</h2>

<div class="content">

<div class="leftrail">
    <c:forEach var="channel" items="${channels}">
        <li><a href="${pageContext.request.contextPath}/managechannel?channel=${channel}">${channel}</a></li>
    </c:forEach> 
</div>
<div class="rightcontent">
    <c:forEach items="${content}" var="item" varStatus="loop">
	    Title: ${item.title} </br>
	    Artist: ${item.artist} </br> 
	    Channel: ${item.channel} </br> 
	    Description: ${item.description} </br>
	    Artist Website: ${item.artistWebsite} </br>  
	    Image: </br>
	    <img src="${item.assetUrl}" class="img" /> </br> </br>
    </c:forEach>
</div>

</div>

</body>
</html>