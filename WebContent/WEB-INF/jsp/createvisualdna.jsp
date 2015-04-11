<%@ include file="/WEB-INF/jsp/header.jsp"%>

<h2>Create Visual DNA</h2>

<div class="row">
<div class="span8 form-group">
<form action="${pageContext.request.contextPath}/admin/createvisualdna" method="POST">
  <label for="channelName">DNA Title: </label><input name="title" type="text" id="title"/>
  <label for="channelDesc">DNA Description: </label><input name="description" type="text" id="dnaDesc"/> <br>
  <input type="submit" value="Create" class="btn btn-primary btn-create"/>
</form>
</div>

<div class="span4">
<h3>Edit DNA</h3>
 <c:forEach var="channel" items="${dna}">
            <li><a href="${pageContext.request.contextPath}/admin/editdna?id=${channel.id}">${dna.Title}</a></li>
    </c:forEach>
</div>

</div>

<!-- closing div from header -->
</div>

</body>
</html>