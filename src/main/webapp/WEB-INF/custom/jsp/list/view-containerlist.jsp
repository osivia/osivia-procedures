<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc"%>

<%@ page isELIgnored="false" %>

<ul class="list-unstyled">
    <c:forEach var="document" items="${documents}">
        <li><ttc:title document="${document}" displayContext="viewproclist"/></li>
    </c:forEach>
</ul>
