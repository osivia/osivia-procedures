<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>

<portlet:defineObjects />

<c:if test="${not empty errorText}">
    <p class="alert alert-danger">
	    <i class="halflings halflings-exclamation-sign"></i>
	    <span>${errorText}</span>
	</p>
</c:if>
