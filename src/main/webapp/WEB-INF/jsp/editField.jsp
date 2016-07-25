<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<div class="col-sm-9">
	<div class="row">
		<c:set var="fieldType" value="${form.procedureModel.variables[field.name].type}" />
		<c:choose>
			<c:when test="${field.input eq true}">
				<c:choose>
					<c:when test="${fieldType eq 'TEXT'}">
						<form:label
							path="procedureInstance.globalVariablesValues['${field.name}']"
							cssClass="col-sm-3 control-label">${form.procedureModel.variables[field.name].label}</form:label>
						<div class="col-sm-9">
							<form:input
								path="procedureInstance.globalVariablesValues['${field.name}']"
								type="text" cssClass="form-control" />
						</div>
					</c:when>
					<c:when test="${fieldType eq 'TEXTAREA'}">
						<form:label
							path="procedureInstance.globalVariablesValues['${field.name}']"
							cssClass="col-sm-3 control-label">${form.procedureModel.variables[field.name].label}</form:label>
						<div class="col-sm-9">
							<form:textarea
								path="procedureInstance.globalVariablesValues['${field.name}']"
								cssClass="form-control" />
						</div>
					</c:when>
					<c:when test="${fieldType eq 'DATE'}">
						<script type="text/javascript">
							$JQry(document)
									.ready(
											function() {
												$JQry(
														"#selectVariable_${status.index}")
														.datepicker(
																{
																	dateFormat : "DD d MM yy"
																});
											});
						</script>
						<form:label
							path="procedureInstance.globalVariablesValues['${field.name}']"
							cssClass="col-sm-3 control-label">${form.procedureModel.variables[field.name].label}</form:label>
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
						<form:label
							path="procedureInstance.globalVariablesValues['${field.name}']"
							cssClass="col-sm-3 control-label">${form.procedureModel.variables[field.name].label}</form:label>
						<div class="col-sm-9">
							<form:input
								path="procedureInstance.globalVariablesValues['${field.name}']"
								type="number" cssClass="form-control" />
						</div>
					</c:when>
					<c:when
						test="${(fieldType eq 'RADIOLIST') or (fieldType eq 'RADIOVOCAB')}">
						<form:label
							path="procedureInstance.globalVariablesValues['${field.name}']"
							cssClass="col-sm-3 control-label">${form.procedureModel.variables[field.name].label}</form:label>
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
						<form:label
							path="procedureInstance.globalVariablesValues['${field.name}']"
							cssClass="col-sm-3 control-label">${form.procedureModel.variables[field.name].label}</form:label>
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
							$JQry(document)
									.ready(
											function() {
												$JQry(
														"#selectVariable_${status.index}")
														.select2({
															theme : "bootstrap"
														});
											});
						</script>
						<form:label
							path="procedureInstance.globalVariablesValues['${field.name}']"
							cssClass="col-sm-3 control-label">${form.procedureModel.variables[field.name].label}</form:label>
						<div class="col-sm-9">
							<form:select
								path="procedureInstance.globalVariablesValues['${field.name}']"
								id="selectVariable_${status.index}"
								items="${form.procedureModel.variables[field.name].varOptions}" />
						</div>
					</c:when>
					<c:when test="${fieldType eq 'SELECTVOCAB'}">
						<portlet:resourceURL id="vocabularySearch"
							var="vocabularySearchUrl">
							<portlet:param name="vocabularyName"
								value="${form.procedureModel.variables[field.name].varOptions[0]}" />
						</portlet:resourceURL>
						<script type="text/javascript">
							select2Vocab("${vocabularySearchUrl}",
									"selectVariable_${status.index}");
						</script>
						<form:label
							path="procedureInstance.globalVariablesValues['${field.name}']"
							cssClass="col-sm-3 control-label">${form.procedureModel.variables[field.name].label}</form:label>
						<div class="col-sm-9">
							<form:select
								path="procedureInstance.globalVariablesValues['${field.name}']"
								id="selectVariable_${status.index}" class="form-control select2" />
						</div>
					</c:when>
					<c:when test="${fieldType eq 'SELECTVOCABMULTI'}">
						<portlet:resourceURL id="vocabularySearch"
							var="vocabularySearchUrl">
							<portlet:param name="vocabularyName"
								value="${form.procedureModel.variables[field.name].varOptions[0]}" />
						</portlet:resourceURL>
						<script type="text/javascript">
							select2Vocab("${vocabularySearchUrl}",
									"selectVariable_${status.index}",
									"${form.procedureModel.variables[field.name].varOptions[0]}");
						</script>
						<form:label
							path="procedureInstance.globalVariablesValues['${field.name}']"
							cssClass="col-sm-3 control-label">${form.procedureModel.variables[field.name].label}</form:label>
						<div class="col-sm-9">
							<form:select
								path="procedureInstance.globalVariablesValues['${field.name}']"
								id="selectVariable_${status.index}" multiple="true"
								class="form-control select2" />
						</div>
					</c:when>
					<c:when test="${fieldType eq 'FILE'}">
						<form:label
							path="procedureInstance.globalVariablesValues['${field.name}']"
							cssClass="col-sm-3 control-label">${form.procedureModel.variables[field.name].label}</form:label>
						<div class="col-sm-3">
							<input type="file" name="file:${field.name}" />
						</div>
						<div class="col-sm-6">
							<a
								href="${form.procedureInstance.filesPath[field.name].downloadLink}">${form.procedureInstance.filesPath[field.name].fileName}</a>
						</div>
					</c:when>
					<c:otherwise>1
				<p>error</p>
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
						<div class="col-sm-3"></div>
					</c:when>
					<c:when
						test="${(fieldType eq 'SELECTVOCAB') or (fieldType eq 'SELECTVOCABMULTI')}">
						<div class="col-sm-3">
							${form.procedureModel.variables[field.name].label} :</div>
						<div class="col-sm-3"></div>
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
			</c:otherwise>
		</c:choose>
	</div>
</div>

<div class="btn-group col-sm-2">
	<button type="button" class="btn btn-default" data-toggle="modal"
		data-target="#editFieldModal${field.name}">
		<i class="glyphicons glyphicons-edit"></i>
	</button>
	<button type="submit" name="deleteField" class="btn btn-default"
		onclick="selectPath(this,'selectedField')">
		<i class="glyphicons glyphicons-remove"></i>
	</button>
</div>

<c:forEach var="pathPart" items="${field.path}" varStatus="status">
	<c:set var="springPath" value="${status.first ? 'theSelectedStep' : springPath}.fields[${pathPart}]" scope="request"/>
</c:forEach>

<div class="modal fade" id="editFieldModal${field.name}" tabindex="-1" role="dialog" aria-labelledby="editFieldModalLabel">
	<div class="modal-dialog" role="document">
	  <div class="modal-content">
	    <div class="modal-header">
	      <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	      <h4 class="modal-title" id="editFieldModalLabel">Modifier un champ</h4>
	    </div>
	    <div class="modal-body">
	      <div class="form-group">
	          <form:label path="${springPath}.name" cssClass="col-sm-3 control-label">Nom</form:label>
	          <div class="col-sm-9">
	              <form:input path="${springPath}.name" type="text" cssClass="form-control" placeholder="Nom" />
	          </div>
	         </div>
	         <div class="form-group">
	          <form:label path="${springPath}.type" cssClass="col-sm-3 control-label">Type</form:label>
	          <div class="col-sm-9">
	              <form:select path="${springPath}.type" cssClass="form-control">
	                  <form:options/>
	              </form:select>
	          </div>
	         </div>
	        	<div class="form-group">
	          <form:label path="${springPath}.label" cssClass="col-sm-3 control-label">Label</form:label>
	          <div class="col-sm-9">
	              <form:input path="${springPath}.label" type="text" cssClass="form-control" placeholder="Label" />
	          </div>
	         </div>
	         <div class="form-group">
	          <form:label path="${springPath}.label" cssClass="col-sm-3 control-label">SuperLabel</form:label>
	          <div class="col-sm-9">
	              <form:input path="${springPath}.superLabel" type="text" cssClass="form-control" placeholder="SuperLabel" />
	          </div>
	         </div>
	         <div class="form-group">
	         	<form:label path="${springPath}.input" cssClass="col-sm-3 control-label">Saisissable</form:label>
	          <div class="col-sm-9">
	                 <form:checkbox path="${springPath}.input" cssClass="form-control"/>
	             </div>
	         </div>
	         <div class="form-group">
	          <form:label path="${springPath}.required" cssClass="col-sm-3 control-label">Requis</form:label>
	             <div class="col-sm-9">
	                 <form:checkbox path="${springPath}.required" cssClass="form-control"/>
	             </div>
	         </div>
	         <div class="form-group">
	          <form:label path="${springPath}.varOptions" cssClass="col-sm-3 control-label">Options</form:label>
	          <div class="col-sm-9">
	          	<form:input path="${springPath}.varOptions" type="text" cssClass="form-control" placeholder="Options" />
	          </div>
	      </div>
	    </div>
	    <div class="modal-footer">
	      <button type="button" class="btn btn-default" data-dismiss="modal">Annuler</button>
	      <button type="submit" name="editField" class="btn btn-primary" data-dismiss="modal" onclick="selector(this,'${field.path}','selectedField');hideModal();">Modifier</button>
	      </div>
	    </div>
	  </div>
</div>


<form:hidden path="${springPath}.path"/>
