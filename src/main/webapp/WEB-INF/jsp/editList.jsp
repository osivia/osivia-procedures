<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<portlet:defineObjects />

<portlet:actionURL name="editList" var="editListUrl">
</portlet:actionURL>

<portlet:resourceURL id="fieldSearch" var="fieldSearchUrl" ></portlet:resourceURL>

<portlet:resourceURL id="groupSearch" var="groupSearchUrl" ></portlet:resourceURL>

<portlet:resourceURL id="modelSearch" var="modelSearchUrl" ></portlet:resourceURL>

<div class="content-navbar">
    <!-- Breadcrumb -->
    <div class="content-navbar-breadcrumb">
        <div id="breadcrumb">
            <div class="">
                <nav>
                    <ol class="breadcrumb hidden-xs">
                        <li><a>Édition d'une liste</a></li>
                    </ol>
                </nav>
            </div>
        </div>
    </div>
</div>

<form:form modelAttribute="form" action="${editListUrl}" method="post" cssClass="form-horizontal" role="form">

    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="<c:if test="${empty activeTab or 'form' ne activeTab}">active</c:if>"><a href="#General" role="tab" data-toggle="tab" data-id="General" class="no-ajax-link">Général</a></li>
        <li role="presentation" class="<c:if test="${'form' eq activeTab}">active</c:if>"><a href="#Edition" role="tab" data-toggle="tab" data-id="Edition" class="no-ajax-link">Édition</a></li>
        <li role="presentation" class="<c:if test="${'presentation' eq activeTab}">active</c:if>"><a href="#Presentation" role="tab" data-toggle="tab" data-id="Presentation" class="no-ajax-link">Présentation</a></li>
    </ul>

    <div class="tab-content">
        <div role="tabpanel" class="tab-pane <c:if test="${empty activeTab or 'form' ne activeTab}">active</c:if>" id="General">
           <div class="form-group">
                <form:label path="procedureModel.name" cssClass="col-sm-2 control-label">Nom</form:label>
                <div class="col-sm-10">
                    <form:input path="procedureModel.name" type="text" cssClass="form-control" placeholder="Nom" />
                </div>
            </div>
            <div class="form-group">
                <form:label path="theSelectedStep.actors" cssClass="col-sm-2 control-label">Acteurs</form:label>
                <div class="col-sm-10">
                    <form:select path="theSelectedStep.actors" multiple="multiple" class="groupSelect-select2 form-control select2" cssStyle="width: 100%;" data-url="${groupSearchUrl}">
                        <form:options items="${form.theSelectedStep.actors  }" />
                    </form:select>
                </div>
            </div>
            <div class="form-group">
                <form:label path="procedureModel.webIdParent" cssClass="col-sm-2 control-label">Modèle parent</form:label>
                <div class="col-sm-10">
                    <form:select path="procedureModel.webIdParent" class="modelSelect-select2 form-control select2" cssStyle="width: 100%;" data-url="${modelSearchUrl}">
                    </form:select>
                </div>
            </div>
        </div>
        
        <div class="row" id="form-pres" style="display: none">
            <div class="col-sm-4">
                <ul class="nav nav-tabs" role="tablist">
                     <li role="presentation" class="<c:if test="${empty activeFormTab or 'edit' ne activeFormTab}">active</c:if>"><a href="#CreateField" role="tab" data-toggle="tab" class="no-ajax-link">Ajouter un champ</a></li>
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
                         <button type="submit" name="addField" class="btn btn-default pull-right" onclick="selector(this,document.getElementById('Formulaire').style.display,'formulaire');">Ajouter</button>
                     
                     </div>
                     <c:if test="${not empty form.selectedField}">
                         <div role="tabpanel" class="tab-pane <c:if test="${'edit' eq activeFormTab}">active</c:if>" id="Edit">
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
            
            <div role="tabpanel" class="tab-pane <c:if test="${'form' eq activeTab}">active</c:if>" id="Edition">
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
	        <div role="tabpanel" class="tab-pane <c:if test="${'presentation' eq activeTab}">active</c:if>" id="Presentation">
	           <div id="procedure-sortable" class="col-sm-8">
                    <ul class="procedure-sortable list-unstyled">
                        <c:forEach var="field" items="${form.procedureModel.steps[1].fields}" varStatus="status">
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
    </div>

    <hr>
    <button type="submit" class="btn btn-primary" name="saveList">Sauvegarder</button>
    <button type="submit" class="btn btn-danger pull-right" name="deleteList">Supprimer</button>
    <input type="submit" class="hidden" name="updateForm">
    <input type="submit" class="hidden" name="selectField">

</form:form>