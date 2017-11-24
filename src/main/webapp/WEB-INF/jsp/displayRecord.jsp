<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>


<portlet:defineObjects />


<div class="row"> 
    <div class="col-md-8 col-lg-9">
        <ul class="procedure-sortable list-unstyled">
            <c:forEach var="field" items="${form.theCurrentStep.fields}" varStatus="status">
	            <li class="form-group">
				    <div class="col-sm-9">
				        <div class="row">
				            <c:set var="fieldType" value="${form.procedureModel.variables[field.name].type}" />
		                    <c:choose>
		                        <c:when
		                            test="${(fieldType eq 'TEXT') or (fieldType eq 'DATE') or (fieldType eq 'SELECTLIST') or (fieldType eq 'RADIOLIST')
		                                        or (fieldType eq 'CHECKBOXLIST') or (fieldType eq 'NUMBER')}">
		                            <label class="col-sm-3 control-label">${field.superLabel}</label>
		                            <div class="col-sm-9">
	                                    <p>${form.record.globalVariablesValues[field.name]}</p>
		                                <c:if test="${not empty field.helpText}">
		                                    <span class="help-block">${field.helpText}</span>
		                                </c:if>
		                            </div>
		                        </c:when>
		                        <c:when test="${fieldType eq 'TEXTAREA'}">
		                            <label class="col-sm-3 control-label">${field.superLabel}</label>
		                            <div class="col-sm-9">
		                                <span class="text-pre-wrap"><c:out value="${form.record.globalVariablesValues[field.name]}" /></span>
		                                <c:if test="${not empty field.helpText}">
		                                    <span class="help-block">${field.helpText}</span>
		                                </c:if>
		                            </div>
		                        </c:when>
		                        <c:otherwise>
		                            <p>error</p>
		                        </c:otherwise>
		                    </c:choose>
				        </div>
				    </div>
				</li>
            </c:forEach>
        </ul>
    </div>
    
    <c:set var="author" value="${form.record.creator}" />
	<c:set var="lastContributor" value="${form.record.lastContributor}" />
	<c:set var="created" value="${form.record.created}" />
	<c:set var="modified" value="${form.record.modified}" />

    <div class="col-md-4 col-lg-3">
	    <div class="metadata">
		    <div class="panel panel-default">
		        <div class="panel-heading">
		            <h3 class="panel-title">
		                <i class="halflings halflings-tags"></i>
		                <span><op:translate key="METADATA" /></span>
		            </h3>
		        </div>
		        <div class="panel-body">
		            <dl>
		                <!-- Creation -->
		                <c:if test="${not empty created}">
		                    <dt><op:translate key="METADATA_CREATION" /></dt>
		                    <dd>
		                        <p>
		                            <span><op:translate key="METADATA_CREATED_ON" /></span>
		                            <span><op:formatRelativeDate value="${created}" /></span>
		                            
		                            <c:if test="${not empty author}">
		                                <br>
		                                <span><op:translate key="METADATA_BY" /></span>
		                                <span><ttc:user name="${author}"/></span>
		                            </c:if>
		                        </p>
		                    </dd>
		                </c:if>
		                
		                <!-- Modification -->
		                <c:if test="${not empty modified}">
		                    <dt><op:translate key="METADATA_MODIFICATION" /></dt>
		                    <dd>
		                        <p>
		                            <span><op:translate key="METADATA_MODIFIED_ON" /></span>
		                            <span><op:formatRelativeDate value="${modified}" /></span>
		                            
		                            <c:if test="${not empty lastContributor}">
		                                <br>
		                                <span><op:translate key="METADATA_BY" /></span>
		                                <span><ttc:user name="${lastContributor}"/></span>
		                            </c:if>
		                        </p>
		                    </dd>
		                </c:if>
		            </dl>
		        </div>
		    </div>
		</div>
	</div>
</div>

