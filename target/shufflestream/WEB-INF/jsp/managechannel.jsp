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
	    Image: </br>
        <img src="${item.assetUrl_orig}" class="img" /> </br> </br>
	    Title: ${item.title} </br>
	    Artist: ${item.artist} </br>
	    <a href="${pageContext.request.contextPath}/editcontent?id=${item.id}">Edit Content</a>
	    </br> 
	    </br>  
	    Channel: ${item.channel} </br> 
	    Description: ${item.description} </br>
	    Artist Website: ${item.artistWebsite} </br>
	    Created Date: ${item.createdDate} </br>
	    Active: ${item.active} </br>     
	    <c:forEach items="${item.attributes}" var="attribute">
	       ${attribute.key} : ${attribute.value} </br> 
	    </c:forEach>
	    </br> 
	    </br> 
	    
    </c:forEach>
</div>

</div>

</body>
</html>