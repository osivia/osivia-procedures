<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal"	prefix="op"%>

<portlet:defineObjects />

<c:set var="filterBkp" value="${filter}" scope="page" />
<c:forEach var="pathPart" items="${filterBkp.filterPath}" varStatus="status">
	<c:set var="springPath"	value="${status.first ? 'theSelectedAction' : springPath}.filters[${pathPart}]"	scope="request" />
</c:forEach>
<div class="panel panel-default">
	<div class="panel-heading">
		<op:translate key="${filterBkp.filterName}" />
		<button type="submit" name="deleteFilter" class="btn btn-default pull-right" onclick="selector(this,'${filterBkp.filterInstanceId}','selectedFilter');">
			<i class="glyphicons glyphicons-bin"></i>
		</button>
	</div>
	<div class="panel-body">
		<form:hidden path="${springPath}.filterId" />
		<form:hidden path="${springPath}.filterPath" />
		<ul class="list-unstyled">
			<c:forEach var="argument" items="${filterBkp.argumentsList}" varStatus="argStatus">
				<li class="form-group">
					<div class="col-sm-3">
						<label class="control-label">${argument.argumentName}</label>
					</div>
					<div class="col-sm-3">
						<c:if test="${argument.type eq 'TEXT'}">
							<form:input	path="${springPath}.argumentsList[${argStatus.index}].argumentValue" type="text" cssClass="form-control" placeholder="argumentValue" />
						</c:if>
						<c:if test="${argument.type eq 'BOOLEAN'}">
							<form:checkbox path="${springPath}.argumentsList[${argStatus.index}].argumentValue"	cssClass="form-control" />
						</c:if>
					</div>
				</li>
			</c:forEach>
		</ul>
        <ul class="list-unstyled filter-sortable">
            <c:forEach var="nestedFilter" items="${filterBkp.filters}" varStatus="status">
	            <li>
                    <c:set var="filter" value="${nestedFilter}" scope="request" />
	                <jsp:include page="editFilters.jsp"/>
	            </li>
            </c:forEach>
		</ul>
	</div>
</div>
