<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<portlet:defineObjects />

<!-- Datepicker language -->
<c:set var="datepickerLanguage" value="${fn:toLowerCase(pageContext.response.locale.language)}" />
<c:if test="${'en' ne datepickerLanguage}">
    <script type="text/javascript" src="/osivia-portal-custom-web-assets/components/jquery-ui/i18n/datepicker-${datepickerLanguage}.js"></script>
</c:if>

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
                                    <c:when test="${fieldType eq 'TEXTAREA'}">
                                    	<div class="col-sm-3">
                                            <label for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
                                        </div>
                                        <div class="col-sm-9">
                                            <form:textarea path="procedureInstance.globalVariablesValues['${field.name}']"  cssClass="form-control"/>
                                        </div>
                                    </c:when>
                                    <c:when test="${fieldType eq 'DATE'}">
                                    	<script type="text/javascript">
                                    		$JQry(document).ready(function(){
                                    			$JQry("#selectVariable_${status.index}").datepicker({
                                    				dateFormat: "DD d MM yy"
                                    			});
                                    		});
						                </script>
                                    	<div class="col-sm-3">
                                            <label for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
                                        </div>
                                        <div class="col-sm-9">
                                        	<div class="input-group">
	                                        	<span class="input-group-addon">
	                                                <i class="halflings halflings-glyph-calendar"></i>
	                                            </span>
	                                        	<form:input path="procedureInstance.globalVariablesValues['${field.name}']" type="text" cssClass="form-control" id="selectVariable_${status.index}"/>
                                        	</div>
                                        </div>
                                    </c:when>
                                    <c:when test="${fieldType eq 'NUMBER'}">
                                    	<div class="col-sm-3">
                                            <label for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
                                        </div>
                                        <div class="col-sm-9">
                                            <form:input path="procedureInstance.globalVariablesValues['${field.name}']" type="number" cssClass="form-control"/>
                                        </div>
                                    </c:when>
                                    <c:when test="${(fieldType eq 'RADIOLIST') or (fieldType eq 'RADIOVOCAB')}">
                                    	<div class="col-sm-3">
                                            <label for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
                                        </div>
                                        <div class="col-sm-9">
                                        	<c:forEach items="${form.procedureModel.variables[field.name].varOptions}" var="varOption">
	                                        	<label class="radio-inline">
												  <input type="radio" name="procedureInstance.globalVariablesValues['${field.name}']" value="${varOption}"> ${varOption}
												</label>
                                        	</c:forEach>
                                    	</div>
                                    </c:when>
                                    <c:when test="${(fieldType eq 'CHECKBOXLIST') or (fieldType eq 'CHECKBOXVOCAB')}">
                                    	<div class="col-sm-3">
                                            <label for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
                                        </div>
                                        <div class="col-sm-9">
                                        	<c:forEach items="${form.procedureModel.variables[field.name].varOptions}" var="varOption">
	                                        	<label class="checkbox-inline">
												  <input type="checkbox" name="procedureInstance.globalVariablesValues['${field.name}']" value="${varOption}"> ${varOption}
												</label>
                                        	</c:forEach>
                                        </div>
                                    </c:when>
                                    <c:when test="${fieldType eq 'SELECTLIST'}">
                                    	<script type="text/javascript">
                                    		$JQry(document).ready(function(){
                                    			$JQry("#selectVariable_${status.index}").select2({
                                    				theme: "bootstrap"
                                    			});
                                    		});
						                </script>
                                    	<div class="col-sm-3">
                                            <label for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
                                        </div>
                                    	<div class="col-sm-9">
	                                    	<form:select path="procedureInstance.globalVariablesValues['${field.name}']" id="selectVariable_${status.index}" 
	                                    				items="${form.procedureModel.variables[field.name].varOptions}"/>
                                    	</div>
                                    </c:when>
                                    <c:when test="${fieldType eq 'SELECTVOCAB'}">
                                    	<script type="text/javascript">
                                    		$JQry(document).ready(function(){
                                    			$JQry("#selectVariable_${status.index}").select2({
								                	data : ${form.procedureModel.variables[field.name].varOptionsJson},
								                	theme: "bootstrap"
							                	});
                                    		});
						                </script>
                                    	<div class="col-sm-3">
                                            <label for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
                                        </div>
                                    	<div class="col-sm-9">
	                                    	<form:select path="procedureInstance.globalVariablesValues['${field.name}']" id="selectVariable_${status.index}"/>
                                    	</div>
                                    </c:when>
                                    <c:when test="${fieldType eq 'SELECTVOCABMULTI'}">
                                    	<script type="text/javascript">
                                    		$JQry(document).ready(function(){
                                    			$JQry("#selectVariable_${status.index}").select2({
								                	data : ${form.procedureModel.variables[field.name].varOptionsJson},
								                	theme: "bootstrap"
							                	});
                                    		});
						                </script>
                                    	<div class="col-sm-3">
                                            <label for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
                                        </div>
                                    	<div class="col-sm-9">
	                                    	<form:select path="procedureInstance.globalVariablesValues['${field.name}']" id="selectVariable_${status.index}" multiple="true"/>
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
                                        <p>vide</p>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${(fieldType eq 'TEXT') or (fieldType eq 'TEXTAREA') or (fieldType eq 'DATE') or (fieldType eq 'SELECTLIST') or (fieldType eq 'SELECTVOCAB') or (fieldType eq 'RADIOLIST')
                                    				or (fieldType eq 'RADIOVOCAB') or (fieldType eq 'CHECKBOXLIST') or (fieldType eq 'CHECKBOXVOCAB') or (fieldType eq 'NUMBER') or (fieldType eq 'SELECTVOCABMULTI')}">
                                        <div class="col-sm-3">
                                            ${form.procedureModel.variables[field.name].label} : 
                                        </div>
                                        <div class="col-sm-3">
                                        	${form.procedureInstance.globalVariablesValues[field.name]}
                                        </div>
                                    </c:when>
                                    <c:when test="${fieldType eq 'FILE'}">
                                        <div class="col-sm-3">
                                            <label for="${form.procedureInstance.globalVariablesValues[field.name]}">${form.procedureModel.variables[field.name].label}</label>
                                        </div>
                                        <div class="col-sm-3">
                                            <a href="${form.procedureInstance.filesPath[field.name].downloadLink}">${form.procedureInstance.filesPath[field.name].fileName}</a> 
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <p>vide</p>
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