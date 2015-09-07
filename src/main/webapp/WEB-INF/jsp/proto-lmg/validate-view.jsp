<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="toutatice" prefix="ttc"%>

<div class="form-group">
	<label class="control-label">Etudiant </label> <span
		class="form-control-static">${procedure.nextActors}</span>
</div>

<div class="form-group">
	<label class="control-label">Rapport de stage </label> <span
		class="form-control-static"> <i
		class="halflings halflings-glyph-paperclip"></i> <a
		href="${procedure.form.documentURL}">${procedure.form.documentName}</a>
	</span>
</div>

<div class="form-group">
	<form:label path="form.comment" class="control-label">Commentaire </form:label>
	<form:input type="textarea" path="form.comment" />
</div>