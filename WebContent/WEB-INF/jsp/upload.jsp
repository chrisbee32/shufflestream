<%@ include file="/WEB-INF/jsp/header.jsp" %>
<body>
<h2 class="title">Add Content</h2>
<form method="POST" action="${pageContext.request.contextPath}/addcontent" enctype="multipart/form-data" >
    <p>Choose File: <input name="file" type="file" /> </p>
    <p>Title: <input name="Title" type="text" /></p>
    <p>Artist: <input name="Artist" type="text" /> </p>
    <p>Description: <input name="Description" type="text" /> </p>
    <p>ArtistWebsite: <input name="ArtistWebsite" type="text" /> </p>
    <p>Channels:
    <select name="Channel">
       <c:forEach var="channel" items="${channels}">
            <option value="${channel}">${channel}</option>                                
       </c:forEach>            
    </select>
    </p>
    <input type="submit" value="Create" />
</form>

</body>
</html>