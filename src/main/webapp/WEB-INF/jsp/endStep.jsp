<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<portlet:defineObjects />


<div class="alert alert-success">
    <i class="glyphicons glyphicons-ok-sign"></i>
    <span>
        <c:choose>
            <c:when test="${empty form.theSelectedStep.endStepMsg}"><op:translate key="MESSAGE_STEP_TERMINATED" /></c:when>
            <c:otherwise>${form.theSelectedStep.endStepMsg}</c:otherwise>
        </c:choose>
    </span>
</div>

<div>
    <a href="${closeUrl}" class="btn btn-default no-ajax-link">
        <span><op:translate key="FINISH" /></span>
    </a>
</div>
