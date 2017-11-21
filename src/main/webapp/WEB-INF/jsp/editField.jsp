<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<li
	class="form-group <c:if test="${form.selectedField.path eq field.path}">fieldSelected</c:if> <c:if test="${field.required eq true}">required</c:if>">
	<div class="col-sm-9">
		<div class="row">
		
			<c:set var="fieldType" value="${form.procedureModel.variables[field.name].type}" />
			<c:set var="fieldNamePath" value="procedureInstance.globalVariablesValues['${field.name}']" />
			<c:set var="fieldValue" value="${form.procedureInstance.globalVariablesValues[field.name]}" />
			<c:set var="fieldVarOptions" value="${form.procedureModel.variables[field.name].varOptions}" />
			<c:set var="downloadLink" value="${form.procedureInstance.filesPath[field.name].downloadLink}" />
			<c:set var="fileName" value="${form.procedureInstance.filesPath[field.name].fileName}" />

			<c:choose>
				<c:when test="${field.input eq true}">
					<c:choose>
						<c:when test="${fieldType eq 'TEXT'}">
							<form:label path="${fieldNamePath}" cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<form:input path="${fieldNamePath}" type="text" cssClass="form-control" />
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'TEXTAREA'}">
							<form:label
								path="${fieldNamePath}"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<form:textarea
									path="${fieldNamePath}"
									cssClass="form-control" />
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'DATE'}">
							<form:label
								path="${fieldNamePath}"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<div class="input-group">
									<span class="input-group-addon"> <i
										class="halflings halflings-calendar"></i>
									</span>
									<form:input path="${fieldNamePath}" type="text" cssClass="form-control dates-selector" />
									<c:if test="${not empty field.helpText}">
										<span class="help-block">${field.helpText}</span>
									</c:if>
								</div>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'NUMBER'}">
							<form:label
								path="${fieldNamePath}"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<form:input
									path="${fieldNamePath}"
									type="number" cssClass="form-control" />
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when
							test="${(fieldType eq 'RADIOLIST') or (fieldType eq 'RADIOVOCAB')}">
							<form:label
								path="${fieldNamePath}"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<input type="text"
									name="${fieldNamePath}"
									data-varOptions='${fieldVarOptions}'
									class="hidden field-radioList-json"
									value="${fieldValue}">
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when
							test="${(fieldType eq 'CHECKBOXLIST') or (fieldType eq 'CHECKBOXVOCAB')}">
							<form:label
								path="${fieldNamePath}"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<input type="text"
									name="${fieldNamePath}"
									data-varOptions='${fieldVarOptions}'
									class="hidden field-checkboxList-json"
									value="${fieldValue}">
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'SELECTLIST'}">
							<script type="text/javascript">
								$JQry(document)
										.ready(
												function() {
													$JQry(
															"#selectVariable_${status.index}")
															.select2(
																	{
																		theme : "bootstrap"
																	});
												});
							</script>
							<form:label
								path="${fieldNamePath}"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<input type="text"
									name="${fieldNamePath}"
									data-varOptions='${fieldVarOptions}'
									class="hidden field-selectList-json"
									value="${fieldValue}">
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'SELECTVOCAB'}">
							<portlet:resourceURL id="vocabularySearch"
								var="vocabularySearchUrl">
								<portlet:param name="vocabularyName"
									value="${fieldVarOptions}" />
							</portlet:resourceURL>
							<form:label
								path="${fieldNamePath}"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<form:select
									path="${fieldNamePath}"
									class="form-control select2 vocabularySelect-select2"
									cssStyle="width: 100%;" data-url="${vocabularySearchUrl}" />
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'SELECTVOCABMULTI'}">
							<portlet:resourceURL id="vocabularySearch"
								var="vocabularySearchUrl">
								<portlet:param name="vocabularyName"
									value="${fieldVarOptions}" />
							</portlet:resourceURL>
							<form:label
								path="${fieldNamePath}"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<form:select
									path="${fieldNamePath}"
									multiple="true"
									class="form-control select2 vocabularySelect-select2"
									cssStyle="width: 100%;" data-url="${vocabularySearchUrl}" />
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'FILE'}">
							<form:label
								path="${fieldNamePath}"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-3">
								<input type="file" name="file:${field.name}" />
							</div>
							<div class="col-sm-6">
								<a
									href="${downloadLink}">${fileName}</a>
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:otherwise>
							<p>error</p>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when
							test="${(fieldType eq 'TEXT') or (fieldType eq 'DATE') or (fieldType eq 'SELECTLIST') or (fieldType eq 'RADIOLIST')
	                    				or (fieldType eq 'RADIOVOCAB') or (fieldType eq 'CHECKBOXLIST') or (fieldType eq 'CHECKBOXVOCAB') or (fieldType eq 'NUMBER')}">
		                    <label class="col-sm-3 control-label">${field.superLabel}</label>
							<div class="col-sm-9">
							    <p class="form-control-static">${fieldValue}</p>
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'TEXTAREA'}">
							<label class="col-sm-3 control-label">${field.superLabel}</label>
							<div class="col-sm-9">
								<span class="text-pre-wrap"><c:out value="${fieldValue}" /></span>
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when
							test="${(fieldType eq 'SELECTVOCAB') or (fieldType eq 'SELECTVOCABMULTI')}">
							<label class="col-sm-3 control-label">${field.superLabel}</label>
							<div class="col-sm-9">
							    <p class="form-control-static">${fieldValue}</p>
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'FILE'}">
							<div class="col-sm-3">
								<label
									for="${fieldValue}">${field.superLabel}</label>
							</div>
							<div class="col-sm-3"></div>
						</c:when>
						<c:otherwise>
							<p>error</p>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</div>
	</div> 
	<c:forEach var="pathPart" items="${field.path}" varStatus="status">
		<c:set var="springPath"
			value="${status.first ? 'theCurrentStep' : springPath}.fields[${pathPart}]"
			scope="request" />
	</c:forEach> <form:hidden path="${springPath}.path" />
</li>
