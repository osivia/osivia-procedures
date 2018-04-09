<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>


<portlet:defineObjects />


<ul class="list-unstyled form-horizontal">
    <c:forEach var="field" items="${form.theCurrentStep.fields}" varStatus="status">
        <c:if test="${not status.first or field.name ne '_title'}">
        	<c:set var="field" value="${field}" scope="request" />
			<jsp:include page="displayField.jsp" />
        </c:if>
    </c:forEach>
</ul>
