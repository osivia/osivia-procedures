<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="action" var="adminActionUrl" portletMode="admin"></portlet:actionURL>

<div>
	<form:form method="post" modelAttribute="adminForm" action="${adminActionUrl}">
		<div class="form-horizontal panel-default">		
			<div>
				<label class="col-sm-12 control-label">Veuillez saisir le chemin du container de procédures.</label>			
			</div>
				
			<div class="form-group">
				<label class="col-sm-4 control-label">Chemin du container de procéduress :</label>
				<form:input class="col-sm-8" path="procedurePath" name="procedurePath" />			
			</div>
			<div class="ligne">
				<div class="bouton">
					<button type="submit" class="btn btn-primary navbar-btn" name="valider" value="valider" >Valider</button>
					<button type="button" class="btn btn-default navbar-btn" onclick="closeFancybox()">Annuler</button>
				</div>
			</div>
		</div>
	</form:form>
</div>