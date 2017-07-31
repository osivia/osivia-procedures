<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>

<portlet:defineObjects />

<portlet:resourceURL id="groupSearch" var="groupSearchUrl" ></portlet:resourceURL>

<portlet:resourceURL id="fieldSearch" var="fieldSearchUrl" ></portlet:resourceURL>

<portlet:actionURL name="editTdb" var="editTdbUrl">
</portlet:actionURL>

<div class="content-navbar">
    <!-- Breadcrumb -->
    <div class="content-navbar-breadcrumb">
        <div id="breadcrumb">
            <div class="">
                <nav>
                    <ol class="breadcrumb hidden-xs">
                        <li>
                            <portlet:actionURL name="editTdb" var="cancelTdbUrl">
                                <portlet:param name="cancelTdb" value="toProc"/>
                            </portlet:actionURL>
                            <a href="${cancelTdbUrl}"><op:translate key="EDIT_STEP" /></a>
                        </li>
                        <li><a><op:translate key="EDIT_DASHBOARD" /></a></li>
                    </ol>
                </nav>
            </div>
        </div>
    </div>
</div>

<form:form modelAttribute="form" action="${editTdbUrl}" method="post" role="form">

    <div class="form-horizontal">
	    <div class="form-group">
	         <form:label path="theSelectedTdb.name" cssClass="col-sm-2 control-label"><op:translate key="DASHBOARD_NAME" /></form:label>
	         <div class="col-sm-10">
	             <form:input path="theSelectedTdb.name" type="text" cssClass="form-control" />
	         </div>
	     </div>
	     
	     <div class="form-group">
	         <form:label path="theSelectedTdb.groups" cssClass="col-sm-2 control-label"><op:translate key="DASHBOARDS_GROUPS" /></form:label>
	         <div class="col-sm-10">
	             <form:select path="theSelectedTdb.groups" multiple="multiple" class="groupSelect-select2 form-control select2" cssStyle="width: 100%;" data-url="${groupSearchUrl}">
	                 <form:options items="${form.theSelectedTdb.groups}" />
	             </form:select>
	         </div>
	     </div>
	     
	     <div class="form-group">
             <form:label path="theSelectedTdb.requestFilter" cssClass="col-sm-2 control-label"><op:translate key="DASHBOARDS_REQUEST" /></form:label>
             <div class="col-sm-10">
                <form:textarea path="theSelectedTdb.requestFilter" rows="5" cssClass="form-control"/>
             </div>
         </div>
	     
     </div>
     
     <h3><op:translate key="DASHBOARDS_COLUMNS" /></h3>
     
    <table class="table">
        <thead>
            <tr>
                <th><op:translate key="LABEL" /></th>
                <th><op:translate key="VARIABLE" /></th>
                <th><op:translate key="SORTABLE" /></th>
                <th></th>
            </tr>
        </thead>
        <tbody class="column-sortable">
		    <c:forEach var="column" items="${form.theSelectedTdb.columns}" varStatus="status">
		        <tr class="procedure-column">
		           <td>${column.label}</td>
		           <td>${column.variableName}</td>
		           <td>
		               <c:if test="${column.sortable}"><i class="halflings halflings-ok"></i></c:if>
		               <c:if test="${not column.sortable}"><i class="halflings halflings-remove"></i></c:if>
		           </td>
		           <td>   
		              <button type="submit" name="deleteCol" class="btn btn-default pull-riht" onclick="selector(this,'${status.index}','selectedCol')">
                          <i class="glyphicons glyphicons-bin"></i>
                      </button>
                      <input type="hidden" name="theSelectedTdb.columns[${status.index}].index" value="${status.index}">
                   </td>
		        </tr>
		    </c:forEach>
		    <tr>
	           <td><form:input path="newColumn.label" type="text" cssClass="form-control" /></td>
               <td><form:select path="newColumn.variableName" class="fieldSelect-select2 form-control select2" cssStyle="width: 100%;" data-url="${fieldSearchUrl}" data-defaultvars="true">>
                </form:select></td>
               <td><form:checkbox path="newColumn.sortable" cssClass="form-control" /></td>
               <td><button type="submit" class="btn btn-default" name="addColumn"><op:translate key="ADD" /></button></td>
		    </tr>
        </tbody>
    </table>
	    
    <button type="submit" class="btn btn-default" name="cancelTdb"><op:translate key="CANCEL" /></button>
    <button type="submit" class="btn btn-primary" name="saveTdb"><op:translate key="SAVE" /></button>
    <button type="submit" class="btn btn-danger pull-right" name="deleteTdb"><op:translate key="DELETE" /></button>
    <input type="submit" class="hidden" name="updateForm">

</form:form>