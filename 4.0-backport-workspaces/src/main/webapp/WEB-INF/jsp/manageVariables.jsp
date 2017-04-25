<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<portlet:defineObjects />

<portlet:actionURL name="manageVariables" var="manageVariablesUrl">
</portlet:actionURL>

<jsp:include page="includes.jsp" />

<div class="content-navbar">
    <!-- Breadcrumb -->
    <div class="content-navbar-breadcrumb">
        <div id="breadcrumb">
        	<div class="">
				<nav>
				    <ol class="breadcrumb hidden-xs">
			            <li>
				            <portlet:actionURL name="manageVariables" var="cancelStepUrl">
				            	<portlet:param name="cancel" value="toProc"/>
							</portlet:actionURL>
							<a href="${cancelStepUrl}">�dition d'une proc�dure</a>
			            </li>
			            <li><a>Gestion des variables</a></li>
				    </ol>
				</nav>
			</div>
		</div>
    </div>
</div>

<form:form modelAttribute="form" action="${manageVariablesUrl}" method="post" cssClass="form-inline" role="form">
	<div class="panel panel-info">
		<div class="panel-heading">Dictionnaire</div>
		<div class="panel-body">
			<div class="col-sm-12">
				<div class="row">
					<div class="col-sm-3">
						<strong>Nom</strong>
					</div>
					<div class="col-sm-3">
						<strong>Label</strong>
					</div>
					<div class="col-sm-3">
						<strong>Type</strong>
					</div>
					<div class="col-sm-3">
						<strong>Utilis� dans</strong>
					</div>
				</div>
			</div>
			<div class="procedure-variables-dico col-sm-12">
				<c:forEach var="variable" items="${form.procedureModel.variables}"> 
					<div class="row">
	 					<div class="col-sm-3">
 							${variable.value.name}
	 					</div>
	 					<div class="col-sm-3">
	 						<c:if test="${not empty form.selectedVariable}">
		 						<c:if test="${variable.value.label eq form.selectedVariable.label}">
		 							<div class="form-group">
				 						<form:input path="selectedVariable.label" type="text" cssClass="form-control"/>
				 						<button type="submit" class="btn btn-default" name="saveVariable">
				 							<i class="glyphicons glyphicons-ok-2"></i>
				 						</button>
		 							</div>
		 						</c:if>
		 						<c:if test="${variable.value.label ne form.selectedVariable.label}">
				 					${variable.value.label}
		 						</c:if>
	 						</c:if>
	 						<c:if test="${empty form.selectedVariable}">
	 							${variable.value.label}
	 						</c:if>
	 					</div>
	 					<div class="col-sm-3">${variable.value.type.label}</div>
	 					<div class="pull-right">
	 						<button type="submit" class="btn btn-default" onclick="selector(this,'${variable.value.name}','selectedVar')" name="selectVariable">
	 							<i class="glyphicons glyphicons-edit"></i>
	 						</button>
	 						<c:if test="${empty variable.value.usedInFields}">
		 						<button type="submit" class="btn btn-default" onclick="selector(this,'${variable.value.name}','selectedVar')" name="deleteVariable">
		 							<i class="glyphicons glyphicons-bin"></i>
		 						</button>
	 						</c:if>
 						</div>
	 					<c:forEach var="usedInField" items="${variable.value.usedInFields}">
	 						<c:forEach var="fieldStep" items="${usedInField.value}">
		 						<div class="col-sm-offset-3 col-sm-3">${fieldStep.superLabel}</div>
			 					<div class="col-sm-offset-3 col-sm-3">${usedInField.key}</div>
	 						</c:forEach>
	 					</c:forEach>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
	<button type="submit" class="btn btn-default" name="cancel">Annuler</button>
	<button type="submit" class="btn btn-primary" name="save">Sauvegarder</button>
</form:form>