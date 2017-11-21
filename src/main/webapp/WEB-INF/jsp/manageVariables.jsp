<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>

<portlet:defineObjects />

<portlet:actionURL name="manageVariables" var="manageVariablesUrl">
</portlet:actionURL>

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
							<a href="${cancelStepUrl}"><op:translate key="EDIT_PROCEDURE" /></a>
			            </li>
			            <li><a><op:translate key="MANAGE_VARIABLES" /></a></li>
				    </ol>
				</nav>
			</div>
		</div>
    </div>
</div>

<form:form modelAttribute="form" action="${manageVariablesUrl}" method="post" cssClass="form-inline" role="form">
	<div class="panel panel-info">
		<div class="panel-heading"><op:translate key="DICTIONARY" /></div>
		<div class="panel-body">
			<div class="col-sm-12">
				<div class="row">
					<div class="col-sm-3">
						<strong><op:translate key="NAME" /></strong>
					</div>
					<div class="col-sm-3">
						<strong><op:translate key="LABEL" /></strong>
					</div>
					<div class="col-sm-3">
						<strong><op:translate key="TYPE" /></strong>
					</div>
					<div class="col-sm-3">
						<strong><op:translate key="USED_IN" /></strong>
					</div>
				</div>
			</div>
			<div class="procedure-variables-dico col-sm-12">
				<c:forEach var="variable" items="${form.editedVariables}"> 
				    <c:if test="${variable.type ne 'FIELDSET'}">
						<div class="row">
		 					<div class="col-sm-3">
	 							${variable.name}
		 					</div>
		 					<div class="col-sm-3">
		 						<c:if test="${not empty form.selectedVariable}">
			 						<c:if test="${variable.label eq form.selectedVariable.label}">
			 							<div class="form-group">
					 						<form:input path="selectedVariable.label" type="text" cssClass="form-control"/>
					 						<button type="submit" class="btn btn-default" name="saveVariable">
					 							<i class="glyphicons glyphicons-ok"></i>
					 						</button>
			 							</div>
			 						</c:if>
			 						<c:if test="${variable.label ne form.selectedVariable.label}">
					 					${variable.label}
			 						</c:if>
		 						</c:if>
		 						<c:if test="${empty form.selectedVariable}">
		 							${variable.label}
		 						</c:if>
		 					</div>
		 					<div class="col-sm-3"><op:translate key="${variable.type}" /></div>
		 					<div class="col-sm-3">
			 					<div class="pull-right">
			 						<button type="submit" class="btn btn-default" onclick="selector(this,'${variable.name}','selectedVar')" name="selectVariable">
			 							<i class="glyphicons glyphicons-edit"></i>
			 						</button>
			 						<c:if test="${empty variable.usedInFields}">
				 						<button type="submit" class="btn btn-default" onclick="selector(this,'${variable.name}','selectedVar')" name="deleteVariable">
				 							<i class="glyphicons glyphicons-bin"></i>
				 						</button>
			 						</c:if>
		 						</div>
		 					</div>
		 					<c:forEach var="usedInField" items="${variable.usedInFields}">
		 						<c:forEach var="fieldStep" items="${usedInField.value}">
			 						<div class="col-sm-offset-3 col-sm-3">${fieldStep.superLabel}</div>
				 					<div class="col-sm-offset-3 col-sm-3">${usedInField.key}</div>
		 						</c:forEach>
		 					</c:forEach>
						</div>
				    </c:if>
				</c:forEach>
			</div>
		</div>
	</div>
	<button type="submit" class="btn btn-default" name="cancel"><op:translate key="CANCEL" /></button>
	<button type="submit" class="btn btn-primary" name="save"><op:translate key="VALIDATE" /></button>
</form:form>