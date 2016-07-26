<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal"	prefix="op"%>

<portlet:defineObjects />

<portlet:actionURL name="editAction" var="editActionUrl">
</portlet:actionURL>


<form:form modelAttribute="form" action="${editActionUrl}" method="post"
	cssClass="form-horizontal" role="form">
	<div class="panel panel-default">
		<div class="panel-heading">
			<ul class="list-unstyled">
				<li class="form-group">
					<div class="col-sm-2">
						<label class="control-label">Label</label>
					</div>
					<div class="col-sm-8">${form.theSelectedAction.label}</div>
				</li>
				<li class="form-group">
					<div class="col-sm-2">
						<label class="control-label">Identifiant de l'action</label>
					</div>
					<div class="col-sm-8">${form.theSelectedAction.actionId}</div>
				</li>
				<li class="form-group">
					<div class="col-sm-2">
						<label class="control-label">Référence de l'étape cible</label>
					</div>
					<div class="col-sm-8">
						${form.theSelectedAction.stepReference}</div>
				</li>
			</ul>
		</div>
		<div class="panel-body" id="filter-sortable">
            <ul class="list-unstyled filter-sortable">
                <c:forEach var="filter" items="${form.theSelectedAction.filters}" varStatus="status">
                    <li>
                        <c:set var="nestedFilter" value="${filter}" scope="request"/>
                        <jsp:include page="editFilters.jsp"/>
                    </li>
                </c:forEach>
            </ul>
		</div>

		<div class="panel-footer">
			<div class="form-group">
			    <div class="col-sm-1">
		            <button type="submit" class="btn btn-default" name="cancelAction">Annuler</button>
		        </div>
				<div class="col-sm-1 pull-right">
					<button type="submit" class="btn btn-primary pull-right" name="saveAction">Sauvegarder</button>
				</div>
				<div class="col-sm-1 pull-right">
					<button type="button" class="btn btn-default pull-right" data-toggle="modal" data-target="#filtreModal">Ajouter un filtre</button>
				</div>
				<input type="submit" class="hidden" name="updateForm">
			</div>
		</div>
	</div>


	<div class="modal fade" id="filtreModal" tabindex="-1" role="dialog"
		aria-labelledby="filtreModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="filtreModalLabel">Filtres
						disponibles</h4>
				</div>
				<div class="modal-body">
					<c:forEach var="filtre" items="${listeFiltres}">
						<div class="panel panel-default">
							<div class="panel-heading">
								<h3 class="panel-title"><op:translate key="${filtre.labelKey}"/></h3>
							</div>
							<div class="panel-body">
								<op:translate key="${filtre.descriptionKey}"/>
								<button type="submit" name="addFilter" class="btn btn-default pull-right"
									onclick="selector(this,'${filtre.id}','selectedFilter');hideModal();" data-dismiss="modal">
									<i class="glyphicons glyphicons-plus"></i>
								</button>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>


</form:form>
