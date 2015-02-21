<%@ include file="/WEB-INF/jsp/header.jsp" %>
<body>
<h2 class="title">Loupe Edit Content</h2>

<div class="content">

<div class="leftrail">
 <a href="${pageContext.request.contextPath}/managechannel">Back to All Channels</a>
</div>
<div class="rightcontent">
    <form method="POST" action="${pageContext.request.contextPath}/editcontent?id=${param.id}">
        Image: </br>
        <img src="${item.assetUrl_orig}" class="img" /> </br> </br>
        Title: <input type="text" name="Title" value="${item.title}" /><br>
        Artist: <input type="text" name="Artist" value="${item.artist}" /> </br>
        Channel:
        <select name="Channel">
          <c:forEach var="channel" items="${channels}">
            <option value="${channel}">${channel}</option>                                
         </c:forEach>            
        </select></br>
        Description: <input type="text" name="Description" value="${item.description}"/> </br>
        Artist Website: <input type="text" name="ArtistWebsite" value="${item.artistWebsite}" /> </br>
        Geo Location: <input type="text" name="Geo Location" /> </br>
        Active: 
        <c:set var="activeFromDB" value="${item.active}" />
        <select name="Active">
            <c:choose>
                <c:when test="${activeFromDB == true }">
                    <option value="true" selected="selected">true</option>
                    <option value="false">false</option>
                </c:when>
                <c:when test="${activeFromDB == false }">
                    <option value="true">true</option>
                    <option value="false" selected="selected">false</option>
                </c:when>
            </c:choose>
        </select>  <br> 
        <c:forEach var="attribute" items="${attributes}"> 
          ${attribute.key}: 
          <select name="${attribute.key}">
             <c:forEach var="attributeValue" items="${attribute.value}">
	              <c:set var="attributeFromDB" value="${item.attributes[attribute.key]}" />
	               <c:choose>
	               <c:when test="${attributeFromDB == attributeValue}">
	                    <option value="${attributeValue}" selected="selected">${attributeValue}</option>
	               </c:when> 
	               <c:otherwise>
	                  <option value="${attributeValue}">${attributeValue}</option>
	               </c:otherwise> 
	               </c:choose> 
	          </c:forEach>
            </select>
            <br>
    </c:forEach>
    <br>
    Last Updated Date: ${item.updatedDate} 
    <br>
    <br>
    <input type="submit" value="Update"/> 
 </form>
       
</div>

</div>

</body>
</html>