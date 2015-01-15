<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<h2>Add Content</h2>
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