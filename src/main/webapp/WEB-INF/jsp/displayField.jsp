<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<c:if test="${not status.first or field.name ne '_title'}">
	<li>
		<div class="form-group">
			<c:set var="fieldType" value="${form.procedureModel.variables[field.name].type}" />
			<c:set var="fieldValue" value="${form.record.globalVariablesValues[field.name]}" />

			<label class="col-sm-3 control-label">${field.superLabel}</label>
			<div class="col-sm-9">
				<c:choose>
					<c:when test="${empty fieldValue}">
						<div class="form-control-static"></div>
					</c:when>
	
					<c:when test="${fieldType eq 'DATE'}">
						<fmt:parseDate var="date" value="${fieldValue}"	pattern="dd/MM/yyyy" />
						<fmt:formatDate var="fieldValue" value="${date}" type="date" dateStyle="long" />
						<div class="form-control-static">${fieldValue}</div>
					</c:when>
	
					<c:when test="${(fieldType eq 'SELECTLIST') or (fieldType eq 'RADIOLIST') or (fieldType eq 'CHECKBOXLIST')}">
						<c:set var="value" value="${fieldValue}" />
						<c:forEach var="option" items="${field.jsonVarOptions}">
							<c:if test="${option['value'] eq value}">
								<c:set var="fieldValue" value="${option['label']}" />
							</c:if>
						</c:forEach>
						<div class="form-control-static">${fieldValue}</div>
					</c:when>
	
					<c:when test="${fieldType eq 'WYSIWYG'}">
						<c:set var="fieldValue">
							<ttc:transformContent content="${fieldValue}" />
						</c:set>
						<div class="form-control-static">${fieldValue}</div>
					</c:when>
	
					<c:when test="${fieldType eq 'VOCABULARY'}">
						<c:set var="vocabularyId" value="${field.jsonVarOptions['vocabularyId']}" />
	
						<c:if test="${not empty vocabularyId}">
							<c:set var="fieldValue">
								<ttc:vocabularyLabel name="${vocabularyId}" key="${fieldValue}" />
							</c:set>
						</c:if>
						<div class="form-control-static">${fieldValue}</div>
					</c:when>
	
					<c:when test="${fieldType eq 'PERSON'}">
						<c:set var="fieldValue">
							<ttc:user name="${fieldValue}" />
						</c:set>
						<div class="form-control-static">${fieldValue}</div>
					</c:when>
	
					<c:when test="${fieldType eq 'RECORD'}">
						<c:set var="fieldValue">
							<ttc:title path="${fieldValue}" />
						</c:set>
						<div class="form-control-static">${fieldValue}</div>
					</c:when>
					
					<c:when test="${fieldType eq 'FIELDLIST'}">
						<div class="panel panel-default list-field">
							<div class="panel-body">
								<table class="table">
				                	<thead>
				                		<tr>
				                			<c:forEach var="nestedField" items="${field.fields}" varStatus="status">
				                				<c:set var="fieldVarOptions" value="${form.procedureModel.variables[nestedField.name].varOptions}" />
				                				<th data-varname="${nestedField.name}" data-varoptions='${fieldVarOptions}'>${nestedField.superLabel}</th>
				                			</c:forEach>
				                		</tr>
				                	</thead>
				                	<tbody>
				                	</tbody>
				                </table>
							</div>
							<input type="hidden" name="procedureInstance.globalVariablesValues['${field.name}']" value='${fieldValue}'>
						</div>
					</c:when>
					
					<c:otherwise>
						<div class="form-control-static ${fieldType eq 'TEXTAREA' ? 'text-pre-wrap' : ''}">${fieldValue}</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</li>
</c:if>