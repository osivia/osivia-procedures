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

<!-- Ajax shadowbox -->
<div class="ajax-shadowbox">
    <div class="progress">
        <div class="progress-bar progress-bar-striped active" role="progressbar">
            <span><op:translate key="AJAX_REFRESH" /></span>
        </div>
    </div>
</div>

<form:form modelAttribute="form" action="${editTdbUrl}" method="post" role="form" data-ajax-shadowbox=".ajax-shadowbox">

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
                <th><op:translate key="ENABLE_LINK" /></th>
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
                       <c:if test="${column.enableLink}"><i class="halflings halflings-ok"></i></c:if>
                       <c:if test="${not column.enableLink}"><i class="halflings halflings-remove"></i></c:if>
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
               <td><form:select path="newColumn.variableName" class="fieldSelect-select2 form-control select2" cssStyle="width: 100%;" data-url="${fieldSearchUrl}" data-defaultvars="true" data-autofill="true">
                </form:select></td>
               <td><div class="checkbox text-center"><form:checkbox path="newColumn.sortable"/></div></td>
               <td><div class="checkbox text-center"><form:checkbox path="newColumn.enableLink"/></div></td>
               <td><button type="submit" class="btn btn-default" name="addColumn"><op:translate key="ADD" /></button></td>
		    </tr>
        </tbody>
    </table>
    
    <h3><op:translate key="DASHBOARDS_EXPORT" /></h3>
    
    <p><op:translate key="DASHBOARDS_EXPORT_VAR" /></p>
    
    <table class="table">
        <tbody class="exportVar-sortable">
            <c:forEach var="exportVar" items="${form.theSelectedTdb.exportVarList}" varStatus="status">
                <tr class="procedure-export">
	                <td>${exportVar}</td>
	                <td>   
	                   <button type="submit" name="deleteExportVar" class="btn btn-default pull-riht" onclick="selector(this,'${status.index}','selectedExportVar')">
	                       <i class="glyphicons glyphicons-bin"></i>
	                   </button>
	                </td>
                </tr>
            </c:forEach>
            <tr>
               <td><form:select path="newExportVar" class="fieldSelect-select2 form-control select2 " cssStyle="width: 100%;" data-url="${fieldSearchUrl}" data-defaultvars="true">
                </form:select></td>
               <td><button type="submit" class="btn btn-default" name="addExportVar"><op:translate key="ADD" /></button></td>
            </tr>
        </tbody>
    </table>
	    
    <button type="submit" class="btn btn-default" name="cancelTdb"><op:translate key="CANCEL" /></button>
    <button type="submit" class="btn btn-primary" name="saveTdb"><op:translate key="SAVE_DASHBOARD" /></button>
    <c:if test="${form.selectedTdbPersisted}">
	    <a href="javascript:;" class="btn btn-danger pull-right" data-fancybox="" data-src="#DASHBOARD_DELETE"><op:translate key="DELETE_DASHBOARD" /></a>
    </c:if>
    <input type="submit" class="hidden" name="updateDashboard">

</form:form>

<div class="hidden">
    <div id="DASHBOARD_DELETE">
        <form:form modelAttribute="form" action="${editTdbUrl}" method="post" role="form" >
            <p><op:translate key="DELETE_DASHBOARD_WARNING" /></p>
            <button type="submit" class="btn btn-warning" name="deleteTdb"><i class="halflings halflings-alert"></i>
                 <span class="hidden-xs"><op:translate key="DELETE_DASHBOARD" /></span>
            </button>
            <button class="btn btn-default" type="button" onclick="closeFancybox()"><op:translate key="CANCEL" /></button>
        </form:form>
    </div>
</div>