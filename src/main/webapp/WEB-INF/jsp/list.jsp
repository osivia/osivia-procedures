<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>

<portlet:defineObjects />

<portlet:actionURL name="editProcedure" var="editProceduresUrl">
</portlet:actionURL>

<a class="btn btn-default btn-sm pull-right" href="${addProcedureUrl}" role="button" title="<op:translate key="CREATE_PROCEDURE" />" data-toggle="tooltip" data-placement="auto bottom"><i class="glyphicons glyphicons-plus-sign"></i></a>
<a class="btn btn-default btn-sm pull-right" href="${addRecordFolderUrl}" role="button" title="<op:translate key="CREATE_RECORDFOLDER" />" data-toggle="tooltip" data-placement="auto bottom"><i class="glyphicons glyphicons-plus-sign"></i></a>

<form:form modelAttribute="form" action="${editProceduresUrl}" method="post" cssClass="form-horizontal" role="form">

    <ul class="list-unstyled">
        <c:forEach var="procedure" items="${procedureList}">
            <li>
                <a href="${procedure.url}" class="no-ajax-link">${procedure.name}</a>
            </li>
        </c:forEach>
    </ul>


</form:form>