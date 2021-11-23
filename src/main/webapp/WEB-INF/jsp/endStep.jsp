<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>

<portlet:defineObjects />

<c:if test="${not empty form.theCurrentStep.endStepMsg}">
	<h2>${form.theCurrentStep.endStepMsg}</h2>
</c:if>
<c:if test="${empty form.theCurrentStep.endStepMsg}">
	<h2><op:translate key="ENDSTEP" /></h2>
</c:if>


