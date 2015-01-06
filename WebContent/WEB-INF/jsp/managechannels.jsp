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
<h2>Manage Channels</h2>

<h3>Create New Channel</h3>
<form action="/createchannel" method="POST">
  <label for="channel">Channel Name: </label><input name="channel" type="text" />
  <input type="submit" value="Create" /> 
</form>

<h3>Channel List</h3>

....


</body>
</html>