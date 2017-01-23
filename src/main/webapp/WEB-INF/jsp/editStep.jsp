<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<portlet:defineObjects />

<!-- Datepicker language -->
<c:set var="datepickerLanguage" value="${fn:toLowerCase(pageContext.response.locale.language)}" />
<c:if test="${'en' ne datepickerLanguage}">
    <script type="text/javascript" src="/osivia-portal-custom-web-assets/components/jquery-ui/i18n/datepicker-${datepickerLanguage}.js"></script>
</c:if>

<portlet:resourceURL id="groupSearch" var="groupSearchUrl" ></portlet:resourceURL>

<portlet:resourceURL id="fieldSearch" var="fieldSearchUrl" ></portlet:resourceURL>

<c:if test="${!form.advancedMode}">
    <portlet:resourceURL id="stepSearch" var="stepSearchUrl" ></portlet:resourceURL>
</c:if>

<portlet:actionURL name="editStep" var="editStepUrl">
</portlet:actionURL>

<div class="content-navbar">
    <!-- Breadcrumb -->
    <div class="content-navbar-breadcrumb">
        <div id="breadcrumb">
        	<div class="">
				<nav>
				    <ol class="breadcrumb hidden-xs">
			            <li>
				            <portlet:actionURL name="editStep" var="cancelStepUrl">
				            	<portlet:param name="cancelStep" value="toProc"/>
							</portlet:actionURL>
							<a href="${cancelStepUrl}">Édition d'une procédure</a>
			            </li>
			            <li><a>Édition d'une étape</a></li>
				    </ol>
				</nav>
			</div>
		</div>
    </div>
</div>


<form:form modelAttribute="form" action="${editStepUrl}" method="post" cssClass="form-horizontal" role="form">

    <div class="form-group">
        <div class="col-sm-12">
            <div class="btn-group pull-right">
                <c:if test="${!form.advancedMode}">
                    <button type="submit" class="btn btn-default" name="changeMode">Mode avançé</button>
                    <button type="submit" class="btn btn-info active" name="changeMode">Mode simplifié</button>
                </c:if>
                <c:if test="${form.advancedMode}">
                    <button type="submit" class="btn btn-info active" name="changeMode">Mode avançé</button>
                    <button type="submit" class="btn btn-default" name="changeMode">Mode simplifié</button>
                </c:if>
            </div>
        </div>
    </div>
    
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="<c:if test="${empty activeTab or ('form' ne activeTab && 'action' ne activeTab)}">active</c:if>"><a href="#Identification" role="tab" data-toggle="tab" class="no-ajax-link">Identification</a></li>
        <li role="presentation" class="<c:if test="${'form' eq activeTab}">active</c:if>"><a href="#Formulaire" role="tab" data-toggle="tab" class="no-ajax-link">Formulaire</a></li>
        <li role="presentation" class="<c:if test="${'action' eq activeTab}">active</c:if>"><a href="#Actions" role="tab" data-toggle="tab" class="no-ajax-link">Actions</a></li>
        <li role="presentation"><a href="#Métadonnées" role="tab" data-toggle="tab" class="no-ajax-link">Métadonnées</a></li>
    </ul>

    <div class="tab-content">
	    <div role="tabpanel" class="tab-pane <c:if test="${empty activeTab or ('form' ne activeTab && 'action' ne activeTab)}">active</c:if>" id="Identification">
	       <div class="form-group">
               <form:label path="theSelectedStep.stepName" cssClass="col-sm-2 control-label">Titre de l'étape</form:label>
               <div class="col-sm-10">
                   <form:input path="theSelectedStep.stepName" type="text" cssClass="form-control" />
               </div>
           </div>
           <div class="form-group">
               <form:label path="theSelectedStep.reference" cssClass="col-sm-2 control-label">Réference de l'étape</form:label>
               <div class="col-sm-10">
                   <form:input path="theSelectedStep.reference" type="text" cssClass="form-control" />
               </div>
           </div>
	    </div>
	    <div role="tabpanel" class="tab-pane <c:if test="${'form' eq activeTab}">active</c:if>" id="Formulaire">
	       <div class="row">
	           <div class="col-sm-4">
                   <ul class="nav nav-tabs" role="tablist">
                        <li role="presentation" class="<c:if test="${empty activeFormTab or 'edit' ne activeFormTab}">active</c:if>"><a href="#CreateField" role="tab" data-toggle="tab" class="no-ajax-link">Ajouter un champ</a></li>
                        <li role="presentation"><a href="#CreateFieldset" role="tab" data-toggle="tab" class="no-ajax-link">Ajouter un Fieldset</a></li>
		                <c:if test="${not empty form.selectedField}">
		                    <li role="presentation" class="<c:if test="${'edit' eq activeFormTab}">active</c:if>"><a href="#Edit" role="tab" data-toggle="tab" class="no-ajax-link">Édition</a></li>
		                </c:if>
                   </ul>
                   <div class="tab-content">
                        <div role="tabpanel" class="tab-pane <c:if test="${empty activeFormTab or 'edit' ne activeFormTab}">active</c:if>" id="CreateField">
                            <div class="form-group">
	                            <form:label path="newField.variableName" cssClass="col-sm-3 control-label">Nom</form:label>
	                            <div class="col-sm-9">
	                                <form:select path="newField.variableName" class="fieldSelect-select2 form-control select2" cssStyle="width: 100%;" data-url="${fieldSearchUrl}">
                                    </form:select>
                                    <span class="help-block">Le nom ne doit pas contenir d'espace.</span>
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <form:label path="newField.label" cssClass="col-sm-3 control-label">Label</form:label>
	                            <div class="col-sm-9">
	                                <form:input path="newField.label" type="text" cssClass="form-control" placeholder="Label" />
	                                <span class="help-block">Le label tel qu'il apparaîtra à l'utilisateur.</span>
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <form:label path="newField.helpText" cssClass="col-sm-3 control-label">Message d'aide</form:label>
	                            <div class="col-sm-9">
	                                <form:input path="newField.helpText" type="text" cssClass="form-control" placeholder="Label" />
	                                <span class="help-block">Un texte court destiné à aider l'utilisateur.</span>
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
	                        <div class="form-group hidden">
	                            <form:label path="newField.varOptions" cssClass="col-sm-3 control-label">Options</form:label>
	                            <div class="col-sm-9">
	                                <form:input path="newField.varOptions" type="text" cssClass="form-control" placeholder="Options" />
	                            </div>
	                        </div>
	                        <div class="form-group hidden" id="formulaire-newField-list-editor">
	                        	<label class="col-sm-3 control-label">Édition des options</label>
		                        <div class="col-sm-9">
		                        	<div class="form-group">
	                        			<label for="formulaire-newField-list-editor-newOption-label" class="col-sm-3 control-label">Label</label>
		                        		<div class="col-sm-9">
											<input type="text" class="form-control" id="formulaire-newField-list-editor-newOption-label" placeholder="Label">
		                        		</div>
		                        	</div>
		                        	<div class="form-group">
	                        			<label for="formulaire-newField-list-editor-newOption-value" class="col-sm-3 control-label">Valeur</label>
		                        		<div class="col-sm-9">
											<input type="text" class="form-control" id="formulaire-newField-list-editor-newOption-value" placeholder="Valeur">
		                        		</div>
		                        	</div>
		                        	<div class="form-group">
		                        		<div class="col-sm-12">
		                        			<button id="formulaire-newField-list-editor-addOption" class="btn btn-default pull-right" type="button">Ajouter une option</button>
		                        		</div>
		                        	</div>
		                        	<div id="formulaire-newField-list-editor-optionList" class="form-group">
			                        	<table class="table table-condensed">
			                        		<thead>
			                        			<tr>
			                        				<th>Label</th>
			                        				<th>Valeur</th>
			                        				<th></th>
			                        			</tr>
			                        		</thead>
			                        		<tbody>
			                        		
			                        		</tbody>
			                        	</table>
		                        	</div>
		                        </div>
	                        </div>
	                        <div class="form-group">
	                           <div class="col-sm-offset-3 col-sm-9">
	                               <div class="checkbox">
	                                    <label>
	                                       <form:checkbox path="newField.input"/><span>Saisissable</span>
	                                    </label>
	                                </div>
	                            </div>
	                            <div class="col-sm-offset-3 col-sm-9">
                                   <div class="checkbox">
                                        <label>
                                            <form:checkbox path="newField.required"/><span>Requis</span>
                                        </label>
                                    </div>
                                </div>
                            </div>
                        
                            <button type="submit" name="addField" class="btn btn-default pull-right">Ajouter</button>
                        
                        </div>
                        <div role="tabpanel" class="tab-pane" id="CreateFieldset">
                            <div class="form-group">
                                <form:label path="newFieldSet.variableName" cssClass="col-sm-3 control-label">Nom</form:label>
                                <div class="col-sm-9">
                                    <form:input path="newFieldSet.variableName" type="text" cssClass="form-control" placeholder="Nom" />
                                </div>
                            </div>
                            <div class="form-group">
                                <form:label path="newFieldSet.label" cssClass="col-sm-3 control-label">Label</form:label>
                                <div class="col-sm-9">
                                    <form:input path="newFieldSet.label" type="text" cssClass="form-control" placeholder="Label" />
                                </div>
                            </div>
                            <button type="submit" name="addFieldSet" class="btn btn-default pull-right">Ajouter</button>
                        
                        </div>
                        <c:if test="${not empty form.selectedField}">
                            <div role="tabpanel" class="tab-pane <c:if test="${'edit' eq activeFormTab}">active</c:if>" id="Edit">
                                <c:if test="${form.selectedField.fieldSet eq true}">
                                    <div class="form-group">
                                      <form:label path="selectedField.name" cssClass="col-sm-3 control-label">Nom</form:label>
                                      <div class="col-sm-9">
                                          <form:input path="selectedField.name" type="text" cssClass="form-control" placeholder="Nom" disabled="true"/>
                                      </div>
                                     </div>
                                    <div class="form-group">
		                                  <form:label path="selectedField.label" cssClass="col-sm-3 control-label">Label</form:label>
		                                  <div class="col-sm-9">
		                                      <form:input path="selectedField.label" type="text" cssClass="form-control" placeholder="Label" />
		                                      <span class="help-block">Le label tel qu'il apparaîtra à l'utilisateur.</span>
		                                  </div>
		                             </div>
		                             <div class="form-group">
			                            <form:label path="selectedField.helpText" cssClass="col-sm-3 control-label">Message d'aide</form:label>
			                            <div class="col-sm-9">
			                                <form:input path="selectedField.helpText" type="text" cssClass="form-control" placeholder="Label" />
			                                <span class="help-block">Un texte court destiné à aider l'utilisateur.</span>
			                            </div>
			                        </div>
                                </c:if>
                                <c:if test="${form.selectedField.fieldSet ne true}">
	                               <div class="form-group">
				                      <form:label path="selectedField.name" cssClass="col-sm-3 control-label">Nom</form:label>
				                      <div class="col-sm-9">
				                          <form:input path="selectedField.name" type="text" cssClass="form-control" placeholder="Nom" />
				                      </div>
			                         </div>
			                        <div class="form-group">
					                      <form:label path="selectedField.label" cssClass="col-sm-3 control-label">Label</form:label>
					                      <div class="col-sm-9">
					                          <form:input path="selectedField.label" type="text" cssClass="form-control" placeholder="Label" />
					                          <span class="help-block">Le label tel qu'il apparaîtra à l'utilisateur.</span>
					                      </div>
				                     </div>
				                     <div class="form-group">
			                            <form:label path="selectedField.helpText" cssClass="col-sm-3 control-label">Message d'aide</form:label>
			                            <div class="col-sm-9">
			                                <form:input path="selectedField.helpText" type="text" cssClass="form-control" placeholder="Label" />
			                                <span class="help-block">Un texte court destiné à aider l'utilisateur.</span>
			                            </div>
			                        </div>
				                     <div class="form-group">
					                      <form:label path="selectedField.type" cssClass="col-sm-3 control-label">Type</form:label>
					                      <div class="col-sm-9">
					                          <form:select path="selectedField.type" cssClass="form-control">
					                              <form:options/>
					                          </form:select>
					                      </div>
				                     </div>
				                     <div class="form-group hidden">
					                      <form:label path="selectedField.varOptions" cssClass="col-sm-3 control-label">Options</form:label>
					                      <div class="col-sm-9">
					                        <form:input path="selectedField.varOptions" type="text" cssClass="form-control" placeholder="Options" />
					                      </div>
					                  </div>
					                  
					                  <div class="form-group hidden" id="formulaire-selectedField-list-editor">
			                        	<label class="col-sm-3 control-label">Édition des options</label>
				                        <div class="col-sm-9">
				                        	<div class="form-group">
			                        			<label for="formulaire-selectedField-list-editor-newOption-label" class="col-sm-3 control-label">Label</label>
				                        		<div class="col-sm-9">
													<input type="text" class="form-control" id="formulaire-selectedField-list-editor-newOption-label" placeholder="Label">
				                        		</div>
				                        	</div>
				                        	<div class="form-group">
			                        			<label for="formulaire-selectedField-list-editor-newOption-value" class="col-sm-3 control-label">Valeur</label>
				                        		<div class="col-sm-9">
													<input type="text" class="form-control" id="formulaire-selectedField-list-editor-newOption-value" placeholder="Valeur">
				                        		</div>
				                        	</div>
				                        	<div class="form-group">
				                        		<div class="col-sm-12">
				                        			<button id="formulaire-selectedField-list-editor-addOption" class="btn btn-default pull-right" type="button">Ajouter une option</button>
				                        		</div>
				                        	</div>
				                        	<div id="formulaire-selectedField-list-editor-optionList" class="form-group">
					                        	<table class="table table-condensed">
					                        		<thead>
					                        			<tr>
					                        				<th>Label</th>
					                        				<th>Valeur</th>
					                        				<th></th>
					                        			</tr>
					                        		</thead>
					                        		<tbody>
					                        		
					                        		</tbody>
					                        	</table>
				                        	</div>
				                        </div>
			                        </div>
                                      <div class="form-group">
			                               <div class="col-sm-offset-3 col-sm-9">
			                                   <div class="checkbox">
			                                        <label>
			                                             <form:checkbox path="selectedField.input"/><span>Saisissable</span>
			                                        </label>
			                                    </div>
			                                </div>
			                                <div class="col-sm-offset-3 col-sm-9">
			                                   <div class="checkbox">
			                                        <label>
			                                             <form:checkbox path="selectedField.required"/><span>Requis</span>
			                                        </label>
			                                    </div>
			                                </div>
			                            </div>
                                </c:if>
			                  <div class="pull-right">
	                               <button type="submit" name="editField" class="btn btn-default">Modifier</button>
	                               <button type="submit" name="deleteField" class="btn btn-default">
	                                    <i class="glyphicons glyphicons-bin"></i>
	                                </button>
		                       </div>
                            </div>
                        </c:if>
                   
                   </div>
	           </div>
	           
		       <div id="procedure-sortable" class="col-sm-8">
	               <ul class="procedure-sortable list-unstyled">
	                   <c:forEach var="field" items="${form.theSelectedStep.fields}" varStatus="status">
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
	                   </c:forEach>
	                   <form:input path="selectedStep" type="hidden" name="selectedStep"/>
	               </ul>
	           </div>
            </div>
	    </div>
	    <div role="tabpanel" class="tab-pane <c:if test="${'action' eq activeTab}">active</c:if>" id="Actions">
	       <ul class="list-unstyled">
	              <li class="form-group">
	                 <div class="col-sm-2">
	                     <label class="control-label">Label</label>
	                 </div>
	                 <div class="col-sm-4">
	                     <label class="control-label">Identifiant de l'action</label>
	                 </div>
	                 <div class="col-sm-4">
	                     <label class="control-label">Étape cible</label>
	                 </div>
	              </li>
	              <c:forEach var="action" items="${form.theSelectedStep.actions}" varStatus="status">
	                  <li class="form-group">
	                      <div class="col-sm-2">
	                          <form:input path="theSelectedStep.actions[${status.index}].label" type="text" cssClass="form-control" placeholder="Label" />
	                      </div>
	                      <div class="col-sm-4">
	                          <form:input path="theSelectedStep.actions[${status.index}].actionId" type="text" cssClass="form-control" placeholder="actionId" />
	                      </div>
	                      <c:if test="${form.advancedMode}">
		                      <div class="col-sm-4">
		                          <form:input path="theSelectedStep.actions[${status.index}].stepReference" type="text" cssClass="form-control" placeholder="stepReference" />
		                      </div>
	                      </c:if>
	                      <c:if test="${!form.advancedMode}">
	                           <div class="col-sm-4">
			                        <form:select path="theSelectedStep.actions[${status.index}].stepReference" class="stepSelect-select2 form-control select2" cssStyle="width: 100%;" data-url="${stepSearchUrl}">
			                           <form:option value="${form.theSelectedStep.actions[status.index].stepReference}" />
			                        </form:select>
		                        </div>
	                       </c:if>
	                      
	                      <div class="btn-group col-sm-2">
	                          <button type="submit" name="editButton" class="btn btn-default" onclick="selector(this,'${status.index}','selectedButton')">
	                              <i class="glyphicons glyphicons-edit"></i>
	                          </button>
	                          <button type="submit" name="deleteButton" class="btn btn-default" onclick="selector(this,'${status.index}','selectedButton')">
	                              <i class="glyphicons glyphicons-remove-2"></i>
	                          </button>
	                      </div>
	                  </li>
	              </c:forEach>
	          </ul>
           <div class="form-group">
		       <div class="col-sm-1">
	               <button type="submit" name="addButton" class="btn btn-default">Ajouter une action</button>
	           </div>
           </div>
	    </div>
	    <div role="tabpanel" class="tab-pane" id="Métadonnées">
		    <c:if test="${form.advancedMode}">
		       <div class="form-group">
		           <div class="col-sm-offset-2 col-sm-1">
			           <div class="checkbox">
		                    <label>
		                       <form:checkbox path="theSelectedStep.notifiable"/><span>notifiable</span>
		                    </label>
		                </div>
	                </div>
	                <div class="col-sm-1">
		                <div class="checkbox">
		                    <label>
		                       <form:checkbox path="theSelectedStep.acquitable"/><span>acquitable</span>
		                    </label>
		                </div>
	                </div>
		           <div class="col-sm-1">
			           <div class="checkbox">
			                <label>
			                      <form:checkbox path="theSelectedStep.closable"/><span>fermable</span>
			                </label>
		                </div>
	                </div>
		       </div>
	           <div class="form-group">
	               <form:label path="theSelectedStep.actionIdClosable" cssClass="col-sm-2 control-label">Identifiant de l'action fermable</form:label>
	               <div class="col-sm-10">
	                   <form:input path="theSelectedStep.actionIdClosable" type="text" cssClass="form-control" />
	               </div>
	           </div>
	           <div class="form-group">
	               <form:label path="theSelectedStep.stringMsg" cssClass="col-sm-2 control-label">Message de notification</form:label>
	               <div class="col-sm-10">
	                   <form:input path="theSelectedStep.stringMsg" type="text" cssClass="form-control" />
	               </div>
	           </div>
	           <div class="form-group">
	               <form:label path="theSelectedStep.actionIdYes" cssClass="col-sm-2 control-label">Identifiant de l'action associé au oui</form:label>
	               <div class="col-sm-10">
	                   <form:input path="theSelectedStep.actionIdYes" type="text" cssClass="form-control" />
	               </div>
	           </div>
	           <div class="form-group">
	               <form:label path="theSelectedStep.actionIdNo" cssClass="col-sm-2 control-label">Identifiant de l'action associé au non</form:label>
	               <div class="col-sm-10">
	                   <form:input path="theSelectedStep.actionIdNo" type="text" cssClass="form-control" />
	               </div>
	           </div>
	           <div class="form-group">
	               <form:label path="theSelectedStep.actionIdDefault" cssClass="col-sm-2 control-label">Identifiant de l'action par défaut</form:label>
	               <div class="col-sm-10">
	                   <form:input path="theSelectedStep.actionIdDefault" cssClass="form-control" />
	               </div>
	           </div>
		    </c:if>
            <div class="form-group">
                <form:label path="theSelectedStep.actors" cssClass="col-sm-2 control-label">Acteurs</form:label>
                <div class="col-sm-10">
                    <form:select path="theSelectedStep.actors" multiple="multiple" class="groupSelect-select2 form-control select2" cssStyle="width: 100%;" data-url="${groupSearchUrl}">
                        <form:options items="${form.theSelectedStep.actors}" />
                    </form:select>
                </div>
            </div>
            <div class="form-group">
                <form:label path="theSelectedStep.endStepMsg" cssClass="col-sm-2 control-label">Message de fin d'étape</form:label>
                <div class="col-sm-10">
                    <form:input path="theSelectedStep.endStepMsg" cssClass="form-control" />
                </div>
            </div>
	    </div>
	</div>
    <hr>
    <button type="submit" class="btn btn-default" name="cancelStep">Annuler</button>
	<button type="submit" class="btn btn-primary" name="saveStep">Sauvegarder</button>
	<button type="submit" class="btn btn-danger pull-right" name="deleteStep">Supprimer</button>
	<input type="submit" class="hidden" name="updateForm">
	<input type="submit" class="hidden" name="selectField">
</form:form>