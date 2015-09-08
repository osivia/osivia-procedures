<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="form-group">
	<label class="col-sm-2 control-label">Auteur de la demande </label> <span
		class="form-control-static">${procedure.initiator}</span>
</div>

<div class="form-group">
	<label class="col-sm-2 control-label">Convention </label> 
	<span
		class="form-control-static"> <i
		class="halflings halflings-glyph-paperclip"></i> <a
		href="${procedure.form.documentURL}" class="no-ajax-link">${procedure.form.documentName}</a>
	</span>
</div>

<div class="form-group">
	<form:label path="form.comment" class="col-sm-2 control-label">Commentaire </form:label>
	<div class="col-sm-10">
	   <form:textarea path="form.comment" cols="60" rows="5" />
	</div>
</div>