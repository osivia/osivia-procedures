<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>

<c:choose>
	<c:when test="${field.fieldSet eq true}">
        <li class="form-group" id="${field.path}">
			<div class="col-sm-12">
				<c:set var="fieldType" value="${form.procedureModel.variables[field.name].type}" />
				<c:choose>
	                <c:when test="${fieldType eq 'FIELDSET'}">
						  <c:choose>
						      <c:when test="${form.selectedField.path eq field.path}">
						          <div class="panel panel-info">
						      </c:when>
						      <c:otherwise>
						          <div class="panel panel-default">
						      </c:otherwise>
						  </c:choose>
						  
						        <c:if test="${not empty field.superLabel}">
						   			<div class="panel-heading">
						   				${field.superLabel}
						   			</div>
						        </c:if>
					   			<div class="panel-body">
					   			    <p class="text-pre-wrap">${form.procedureModel.variables[field.name].varOptions}</p>
					   				<ul class="procedure-sortable list-unstyled">
										<c:set var="fieldBkp" value="${field}" scope="page"/>
										<c:forEach var="nestedField" items="${fieldBkp.fields}" varStatus="status">
											<c:set var="field" value="${nestedField}" scope="request"/>
											<jsp:include page="editFields.jsp" />
										</c:forEach>
									</ul>
					   			</div>
				   			</div>
	                </c:when>
	                <c:when test="${fieldType eq 'FIELDLIST'}">
	                	<div class="panel panel-default list-field">
	                		<div class="panel-body">
			                	<caption>${field.superLabel}</caption>
				                <ul class="procedure-sortable list-unstyled">
				                	<c:set var="fieldBkp" value="${field}" scope="page"/>
									<c:forEach var="nestedField" items="${fieldBkp.fields}" varStatus="status">
										<c:set var="field" value="${nestedField}" scope="request"/>
										<jsp:include page="editFields.jsp" />
									</c:forEach>
				                </ul>

								<c:choose>
									<c:when test="${form.selectedListFieldPath eq fieldBkp.path}">
										<div class="btn-group pull-right">
				                            <button type="submit" name="cancelEditFieldInList" class="btn btn-default">
				                                <op:translate key="CANCEL" />
				                            </button>
											<button type="submit" name="validateEditFieldInList" class="btn btn-primary">
				                                <op:translate key="VALIDATE" />
				                            </button>
										</div>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${editionMode}">
												<button type="submit" name="addFieldInList" class="btn btn-default pull-right" disabled="disabled">
					                                <op:translate key="ADD" />
					                            </button>
											</c:when>
											<c:otherwise>
												<button type="submit" name="addFieldInList" class="btn btn-default pull-right" onclick="selector(this,'${fieldBkp.path}','selectedFieldPath')">
					                                <op:translate key="ADD" />
					                            </button>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>

				                <table class="table">
				                	<thead>
				                		<tr>
				                			<c:forEach var="nestedField" items="${fieldBkp.fields}" varStatus="status">
				                				<th data-varname="${nestedField.name}">${nestedField.superLabel}</th>
				                			</c:forEach>
				                			<th></th>
				                		</tr>
				                	</thead>
				                	<tbody>
				                	</tbody>
				                </table>
			                </div>
							<form:hidden path="procedureInstance.globalVariablesValues['${fieldBkp.name}']" />
		                </div>
	                </c:when>
	            </c:choose>
   			</div>
			<c:forEach var="pathPart" items="${fieldBkp.path}" varStatus="status">
                <c:choose>
                    <c:when test="${not empty form.procedureInstance and not empty form.procedureInstance.currentStep}">
                        <c:set var="springPathNested" value="${status.first ? 'theCurrentStep' : springPathNested}.fields[${pathPart}]" scope="request"/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="springPathNested" value="${status.first ? 'theSelectedStep' : springPathNested}.fields[${pathPart}]" scope="request"/>
                    </c:otherwise>
                </c:choose>
			</c:forEach>
			<form:hidden path="${springPathNested}.path"/>
   		</li>
	</c:when>
	<c:otherwise>
		<jsp:include page="editField.jsp" />
	</c:otherwise>
</c:choose>
