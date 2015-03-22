<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<link href="<c:url value="/statics/css/shufflestyle.css" />" rel="stylesheet">
<link href="<c:url value="/statics/bootstrap/css/bootstrap.min.css" />" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script src="/statics/bootstrap/js/bootstrap.min.js"></script>
<script src="/statics/js/shufflescript.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Loupe Admin</title>
</head>
<body>
<div class="container">
<h1>Loupe Content Manager</h1>
<div class="nav">
<ul class="nav nav-pills navul">
  <li role="presentation"><a href="${pageContext.request.contextPath}/admin/upload">Add Content</a></li>
  <li role="presentation"><a href="${pageContext.request.contextPath}/admin/managechannel">Manage Channels</a></li>
  <li role="presentation"><a href="${pageContext.request.contextPath}/admin/createchannel">Create Channel</a></li>
  <li role="presentation" class="dropdown">
    <a class="dropdown-toggle" data-toggle="dropdown" href="#" role="button" aria-expanded="false">
      APIs <span class="caret"></span>
    </a>
     <ul class="dropdown-menu" role="menu">
        <li role="presentation"><a href="${pageContext.request.contextPath}/getchannels">Get Channels API</a></li>
       <li role="presentation"><a href="${pageContext.request.contextPath}/getcontent">Get Content API</a></li>
  </ul>
</li>
</ul>
</div>

