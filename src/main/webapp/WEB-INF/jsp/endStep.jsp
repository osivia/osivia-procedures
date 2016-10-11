<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<portlet:defineObjects />

<c:if test="${not empty form.theSelectedStep.endStepMsg}">
	<h2>${form.theSelectedStep.endStepMsg}</h2>
</c:if>
<c:if test="${empty form.theSelectedStep.endStepMsg}">
	<h2>L'Étape est terminée</h2>
</c:if>

<div>
    <a href="${closeUrl}" class="btn btn-default">
        <span>Terminer</span>
    </a>
</div>
