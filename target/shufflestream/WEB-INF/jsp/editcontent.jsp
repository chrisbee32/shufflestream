<%@ include file="/WEB-INF/jsp/header.jsp" %>

<h2 class="title">Edit Content</h2>
<div class="row">
    <div class="span12">
        <a href="${pageContext.request.contextPath}/admin/managechannel"> &lt;&lt; Back to Channel Manager</a>
    </div>
</div>

    <form method="POST" action="${pageContext.request.contextPath}/admin/editcontent?id=${param.id}">
        <div class="row">
        <div class="span4 form-group">
        <img src="${item.assetUrl_orig}" class="img" /> 
        <br>
        
        <label for="Title">Title:</label> <input id="Title" type="text" name="Title" value="${item.title}" />
        <label for="Artist">Artist:</label> <input id="Artist" type="text" name="Artist" value="${item.artist}" /> 
        <label for="Description">Description:</label> <textarea id="Description" type="text" name="Description" rows="5">${item.description}</textarea>
        <label for="ArtistWebsite">Artist Website:</label>  <input id="ArtistWebsite" type="text" name="ArtistWebsite" value="${item.artistWebsite}" /> 
        <label for="Geo">Geo Location:</label> <input id="Geo" type="text" name="Geo Location" value="${item.geoLocation}" /> 
        <label for="Active">Active:</label> 
        <c:set var="activeFromDB" value="${item.active}" />
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
      
      <label for="Channels">Channels:</label>       
       <c:forEach var="itemChannel" items="${item.channels}">
        <select name="Channels" id="Channels" class="Channels">
          <c:forEach var="channel" items="${channels}">         
           <c:choose>
            <c:when test="${channel eq itemChannel.key}">
                <option value="${channel}" selected="selected">${channel}</option>
            </c:when>
            <c:otherwise>
                <option value="${channel}">${channel}</option>
            </c:otherwise>
           </c:choose>
         </c:forEach>            
        </select>
       </c:forEach><br>
        <input type="hidden" name="channelcount" value="${fn:length(channels)}" >
        <input type="button" id="removeChannel" value="Remove Channel"/>
        <input type="button" id="addChannel" value="Add Channel" /> 
       </div>
       
        <div class="span4 form-group">
        <c:forEach var="attribute" items="${attributes}"> 
         <label for="attributeValue"> ${attribute.key}: </label> 
          <select name="${attribute.key}">
             <c:forEach var="attributeValue" items="${attribute.value}">
	              <c:set var="attributeFromDB" value="${item.attributes[attribute.key]}" />
	               <c:choose>
	               <c:when test="${attributeFromDB == attributeValue}">
	                    <option value="${attributeValue}" selected="selected" id="attributeValue">${attributeValue}</option>
	               </c:when> 
	               <c:otherwise>
	                  <option value="${attributeValue}" id="attributeValue">${attributeValue}</option>
	               </c:otherwise> 
	               </c:choose> 
	          </c:forEach>
            </select>
            <br>
         </c:forEach>
	    <br>
      </div>
      <div class="span3">
        <input type="submit" value="Update" class="btn btn-primary btn-create"/>
      </div>
   </div>
 </form>
    <div class="span12">
          Last Updated: ${item.updatedDate} 
    </div>           
 <br>
 <br>
<!-- closing div from header -->
</div>

</body>
</html>