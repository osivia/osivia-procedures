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

<portlet:resourceURL id="groupSearch" var="groupSearchUrl" ></portlet:resourceURL>
<script type="text/javascript">
initGroupSelect("${groupSearchUrl}");
</script>

<portlet:actionURL name="editStep" var="editStepUrl">
</portlet:actionURL>

<form:form modelAttribute="form" action="${editStepUrl}" method="post" cssClass="form-horizontal" role="form">

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Identification</h3>
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <form:label path="theSelectedStep.stepName" cssClass="col-sm-2 control-label">Titre de l'�tape</form:label>
                    <div class="col-sm-10">
                        <form:input path="theSelectedStep.stepName" type="text" cssClass="form-control" />
                    </div>
                </div>
                <div class="form-group">
                    <form:label path="theSelectedStep.reference" cssClass="col-sm-2 control-label">R�ference de l'�tape</form:label>
                    <div class="col-sm-10">
                        <form:input path="theSelectedStep.reference" type="text" cssClass="form-control" />
                    </div>
                </div>
            </div>
        </div>
        
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Champs compl�mentaires</h3>
            </div>
            <div class="panel-body">
            	<div id="procedure-sortable">
			        <ul class="procedure-sortable list-unstyled">
			             <c:forEach var="field" items="${form.theSelectedStep.fields}" varStatus="status">
			             	<li class="form-group">
			             		<div class="col-sm-1 sortable-handle">
			                        <i class="glyphicons glyphicons-sorting pull-right"></i>
			                    </div>
	                    
		                       	<c:choose>
			                      <c:when test="${field.fieldSet eq true}">
			                      	<c:set var="field" value="${field}" scope="request"/>
			                      	<jsp:include page="editFields.jsp"/>
			                      </c:when>
			                      <c:otherwise>
			                      	<c:set var="field" value="${field}" scope="request"/>
			                      	<jsp:include page="editField.jsp"/>
			                      </c:otherwise>
		                      	</c:choose>
			                    
			                    <div class="modal fade" id="editFieldModal${field.name}" tabindex="-1" role="dialog" aria-labelledby="editFieldModalLabel">
			                      <div class="modal-dialog" role="document">
			                        <div class="modal-content">
			                          <div class="modal-header">
			                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
			                            <h4 class="modal-title" id="editFieldModalLabel">Modifier un champ</h4>
			                          </div>
			                          <div class="modal-body">
			                            <div class="form-group">
			                                <form:label path="theSelectedStep.fields[${status.index}].name" cssClass="col-sm-3 control-label">Nom</form:label>
			                                <div class="col-sm-9">
			                                    <form:input path="theSelectedStep.fields[${status.index}].name" type="text" cssClass="form-control" placeholder="Nom" />
			                                </div>
		                                </div>
		                                <div class="form-group">
			                                <form:label path="theSelectedStep.fields[${status.index}].type" cssClass="col-sm-3 control-label">Type</form:label>
			                                <div class="col-sm-9">
			                                    <form:select path="theSelectedStep.fields[${status.index}].type" cssClass="form-control">
			                                        <form:options/>
			                                    </form:select>
			                                </div>
		                                </div>
		                               	<div class="form-group">
			                                <form:label path="theSelectedStep.fields[${status.index}].label" cssClass="col-sm-3 control-label">Label</form:label>
			                                <div class="col-sm-9">
			                                    <form:input path="theSelectedStep.fields[${status.index}].label" type="text" cssClass="form-control" placeholder="Label" />
			                                </div>
		                                </div>
		                                <div class="form-group">
			                                <form:label path="theSelectedStep.fields[${status.index}].label" cssClass="col-sm-3 control-label">SuperLabel</form:label>
			                                <div class="col-sm-9">
			                                    <form:input path="theSelectedStep.fields[${status.index}].superLabel" type="text" cssClass="form-control" placeholder="SuperLabel" />
			                                </div>
		                                </div>
		                                <div class="form-group">
		                                	<form:label path="theSelectedStep.fields[${status.index}].input" cssClass="col-sm-3 control-label">Saisissable</form:label>
			                                <div class="col-sm-9">
		                                        <form:checkbox path="theSelectedStep.fields[${status.index}].input" cssClass="form-control"/>
		                                    </div>
		                                </div>
		                                <div class="form-group">
			                                <form:label path="theSelectedStep.fields[${status.index}].required" cssClass="col-sm-3 control-label">Requis</form:label>
		                                    <div class="col-sm-9">
		                                        <form:checkbox path="theSelectedStep.fields[${status.index}].required" cssClass="form-control"/>
		                                    </div>
		                                </div>
		                                <div class="form-group">
			                                <form:label path="theSelectedStep.fields[${status.index}].varOptions" cssClass="col-sm-3 control-label">Options</form:label>
			                                <div class="col-sm-9">
			                                	<form:input path="theSelectedStep.fields[${status.index}].varOptions" type="text" cssClass="form-control" placeholder="Options" />
			                                </div>
			                            </div>
			                          </div>
			                          <div class="modal-footer">
			                            <button type="button" class="btn btn-default" data-dismiss="modal">Annuler</button>
			                            <button type="submit" name="editField" class="btn btn-primary" onclick="selector(this,'${field.name}','selectedField')">Modifier</button>
			                          </div>
			                        </div>
			                      </div>
			                    </div>
			                    
			             	</li>
			             </c:forEach>
			             <li class="form-group procedure-sortable"/></li>
			             <form:input path="selectedStep" type="hidden" name="selectedStep"/>
			        </ul>
		        </div>
        
	            <div class="modal fade" id="addFieldModal" tabindex="-1" role="dialog" aria-labelledby="addFieldModalLabel">
	              <div class="modal-dialog" role="document">
	                <div class="modal-content">
	                  <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                    <h4 class="modal-title" id="addFieldModalLabel">Ajouter un champ</h4>
	                  </div>
	                  <div class="modal-body">
	                    <div class="form-group">
	                        <form:label path="newField.variableName" cssClass="col-sm-3 control-label">Nom</form:label>
	                        <div class="col-sm-9">
	                            <form:input path="newField.variableName" type="text" cssClass="form-control" placeholder="Nom" />
	                        </div>
                        </div>
                        <div class="form-group">
	                        <form:label path="newField.type" cssClass="col-sm-3 control-label">Type</form:label>
	                        <div class="col-sm-9">
	                            <form:select path="newField.type" cssClass="form-control">
	                                <form:options/>
	                            </form:select>
	                        </div>
                        </div>
                        <div class="form-group">
	                        <form:label path="newField.label" cssClass="col-sm-3 control-label">Label</form:label>
	                        <div class="col-sm-9">
	                            <form:input path="newField.label" type="text" cssClass="form-control" placeholder="Label" />
	                        </div>
                        </div>
                        <div class="form-group">
	                        <form:label path="newField.varOptions" cssClass="col-sm-3 control-label">Options</form:label>
	                        <div class="col-sm-9">
	                        	<form:input path="newField.varOptions" type="text" cssClass="form-control" placeholder="Options" />
	                        </div>
	                    </div>
	                  </div>
	                  <div class="modal-footer">
	                    <button type="button" class="btn btn-default" data-dismiss="modal">Annuler</button>
	                    <button type="submit" name="addField" class="btn btn-primary">Ajouter</button>
	                  </div>
	                </div>
	              </div>
	            </div>
        	
	        	<div class="modal fade" id="addFieldSetModal" tabindex="-1" role="dialog" aria-labelledby="addFielSetdModalLabel">
	              <div class="modal-dialog" role="document">
	                <div class="modal-content">
	                  <div class="modal-header">
	                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	                    <h4 class="modal-title" id="addFieldSetModalLabel">Ajouter un FieldSet</h4>
	                  </div>
	                  <div class="modal-body">
	                       <div class="form-group">
	                        <label class="col-sm-3 control-label" for="newFieldSetLabel">Label</label>
	                        <div class="col-sm-9">
	                        	<input name="newFieldSetLabel" type="text" class="form-control" placeholder="Label">
	                        </div>
	                       </div>
	                  </div>
	                  <div class="modal-footer">
	                    <button type="button" class="btn btn-default" data-dismiss="modal">Annuler</button>
	                    <button type="submit" name="addFieldSet" class="btn btn-primary">Ajouter</button>
	                  </div>
	                </div>
	              </div>
	            </div>
            
           </div>
            
            <div class="panel-footer">
                <div class="form-group">
                    <div class="col-sm-11">
                        <button type="button" class="btn btn-default" data-toggle="modal" data-target="#addFieldModal">Ajouter un champ</button>
                        <button type="button" class="btn btn-default" data-toggle="modal" data-target="#addFieldSetModal">Ajouter un FieldSet</button>
                    </div>
                </div>
            </div>
        </div>
        
	        <div class="panel panel-default">
	            <div class="panel-heading">
	                <h3 class="panel-title">Actions</h3>
	            </div>
	            <div class="panel-body">
	                <ul class="list-unstyled">
	                    <li class="form-group">
	                       <div class="col-sm-2">
	                           <label class="control-label">Label</label>
	                       </div>
	                       <div class="col-sm-10">
	                           <label class="control-label">R�f�rence de l'�tape cible</label>
	                       </div>
	                    </li>
	                    <c:forEach var="action" items="${form.theSelectedStep.actions}" varStatus="status">
	                    	
	                    	<portlet:renderURL var="editActionUrl" >
	                    		<portlet:param name="editAction" value="${status.index}"/>
	                    	</portlet:renderURL>
	                    
	                        <li class="form-group">
	                            <div class="col-sm-2">
	                                <form:input path="theSelectedStep.actions[${status.index}].label" type="text" cssClass="form-control" placeholder="Label" />
	                            </div>
	                            <div class="col-sm-8">
	                                <form:input path="theSelectedStep.actions[${status.index}].stepReference" type="text" cssClass="form-control" placeholder="stepReference" />
	                            </div>
	                            <div class="btn-group col-sm-2">
	                            	<a class="btn btn-default" href="${editActionUrl}">
		                                <i class="glyphicons glyphicons-edit"></i>
	                            	</a>
	                                <button type="submit" name="deleteButton" class="btn btn-default" onclick="selector(this,'${status.index}','selectedButton')">
	                                    <i class="glyphicons glyphicons-remove-2"></i>
	                                </button>
	                            </div>
	                        </li>
	                    </c:forEach>
	                </ul>
	            </div>
	            <div class="panel-footer">
	                <div class="form-group">
	                    <div class="col-sm-11">
	                        <button type="submit" name="addButton" class="btn btn-default">Ajouter un bouton</button>
	                    </div>
	                </div>
	            </div>
	        </div>
        
        
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Droits d'acc�s</h3>
            </div>
            <div class="panel-body">
            	<ul class="list-unstyled">
            		<li class="form-group">
           				<form:label path="theSelectedStep.groups" cssClass="col-sm-2 control-label">Utilisateurs ou groupes</form:label>
           				<div class="col-sm-10">
                        	<form:select path="theSelectedStep.groups" multiple="multiple" class="groupSelect-select2 form-control select2">
                        		<form:options items="${form.theSelectedStep.groups}" />
                        	</form:select>
	                    </div>
           			</li>
            	</ul>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-1">
                <button type="submit" class="btn btn-primary" name="saveStep">Sauvegarder</button>
            </div>
            <div class="col-sm-1 pull-right">
                <button type="submit" class="btn btn-danger pull-right" name="deleteStep">Supprimer</button>
            </div>
            <input type="submit" class="hidden" name="updateForm">
        </div>
</form:form>