<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<a class="btn btn-default btn-sm" href="${addRecordUrl}" role="button" title="Ajouter un élément"><i class="glyphicons glyphicons-plus-sign"></i> Ajouter un élément</a>

<div class="table-responsive">
    <table class="table table-hover">
        <thead>
            <th></th>
            <c:forEach var="field" items="${fields}">
                <th>${field.label}</th>
            </c:forEach>
        </thead>
        <tbody>
            <c:forEach var="procedureInstance" items="${documents}">
                <tr>
                    <td><a class="btn btn-default btn-sm" href="${procedureInstance.properties['url']}" role="button" title="Éditer l'élément"><i class="glyphicons glyphicons-search"></i></a></td>
	                <c:forEach var="field" items="${fields}">
	                   <td>${procedureInstance.properties['pi:globalVariablesValues'][field.name]}</td>
	                </c:forEach>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>