<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<portlet:defineObjects />


<ul class="nav nav-tabs" role="tablist">
    <li role="presentation" class="active"><a href="#Formulaire" role="tab" data-toggle="tab" class="no-ajax-link">Formulaire</a></li>
    <li role="presentation"><a href="#Metadata" role="tab" data-toggle="tab" class="no-ajax-link">Métadonnées</a></li>
</ul>


<div class="tab-content">
    <div role="tabpanel" class="tab-pane active" id="Formulaire">
        <ul class="procedure-sortable list-unstyled">
            <c:forEach var="field" items="${form.theCurrentStep.fields}" varStatus="status">
	            <li class="form-group">
				    <div class="col-sm-9">
				        <div class="row">
				            <c:set var="fieldType" value="${form.procedureModel.variables[field.name].type}" />
		                    <c:choose>
		                        <c:when
		                            test="${(fieldType eq 'TEXT') or (fieldType eq 'DATE') or (fieldType eq 'SELECTLIST') or (fieldType eq 'RADIOLIST')
		                                        or (fieldType eq 'RADIOVOCAB') or (fieldType eq 'CHECKBOXLIST') or (fieldType eq 'CHECKBOXVOCAB') or (fieldType eq 'NUMBER')}">
		                            <label class="col-sm-3 control-label">${field.superLabel}</label>
		                            <div class="col-sm-9">
		                                <c:out
		                                    value="${form.record.globalVariablesValues[field.name]}" />
		                                <c:if test="${not empty field.helpText}">
		                                    <span class="help-block">${field.helpText}</span>
		                                </c:if>
		                            </div>
		                        </c:when>
		                        <c:when test="${fieldType eq 'TEXTAREA'}">
		                            <label class="col-sm-3 control-label">${field.superLabel}</label>
		                            <div class="col-sm-9">
		                                <span class="pre-wrap"><c:out value="${form.record.globalVariablesValues[field.name]}" /></span>
		                                <c:if test="${not empty field.helpText}">
		                                    <span class="help-block">${field.helpText}</span>
		                                </c:if>
		                            </div>
		                        </c:when>
		                        <c:when
		                            test="${(fieldType eq 'SELECTVOCAB') or (fieldType eq 'SELECTVOCABMULTI')}">
		                            <label class="col-sm-3 control-label">${field.superLabel}</label>
		                            <div class="col-sm-9">${form.record.globalVariablesValues[field.name]}
		                                <c:if test="${not empty field.helpText}">
		                                    <span class="help-block">${field.helpText}</span>
		                                </c:if>
		                            </div>
		                        </c:when>
		                        <c:when test="${fieldType eq 'FILE'}">
		                            <div class="col-sm-3">
		                                <label
		                                    for="${form.record.globalVariablesValues[field.name]}">${field.superLabel}</label>
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
    <div role="tabpanel" class="tab-pane" id="Metadata">
        <div class="row">
	        <label class="col-sm-3 control-label">Date de création</label>
	        <div class="col-sm-9">
	           <span class="pre-wrap"><c:out value="${form.record.created}" /></span>
	        </div>
	    </div>
	    <div class="row">
            <label class="col-sm-3 control-label">Créé par</label>
            <div class="col-sm-9">
               <span class="pre-wrap"><c:out value="${form.record.creator}" /></span>
            </div>
        </div>
        <div class="row">
            <label class="col-sm-3 control-label">Date de dernière modification</label>
            <div class="col-sm-9">
               <span class="pre-wrap"><c:out value="${form.record.modified}" /></span>
            </div>
        </div>
        <div class="row">
            <label class="col-sm-3 control-label">Modifié par</label>
            <div class="col-sm-9">
               <span class="pre-wrap"><c:out value="${form.record.lastContributor}" /></span>
            </div>
        </div>
    </div>
</div>

