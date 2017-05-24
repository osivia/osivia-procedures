<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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
                            <a href="${cancelTdbUrl}">Édition d'une procédure</a>
                        </li>
                        <li><a>Édition d'un tableau de bord</a></li>
                    </ol>
                </nav>
            </div>
        </div>
    </div>
</div>

<form:form modelAttribute="form" action="${editTdbUrl}" method="post" role="form">

    <div class="form-horizontal">
	    <div class="form-group">
	         <form:label path="theSelectedTdb.name" cssClass="col-sm-2 control-label">Nom du tableau de bord</form:label>
	         <div class="col-sm-10">
	             <form:input path="theSelectedTdb.name" type="text" cssClass="form-control" />
	         </div>
	     </div>
	     
	     <div class="form-group">
	         <form:label path="theSelectedTdb.groups" cssClass="col-sm-2 control-label">Groupes du tableau de bord</form:label>
	         <div class="col-sm-10">
	             <form:select path="theSelectedTdb.groups" multiple="multiple" class="groupSelect-select2 form-control select2" cssStyle="width: 100%;" data-url="${groupSearchUrl}">
	                 <form:options items="${form.theSelectedTdb.groups}" />
	             </form:select>
	         </div>
	     </div>
     </div>
     
     <h3>Colonnes</h3>
     
    <table class="table">
        <thead>
            <tr>
                <th>Label</th>
                <th>Variable</th>
                <th>Triable</th>
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
               <td><form:select path="newColumn.variableName" class="fieldSelect-select2 form-control select2" cssStyle="width: 100%;" data-url="${fieldSearchUrl}">
                </form:select></td>
               <td><form:checkbox path="newColumn.sortable" cssClass="form-control" /></td>
               <td><button type="submit" class="btn btn-default" name="addColumn">Ajouter</button></td>
		    </tr>
        </tbody>
    </table>
	    
    <button type="submit" class="btn btn-default" name="cancelTdb">Annuler</button>
    <button type="submit" class="btn btn-primary" name="saveTdb">Sauvegarder</button>
    <button type="submit" class="btn btn-danger pull-right" name="deleteTdb">Supprimer</button>
    <input type="submit" class="hidden" name="updateForm">

</form:form>