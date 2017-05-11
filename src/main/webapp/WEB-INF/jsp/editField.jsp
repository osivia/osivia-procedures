<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<li
	class="form-group <c:if test="${form.selectedField.path eq field.path}">fieldSelected</c:if> <c:if test="${field.required eq true}">required</c:if>">
	<div class="col-sm-9">
		<div class="row">
			<c:set var="fieldType"
				value="${form.procedureModel.variables[field.name].type}" />
			<c:choose>
				<c:when test="${field.input eq true}">
					<c:choose>
						<c:when test="${fieldType eq 'TEXT'}">
							<form:label
								path="procedureInstance.globalVariablesValues['${field.name}']"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<form:input
									path="procedureInstance.globalVariablesValues['${field.name}']"
									type="text" cssClass="form-control" />
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'TEXTAREA'}">
							<form:label
								path="procedureInstance.globalVariablesValues['${field.name}']"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<form:textarea
									path="procedureInstance.globalVariablesValues['${field.name}']"
									cssClass="form-control" />
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'DATE'}">
							<script type="text/javascript">
								$JQry(document)
										.ready(
												function() {
													$JQry(
															"#selectVariable_${field.name}")
															.datepicker(
																	{
																		dateFormat : "yy-mm-dd"
																	});
												});
							</script>
							<form:label
								path="procedureInstance.globalVariablesValues['${field.name}']"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<div class="input-group">
									<span class="input-group-addon"> <i
										class="halflings halflings-glyph-calendar"></i>
									</span>
									<form:input
										path="procedureInstance.globalVariablesValues['${field.name}']"
										type="text" cssClass="form-control"
										id="selectVariable_${field.name}" />
									<c:if test="${not empty field.helpText}">
										<span class="help-block">${field.helpText}</span>
									</c:if>
								</div>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'NUMBER'}">
							<form:label
								path="procedureInstance.globalVariablesValues['${field.name}']"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<form:input
									path="procedureInstance.globalVariablesValues['${field.name}']"
									type="number" cssClass="form-control" />
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when
							test="${(fieldType eq 'RADIOLIST') or (fieldType eq 'RADIOVOCAB')}">
							<form:label
								path="procedureInstance.globalVariablesValues['${field.name}']"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<input type="text"
									name="procedureInstance.globalVariablesValues['${field.name}']"
									data-varOptions='${form.procedureModel.variables[field.name].varOptions}'
									class="hidden field-radioList-json"
									value="${form.procedureInstance.globalVariablesValues[field.name]}">
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when
							test="${(fieldType eq 'CHECKBOXLIST') or (fieldType eq 'CHECKBOXVOCAB')}">
							<form:label
								path="procedureInstance.globalVariablesValues['${field.name}']"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<input type="text"
									name="procedureInstance.globalVariablesValues['${field.name}']"
									data-varOptions='${form.procedureModel.variables[field.name].varOptions}'
									class="hidden field-checkboxList-json"
									value="${form.procedureInstance.globalVariablesValues[field.name]}">
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
								path="procedureInstance.globalVariablesValues['${field.name}']"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<input type="text"
									name="procedureInstance.globalVariablesValues['${field.name}']"
									data-varOptions='${form.procedureModel.variables[field.name].varOptions}'
									class="hidden field-selectList-json"
									value="${form.procedureInstance.globalVariablesValues[field.name]}">
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'SELECTVOCAB'}">
							<portlet:resourceURL id="vocabularySearch"
								var="vocabularySearchUrl">
								<portlet:param name="vocabularyName"
									value="${form.procedureModel.variables[field.name].varOptions}" />
							</portlet:resourceURL>
							<form:label
								path="procedureInstance.globalVariablesValues['${field.name}']"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<form:select
									path="procedureInstance.globalVariablesValues['${field.name}']"
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
									value="${form.procedureModel.variables[field.name].varOptions}" />
							</portlet:resourceURL>
							<form:label
								path="procedureInstance.globalVariablesValues['${field.name}']"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-9">
								<form:select
									path="procedureInstance.globalVariablesValues['${field.name}']"
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
								path="procedureInstance.globalVariablesValues['${field.name}']"
								cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
							<div class="col-sm-3">
								<input type="file" name="file:${field.name}" />
							</div>
							<div class="col-sm-6">
								<a
									href="${form.procedureInstance.filesPath[field.name].downloadLink}">${form.procedureInstance.filesPath[field.name].fileName}</a>
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
							<div class="col-sm-3">${field.superLabel}:</div>
							<div class="col-sm-9">
								<c:out
									value="${form.procedureInstance.globalVariablesValues[field.name]}" />
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'TEXTAREA'}">
							<div class="col-sm-3">${field.superLabel}:</div>
							<div class="col-sm-9">
								<span class="pre-wrap"><c:out value="${form.procedureInstance.globalVariablesValues[field.name]}" /></span>
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when
							test="${(fieldType eq 'SELECTVOCAB') or (fieldType eq 'SELECTVOCABMULTI')}">
							<div class="col-sm-3">${field.superLabel}:</div>
							<div class="col-sm-9">${form.procedureInstance.globalVariablesValues[field.name]}
								<c:if test="${not empty field.helpText}">
									<span class="help-block">${field.helpText}</span>
								</c:if>
							</div>
						</c:when>
						<c:when test="${fieldType eq 'FILE'}">
							<div class="col-sm-3">
								<label
									for="${form.procedureInstance.globalVariablesValues[field.name]}">${field.superLabel}</label>
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
	</div> <c:forEach var="pathPart" items="${field.path}" varStatus="status">
		<c:set var="springPath"
			value="${status.first ? 'theSelectedStep' : springPath}.fields[${pathPart}]"
			scope="request" />
	</c:forEach> <form:hidden path="${springPath}.path" />
</li>
