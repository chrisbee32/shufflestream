<%@ include file="/WEB-INF/jsp/header.jsp"%>

<h2>Create Channel</h2>

<div class="row">
<div class="span8 form-group">
<form action="${pageContext.request.contextPath}/createchannel" method="POST">
  <label for="channelName">Channel Name: </label><input name="channel" type="text" id="channelName"/>
  <label for="channelDesc">Channel Description: </label><input name="description" type="text" id="channelDesc"/> <br>
  <input type="submit" value="Create" class="btn btn-primary btn-create"/> 
</form>
</div>

<div class="span4">
<h3>Edit Channel</h3>
 <c:forEach var="channel" items="${channels}">
            <li><a href="${pageContext.request.contextPath}/editchannel?id=${channel.id}">${channel.channelName}</a></li>
    </c:forEach>
</div>

</div>

<!-- closing div from header -->
</div>

</body>
</html>