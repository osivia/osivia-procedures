<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<portlet:defineObjects />

<portlet:actionURL name="nextStepAction" var="nextStepActionURL" />

<div class="well">

	    <c:choose> 
        <c:when test="${procedure.currentUserIsActor}">
			<div class="form-group">
				<label class="control-label">Procedure: </label>
				<span class="form-control-static">${procedure.name}</span>
			</div>
			<div class="form-group">
				<label class="control-label">Current Step: </label>
		        <span class="form-control-static">${procedure.currentStep}</span>
		    </div>
		    
		    <form:form modelAttribute="procedure" action="${nextStepActionURL}" method="post" enctype="multipart/form-data" role="form">
		        
			    <jsp:include page="${procedure.formId}.jsp" />
            
			    <button type="submit" class="btn btn-primary">
	                <span>Valider</span>
	            </button>
		    
		    </form:form>

		</c:when>
		<c:otherwise>
		  <h3>The End for You!</h3>
		</c:otherwise>
		
    </c:choose>

</div>