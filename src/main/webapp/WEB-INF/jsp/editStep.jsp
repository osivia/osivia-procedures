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
	                                <form:input path="newField.variableName" type="text" cssClass="form-control" placeholder="Nom" />
	                            </div>
	                        </div>
	                        <div class="form-group">
	                            <form:label path="newField.label" cssClass="col-sm-3 control-label">Label</form:label>
	                            <div class="col-sm-9">
	                                <form:input path="newField.label" type="text" cssClass="form-control" placeholder="Label" />
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
                               <form:label path="newField.input" cssClass="col-sm-3 control-label">Saisissable</form:label>
                                 <div class="col-sm-9">
                                        <form:checkbox path="newField.input" cssClass="form-control"/>
                                    </div>
                            </div>
                            <div class="form-group">
                                 <form:label path="newField.required" cssClass="col-sm-3 control-label">Requis</form:label>
                                    <div class="col-sm-9">
                                        <form:checkbox path="newField.required" cssClass="form-control"/>
                                    </div>
                            </div>
	                        <div class="form-group">
	                            <form:label path="newField.varOptions" cssClass="col-sm-3 control-label">Options</form:label>
	                            <div class="col-sm-9">
	                                <form:input path="newField.varOptions" type="text" cssClass="form-control" placeholder="Options" />
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
                                          <form:input path="selectedField.name" type="text" cssClass="form-control" placeholder="Nom" />
                                      </div>
                                     </div>
                                    <div class="form-group">
		                                  <form:label path="selectedField.label" cssClass="col-sm-3 control-label">Label</form:label>
		                                  <div class="col-sm-9">
		                                      <form:input path="selectedField.superLabel" type="text" cssClass="form-control" placeholder="Label" />
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
				                     <div class="form-group">
		                                <form:label path="selectedField.input" cssClass="col-sm-3 control-label">Saisissable</form:label>
					                      <div class="col-sm-9">
					                             <form:checkbox path="selectedField.input" cssClass="form-control"/>
					                         </div>
				                     </div>
				                     <div class="form-group">
					                      <form:label path="selectedField.required" cssClass="col-sm-3 control-label">Requis</form:label>
					                         <div class="col-sm-9">
					                             <form:checkbox path="selectedField.required" cssClass="form-control"/>
					                         </div>
				                     </div>
				                     <div class="form-group">
					                      <form:label path="selectedField.varOptions" cssClass="col-sm-3 control-label">Options</form:label>
					                      <div class="col-sm-9">
					                        <form:input path="selectedField.varOptions" type="text" cssClass="form-control" placeholder="Options" />
					                      </div>
					                  </div>
                                </c:if>
			                  <div class="row">
		                            <div class="col-sm-1 pull-right">
		                               <button type="submit" name="deleteField" class="btn btn-default pull-right">
		                                    <i class="glyphicons glyphicons-bin"></i>
		                                </button>
		                            </div>
		                            <div class="col-sm-1 pull-right">
		                               <button type="submit" name="editField" class="btn btn-default pull-right">Modifier</button>
		                           </div>
		                       </div>
                            </div>
                        </c:if>
                   
                   </div>
	           </div>
	           
		       <div id="procedure-sortable" class="col-sm-8">
	               <ul class="procedure-sortable list-unstyled">
	                   <c:forEach var="field" items="${form.theSelectedStep.fields}" varStatus="status">
	                      <li class="form-group">
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
	                      </li>
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
	                     <label class="control-label">Référence de l'étape cible</label>
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
	                      <div class="col-sm-4">
	                          <form:input path="theSelectedStep.actions[${status.index}].stepReference" type="text" cssClass="form-control" placeholder="stepReference" />
	                      </div>
	                      <div class="btn-group col-sm-2">
	                          <button type="submit" name="editButton" class="btn btn-default" onclick="selector(this,'${status.index}','selectedButton')">
	                              <i class="glyphicons glyphicons-edit"></i>
	                          </button>
	                          <button type="submit" name="deleteButton" class="btn btn-default" onclick="selector(this,'${status.index}','selectedButton')">
	                              <i class="glyphicons glyphicons-remove"></i>
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
	       <div class="form-group">
	           <div class="col-sm-offset-2 col-sm-1">
		           <div class="checkbox">
	                    <label>
	                        <input type="checkbox" <c:if test="${form.theSelectedStep.notifiable}">checked="checked"</c:if> name="theSelectedStep.notifiable"><span>notifiable</span>
	                    </label>
	                </div>
                </div>
                <div class="col-sm-1">
	                <div class="checkbox">
	                    <label>
	                        <input type="checkbox" <c:if test="${form.theSelectedStep.acquitable}">checked="checked"</c:if> name="theSelectedStep.acquitable"><span>acquitable</span>
	                    </label>
	                </div>
                </div>
	           <div class="col-sm-1">
		           <div class="checkbox">
		                <label>
		                    <input type="checkbox" <c:if test="${form.theSelectedStep.closable}">checked="checked"</c:if> name="theSelectedStep.closable"><span>closable</span>
		                </label>
	                </div>
                </div>
	       </div>
           <div class="form-group">
               <form:label path="theSelectedStep.actionIdClosable" cssClass="col-sm-2 control-label">actionIdClosable</form:label>
               <div class="col-sm-10">
                   <form:input path="theSelectedStep.actionIdClosable" type="text" cssClass="form-control" />
               </div>
           </div>
           <div class="form-group">
               <form:label path="theSelectedStep.stringMsg" cssClass="col-sm-2 control-label">stringMsg</form:label>
               <div class="col-sm-10">
                   <form:input path="theSelectedStep.stringMsg" type="text" cssClass="form-control" />
               </div>
           </div>
           <div class="form-group">
               <form:label path="theSelectedStep.actionIdYes" cssClass="col-sm-2 control-label">actionIdYes</form:label>
               <div class="col-sm-10">
                   <form:input path="theSelectedStep.actionIdYes" type="text" cssClass="form-control" />
               </div>
           </div>
           <div class="form-group">
               <form:label path="theSelectedStep.actionIdNo" cssClass="col-sm-2 control-label">actionIdNo</form:label>
               <div class="col-sm-10">
                   <form:input path="theSelectedStep.actionIdNo" type="text" cssClass="form-control" />
               </div>
           </div>
           <div class="form-group">
               <form:label path="theSelectedStep.actionIdDefault" cssClass="col-sm-2 control-label">actionIdDefault</form:label>
               <div class="col-sm-10">
                   <form:input path="theSelectedStep.actionIdDefault" cssClass="form-control" />
               </div>
           </div>
	       <ul class="list-unstyled">
               <li class="form-group">
                   <form:label path="theSelectedStep.groups" cssClass="col-sm-2 control-label">Groupes</form:label>
                   <div class="col-sm-10">
                       <form:select path="theSelectedStep.groups" multiple="multiple" class="groupSelect-select2 form-control select2" cssStyle="width: 100%;">
                           <form:options items="${form.theSelectedStep.groups}" />
                       </form:select>
                   </div>
               </li>
           </ul>
	    </div>
	</div>
    <hr>
    <div class="row">
        <div class="col-sm-1">
            <button type="submit" class="btn btn-default" name="cancelStep">Annuler</button>
        </div>
        <div class="col-sm-1 pull-right">
            <button type="submit" class="btn btn-danger pull-right" name="deleteStep">Supprimer</button>
        </div>
        <div class="col-sm-1 pull-right">
            <button type="submit" class="btn btn-primary pull-right" name="saveStep">Sauvegarder</button>
        </div>
        <input type="submit" class="hidden" name="updateForm">
        <input type="submit" class="hidden" name="selectField">
    </div>
</form:form>