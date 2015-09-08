<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="form" value="${procedure.form}" /> 

<div class="form-group">
    <label class="col-sm-2 control-label">Commentaire </label> <span
        class="form-control-static">${form.comment}</span>
</div>
 
<div class="form-group"> 
    <form:label path="form.nature" class="col-sm-2 control-label">Nature du rapport </form:label>  
    <form:select path="form.nature" class="col-sm-10 form-control"  style="width:auto">
         <c:forEach var="child" items="${form.vocabEntry.children}">
               <option value="${child.value.id}"
                        <c:if test="${child.value.id eq form.nature}">selected="selected"</c:if>
                >${child.value.label}</option>
         </c:forEach>
    </form:select>
</div> 