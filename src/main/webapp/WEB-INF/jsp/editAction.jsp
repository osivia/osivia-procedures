<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<portlet:defineObjects />


<portlet:actionURL name="editAction" var="editActionUrl">
</portlet:actionURL>


<form:form modelAttribute="form" action="${editActionUrl}" method="post" cssClass="form-horizontal" role="form">


	<div class="panel panel-default">
	    <div class="panel-body">
	        <ul class="list-unstyled">
	            <li class="form-group">
	                <div class="col-sm-2">
	                    <label class="control-label">Label</label>
	                </div>
	                <div class="col-sm-8">
	                	
	                </div>
	            </li>
	            <li class="form-group">
	             	<div class="col-sm-2">
	                    <label class="control-label">Référence de l'étape cible</label>
	                </div>
	                <div class="col-sm-8">
	                
	                </div>
	            </li>
            </ul>
    	</div>
	</div>




</form:form>





