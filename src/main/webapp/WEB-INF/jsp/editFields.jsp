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
                        <c:set var="occurrences" value="${form.procedureInstance.jsonValues[fieldBkp.name]}" scope="page" />

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
                                            <th>${nestedField.superLabel}</th>
                                        </c:forEach>
                                        <th width="1%"></th>
                                    </tr>
                                </thead>
                                
                                <tbody>
                                    <c:forEach var="occurrence" items="${occurrences}" varStatus="status">
                                        <tr class="${form.selectedListFieldPath eq fieldBkp.path and form.selectedListFieldRowIndex eq status.index ? 'info' : ''}">
                                            <c:forEach var="nestedField" items="${fieldBkp.fields}">
                                                <td>
                                                    <c:set var="field" value="${nestedField}" scope="request" />
                                                    <c:set var="fieldType" value="${nestedField.type}" scope="request" />
                                                    <c:set var="fieldValue" value="${occurrence[nestedField.name]}" scope="request" />
                                                    <c:set var="fieldJsonValue" value="${occurrence[nestedField.name]}" scope="request" />
                                                    <c:set var="fieldLevel" value="2" scope="request" />
                                                    <c:set var="rowIndex" value="${status.index}" scope="request" />
                                                    <c:set var="fieldName" value="${fieldBkp.name}" scope="request" />
                                                    <jsp:include page="displayFieldValue.jsp" />
                                                </td>
                                            </c:forEach>
                                            
                                            <td class="text-nowrap">
                                                <button type="submit" name="editFieldInList" value="${fieldBkp.path}|${status.index}" class="btn btn-default btn-xs">
                                                    <i class="glyphicons glyphicons-edit"></i>
                                                    <span class="sr-only"><op:translate key="EDIT" /></span>
                                                </button>
                                                
                                                <button type="submit" name="removeFieldInList" value="${fieldBkp.path}|${status.index}" class="btn btn-default btn-xs">
                                                    <i class="glyphicons glyphicons-bin"></i>
                                                    <span class="sr-only"><op:translate key="DELETE" /></span>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    
                                    <c:if test="${empty occurrences}">
                                        <tr>
                                            <td colspan="${fn:length(fieldBkp.fields)}" class="text-center text-muted"><op:translate key="NO_OCCURRENCE" /></td>
                                        </tr>
                                    </c:if>
                                </tbody>


			                   	<c:set var="displayFooter" value="false" />
								<c:forEach var="nestedField" items="${fieldBkp.fields}" varStatus="status">
			                  			<c:set var="sumName" value="osivia.${fieldBkp.name}.${nestedField.name}" />
										<c:set var="sumValue" value="${form.procedureInstance.globalVariablesValues[sumName]}" />
								
										<c:if test="${not empty sumValue}">
			                                    		<c:set var="displayFooter" value="true" />
			                             </c:if>
			
			                    </c:forEach>     
			                                   
			                    <c:if test="${displayFooter}">
									<tfoot>
				                        <tr class="active">    
				                           <c:forEach var="nestedField" items="${fieldBkp.fields}" varStatus="status">
				                               	<td>   
				                           			<c:set var="sumName" value="osivia.${fieldBkp.name}.${nestedField.name}" />
													<c:set var="sumValue" value="${form.procedureInstance.globalVariablesValues[sumName]}" />
											
													<c:if test="${not empty sumValue}">
				                                      		${sumValue}	
				                                    </c:if>
				                                </td>
				                            </c:forEach>
										</tr>                                    
									</tfoot>  
								</c:if>






							</table>

                            <div class="panel-body">
                                <ul class="list-unstyled ${editionMode ? 'procedure-sortable' : ''}">
                                    <c:forEach var="nestedField" items="${fieldBkp.fields}">
                                        <c:set var="field" value="${nestedField}" scope="request" />
                                        <jsp:include page="editFields.jsp" />
                                    </c:forEach>
                                </ul>

                                <div class="row">
                                    <div class="col-sm-offset-3 col-sm-9 col-lg-offset-2 col-lg-10">
                                        <c:choose>
                                            <c:when test="${form.selectedListFieldPath eq fieldBkp.path}">
                                                <button type="submit" name="validateEditFieldInList" value="${fieldBkp.path}|${status.index}" class="btn btn-primary">
                                                    <op:translate key="VALIDATE" />
                                                </button>
                                            
                                                <button type="submit" name="cancelEditFieldInList" class="btn btn-default">
                                                    <op:translate key="CANCEL" />
                                                </button>
                                            </c:when>

                                            <c:when test="${not editionMode}">
                                                <button type="submit" name="addFieldInList" value="${fieldBkp.path}" class="btn btn-default">
                                                    <op:translate key="ADD" />
                                                </button>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                            <%-- <form:hidden path="selectedListFieldRowIndex" />
                            <form:hidden path="procedureInstance.globalVariablesValues['${fieldBkp.name}']" /> --%>
                        </div>
                    </c:when>
                </c:choose>
            </div> 
            
            <c:forEach var="pathPart" items="${fieldBkp.path}" varStatus="status">
                <c:choose>
                    <c:when test="${not empty form.procedureInstance and not empty form.procedureInstance.currentStep}">
                        <c:set var="springPathNested" value="${status.first ? 'theCurrentStep' : springPathNested}.fields[${pathPart}]" scope="request" />
                    </c:when>
                    
                    <c:otherwise>
                        <c:set var="springPathNested" value="${status.first ? 'theSelectedStep' : springPathNested}.fields[${pathPart}]" scope="request" />
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            
            <form:hidden path="${springPathNested}.path" /></li>
    </c:when>
    
    <c:otherwise>
        <jsp:include page="editField.jsp" />
    </c:otherwise>
</c:choose>
