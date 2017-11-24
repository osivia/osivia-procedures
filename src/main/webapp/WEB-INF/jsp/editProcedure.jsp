<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>

<portlet:defineObjects />

<portlet:actionURL name="editProcedure" var="editProcedureUrl">
</portlet:actionURL>

<c:if test="${!form.advancedMode}">
	<portlet:resourceURL id="stepSearch" var="stepSearchUrl" ></portlet:resourceURL>
</c:if>

<div class="content-navbar">
    <!-- Breadcrumb -->
    <div class="content-navbar-breadcrumb">
        <div id="breadcrumb">
        	<div class="">
				<nav>
				    <ol class="breadcrumb hidden-xs">
			            <li><a><op:translate key="EDIT_PROCEDURE" /></a></li>
				    </ol>
				</nav>
			</div>
		</div>
    </div>
</div>


<form:form modelAttribute="form" action="${editProcedureUrl}" method="post" cssClass="form-horizontal" role="form">

    <div class="form-group">
        <div class="col-sm-12">
		    <div class="btn-group pull-right">
			    <c:if test="${!form.advancedMode}">
			        <button type="submit" class="btn btn-default" name="changeMode"><op:translate key="ADVANCED_MODE" /></button>
			        <button type="submit" class="btn btn-primary active" name="changeMode"><op:translate key="SIMPLE_MODE" /></button>
			    </c:if>
			    <c:if test="${form.advancedMode}">
		            <button type="submit" class="btn btn-primary active" name="changeMode"><op:translate key="ADVANCED_MODE" /></button>
		            <button type="submit" class="btn btn-default" name="changeMode"><op:translate key="SIMPLE_MODE" /></button>
			    </c:if>
		    </div>
	    </div>
    </div>

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title"><op:translate key="PROCEDURE_IDENTIFICATION" /></h3>
        </div>
        <div class="panel-body">
            <div class="form-group">
				<form:label path="procedureModel.name" cssClass="col-sm-3 control-label"><op:translate key="NAME" /></form:label>
				<div class="col-sm-9">
                    <input name="procedureModel.name" class="form-control" placeholder='<op:translate key="NAME" />' value="${form.procedureModel.name}" type="text">
			    </div>
			</div>
			<c:if test="${not empty form.procedureModel.currentWebId}">
				<div class="form-group">
	                <form:label path="procedureModel.currentWebId" cssClass="col-sm-3 control-label"><op:translate key="IDENTITY" /></form:label>
	                <div class="col-sm-9">
	                    <p class="form-control-static">${form.procedureModel.currentWebId}</p>
	                </div>
	            </div>
            </c:if>
		    <c:if test="${form.advancedMode}">
                <div class="form-group">
		            <form:label path="procedureModel.newWebId" cssClass="col-sm-3 control-label"><op:translate key="IDENTITY_SET" /></form:label>
	                <div class="col-sm-9">
	                	<div class="input-group">
		                	<span class="input-group-addon">${webIdPrefix}</span>
		                    <input name="procedureModel.newWebId" class="form-control" placeholder='<op:translate key="IDENTITY" />' value="${form.procedureModel.newWebId}" type="text">
	                	</div>
	                </div>
                </div>
            </c:if>
        </div>
    </div>
        
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title"><op:translate key="PROCEDURE_STEP_LIST" /></h3>
        </div>
        <div class="panel-body">
            <ul class="list-unstyled steps-sortable">
                <c:forEach var="step" items="${form.procedureModel.steps}" varStatus="status">
                    <li class="form-group">
                        <div class="col-sm-10">${step.stepName}</div>
                        <div class="btn-group col-sm-2">
                            <button type="submit" name="editStep" onclick="selector(this,'${status.index}','selectedStep')" class="btn btn-default pull-riht">
                                <i class="glyphicons glyphicons-edit"></i>
                            </button>
                        </div>
                        <form:hidden path="procedureModel.steps[${status.index}].index"/>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="panel-footer">
            <div class="form-group">
                <div class="col-sm-12">
                    <button type="submit" class="btn btn-default" name="addStep"><op:translate key="PROCEDURE_ADD_STEP" /></button>
                    <button type="submit" class="btn btn-default pull-right" name="manageVariables"><op:translate key="PROCEDURE_ACCESS_DICTIONARY" /></button>
                </div>
            </div>
        </div>
    </div>
    
    <c:if test="${form.advancedMode and 1==0}">
    
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title"><op:translate key="PROCEDURE_BUSINESS_OBJECTS" /></h3>
            </div>
            <div class="panel-body">
                <ul class="list-unstyled">
                    <li class="form-group">
                        <div class="col-sm-4">
                            <label class="control-label"><op:translate key="NAME" /></label>
                        </div>
                        <div class="col-sm-4">
                            <label class="control-label"><op:translate key="PATH" /></label>
                        </div>
                        <div class="col-sm-4">
                            <label class="control-label"><op:translate key="TYPE" /></label>
                        </div>
                    </li>
                    <c:forEach var="procedureObject" items="${form.procedureModel.procedureObjects}" varStatus="status">
                         <li class="form-group">
                            <div class="col-sm-4">
                                <input name="procedureModel.procedureObjects[${status.index}].name" class="form-control" placeholder='<op:translate key="NAME" />' value="${form.procedureModel.procedureObjects[status.index].name}" type="text">
                            </div>
                            <div class="col-sm-4">
                                <input name="procedureModel.procedureObjects[${status.index}].name" class="form-control" placeholder='<op:translate key="PATH" />' value="${form.procedureModel.procedureObjects[status.index].name}" type="text">
                            </div>
                            <div class="col-sm-3">
                                <form:select path="procedureModel.procedureObjects[${status.index}].type" cssClass="form-control">
                                    <form:option value="FILE"><op:translate key="FILE" /></form:option>
                                </form:select>
                            </div>
                            <div class="btn-group col-sm-1">
                                <button type="submit" name="deleteObject" class="btn btn-default" onclick="selector(this,'${status.index}','selectedObject')">
                                    <i class="glyphicons glyphicons-bin"></i>
                                </button>
                            </div>
                         </li>
                    </c:forEach>
                </ul>
            </div>
            <div class="panel-footer">
                <div class="form-group">
                    <div class="col-sm-11">
                        <button type="submit" name="addObject" class="btn btn-default"><op:translate key="PROCEDURE_ADD_BO" /></button>
                    </div>
                </div>
            </div>
        </div>
    
    </c:if>
    
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title"><op:translate key="PROCEDURE_STARTING_STEP" /></h3>
        </div>
        <div class="panel-body">
            <div class="form-group">
                <div class="col-sm-6">
                    <c:if test="${form.advancedMode}">
	                    <form:input path="procedureModel.startingStep" type="text" cssClass="form-control" />
                    </c:if>
                    <c:if test="${!form.advancedMode}">
                        <form:select path="procedureModel.startingStep" class="stepSelect-select2 form-control select2" data-url="${stepSearchUrl}" cssStyle="width: 100%;">
                           <option value="${form.theCurrentStep.reference}" selected="selected">${form.theCurrentStep.stepName}</option>
                        </form:select>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
    
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title"><op:translate key="PROCEDURE_DASHBOARDS" /></h3>
        </div>
        <div class="panel-body">
            <ul class="list-unstyled">
                <li class="form-group">
                    <div class="col-sm-5">
                        <label class="control-label"><op:translate key="NAME" /></label>
                    </div>
                    <div class="col-sm-5">
                        <label class="control-label"><op:translate key="GROUPS" /></label>
                    </div>
                </li>
                <c:forEach var="dashboard" items="${form.procedureModel.dashboards}" varStatus="status">
                     <li class="form-group">
	                     <div class="col-sm-5">${dashboard.name}</div>
	                     <div class="col-sm-5">${dashboard.groupsString}</div>
	                     <div class="btn-group col-sm-2">
	                         <button type="submit" name="editTdb" onclick="selector(this,'${status.index}','selectedTdb')" class="btn btn-default pull-riht">
	                             <i class="glyphicons glyphicons-edit"></i>
	                         </button>
	                     </div>
	                 </li>
                </c:forEach>
            </ul>
        </div>
        <div class="panel-footer">
            <div class="form-group">
                <div class="col-sm-11">
                    <button type="submit" class="btn btn-default" name="addTdb"><op:translate key="PROCEDURE_ADD_DASHBOARD" /></button>
                </div>
            </div>
        </div>
    </div>
    

    <button type="submit" class="btn btn-default" name="exit"><op:translate key="EXIT" /></button>
    <button type="submit" class="btn btn-primary" name="saveProcedure"><op:translate key="SAVE_PROCEDURE" /></button>
    <c:if test="${not empty form.procedureModel.currentWebId}">
	    <a href="javascript:;" class="btn btn-danger pull-right" data-fancybox="" data-src="#PROCEDURE_DELETE"><op:translate key="DELETE_PROCEDURE" /></a>
    </c:if>
</form:form>


<div class="hidden">
    <div id="PROCEDURE_DELETE">
        <form:form modelAttribute="form" action="${editProcedureUrl}" method="post" role="form" >
            <p><op:translate key="DELETE_PROCEDURE_WARNING" /></p>
            <button type="submit" class="btn btn-warning" name="deleteProcedure"><i class="halflings halflings-alert"></i>
                 <span class="hidden-xs"><op:translate key="DELETE_PROCEDURE" /></span>
            </button>
            <button class="btn btn-default" type="button" onclick="closeFancybox()"><op:translate key="CANCEL" /></button>
        </form:form>
    </div>
</div>
