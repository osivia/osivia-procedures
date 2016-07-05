<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<portlet:defineObjects />


<portlet:actionURL name="editAction" var="editActionUrl">
</portlet:actionURL>


<form:form modelAttribute="form" action="${editActionUrl}" method="post" cssClass="form-horizontal" role="form">
	<div class="panel panel-default">
		<div class="panel-heading">
	        <ul class="list-unstyled">
	            <li class="form-group">
	                <div class="col-sm-2">
	                    <label class="control-label">Label</label>
	                </div>
	                <div class="col-sm-8">
	                	${form.theSelectedAction.label}
	                </div>
	            </li>
	            <li class="form-group">
	             	<div class="col-sm-2">
	                    <label class="control-label">Identifiant de l'action</label>
	                </div>
	                <div class="col-sm-8">
	                	${form.theSelectedAction.actionId}
	                </div>
	            </li>
	            <li class="form-group">
	             	<div class="col-sm-2">
	                    <label class="control-label">Référence de l'étape cible</label>
	                </div>
	                <div class="col-sm-8">
	                	${form.theSelectedAction.stepReference}
	                </div>
	            </li>
	    	</ul>
    	</div>
    	<div class="panel-body">
    		<c:forEach var="filter" items="${form.theSelectedAction.filtersList}" varStatus="status">
    			<ul class="list-unstyled">
	            	<li class="form-group">
	            		<div class="col-sm-2">
		                    <label class="control-label">Nom du filtre</label>
		                </div>
	            		<div class="col-sm-2">
                            <form:input path="theSelectedAction.filtersList[${status.index}].filterName" type="text" cssClass="form-control" placeholder="filterName" />
                        </div>
                        <div class="col-sm-2">
		                    <label class="control-label">Identifiant du filtre</label>
		                </div>
                        <div class="col-sm-2">
                            <form:input path="theSelectedAction.filtersList[${status.index}].filterId" type="text" cssClass="form-control" placeholder="filterId" />
                        </div>
                        <div class="col-sm-2">
		                    <label class="control-label">Path du filtre</label>
		                </div>
                        <div class="col-sm-2">
                            <form:input path="theSelectedAction.filtersList[${status.index}].filterPath" type="text" cssClass="form-control" placeholder="filterPath" />
                        </div>
                    </li>
           			<li class="form-group">
           				<div class="col-sm-3">
		                    <label class="control-label">Nom de l'argument 1</label>
		                </div>
           				<div class="col-sm-3">
                            <form:input path="theSelectedAction.filtersList[${status.index}].argumentsList[0].argumentName" type="text" cssClass="form-control" placeholder="argument1Name" />
                        </div>
                        <div class="col-sm-3">
		                    <label class="control-label">Valeur de l'argument 1</label>
		                </div>
                        <div class="col-sm-3">
                            <form:input path="theSelectedAction.filtersList[${status.index}].argumentsList[0].argumentValue" type="text" cssClass="form-control" placeholder="argument1Value" />
                        </div>
           			</li>
           			<li class="form-group">
           				<div class="col-sm-3">
		                    <label class="control-label">Nom de l'argument 2</label>
		                </div>
           				<div class="col-sm-3">
                            <form:input path="theSelectedAction.filtersList[${status.index}].argumentsList[1].argumentName" type="text" cssClass="form-control" placeholder="argument2Name" />
                        </div>
                        <div class="col-sm-3">
		                    <label class="control-label">Valeur de l'argument 1</label>
		                </div>
                        <div class="col-sm-3">
                            <form:input path="theSelectedAction.filtersList[${status.index}].argumentsList[1].argumentValue" type="text" cssClass="form-control" placeholder="argument2Value" />
                        </div>
           			</li>
            	</ul>
    		</c:forEach>
    	</div>
    	<div class="panel-footer">
	        <div class="form-group">
	            <div class="col-sm-11">
	                <button type="submit" class="btn btn-primary" name="saveAction">Sauvegarder</button>
	                <button type="submit" class="btn btn-default" name="addFilter">Ajouter un filtre</button>
	            </div>
	        </div>
	    </div>
	</div>
</form:form>
