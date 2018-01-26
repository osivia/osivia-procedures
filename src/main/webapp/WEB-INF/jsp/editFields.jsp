<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>

<c:choose>
    <c:when test="${field.fieldSet eq true}">
        <li class="form-group" id="${field.path}"><form:label path="${fieldNamePath}" cssClass="col-sm-3 col-lg-2 control-label">${field.superLabel}</form:label>
            <div class="col-sm-9 col-lg-10">
                <c:set var="fieldType" value="${form.procedureModel.variables[field.name].type}" />
                <c:choose>
                    <c:when test="${fieldType eq 'FIELDSET'}">
                        <div class="panel panel-${form.selectedField.path eq field.path ? 'info' : 'default'}">
                            <c:if test="${not empty field.superLabel}">
                                <div class="panel-heading">${field.superLabel}</div>
                            </c:if>
                            <div class="panel-body">
                                <p class="text-pre-wrap">${form.procedureModel.variables[field.name].varOptions}</p>
                                <ul class="list-unstyled ${editionMode ? 'procedure-sortable' : ''}">
                                    <c:set var="fieldBkp" value="${field}" scope="page" />
                                    <c:forEach var="nestedField" items="${fieldBkp.fields}" varStatus="status">
                                        <c:set var="field" value="${nestedField}" scope="request" />
                                        <jsp:include page="editFields.jsp" />
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                    </c:when>

                    <c:when test="${fieldType eq 'FIELDLIST'}">
                        <c:set var="fieldBkp" value="${field}" scope="page" />

                        <div class="panel panel-default no-margin-bottom list-field">
                            <c:if test="${not empty fieldBkp.helpText}">
                                <div class="panel-body">
                                    <div class="text-muted">${fieldBkp.helpText}</div>
                                </div>
                            </c:if>

                            <table class="table">
                                <thead>
                                    <tr>
                                        <c:forEach var="nestedField" items="${fieldBkp.fields}" varStatus="status">
                                            <c:set var="fieldVarOptions" value="${form.procedureModel.variables[nestedField.name].varOptions}" />
                                            <th data-varname="${nestedField.name}" data-varOptions='${fieldVarOptions}'>${nestedField.superLabel}</th>
                                        </c:forEach>
                                        <th width="1%"></th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>

                            <div class="panel-body">
                                <ul class="list-unstyled ${editionMode ? 'procedure-sortable' : ''}">
                                    <c:set var="fieldBkp" value="${field}" scope="page" />
                                    <c:forEach var="nestedField" items="${fieldBkp.fields}" varStatus="status">
                                        <c:set var="field" value="${nestedField}" scope="request" />
                                        <jsp:include page="editFields.jsp" />
                                    </c:forEach>
                                </ul>

                                <div class="row">
                                    <div class="col-sm-offset-3 col-sm-9 col-lg-offset-2 col-lg-10">
                                        <c:choose>
                                            <c:when test="${form.selectedListFieldPath eq fieldBkp.path}">
                                                <button type="submit" name="cancelEditFieldInList" class="btn btn-default">
                                                    <op:translate key="CANCEL" />
                                                </button>

                                                <button type="submit" name="validateEditFieldInList" class="btn btn-primary">
                                                    <op:translate key="VALIDATE" />
                                                </button>
                                            </c:when>

                                            <c:when test="${not editionMode}">
                                                <button type="submit" name="addFieldInList" class="btn btn-default" onclick="selector(this,'${fieldBkp.path}','selectedFieldPath')">
                                                    <op:translate key="ADD" />
                                                </button>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                            <form:hidden path="selectedListFieldRowIndex" />
                            <form:hidden path="procedureInstance.globalVariablesValues['${fieldBkp.name}']" />
                        </div>
                    </c:when>
                </c:choose>
            </div> <c:forEach var="pathPart" items="${fieldBkp.path}" varStatus="status">
                <c:choose>
                    <c:when test="${not empty form.procedureInstance and not empty form.procedureInstance.currentStep}">
                        <c:set var="springPathNested" value="${status.first ? 'theCurrentStep' : springPathNested}.fields[${pathPart}]" scope="request" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="springPathNested" value="${status.first ? 'theSelectedStep' : springPathNested}.fields[${pathPart}]" scope="request" />
                    </c:otherwise>
                </c:choose>
            </c:forEach> <form:hidden path="${springPathNested}.path" /></li>
    </c:when>
    <c:otherwise>
        <jsp:include page="editField.jsp" />
    </c:otherwise>
</c:choose>
