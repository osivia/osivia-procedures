<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<portlet:defineObjects />

<portlet:actionURL name="editProcedure" var="editProcedureUrl">
</portlet:actionURL>

<a class="btn btn-default btn-sm pull-right" href="${form.addUrl}" role="button" title="Créer une procedure" data-toggle="tooltip" data-placement="auto bottom"><i class="glyphicons glyphicons-circle-plus"></i></a>

<form:form modelAttribute="form" action="${editProcedureUrl}" method="post" cssClass="form-horizontal" role="form">


    <ul class="list-unstyled">
        <c:forEach var="procedure" items="${form.procedureList}">
            <li>
                <a href="${procedure.url}">${procedure.name}</a>
            </li>
        </c:forEach>
    </ul>


</form:form>