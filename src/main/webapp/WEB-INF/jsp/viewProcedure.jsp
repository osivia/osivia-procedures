<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<portlet:defineObjects />

<portlet:actionURL name="actionProcedure" var="actionProcedureUrl" />


<c:set var="namespace"><portlet:namespace /></c:set>

<c:set var="inputMode" value="true" scope="request" />


<!-- Datepicker language -->
<c:set var="datepickerLanguage" value="${fn:toLowerCase(pageContext.response.locale.language)}" />
<c:if test="${'en' ne datepickerLanguage}">
    <script type="text/javascript" src="/osivia-portal-custom-web-assets/components/jquery-ui/i18n/datepicker-${datepickerLanguage}.js"></script>
</c:if>

<div class="procedureContainer">
	<form:form modelAttribute="form" action="${actionProcedureUrl}" method="post" enctype="multipart/form-data" cssClass="form-horizontal" role="form">
	    <ul class="list-unstyled">
	        <c:forEach var="field" items="${form.theCurrentStep.fields}" varStatus="status">
	            <c:set var="field" value="${field}" scope="request" />
	            <jsp:include page="editFields.jsp" />
	        </c:forEach>
	    </ul>
	
	    <div class="form-group">
	        <div class="col-sm-offset-3 col-sm-9 col-lg-offset-2 col-lg-10">
	            <c:forEach var="action" items="${form.theCurrentStep.actions}" varStatus="status">
	                <button type="button" onclick="$JQry('#${namespace}-action').val('${action.actionId}'); $JQry('#${namespace}-proceed-procedure').click();" class="btn btn-primary">${action.label}</button>
	            </c:forEach>
	            
	            <input id="${namespace}-action" type="hidden" name="actionId" value="${form.theCurrentStep.actions[0].actionId}">
	            
	            <input id="${namespace}-proceed-procedure" type="submit" name="proceedProcedure" class="hidden">
	        </div>
	    </div>
	
	    <input id="${namespace}-upload-file" type="submit" name="upload-file" class="hidden">
	    <input  type="submit" name="applyRules" class="hidden">   
	
	</form:form>
</div>
