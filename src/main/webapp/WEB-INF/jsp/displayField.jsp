<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<c:set var="applicableName" value="osivia.${field.name}.applicable" />
<c:set var="applicable" value="${form.record.globalVariablesValues[applicableName]}" />

<c:if test="${applicable ne 'false' }">

	<li>
	    <div class="form-group">
	        <label class="col-sm-3 control-label">${field.superLabel}</label>
	        <div class="col-sm-9">
	            <c:set var="fieldType" value="${form.procedureModel.variables[field.name].type}" scope="request" />
	            <c:set var="fieldValue" value="${form.record.globalVariablesValues[field.name]}" scope="request" />
	            <c:set var="fieldJsonValue" value="${form.record.jsonValues[field.name]}" scope="request" />
	            <c:set var="fieldLevel" value="1" scope="request" />
	            <jsp:include page="displayFieldValue.jsp" />
	        </div>
	    </div>
	</li>

</c:if>