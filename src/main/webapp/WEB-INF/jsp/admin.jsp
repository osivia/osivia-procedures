<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="action" var="adminActionUrl" portletMode="admin"></portlet:actionURL>

<div>
	<form:form method="post" modelAttribute="adminForm" action="${adminActionUrl}">
		<div class="form-horizontal panel-default">		
			<div>
				<label class="col-sm-12 control-label"><op:translate key="ADMIN_TYPE_PROCEDURE_PATH" /></label>			
			</div>
				
			<div class="form-group">
				<label class="col-sm-4 control-label"><op:translate key="ADMIN_PROCEDURE_PATH" /></label>
				<form:input class="col-sm-8" path="procedurePath" name="procedurePath" />			
			</div>
			<div class="ligne">
				<div class="bouton">
					<button type="submit" class="btn btn-primary navbar-btn" name="valider" value="valider" ><op:translate key="VALIDATE" /></button>
					<button type="button" class="btn btn-default navbar-btn" onclick="closeFancybox()"><op:translate key="CANCEL" /></button>
				</div>
			</div>
		</div>
	</form:form>
</div>