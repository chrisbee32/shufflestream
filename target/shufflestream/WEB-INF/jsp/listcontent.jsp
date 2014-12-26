<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Content List</title>
</head>
<body>
<p>Add Content</p>

<c:out value="${title}" /> 

<h2>Add a Channel</h2>
<form method="POST" action="${pageContext.request.contextPath}/addcontent" enctype="multipart/form-data" >
    <input name="file" type="file" /> <br />
    <input type="submit" value="Create" />
</form>

</body>
</html>