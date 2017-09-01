<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<portlet:defineObjects />

<p><a href="${linkProcedureUrl}" class="no-ajax-link"><op:translate key="LAUNCH_THE_PROCEDURE" /></a></p>


<c:if test="${not empty editProcedureUrl}">
	<p><a href="${editProcedureUrl}" class="no-ajax-link" ><op:translate key="EDIT_THE_PROCEDURE" /> </a></p>
</c:if>

