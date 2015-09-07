<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<portlet:actionURL name="startProcedureAction" var="startProcedureActionURL">
    <portlet:param name="start" value="true" />
</portlet:actionURL>

<div class="well">
    <h3>Démarrer la procédure ${procedureModel.name}</h3>
    
    <a class="btn btn-primary" href="${startProcedureActionURL}">Valider</a>
</div>