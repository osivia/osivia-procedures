<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<portlet:defineObjects />

<portlet:actionURL name="actionProcedure" var="actionProcedureUrl">
</portlet:actionURL>

<form:form modelAttribute="form" action="${actionProcedureUrl}" method="post" cssClass="form-horizontal" role="form" enctype="multipart/form-data">

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">${form.theCurrentStep.stepName}</h3>
        </div>
        <div class="panel-body">
            <c:if test="${not empty form.alertSuccess}">
                <div class="alert alert-success" role="alert">${form.alertSuccess}</div>
            </c:if>
        
            <ul class="procedure-sortable list-unstyled">
                <c:forEach var="field" items="${form.theCurrentStep.fields}" varStatus="status">
                    <c:set var="fieldType" value="${form.procedureModel.variables[field.name].type}" />
                    <li class="form-group">
                        <c:choose>
                            <c:when test="${field.input eq true}">
                                <c:choose>
                                    <c:when test="${fieldType eq 'TEXT'}">
                                        <div class="col-sm-3">
                                            <label for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
                                        </div>
                                        <div class="col-sm-9">
                                            <form:input path="procedureInstance.globalVariablesValues['${field.name}']" type="text" cssClass="form-control"/>
                                        </div>
                                    </c:when>
                                    <c:when test="${fieldType eq 'FILE'}">
                                        <div class="col-sm-3">
                                            <label for="${form.procedureInstance.globalVariablesValues[field.name]}">${form.procedureModel.variables[field.name].label}</label>
                                        </div>
                                        <div class="col-sm-3">
                                            <input type="file" name="file:${field.name}"/>
                                        </div>
                                        <div class="col-sm-6">
                                            <a href="${form.procedureInstance.filesPath[field.name].downloadLink}">${form.procedureInstance.filesPath[field.name].fileName}</a> 
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <p>empty</p>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${fieldType eq 'FILE'}">
                                        <div class="col-sm-3">
                                            <label for="${form.procedureInstance.globalVariablesValues[field.name]}">${form.procedureModel.variables[field.name].label}</label>
                                        </div>
                                        <div class="col-sm-3">
                                            <a href="${form.procedureInstance.filesPath[field.name].downloadLink}">${form.procedureInstance.filesPath[field.name].fileName}</a> 
                                        </div>
                                    </c:when>
                                    <c:when test="${fieldType eq 'TEXT'}">
                                        <div class="col-sm-3">
                                            ${form.procedureModel.variables[field.name].label} : 
                                        </div>
                                        <div class="col-sm-3">
                                            ${form.procedureInstance.globalVariablesValues[field.name]}
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <p>empty</p>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="panel-footer">
            <div class="form-group">
                <div class="col-sm-10">
                    <c:forEach var="action" items="${form.theCurrentStep.actions}" varStatus="status">
                        <button type="submit" name="proceedProcedure" class="btn btn-default" onclick="selector(this,'${action.stepReference}','stepReference')" >${action.label}</button>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

</form:form>