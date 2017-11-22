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
							<a href="${cancelActionToProcUrl}"><op:translate key="EDIT_PROCEDURE" /></a>
			            </li>
			            <li>
				            <portlet:actionURL name="editAction" var="cancelActionToStepUrl">
				            	<portlet:param name="cancelAction" value="toStep"/>
							</portlet:actionURL>
							<a href="${cancelActionToStepUrl}"><op:translate key="EDIT_STEP" /></a>
			            </li>
			            <li><a><op:translate key="EDIT_ACTION" /></a></li>
				    </ol>
				</nav>
			</div>
		</div>
    </div>
</div>

<form:form modelAttribute="form" action="${editActionUrl}" method="post" cssClass="form-horizontal" role="form">
	<c:if test="${form.selectedAction < 0}">
		<h3><op:translate key="ACTION_INIT_STEP" /></h3>
	</c:if>
	<c:if test="${form.selectedAction >= 0}">
	    <div class="form-group">
	        <div class="col-sm-2">
	            <label class="control-label"><op:translate key="LABEL" /></label>
	        </div>
	        <div class="col-sm-8"><p class="form-control-static">${form.theSelectedAction.label}</p></div>
	    </div>
	    <div class="form-group">
	        <div class="col-sm-2">
	            <label class="control-label"><op:translate key="ACTION_ID" /></label>
	        </div>
	        <div class="col-sm-8"><p class="form-control-static">${form.theSelectedAction.actionId}</p></div>
	    </div>
	    <div class="form-group">
		    <div class="col-sm-2">
		        <label class="control-label"><op:translate key="ACTION_TARGET_REF" /></label>
		    </div>
		    <div class="col-sm-8"><p class="form-control-static">${form.theSelectedAction.stepReference}</p></div>
	    </div>
    </c:if>
    <hr>

    <div class="row">
        <div class="col-sm-4">
            <ul class="nav nav-tabs" role="tablist">
                <li role="presentation" class="<c:if test="${empty activeTab or 'edit' ne activeTab}">active</c:if>"><a href="#Create" role="tab" data-toggle="tab" class="no-ajax-link"><op:translate key="ACTION_ADD_FILTER" /></a></li>
                <c:if test="${not empty form.selectedFilter}">
	                <li role="presentation" class="<c:if test="${'edit' eq activeTab}">active</c:if>"><a href="#Edit" role="tab" data-toggle="tab" class="no-ajax-link"><op:translate key="ACTION_EDIT_FILTER" /></a></li>
                </c:if>
            </ul>
            <div class="tab-content">
                <div role="tabpanel" class="tab-pane <c:if test="${empty activeTab or 'edit' ne activeTab}">active</c:if>" id="Create">
					
					<div class="form-group">
						<div class="col-sm-12">
		                    <input onkeyup="updateFilters(this)" class="form-control" placeholder='<op:translate key="ACTION_FILTER_SEARCH" />' title='<op:translate key="ACTION_FILTER_SEARCH" />' type="text">
						</div>
					</div>
                    
                   	<div class="filterSelect-results">
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
                </div>
                <c:if test="${not empty form.selectedFilter}">
                    <div role="tabpanel" class="tab-pane <c:if test="${'edit' eq activeTab}">active</c:if>" id="Edit">
                    
	                    <div class="panel panel-default">
	                         <div class="panel-heading">
	                             <div class="form-group">
	                             	<div class="col-sm-8">
	                             		<h3 class="panel-title"><op:translate key="${form.selectedFilter.labelKey}" classLoader="${form.selectedFilter.classLoader}"/></h3>
                    				</div>
                    				<div class="col-sm-4">
			                             <div class="pull-right">
					                    	<button class="btn btn-default" type="button" data-toggle="collapse" data-target="#collapseHelp" aria-expanded="false">
					                    		<span data-toggle="tooltip" title="Aide"><op:translate key="ACTION_FILTER_HELP_LABEL" /></span>
											</button>
					                    	<button class="btn btn-default" type="button" data-toggle="collapse" data-target="#collapseDico" aria-expanded="false">
					                    		<op:translate key="ACTION_FILTER_DICTIONARY_LABEL" />
					                   		</button>
					                   	</div>
				                   	</div>
			                   	</div>
	                         </div>
	                         <div class="panel-body">
		                        <div class="col-sm-12">
			                        <div class="form-group">
						                <form:label path="selectedFilter.filterName"><op:translate key="ACTION_FILTER_NAME" /></form:label>
					                    <input name="selectedFilter.filterName" class="form-control" placeholder='<op:translate key="ACTION_FILTER_NAME" />' value="${form.selectedFilter.filterName}" type="text">
				                    </div>
		                        </div>
	                             <c:if test="${not empty form.selectedFilter.descriptionKey}">
	                              <op:translate key="${form.selectedFilter.descriptionKey}" classLoader="${form.selectedFilter.classLoader}"/>
	                             </c:if>
		                        <c:if test="${not empty form.selectedFilter.argumentsList}">
		                            <h3><op:translate key="ACTION_FILTER_ARGS" /></h3>
		                        </c:if>
		                        <c:forEach var="argument" items="${form.selectedFilter.argumentsList}" varStatus="argStatus">
						            <div class="form-group">
						                <div class="col-sm-4">
						                    <label class="control-label">${argument.argumentName}</label>
						                </div>
						                <div class="col-sm-8">
						                    <c:if test="${argument.type eq 'TEXT'}">
						                        <form:input path="selectedFilter.argumentsList[${argStatus.index}].argumentValue" type="text" cssClass="form-control filter-argument" placeholder="argumentValue" />
						                    </c:if>
						                    <c:if test="${argument.type eq 'TEXTAREA'}">
						                        <form:textarea path="selectedFilter.argumentsList[${argStatus.index}].argumentValue" cssClass="form-control filter-argument" />
						                    </c:if>
						                    <c:if test="${argument.type eq 'BOOLEAN'}">
						                        <form:checkbox path="selectedFilter.argumentsList[${argStatus.index}].argumentValue" cssClass="form-control" value="true"/>
						                    </c:if>
						                </div>
						            </div>
						        </c:forEach>
	                         </div>
			                 <div class="panel-footer">
						     	<div class="form-group">
                    				<div class="col-sm-12">
			                             <div class="pull-right">
					                        <button type="submit" name="editFilter" class="btn btn-default"><op:translate key="MODIFY" /></button>
						                    <button type="submit" name="deleteFilter" class="btn btn-default">
									            <i class="glyphicons glyphicons-bin"></i>
									        </button>
						            	</div>
					            	</div>
			                	</div>
							</div>
	                     </div>
	                </div>
                </c:if>
            </div>
        </div>
		<div id="filter-sortable" class="col-sm-8">
			<div class="panel panel-info collapse" id="collapseHelp">
				<div class="panel-heading"><op:translate key="ACTION_FILTER_HELP_HEADING" /></div>
				<div class="panel-body">
					<op:translate key="ACTION_FILTER_HELP" />
            	</div>
            </div>
            
            <div class="panel panel-info collapse" id="collapseDico">
            	<div class="panel-heading"><op:translate key="ACTION_FILTER_DICTIONARY_LABEL" /></div>
            	<div class="panel-body">
            		<op:translate key="ACTION_FILTER_DICTIONARY" />
            		<div class="col-sm-12">
	            		<div class="row">
	           				<div class="col-sm-4"><strong><op:translate key="NAME" /></strong></div>
	           				<div class="col-sm-4"><strong><op:translate key="LABEL" /></strong></div>
	           				<div class="col-sm-4"><strong><op:translate key="TYPE" /></strong></div>
	           			</div>
            		</div>
           			<div class="procedure-variables col-sm-12">
	          			<c:forEach var="variable" items="${form.editedVariables}" varStatus="varStatus">
	          			  <c:if test="${variable.type ne 'FIELDSET'}">
		           			<div class="row" onclick="insertVarValueAtCaret(this);">
		           				<div class="col-sm-4">${variable.name}</div>
		           				<div class="col-sm-4">${variable.label}</div>
		           				<div class="col-sm-4">${variable.type}</div>
		           			</div>
	          			  </c:if>
	          			</c:forEach>
           			</div>
               	</div>
            </div>
		
		  <h4><op:translate key="ACTION_FILTER_INSTALLED" /></h4>
	        <ul class="filter-sortable">
	            <c:forEach var="filter" items="${form.theSelectedAction.filters}" varStatus="status">
	                <li>
	                    <c:set var="nestedFilter" value="${filter}" scope="request"/>
	                    <jsp:include page="editFilters.jsp"/>
	                </li>
	            </c:forEach>
	        </ul>
		</div>
	</div>
    <hr>
    <button type="submit" class="btn btn-default" name="cancelAction"><op:translate key="CANCEL" /></button>
	<button type="submit" class="btn btn-primary" name="saveAction"><op:translate key="SAVE_ACTION" /></button>
	<input type="submit" class="hidden" name="updateForm">
	<input type="submit" class="hidden" name="selectFilter">

</form:form>
