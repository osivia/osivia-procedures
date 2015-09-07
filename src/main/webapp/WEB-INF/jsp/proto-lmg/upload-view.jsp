<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/WEB-INF/tld/vocabulary-selector.tld" prefix="vs" %>

<div class="form-group">
    <label class="control-label">Commentaire </label> <span
        class="form-control-static">${procedure.form.comment}</span>
</div>

<%-- <div class="form-group">
    <form:label path="form.nature" class="control-label">Nature du rapport </form:label>
    <vs:label id="${item}" entry="[CNS] Nature" othersLabel="${othersLabel}" preselect="${preselect1}" /></span>
    <form:input type="textarea" path="form.comment" />
    <form:select path="form.nature">
         <c:forEach var="child" items="${procedure.form.vocabEntry.children}">
                                <option value="${child.value.id}"
                                    <c:if test="${child.value.id eq vocab1Id}">selected="selected"</c:if>
                                >${child.value.label}</option>
                            </c:forEach>
    </form:select>
</div> --%>