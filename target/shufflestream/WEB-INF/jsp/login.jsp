<%@ include file="/WEB-INF/jsp/header.jsp"%>

<h2>Login</h2>

<%
String errorString = (String)request.getAttribute("error");
if(errorString != null && errorString.trim().equals("true")){
   out.println("Incorrect login name or password. Please retry using correct login name and password.");
}
%>
<br>
<div class="row">
<div class="span8 form-group">
<form action="<c:url value='j_spring_security_check' />" method="POST">
   <label for="j_username">Username: </label><input type='text' name='j_username' value=''> <br>
   <label for="j_password">Password: </label><input type='password' name='j_password' /> <br>
   <input name="submit" type="submit" value="submit" />
</form>
</div>

</div>

<!-- closing div from header -->
</div>

</body>
</html>