<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<portlet:defineObjects />

<div class="alert alert-success">
    <i class="glyphicons glyphicons-ok-sign"></i>
    <span>Votre demande a été soumise.</span>
</div>

<div>
    <a href="${closeUrl}" class="btn btn-default no-ajax-link">
        <span>Fermer</span>
    </a>
</div>
