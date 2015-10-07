<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<portlet:defineObjects />

<portlet:actionURL name="editStep" var="editStepUrl">
</portlet:actionURL>

<form:form modelAttribute="form" action="${editStepUrl}" method="post" cssClass="form-horizontal" role="form">

        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Identification</h3>
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <div class="col-sm-2">
                        <label for="theSelectedStep.stepName">Titre de l'étape</label>
                    </div>
                    <div class="col-sm-10">
                        <form:input path="theSelectedStep.stepName" type="text" cssClass="form-control" />
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-2">
                        <label for="theSelectedStep.reference">Réference de l'étape</label>
                    </div>
                    <div class="col-sm-10">
                        <form:input path="theSelectedStep.reference" type="text" cssClass="form-control" />
                    </div>
                </div>
            </div>
        </div>
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">Champs complémentaires</h3>
            </div>
            <div class="panel-body">
                <ul class="list-unstyled">
                    <li class="form-group">
                        <div class="col-sm-1"></div>
                        <div class="col-sm-9">
                            <div class="row">
                                <div class="col-sm-4">
                                    <label class="control-label">Nom</label>
                                </div>
                                <div class="col-sm-4">
                                    <label class="control-label">Type</label>
                                </div>
                                <div class="col-sm-4">
                                    <label class="control-label">Label</label>
                                </div>
                            </div>
                        </div>
                    </li>
                </ul>
                <ul class="procedure-sortable list-unstyled">
                    <c:forEach var="field" items="${form.theSelectedStep.fields}" varStatus="status">
                        <li class="form-group">
                            <div class="col-sm-1 sortable-handle">
                                <i class="glyphicons glyphicons-sorting pull-right"></i>
                            </div>
                            <div class="col-sm-10">
                                <div class="row">
                                    <div class="col-sm-4">
                                        <form:input path="theSelectedStep.fields[${status.index}].name" type="text" cssClass="form-control" placeholder="Nom" />
                                    </div>
                                    <div class="col-sm-3">
                                        <form:select path="theSelectedStep.fields[${status.index}].type" cssClass="form-control">
                                            <form:option value="TEXT">Texte</form:option>
                                            <form:option value="FILE">Fichier</form:option>
                                        </form:select>
                                    </div>
                                    <div class="col-sm-3">
                                        <form:input path="theSelectedStep.fields[${status.index}].label" type="text" cssClass="form-control" placeholder="Label" />
                                    </div>
                                    <div class="col-sm-2">
                                        <form:checkbox path="theSelectedStep.fields[${status.index}].input" cssClass="form-control" label="Éditable:"/>
                                    </div>
                                </div>
                            </div>
                            <div class="btn-group col-sm-1">
                                <button type="submit" name="deleteField" class="btn btn-default" onclick="selector(this,'${status.index}','selectedRow')">
                                    <i class="glyphicons glyphicons-remove-2"></i>
                                </button>
                            </div>
                            <form:input path="theSelectedStep.fields[${status.index}].order" type="hidden" name="order"/>
                        </li>                    
                    </c:forEach>
                    <form:input path="selectedStep" type="hidden" name="selectedStep"/>
                </ul>
            </div>
            <div class="panel-footer">
                <div class="form-group">
                    <div class="col-sm-11">
                        <button type="submit" name="addField" class="btn btn-default">Ajouter un champ</button>
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
                            <label class="control-label">Référence de l'étape cible</label>
                        </div>
                    </li>
                    <c:forEach var="action" items="${form.theSelectedStep.actions}" varStatus="status">
                        <li class="form-group">
                            <div class="col-sm-2">
                                <form:input path="theSelectedStep.actions[${status.index}].label" type="text" cssClass="form-control" placeholder="Label" />
                            </div>
                            <div class="col-sm-8">
                                <form:input path="theSelectedStep.actions[${status.index}].stepReference" type="text" cssClass="form-control" placeholder="stepReference" />
                            </div>
                            <div class="btn-group col-sm-2">
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

        <div class="form-group">
            <div class="col-sm-1">
                <button type="submit" class="btn btn-primary" name="saveStep">Sauvegarder</button>
            </div>
            <div class="col-sm-1 pull-right">
                <button type="submit" class="btn btn-danger pull-right" name="deleteStep">Supprimer</button>
            </div>
        </div>
</form:form>