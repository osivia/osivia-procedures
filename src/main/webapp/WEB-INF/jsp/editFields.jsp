<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:choose>
	<c:when test="${field.fieldSet eq true}">
        <li class="form-group">
			<div class="col-sm-12">
			  <c:choose>
			      <c:when test="${form.selectedField.path eq field.path}">
			          <div class="panel panel-info">
			      </c:when>
			      <c:otherwise>
			          <div class="panel panel-default">
			      </c:otherwise>
			  </c:choose>
		   			<div class="panel-heading">
		   				${field.superLabel}
		   			</div>
		   			<div class="panel-body">
		   				<ul class="procedure-sortable list-unstyled">
							<c:set var="fieldBkp" value="${field}" scope="page"/>
							<c:forEach var="nestedField" items="${fieldBkp.fields}" varStatus="status">
								<c:set var="field" value="${nestedField}" scope="request"/>
								<jsp:include page="editFields.jsp" />
							</c:forEach>
						</ul>
		   			</div>
	   			</div>
   			</div>
			<c:forEach var="pathPart" items="${fieldBkp.path}" varStatus="status">
				<c:set var="springPathNested" value="${status.first ? 'theSelectedStep' : springPathNested}.fields[${pathPart}]" scope="request"/>
			</c:forEach>
			<form:hidden path="${springPathNested}.path"/>
   		</li>
	</c:when>
	<c:otherwise>
		<jsp:include page="editField.jsp" />
	</c:otherwise>
</c:choose>
