<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<c:choose>
	<c:when test="${field.fieldSet eq true}">
		<fieldset class="col-sm-9">
			<legend>${field.superLabel}</legend>
			<ul class="procedure-sortable list-unstyled">
				<c:set var="fieldBkp" value="${field}" scope="request"/>
				<c:forEach var="fieldNested" items="${field.fields}" varStatus="status">
					<li class="form-group">
						<div class="col-sm-1 sortable-handle">
	                        <i class="glyphicons glyphicons-sorting pull-right"></i>
	                    </div>
						<c:set var="field" value="${fieldNested}" scope="request"/>
						<jsp:include page="editFields.jsp" />
					</li>
				</c:forEach>
			</ul>
		</fieldset>
		<div class="btn-group col-sm-2">
			<button type="button" class="btn btn-default" data-toggle="modal"
				data-target="#editFieldModal${fieldBkp.name}">
				<i class="glyphicons glyphicons-edit"></i>
			</button>
			<button type="submit" name="deleteField" class="btn btn-default"
				onclick="selectPath(this,'selectedField')">
				<i class="glyphicons glyphicons-remove-2"></i>
			</button>
		</div>
		
		<c:forEach var="pathPart" items="${fieldBkp.path}" varStatus="status">
			<c:set var="springPathNested" value="${status.first ? 'theSelectedStep' : springPathNested}.fields[${pathPart}]" scope="request"/>
		</c:forEach>
		<c:out value="springPath : ${springPath}"></c:out>
		
		<form:hidden path="${springPathNested}.path"/>
	</c:when>
	<c:otherwise>
		<jsp:include page="editField.jsp" />
	</c:otherwise>
</c:choose>
