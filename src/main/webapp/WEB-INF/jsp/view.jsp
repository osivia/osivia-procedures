<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<portlet:defineObjects />

<portlet:actionURL name="nextStepAction" var="nextStepActionURL" />


	<c:choose> 
        <c:when test="${procedure.currentUserIsActor}">
		    
		    <form:form modelAttribute="procedure" action="${nextStepActionURL}" method="post" enctype="multipart/form-data" role="form"
		               class="form-horizontal">
		        
			    <jsp:include page="${procedure.formId}.jsp" />
            
                <div class="col-sm-offset-2">
				    <button type="submit" class="btn btn-primary">
		                <span>Valider</span>
		            </button>
	            </div>
		    
		    </form:form>

		</c:when>
		<c:otherwise>
		  <fieldset>
		      <div>${procedure.message}</div>
		  </fieldset>
		</c:otherwise>
		
    </c:choose>
