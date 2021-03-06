<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal"	prefix="op"%>


<c:set var="filterBkp" value="${nestedFilter}" scope="page" />
<c:forEach var="pathPart" items="${filterBkp.filterPath}" varStatus="status">
	<c:set var="springPath"	value="${status.first ? 'theSelectedAction' : springPath}.filters[${pathPart}]"	scope="request" />
</c:forEach>
        <span class="filter-title <c:if test="${form.selectedFilter.filterPath eq filterBkp.filterPath}">filter-selected</c:if>">
           <i class="halflings halflings-menu-right"></i>
		   <c:if test="${empty filterBkp.filterName}">
				<op:translate key="${filterBkp.labelKey}" classLoader="${filterBkp.classLoader}"/>
		   </c:if>
		   <c:if test="${not empty filterBkp.filterName}">
	            ${filterBkp.filterName}
	       </c:if>
       </span>
		<form:hidden path="${springPath}.filterId" />
		<form:hidden path="${springPath}.filterPath" />
		<c:if test="${filterBkp.hasChildren}">
	        <ul class="filter-sortable">
	            <c:forEach var="nestedFilter" items="${filterBkp.filters}" varStatus="status">
		            <li>
	                    <c:set var="nestedFilter" value="${nestedFilter}" scope="request" />
		                <jsp:include page="editFilters.jsp"/>
		            </li>
	            </c:forEach>
			</ul>
		</c:if>
