<%@ include file="/WEB-INF/jsp/header.jsp" %>

<h2>Add Content</h2>
<div class="row">
<form method="POST" action="${pageContext.request.contextPath}/admin/addcontent" enctype="multipart/form-data">
   <div class="span4 form-group">
   <label class="btn btn-primary" for="fileUploader">
    <input id="fileUploader" name="file" type="file" style="display:none;">
    Select File
    </label>
    <br>
    <img id="uploadPreview" src="#" alt="Preview Image" class="img" style="display:none;" /><br>
    <h3>Properties:</h3>
    <label for="Title">Title:</label> <input name="Title" type="text" id="Title"/>
    <label for="Artist">Artist:</label> <input name="Artist" type="text" id="Artist" /> 
    <label for="Description">Description:</label> <textarea name="Description" rows="5" id="Description"></textarea> 
    <label for="ArtistWebsite">Artist Website:</label> <input name="ArtistWebsite" type="text" id="ArtistWebsite"/> 
    <label for="Geo">Geo Location:</label> <input name="Geo Location" type="text" id="Geo"/> 
    <label for="Channels">Channel:</label>
    <select name="Channels" id="Channels" class="Channels">
       <c:forEach var="channel" items="${channels}">
            <option value="${channel}">${channel}</option>                                
       </c:forEach>            
    </select><br>
    <input type="hidden" name="channelcount" value="${fn:length(channels)}" >
    <input type="button" id="addChannel" value="Add Channel" class="btn btn-default"/>
   </div> 
    
   <div class="span4 form-group">
    <h3>Attributes:</h3>
    <c:forEach var="attribute" items="${attributes}"> 
        <label for="attributeValue"> ${attribute.key}: </label> 
          <select name="${attribute.key}">
            <c:forEach var="attributeValue" items="${attribute.value}">
                <option value="${attributeValue}" id="attributeValue">${attributeValue}</option> 
            </c:forEach>
            </select>
    </c:forEach>
    <br><br>
    </div> 
    <div class="span3">
        <input type="submit" value="Upload Content" class="btn btn-primary btn-create" />
    </div>
    
</form>
</div>
<!-- closing div from header -->
</div>
</body>
</html>