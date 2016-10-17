<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<portlet:renderURL var="viewProcedureUrl">
    <portlet:param name="action" value="viewProcedure"/>
</portlet:renderURL>


<div class="panel panel-default">
    <div class="panel-heading">
        <h3 class="panel-title">D�tail d'un �l�ment de liste</h3>
    </div>
    <div class="panel-body">
        <ul class="list-unstyled">
            <c:forEach var="field" items="${form.theCurrentStep.fields}" varStatus="status">
	            <li class="form-group">
	                <div class="col-sm-9">
				        <div class="row">
				            <c:set var="fieldType" value="${form.procedureModel.variables[field.name].type}" />
				            <c:choose>
		                        <c:when
		                            test="${(fieldType eq 'TEXT') or (fieldType eq 'TEXTAREA') or (fieldType eq 'DATE') or (fieldType eq 'SELECTLIST') or (fieldType eq 'RADIOLIST')
		                                        or (fieldType eq 'RADIOVOCAB') or (fieldType eq 'CHECKBOXLIST') or (fieldType eq 'CHECKBOXVOCAB') or (fieldType eq 'NUMBER')}">
		                            <div class="col-sm-3">
		                                ${form.procedureModel.variables[field.name].label} :</div>
		                            <div class="col-sm-9">
		                                ${form.procedureInstance.globalVariablesValues[field.name]}
		                                <c:if test="${not empty field.helpText}">
		                                    <span class="help-block">${field.helpText}</span>
		                                </c:if>
		                            </div>
		                        </c:when>
		                        <c:when
		                            test="${(fieldType eq 'SELECTVOCAB') or (fieldType eq 'SELECTVOCABMULTI')}">
		                            <div class="col-sm-3">
		                                ${form.procedureModel.variables[field.name].label} :</div>
		                            <div class="col-sm-9">${form.procedureInstance.globalVariablesValues[field.name]}
		                                <c:if test="${not empty field.helpText}">
		                                    <span class="help-block">${field.helpText}</span>
		                                </c:if>
		                            </div>
		                        </c:when>
		                        <c:when test="${fieldType eq 'FILE'}">
		                            <div class="col-sm-3">
		                                <label
		                                    for="${form.procedureInstance.globalVariablesValues[field.name]}">${form.procedureModel.variables[field.name].label}</label>
		                            </div>
		                            <div class="col-sm-3"></div>
		                        </c:when>
		                        <c:otherwise>
		                            <p>error</p>
		                        </c:otherwise>
		                    </c:choose>
	                    </div>
	                </div>
	            </li>
            </c:forEach>
        </ul>
    </div>
    <div class="panel-footer">
        <div class="form-group">
            <a class="btn btn-default" href="${form.procedureInstance.url}" role="button" title="�diter l'�l�ment"><i class="glyphicons glyphicons-edit"></i> �diter l'�l�ment</a>
        </div>
    </div>
</div>