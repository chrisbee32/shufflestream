<%@ include file="/WEB-INF/jsp/header.jsp"%>

<h2>Create Visual DNA</h2>

<div class="row">
<div class="span8 form-group">
<form action="${pageContext.request.contextPath}/admin/createvisualdna" method="POST">
  <label for="title">DNA Title: </label><input name="title" type="text" id="title"/>
  <label for="parentId">DNA Paren ID: </label><input name="parentId" type="text" id="parentId"/>
  <label for="scaleMin">DNA Scale Min: </label><input name="scaleMin" type="text" id="scaleMin"/>
  <label for="scaleMax">DNA Scale Max: </label><input name="scaleMax" type="text" id="scaleMax"/>
  <label for="interfaceOrder">DNA Order: </label><input name="interfaceOrder" type="text" id="interfaceOrder"/>
  <label for="group">DNA Group: </label><input name="group" type="text" id="group"/>
  <label for="isTopDNA">DNA IsTop: </label><input name="isTopDNA" type="text" id="isTopDNA"/>
  <label for="scaleValues">DNA Scale Values: </label><input name="scaleValues" type="text" id="scaleValues"/>
  <label for="description">DNA Description: </label><input name="description" type="text" id="description"/> <br>
  <label for="uuid">DNA UUID: </label><input name="uuid" type="text" id="uuid"/> <br>
  <input type="submit" value="Create" class="btn btn-primary btn-create"/>
</form>
</div>

<div class="span4">
<h3>Edit DNA</h3>
 <c:forEach var="dna" items="${dna}">
            <li><a href="${pageContext.request.contextPath}/admin/createvisualdna?id=${dna.Id}">${dna.Title}</a></li>
    </c:forEach>
</div>

</div>

<!-- closing div from header -->
</div>

</body>
</html>