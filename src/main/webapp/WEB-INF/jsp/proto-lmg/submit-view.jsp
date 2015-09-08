<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

		<div class="form-group">
            <form:label path="form.document" class="col-sm-2 control-label">Convention </form:label>
            <div class="col-sm-10">
                <form:input type="file" path="form.document" class="col-sm-10" />
            </div>
        </div>
