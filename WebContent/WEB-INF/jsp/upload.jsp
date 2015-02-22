<%@ include file="/WEB-INF/jsp/header.jsp" %>
<body>
<h2 class="title">Add Content</h2>
<form method="POST" action="${pageContext.request.contextPath}/addcontent" enctype="multipart/form-data" >
    <p>Choose File: <input name="file" type="file" /> </p>
    <p>Title: <input name="Title" type="text" /></p>
    <p>Artist: <input name="Artist" type="text" /> </p>
    <p>Description: <input name="Description" type="text" /> </p>
    <p>ArtistWebsite: <input name="ArtistWebsite" type="text" /> </p>
    <p>Channel:
    <select name="Channel">
       <c:forEach var="channel" items="${channels}">
            <option value="${channel}">${channel}</option>                                
       </c:forEach>            
    </select>
    </p>
    <h3>Attributes:</h3>
    <p>Geo Location: <input name="Geo Location" type="text" /> </p>
    <c:forEach var="attribute" items="${attributes}"> 
          <p>${attribute.key}: 
          <select name="${attribute.key}">
            <c:forEach var="attributeValue" items="${attribute.value}">
                <option value="${attributeValue}">${attributeValue}</option> 
            </c:forEach>
            </select>
          </p>
    </c:forEach>
    <input type="submit" value="Create" />
</form>

</body>
</html>