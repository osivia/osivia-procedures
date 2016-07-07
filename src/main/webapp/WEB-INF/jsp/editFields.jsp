<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page import="org.osivia.services.procedure.portlet.model.Field" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>

<c:choose>
	<c:when test="${field.fieldSet eq true}">
		<div class="procedure-hover">
   		</div>
		<div class="col-sm-12">
   			<div class="panel panel-default">
	   			<div class="panel-heading">
	   				${field.superLabel}
	   			</div>
	   			<div class="panel-body">
	   				<ul class="procedure-sortable list-unstyled">
						<c:set var="fieldBkp" value="${field}" scope="page"/>
						<c:forEach var="nestedField" items="${fieldBkp.fields}" varStatus="status">
							<li class="form-group">
								<c:set var="field" value="${nestedField}" scope="request"/>
								<jsp:include page="editFields.jsp" />
							</li>
						</c:forEach>
					</ul>
					<div class="btn-group col-sm-2">
						<button type="button" class="btn btn-default" data-toggle="modal"
							data-target="#editFieldModal${fieldBkp.name}">
							<i class="glyphicons glyphicons-edit"></i>
						</button>
						<button type="submit" name="deleteField" class="btn btn-default"
							onclick="selectPath(this,'selectedField')">
							<i class="glyphicons glyphicons-remove-2"></i>
						</button>
					</div>
	   			</div>
   			</div>
   		</div>

		<c:forEach var="pathPart" items="${fieldBkp.path}" varStatus="status">
			<c:set var="springPathNested" value="${status.first ? 'theSelectedStep' : springPathNested}.fields[${pathPart}]" scope="request"/>
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
			          <form:label path="${springPathNested}.name" cssClass="col-sm-3 control-label">Nom</form:label>
			          <div class="col-sm-9">
			              <form:input path="${springPathNested}.name" type="text" cssClass="form-control" placeholder="Nom" />
			          </div>
			         </div>
			         <div class="form-group">
			          <form:label path="${springPathNested}.type" cssClass="col-sm-3 control-label">Type</form:label>
			          <div class="col-sm-9">
			              <form:select path="${springPathNested}.type" cssClass="form-control">
			                  <form:options/>
			              </form:select>
			          </div>
			         </div>
			        	<div class="form-group">
			          <form:label path="${springPathNested}.label" cssClass="col-sm-3 control-label">Label</form:label>
			          <div class="col-sm-9">
			              <form:input path="${springPathNested}.label" type="text" cssClass="form-control" placeholder="Label" />
			          </div>
			         </div>
			         <div class="form-group">
			          <form:label path="${springPathNested}.label" cssClass="col-sm-3 control-label">SuperLabel</form:label>
			          <div class="col-sm-9">
			              <form:input path="${springPathNested}.superLabel" type="text" cssClass="form-control" placeholder="SuperLabel" />
			          </div>
			         </div>
			         <div class="form-group">
			         	<form:label path="${springPathNested}.input" cssClass="col-sm-3 control-label">Saisissable</form:label>
			          <div class="col-sm-9">
			                 <form:checkbox path="${springPathNested}.input" cssClass="form-control"/>
			             </div>
			         </div>
			         <div class="form-group">
			          <form:label path="${springPathNested}.required" cssClass="col-sm-3 control-label">Requis</form:label>
			             <div class="col-sm-9">
			                 <form:checkbox path="${springPathNested}.required" cssClass="form-control"/>
			             </div>
			         </div>
			         <div class="form-group">
			          <form:label path="${springPathNested}.varOptions" cssClass="col-sm-3 control-label">Options</form:label>
			          <div class="col-sm-9">
			          	<form:input path="${springPathNested}.varOptions" type="text" cssClass="form-control" placeholder="Options" />
			          </div>
			      </div>
			    </div>
			    <div class="modal-footer">
			      <button type="button" class="btn btn-default" data-dismiss="modal">Annuler</button>
			      <button type="submit" name="editField" data-dismiss="modal" class="btn btn-primary" onclick="selector(this,'${field.path}','selectedField');hideModal(this);">Modifier</button>
			      </div>
			    </div>
			  </div>
		</div>
		
		<form:hidden path="${springPathNested}.path"/>
	</c:when>
	<c:otherwise>
		<jsp:include page="editField.jsp" />
	</c:otherwise>
</c:choose>
