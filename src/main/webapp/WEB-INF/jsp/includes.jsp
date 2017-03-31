<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<div class="procedure-notifications">
	<!-- AJAX waiter-->
	<div class="pull-right">
	    <p class="ajax-waiter">
	        <span class="label label-info">
	            <i class="halflings halflings-refresh"></i>
	            <span><op:translate key="AJAX_REFRESH" /></span>
	        </span>
	    </p>
	</div>
	
	<c:if test="${not empty form.filterMessage}">
		<!-- Messages-->
		<div class="alert alert-danger alert-dismissible">
			<button type="button" class="close" data-dismiss="alert">
                <span>&times;</span>
            </button>
			<p class="text-danger">
				<i class="halflings halflings-exclamation-sign"></i>
				<span>${form.filterMessage}</span>
			</p>
		</div>
	</c:if>
	<c:if test="${not empty successMessage}">
		<!-- Messages-->
		<div class="alert alert-success alert-dismissible">
			<button type="button" class="close" data-dismiss="alert">
                <span>&times;</span>
            </button>
			<p class="text-success">
				<span>${successMessage}</span>
			</p>
		</div>
	</c:if>
</div>