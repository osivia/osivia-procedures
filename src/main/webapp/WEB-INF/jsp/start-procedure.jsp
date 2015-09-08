<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<portlet:actionURL name="startProcedureAction" var="startProcedureActionURL">
    <portlet:param name="start" value="true" />
</portlet:actionURL>

<fieldset>
    <div class="col-sm-12">${procedureModel.description}</div>
</fieldset>

<div class="col-sm-12">
    <a class="col-sm-offset-2  btn btn-primary" href="${startProcedureActionURL}">Démarrer</a>
</div>   
