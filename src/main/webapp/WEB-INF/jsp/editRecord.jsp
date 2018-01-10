<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>

<c:set var="editionMode" value="true" scope="request" />

<portlet:defineObjects />

<portlet:actionURL name="editRecord" var="editRecordUrl">
</portlet:actionURL>

<portlet:resourceURL id="fieldSearch" var="fieldSearchUrl"></portlet:resourceURL>



<form:form modelAttribute="form" action="${editRecordUrl}" method="post" cssClass="form-horizontal" role="form">

    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="<c:if test="${empty activeTab or ('form' ne activeTab && 'dashboard' ne activeTab)}">active</c:if>"><a
            href="#General" role="tab" data-toggle="tab" data-id="General" class="no-ajax-link"><op:translate key="GENERAL" /></a></li>
        <li role="presentation" class="<c:if test="${'form' eq activeTab}">active</c:if>"><a href="#Formulaire" role="tab" data-toggle="tab"
            data-id="Formulaire" class="no-ajax-link"><op:translate key="FORM" /></a></li>
        <li role="presentation" class="<c:if test="${'dashboard' eq activeTab}">active</c:if>"><a href="#Dashboard" role="tab" data-toggle="tab"
            data-id="Dashboard" class="no-ajax-link"><op:translate key="PROCEDURE_DASHBOARD" /></a></li>
    </ul>

    <div class="tab-content">
        <div role="tabpanel" class="tab-pane <c:if test="${empty activeTab or ('form' ne activeTab && 'dashboard' ne activeTab)}">active</c:if>" id="General">
            <div class="form-group">
                <form:label path="procedureModel.name" cssClass="col-sm-2 control-label">
                    <op:translate key="NAME" />
                </form:label>
                <div class="col-sm-10">
                    <input name="procedureModel.name" class="form-control" placeholder='<op:translate key="NAME" />' value="${form.procedureModel.name}"
                        type="text">
                </div>
            </div>
            <div class="form-group">
                <label class="col-sm-2 control-label"><op:translate key="RECORD_PARENT_MODEL" /></label>
                <div class="col-sm-10">
                    <p class="form-control-static">${form.procedureModel.webIdParent}</p>
                </div>
            </div>
        </div>

        <div role="tabpanel" class="tab-pane <c:if test="${'form' eq activeTab}">active</c:if>" id="Formulaire">
            <div class="row">
                <div class="col-sm-4">
                    <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation" class="<c:if test="${empty activeFormTab or 'edit' ne activeFormTab}">active</c:if>"><a href="#CreateField"
                            role="tab" data-toggle="tab" class="no-ajax-link"><op:translate key="ADD_FIELD" /></a></li>
                        <c:if test="${not empty form.selectedField}">
                            <li role="presentation" class="<c:if test="${'edit' eq activeFormTab}">active</c:if>"><a href="#Edit" role="tab"
                                data-toggle="tab" class="no-ajax-link"><op:translate key="EDIT" /></a></li>
                        </c:if>
                    </ul>
                    <div class="tab-content">
                        <div role="tabpanel" class="tab-pane <c:if test="${empty activeFormTab or 'edit' ne activeFormTab}">active</c:if>" id="CreateField">
                            <div class="form-group">
                                <form:label path="newField.label" cssClass="col-sm-3 control-label">
                                    <op:translate key="LABEL" />
                                </form:label>
                                <div class="col-sm-9">
                                    <input name="newField.label" class="form-control" placeholder='<op:translate key="LABEL" />' type="text"> <span
                                        class="help-block"><op:translate key="LABEL_HELP" /></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <form:label path="newField.helpText" cssClass="col-sm-3 control-label">
                                    <op:translate key="HELP_MSG" />
                                </form:label>
                                <div class="col-sm-9">
                                    <input name="newField.helpText" class="form-control" placeholder='<op:translate key="HELP_MSG" />'
                                        value="${form.newField.helpText}" type="text"> <span class="help-block"><op:translate key="HELP_MSG_HELP" /></span>
                                </div>
                            </div>
                            <div class="form-group">
                                <form:label path="newField.type" cssClass="col-sm-3 control-label">
                                    <op:translate key="TYPE" />
                                </form:label>
                                <div class="col-sm-9">
                                    <form:select path="newField.type" cssClass="form-control">
                                        <c:forEach items="${form.variableTypesAltEnum}" var="variableType">
                                            <form:option value="${variableType}">
                                                <op:translate key="${variableType}" />
                                            </form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </div>
                            <div class="form-group hidden">
                                <form:label path="newField.varOptions" cssClass="col-sm-3 control-label">
                                    <op:translate key="OPTIONS" />
                                </form:label>
                                <div class="col-sm-9">
                                    <input name="newField.varOptions" class="form-control" placeholder='<op:translate key="OPTIONS" />'
                                        value="${form.newField.varOptions}" type="text">
                                </div>
                            </div>
                            <div class="form-group hidden" id="formulaire-newField-list-editor">
                                <label class="col-sm-3 control-label"><op:translate key="EDIT_OPTIONS" /></label>
                                <div class="col-sm-9">
                                    <div class="form-group">
                                        <label for="formulaire-newField-list-editor-newOption-label" class="col-sm-3 control-label"><op:translate
                                                key="LABEL" /></label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" id="formulaire-newField-list-editor-newOption-label"
                                                placeholder='<op:translate key="LABEL" />'>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="formulaire-newField-list-editor-newOption-value" class="col-sm-3 control-label"><op:translate
                                                key="VALUE" /></label>
                                        <div class="col-sm-9">
                                            <input type="text" class="form-control" id="formulaire-newField-list-editor-newOption-value"
                                                placeholder='<op:translate key="VALUE" />'>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="col-sm-12">
                                            <button id="formulaire-newField-list-editor-addOption" class="btn btn-default pull-right" type="button">
                                                <op:translate key="ADD_OPTION" />
                                            </button>
                                        </div>
                                    </div>
                                    <div id="formulaire-newField-list-editor-optionList" class="form-group">
                                        <table class="table table-condensed">
                                            <thead>
                                                <tr>
                                                    <th><op:translate key="LABEL" /></th>
                                                    <th><op:translate key="VALUE" /></th>
                                                    <th></th>
                                                </tr>
                                            </thead>
                                            <tbody>

                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            
                            <div id="formulaire-newField-vocabulary" class="form-group required hidden">
                                <label class="col-sm-3 control-label"><op:translate key="VOCABULARY_ID" /></label>
                                <div class="col-sm-9">
                                    <input type="text" name="vocabularyId" class="form-control" />
                                </div>
                            </div>
                            
                            <button type="submit" name="addField" class="btn btn-default pull-right">
                                <op:translate key="ADD" />
                            </button>
                        </div>
                        <c:if test="${not empty form.selectedField}">
                            <div role="tabpanel" class="tab-pane <c:if test="${'edit' eq activeFormTab}">active</c:if>" id="Edit">
                                <div class="form-group">
                                    <form:label path="selectedField.superLabel" cssClass="col-sm-3 control-label">
                                        <op:translate key="LABEL" />
                                    </form:label>
                                    <div class="col-sm-9">
                                        <input name="selectedField.superLabel" class="form-control" placeholder='<op:translate key="LABEL" />'
                                            value="${form.selectedField.superLabel}" type="text"> <span class="help-block"><op:translate
                                                key="LABEL_HELP" /></span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <form:label path="selectedField.helpText" cssClass="col-sm-3 control-label">
                                        <op:translate key="HELP_MSG" />
                                    </form:label>
                                    <div class="col-sm-9">
                                        <input name="selectedField.helpText" class="form-control" placeholder='<op:translate key="HELP_MSG" />'
                                            value="${form.selectedField.helpText}" type="text"> <span class="help-block"><op:translate
                                                key="HELP_MSG_HELP" /></span>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <form:label path="selectedField.type" cssClass="col-sm-3 control-label">
                                        <op:translate key="TYPE" />
                                    </form:label>
                                    <div class="col-sm-9">
                                        <p class="form-control-static">
                                            <op:translate key="${form.selectedField.type}" />
                                        </p>
                                        <input name="selectedField.type" value="${form.selectedField.type}" hidden="true">
                                    </div>
                                </div>
                                <div class="form-group hidden">
                                    <form:label path="selectedField.varOptions" cssClass="col-sm-3 control-label">
                                        <op:translate key="OPTIONS" />
                                    </form:label>
                                    <div class="col-sm-9">
                                        <input name="selectedField.varOptions" class="form-control" value='${form.selectedField.varOptions}' type="text">
                                    </div>
                                </div>
                                <div class="form-group hidden" id="formulaire-selectedField-list-editor">
                                    <label class="col-sm-3 control-label"><op:translate key="EDIT_OPTIONS" /></label>
                                    <div class="col-sm-9">
                                        <div class="form-group">
                                            <label for="formulaire-selectedField-list-editor-newOption-label" class="col-sm-3 control-label"><op:translate
                                                    key="LABEL" /></label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control" id="formulaire-selectedField-list-editor-newOption-label"
                                                    placeholder='<op:translate key="LABEL" />'>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label for="formulaire-selectedField-list-editor-newOption-value" class="col-sm-3 control-label"><op:translate
                                                    key="VALUE" /></label>
                                            <div class="col-sm-9">
                                                <input type="text" class="form-control" id="formulaire-selectedField-list-editor-newOption-value"
                                                    placeholder='<op:translate key="VALUE" />'>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <div class="col-sm-12">
                                                <button id="formulaire-selectedField-list-editor-addOption" class="btn btn-default pull-right" type="button">
                                                    <op:translate key="ADD_OPTION" />
                                                </button>
                                            </div>
                                        </div>
                                        <div id="formulaire-selectedField-list-editor-optionList" class="form-group">
                                            <table class="table table-condensed">
                                                <thead>
                                                    <tr>
                                                        <th><op:translate key="LABEL" /></th>
                                                        <th><op:translate key="VALUE" /></th>
                                                        <th></th>
                                                    </tr>
                                                </thead>
                                                <tbody>

                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                                
                                <div id="formulaire-selectedField-vocabulary" class="form-group required hidden">
                                    <label class="col-sm-3 control-label"><op:translate key="VOCABULARY_ID" /></label>
                                    <div class="col-sm-9">
                                        <input type="text" name="vocabularyId" class="form-control" />
                                    </div>
                                </div>
                                
                                <div class="pull-right">
                                    <button type="submit" name="editField" class="btn btn-default">
                                        <op:translate key="MODIFY" />
                                    </button>
                                    <c:if test="${form.selectedField.deletable}">
                                        <button type="submit" name="deleteField" class="btn btn-default">
                                            <i class="glyphicons glyphicons-bin"></i>
                                        </button>
                                    </c:if>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>

                <div role="tabpanel" class="tab-pane <c:if test="${'form' eq activeTab}">active</c:if>" id="Edition">
                    <div id="procedure-sortable" class="col-sm-8">
                        <ul class="procedure-sortable list-unstyled">
                            <c:forEach var="field" items="${form.theSelectedStep.fields}" varStatus="status">
                                <c:choose>
                                    <c:when test="${field.fieldSet eq true}">
                                        <c:set var="field" value="${field}" scope="request" />
                                        <jsp:include page="editFields.jsp" />
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="field" value="${field}" scope="request" />
                                        <jsp:include page="editField.jsp" />
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            <form:input path="selectedStep" type="hidden" name="selectedStep" />
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <div role="tabpanel" class="tab-pane <c:if test="${'dashboard' eq activeTab}">active</c:if>" id="Dashboard">
            <div class="table">
                <div class="table-header table-row">
                    <div class="row">
                        <div class="col-sm-4 col-md-5 col-lg-6">
                            <div class="text-overflow">
                                <span><op:translate key="LABEL" /></span>
                            </div>
                        </div>
                        <div class="col-sm-5 col-md-5 col-lg-4">
                            <div class="text-overflow">
                                <span><op:translate key="NAME" /></span>
                            </div>
                        </div>
                        <div class="col-sm-2 col-md-1 col-lg-1">
                            <div class="text-overflow">
                                <span><op:translate key="SORTABLE" /></span>
                            </div>
                        </div>
                        <div class="col-sm-1 col-md-1 col-lg-1"></div>
                    </div>
                </div>
                <div class="table-body column-sortable">
                    <c:forEach var="column" items="${form.theSelectedTdb.columns}" varStatus="status">
                        <div class="table-row procedure-column">
                            <div class="row">
                                <div class="col-sm-4 col-md-5 col-lg-6">
                                    <div class="text-overflow">
                                        <span>${column.label}</span>
                                    </div>
                                </div>
                                <div class="col-sm-5 col-md-5 col-lg-4">
                                    <div class="text-overflow">
                                        <span>${column.variableName}</span>
                                    </div>
                                </div>
                                <div class="col-sm-2 col-md-1 col-lg-1 text-center">
                                    <c:if test="${column.sortable}">
                                        <i class="halflings halflings-ok"></i>
                                    </c:if>
                                    <c:if test="${not column.sortable}">
                                        <i class="halflings halflings-remove"></i>
                                    </c:if>
                                </div>
                                <div class="col-sm-1 col-md-1 col-lg-1">
                                    <c:if test="${field.deletable}">
                                        <button type="submit" name="deleteCol" class="btn btn-default pull-riht"
                                            onclick="selector(this,'${status.index}','selectedCol')">
                                            <i class="glyphicons glyphicons-bin"></i>
                                        </button>
                                    </c:if>
                                    <input type="hidden" name="theSelectedTdb.columns[${status.index}].index" value="${status.index}">
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <div class="table-footer">
                    <div class="table-row">
                        <div class="row">
                            <div class="col-sm-4 col-md-5 col-lg-6">
                                <form:input path="newColumn.label" type="text" cssClass="form-control" />
                            </div>
                            <div class="col-sm-5 col-md-5 col-lg-4">
                                <form:select path="newColumn.variableName" class="fieldSelect-select2 form-control select2" cssStyle="width: 100%;"
                                    data-url="${fieldSearchUrl}" data-defaultvars="true" data-autofill="true">
                                </form:select>
                            </div>
                            <div class="col-sm-2 col-md-1 col-lg-1 text-center">
                                <form:checkbox path="newColumn.sortable" />
                            </div>
                            <div class="col-sm-1 col-md-1 col-lg-1">
                                <button type="submit" class="btn btn-default" name="addColumn">
                                    <op:translate key="ADD" />
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <button type="submit" class="btn btn-primary" name="saveRecord">
        <op:translate key="SAVE" />
    </button>
    <button type="submit" class="btn btn-danger pull-right" name="deleteRecord">
        <op:translate key="DELETE" />
    </button>
    <input type="submit" class="hidden" name="updateForm">
    <input type="submit" class="hidden" name="updateDashboard">
    <input type="submit" class="hidden" name="selectField">

</form:form>