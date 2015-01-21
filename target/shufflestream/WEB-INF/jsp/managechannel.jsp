<%@ include file="/WEB-INF/jsp/header.jsp" %>
<body>
<h2 class="title">Channel Manager</h2>

<div class="content">

<div class="leftrail">
<c:forEach var="channel" items="${channels}">
        <li><a href="${pageContext.request.contextPath}/managechannel?channel=${channel}">${channel}</a></li>
    </c:forEach> 
</div>
<div class="rightcontent">
<c:forEach items="${content}" var="entry">
    Title: ${entry.key} </br>
    Artist: ${entry.value.artist} </br> 
    Channel: ${entry.value.channel} </br> 
    Description: ${entry.value.description} </br>
    Artist Website: ${entry.value.artistWebsite} </br>  
    Image: </br>
    <img src="${entry.value.assetUrl}" class="img" /> </br> </br> 
    


</c:forEach>
</div>

</div>

</body>
</html>