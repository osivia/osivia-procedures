<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<c:set var="fieldType" value="${form.procedureModel.variables[field.name].type}" />
<c:choose>
	<c:when test="${field.input eq true}">
		<c:choose>
			<c:when test="${fieldType eq 'TEXT'}">
				<div class="col-sm-3">
					<label
						for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
				</div>
				<div class="col-sm-9">
					<form:input
						path="procedureInstance.globalVariablesValues['${field.name}']"
						type="text" cssClass="form-control" />
				</div>
			</c:when>
			<c:when test="${fieldType eq 'TEXTAREA'}">
				<div class="col-sm-3">
					<label
						for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
				</div>
				<div class="col-sm-9">
					<form:textarea
						path="procedureInstance.globalVariablesValues['${field.name}']"
						cssClass="form-control" />
				</div>
			</c:when>
			<c:when test="${fieldType eq 'DATE'}">
				<script type="text/javascript">
					$JQry(document).ready(function() {
						$JQry("#selectVariable_${status.index}").datepicker({
							dateFormat : "ISO_8601"
						});
					});
				</script>
				<div class="col-sm-3">
					<label
						for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
				</div>
				<div class="col-sm-9">
					<div class="input-group">
						<span class="input-group-addon"> <i
							class="halflings halflings-glyph-calendar"></i>
						</span>
						<form:input
							path="procedureInstance.globalVariablesValues['${field.name}']"
							type="text" cssClass="form-control"
							id="selectVariable_${status.index}" />
					</div>
				</div>
			</c:when>
			<c:when test="${fieldType eq 'NUMBER'}">
				<div class="col-sm-3">
					<label
						for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
				</div>
				<div class="col-sm-9">
					<form:input
						path="procedureInstance.globalVariablesValues['${field.name}']"
						type="number" cssClass="form-control" />
				</div>
			</c:when>
			<c:when
				test="${(fieldType eq 'RADIOLIST') or (fieldType eq 'RADIOVOCAB')}">
				<div class="col-sm-3">
					<label
						for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
				</div>
				<div class="col-sm-9">
					<c:forEach
						items="${form.procedureModel.variables[field.name].varOptions}"
						var="varOption">
						<label class="radio-inline"> <input type="radio"
							name="procedureInstance.globalVariablesValues['${field.name}']"
							value="${varOption}"
							<c:if test="${form.procedureInstance.globalVariablesValues[field.name] eq varOption}">checked="checked"</c:if>>
							${varOption}
						</label>
					</c:forEach>
				</div>
			</c:when>
			<c:when
				test="${(fieldType eq 'CHECKBOXLIST') or (fieldType eq 'CHECKBOXVOCAB')}">
				<div class="col-sm-3">
					<label
						for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
				</div>
				<div class="col-sm-9">
					<c:forEach
						items="${form.procedureModel.variables[field.name].varOptions}"
						var="varOption">
						<label class="checkbox-inline"> <input type="checkbox"
							name="procedureInstance.globalVariablesValues['${field.name}']"
							value="${varOption}"
							<c:if test="${form.procedureInstance.globalVariablesValues[field.name] eq varOption}">checked="checked"</c:if>>
							${varOption}
						</label>
					</c:forEach>
				</div>
			</c:when>
			<c:when test="${fieldType eq 'SELECTLIST'}">
				<script type="text/javascript">
					$JQry(document).ready(function() {
						$JQry("#selectVariable_${status.index}").select2({
							theme : "bootstrap"
						});
					});
				</script>
				<div class="col-sm-3">
					<label
						for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
				</div>
				<div class="col-sm-9">
					<form:select
						path="procedureInstance.globalVariablesValues['${field.name}']"
						id="selectVariable_${status.index}"
						items="${form.procedureModel.variables[field.name].varOptions}" />
				</div>
			</c:when>
			<c:when test="${fieldType eq 'SELECTVOCAB'}">
				<portlet:resourceURL id="vocabularySearch" var="vocabularySearchUrl">
					<portlet:param name="vocabularyName"
						value="${form.procedureModel.variables[field.name].varOptions[0]}" />
				</portlet:resourceURL>
				<div class="col-sm-3">
					<label
						for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
				</div>
				<div class="col-sm-9">
					<form:select
						path="procedureInstance.globalVariablesValues['${field.name}']" class="form-control select2" data-url="${vocabularySearchUrl}" />
				</div>
			</c:when>
			<c:when test="${fieldType eq 'SELECTVOCABMULTI'}">
				<portlet:resourceURL id="vocabularySearch" var="vocabularySearchUrl">
					<portlet:param name="vocabularyName" value="${form.procedureModel.variables[field.name].varOptions[0]}" />
				</portlet:resourceURL>
				<div class="col-sm-3">
					<label
						for="${form.procedureInstance.globalVariablesValues['{field.name}']}">${form.procedureModel.variables[field.name].label}</label>
				</div>
				<div class="col-sm-9">
					<form:select
						path="procedureInstance.globalVariablesValues['${field.name}']" multiple="true" data-url="${vocabularySearchUrl}" class="form-control select2" />
				</div>
			</c:when>
			<c:when test="${fieldType eq 'FILE'}">
				<div class="col-sm-3">
					<label
						for="${form.procedureInstance.globalVariablesValues[field.name]}">${form.procedureModel.variables[field.name].label}</label>
				</div>
				<div class="col-sm-3">
					<input type="file" name="file:${field.name}" />
				</div>
				<div class="col-sm-6">
					<a
						href="${form.procedureInstance.filesPath[field.name].downloadLink}">${form.procedureInstance.filesPath[field.name].fileName}</a>
				</div>
			</c:when>
			<c:otherwise>
				<p>vide</p>
			</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when
				test="${(fieldType eq 'TEXT') or (fieldType eq 'TEXTAREA') or (fieldType eq 'DATE') or (fieldType eq 'SELECTLIST') or (fieldType eq 'RADIOLIST')
                                                    or (fieldType eq 'RADIOVOCAB') or (fieldType eq 'CHECKBOXLIST') or (fieldType eq 'CHECKBOXVOCAB') or (fieldType eq 'NUMBER')}">
				<div class="col-sm-3">
					${form.procedureModel.variables[field.name].label} :</div>
				<div class="col-sm-3">
					${form.procedureInstance.globalVariablesValues[field.name]}</div>
			</c:when>
			<c:when
				test="${(fieldType eq 'SELECTVOCAB') or (fieldType eq 'SELECTVOCABMULTI')}">
				<div class="col-sm-3">
					${form.procedureModel.variables[field.name].label} :</div>
				<div class="col-sm-3">
					TODO backport vocab
				</div>
			</c:when>
			<c:when test="${fieldType eq 'FILE'}">
				<div class="col-sm-3">
					<label
						for="${form.procedureInstance.globalVariablesValues[field.name]}">${form.procedureModel.variables[field.name].label}</label>
				</div>
				<div class="col-sm-3">
					<a
						href="${form.procedureInstance.filesPath[field.name].downloadLink}">${form.procedureInstance.filesPath[field.name].fileName}</a>
				</div>
			</c:when>
			<c:otherwise>
				<p>vide</p>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>