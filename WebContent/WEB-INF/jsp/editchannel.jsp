<%@ include file="/WEB-INF/jsp/header.jsp" %>

<h2 class="title">Edit Channel</h2>
<div class="row">
    <div class="span12">
        <a href="${pageContext.request.contextPath}/createchannel"> &lt;&lt; Back to Create Channel</a>
    </div>
</div>
<br>
    <form method="POST" action="${pageContext.request.contextPath}/editchannel?id=${param.id}">
      <div class="row">
        <div class="span4 form-group">      
        <label for="ChannelName">Channel Name:</label> <input id="ChannelName" type="text" name="ChannelName" value="${channel.channelName}" />
        <label for="Description">Description:</label> <input id="Description" type="text" name="Description" value="${channel.description}" /> 
        <label for="Active">Active:</label> 
        <c:set var="activeFromDB" value="${channel.active}" />
        <select name="Active" id="Active">
            <c:choose>
                <c:when test="${activeFromDB == true }">
                    <option value="true" selected="selected">true</option>
                    <option value="false">false</option>
                </c:when>
                <c:when test="${activeFromDB == false }">
                    <option value="true">true</option>
                    <option value="false" selected="selected">false</option>
                </c:when>
            </c:choose>
        </select>
      <div class="span4">
        <input type="submit" value="Update" class="btn btn-primary btn-create"/>
      </div>
   </div>
   </div>
   <br>
 </form>
  <div class="row">
    <div class="span12">
          Last Updated: ${channel.updatedDate} 
    </div>        
  </div>   
 <br>
 <br>
<!-- closing div from header -->
</div>

</body>
</html>