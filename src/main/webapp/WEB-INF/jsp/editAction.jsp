<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal"	prefix="op"%>

<portlet:defineObjects />

<portlet:actionURL name="editAction" var="editActionUrl">
</portlet:actionURL>

<div class="content-navbar">
    <!-- Breadcrumb -->
    <div class="content-navbar-breadcrumb">
        <div id="breadcrumb">
        	<div class="">
				<nav>
				    <ol class="breadcrumb hidden-xs">
			            <li>
				            <portlet:actionURL name="editStep" var="cancelActionToProcUrl">
				            	<portlet:param name="cancelStep" value="toProc"/>
							</portlet:actionURL>
							<a href="${cancelActionToProcUrl}">Édition d'une procédure</a>
			            </li>
			            <li>
				            <portlet:actionURL name="editAction" var="cancelActionToStepUrl">
				            	<portlet:param name="cancelAction" value="toStep"/>
							</portlet:actionURL>
							<a href="${cancelActionToStepUrl}">Édition d'une étape</a>
			            </li>
			            <li><a>Édition d'une action</a></li>
				    </ol>
				</nav>
			</div>
		</div>
    </div>
</div>

<form:form modelAttribute="form" action="${editActionUrl}" method="post" cssClass="form-horizontal" role="form">
    <div class="form-group">
        <div class="col-sm-2">
            <label class="control-label">Label</label>
        </div>
        <div class="col-sm-8">${form.theSelectedAction.label}</div>
    </div>
    <div class="form-group">
        <div class="col-sm-2">
            <label class="control-label">Identifiant de l'action</label>
        </div>
        <div class="col-sm-8">${form.theSelectedAction.actionId}</div>
    </div>
    <div class="form-group">
	    <div class="col-sm-2">
	        <label class="control-label">Référence de l'étape cible</label>
	    </div>
	    <div class="col-sm-8">${form.theSelectedAction.stepReference}</div>
    </div>
    <hr>

    <div class="row">
        <div class="col-sm-4">
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="<c:if test="${empty activeTab or 'edit' ne activeTab}">active</c:if>"><a href="#Create" role="tab" data-toggle="tab" class="no-ajax-link">Ajouter un filtre</a></li>
                <c:if test="${not empty form.selectedFilter}">
	                <li role="presentation" class="<c:if test="${'edit' eq activeTab}">active</c:if>"><a href="#Edit" role="tab" data-toggle="tab" class="no-ajax-link">Actions</a></li>
                </c:if>
            </ul>
            <div class="tab-content">
                <div role="tabpanel" class="tab-pane <c:if test="${empty activeTab or 'edit' ne activeTab}">active</c:if>" id="Create">
                    <h4>Filtres disponibles : </h4>
                    <c:forEach var="filtre" items="${listeFiltres}">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <h3 class="panel-title"><op:translate key="${filtre.labelKey}" classLoader="${filtre.class.classLoader}"/></h3>
                            </div>
                            <div class="panel-body">
                                <c:if test="${not empty filtre.descriptionKey}">
	                                <op:translate key="${filtre.descriptionKey}" classLoader="${filtre.class.classLoader}"/>
                                </c:if>
                                <button type="submit" name="addFilter" class="btn btn-default pull-right" onclick="selector(this,'${filtre.id}','selectedFilterId');" >
                                    <i class="glyphicons glyphicons-plus"></i>
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <c:if test="${not empty form.selectedFilter}">
                    <div role="tabpanel" class="tab-pane <c:if test="${'edit' eq activeTab}">active</c:if>" id="Edit">
                        <div class="col-sm-12">
	                        <div class="form-group">
				                <form:label path="selectedFilter.filterName">Nom du filtre</form:label>
			                    <form:input path="selectedFilter.filterName" type="text" cssClass="form-control" placeholder="Nom du filtre" />
		                    </div>
                        </div>
                        <hr>
                        <c:if test="${not empty form.selectedFilter.argumentsList}">
                            <h3>Arguments:</h3>
                        </c:if>
                        <c:forEach var="argument" items="${form.selectedFilter.argumentsList}" varStatus="argStatus">
				            <div class="form-group">
				                <div class="col-sm-4">
				                    <label class="control-label">${argument.argumentName}</label>
				                </div>
				                <div class="col-sm-8">
				                    <c:if test="${argument.type eq 'TEXT'}">
				                        <form:input path="selectedFilter.argumentsList[${argStatus.index}].argumentValue" type="text" cssClass="form-control" placeholder="argumentValue" />
				                    </c:if>
				                    <c:if test="${argument.type eq 'TEXTAREA'}">
				                        <form:textarea path="selectedFilter.argumentsList[${argStatus.index}].argumentValue" cssClass="form-control" />
				                    </c:if>
				                    <c:if test="${argument.type eq 'BOOLEAN'}">
				                        <form:checkbox path="selectedFilter.argumentsList[${argStatus.index}].argumentValue" cssClass="form-control" value="true"/>
				                    </c:if>
				                </div>
				            </div>
				        </c:forEach>
				        <div class="row">
					        <div class="col-sm-1 pull-right">
			                   <button type="submit" name="deleteFilter" class="btn btn-default pull-right">
						            <i class="glyphicons glyphicons-bin"></i>
						        </button>
					        </div>
					        <div class="col-sm-1 pull-right">
		                       <button type="submit" name="editFilter" class="btn btn-default pull-right">Modifier</button>
		                   </div>
	                   </div>
	                </div>
                </c:if>
            </div>
        </div>
		<div id="filter-sortable" class="col-sm-8">
		  <h4>Filtres installés : </h4>
	        <ul class="filter-sortable">
	            <c:forEach var="filter" items="${form.theSelectedAction.filters}" varStatus="status">
	                <li>
	                   <i class="halflings halflings-menu-right"></i>
	                    <c:set var="nestedFilter" value="${filter}" scope="request"/>
	                    <jsp:include page="editFilters.jsp"/>
	                </li>
	            </c:forEach>
	        </ul>
		</div>
	</div>
    <hr>
	<div class="row">
	    <div class="col-sm-1">
            <button type="submit" class="btn btn-default" name="cancelAction">Annuler</button>
        </div>
		<div class="col-sm-1 pull-right">
			<button type="submit" class="btn btn-primary pull-right" name="saveAction">Sauvegarder</button>
		</div>
		<input type="submit" class="hidden" name="updateForm">
		<input type="submit" class="hidden" name="selectFilter">
	</div>

</form:form>
