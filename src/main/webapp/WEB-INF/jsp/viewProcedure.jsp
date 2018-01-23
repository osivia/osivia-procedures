<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<portlet:defineObjects />

<!-- Datepicker language -->
<c:set var="datepickerLanguage" value="${fn:toLowerCase(pageContext.response.locale.language)}" />
<c:if test="${'en' ne datepickerLanguage}">
    <script type="text/javascript" src="/osivia-portal-custom-web-assets/components/jquery-ui/i18n/datepicker-${datepickerLanguage}.js"></script>
</c:if>

<portlet:actionURL name="actionProcedure" var="actionProcedureUrl">
</portlet:actionURL>

<form:form modelAttribute="form" action="${actionProcedureUrl}" method="post" cssClass="form-horizontal" role="form">
    <ul class="list-unstyled">
        <c:forEach var="field" items="${form.theCurrentStep.fields}" varStatus="status">
         <c:set var="field" value="${field}" scope="request"/>
         <jsp:include page="editFields.jsp"/>
        </c:forEach>
    </ul>
    
    <div class="form-group">
		<div class="col-sm-offset-3 col-sm-9 col-lg-offset-2 col-lg-10">
            <c:forEach var="action" items="${form.theCurrentStep.actions}" varStatus="status">
                <button type="submit" name="proceedProcedure" class="btn btn-primary" onclick="selector(this,'${action.actionId}','actionId');" >${action.label}</button>
            </c:forEach>
        </div>
    </div>
</form:form>