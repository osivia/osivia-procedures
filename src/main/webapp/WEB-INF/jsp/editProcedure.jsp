<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<portlet:defineObjects />

<portlet:actionURL name="editProcedure" var="editProcedureUrl">
</portlet:actionURL>


<form:form modelAttribute="form" action="${editProcedureUrl}" method="post" cssClass="form-horizontal" role="form">

    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Nom de la procédure</h3>
        </div>
        <div class="panel-body">
            <div class="form-group">
                <div class="col-sm-6">
                    <form:input path="procedureModel.name" type="text" cssClass="form-control" />
                </div>
            </div>
        </div>
    </div>
        
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Liste des étapes</h3>
        </div>
        <div class="panel-body">
            <ul class="list-unstyled">
                <c:forEach var="step" items="${form.procedureModel.steps}" varStatus="status">
                    <li class="form-group">
                        <div class="col-sm-9">${step.stepName}</div>
                        <div class="btn-group col-sm-3">
                            <button type="submit" name="editStep" onclick="selector(this,'${status.index}','selectedStep')" class="btn btn-default">
                                <i class="glyphicons glyphicons-edit"></i>
                            </button>
                            <button type="submit" name="deleteStep" class="btn btn-default" onclick="selector(this,'${status.index}','selectedStep')">
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
                    <button type="submit" class="btn btn-default" onclick="selector(this,'0','selectedStep')" name="addStep">Ajouter une étape</button>
                </div>
            </div>
        </div>
    </div>
    
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">Étape de départ</h3>
        </div>
        <div class="panel-body">
            <div class="form-group">
                <div class="col-sm-6">
                    <form:input path="procedureModel.startingStep" type="text" cssClass="form-control" />
                </div>
            </div>
        </div>
    </div>

    <div class="form-group">
        <div class="col-sm-1">
            <button type="submit" class="btn btn-primary" name="saveProcedure">Sauvegarder</button>
        </div>
        <div class="col-sm-1">
            <button type="submit" class="btn btn-default" name="launchProcedure">Lancer le procedure</button>
        </div>
        <div class="col-sm-1 pull-right">
            <button type="submit" class="btn btn-danger pull-right" name="deleteProcedure">Supprimer</button>
        </div>
    </div>
</form:form>


