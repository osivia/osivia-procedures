<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="h" %>


<c:set var="level" value="${fieldLevel}" scope="page" />

<c:choose>
    <c:when test="${level eq 1}"><c:set var="htmlClasses" value="form-control-static" /></c:when>
    <c:otherwise><c:set var="htmlClasses" value="no-margin-bottom" /></c:otherwise>
</c:choose>


<c:choose>
    <c:when test="${fieldType eq 'FIELDLIST'}">
        <c:set var="fieldBkp" value="${field}" scope="page" />
        <c:set var="occurrences" value="${fieldJsonValue}" scope="page" />
    
        <c:if test="${not empty occurrences or level eq 1}">
            <table class="table ${htmlClasses} ${level gt 1 ? 'table-condensed table-bordered' : ''}">
                <thead>
                    <tr>
                        <c:forEach var="nestedField" items="${fieldBkp.fields}">
                            <th>${nestedField.superLabel}</th>
                        </c:forEach>
                    </tr>
                </thead>
    
                <tbody>
                    <c:forEach var="occurrence" items="${occurrences}" varStatus="status">
                        <tr>
                            <c:forEach var="nestedField" items="${fieldBkp.fields}">
                                <td>
                                    <c:set var="field" value="${nestedField}" scope="request" />
                                    <c:set var="fieldType" value="${nestedField.type}" scope="request" />
                                    <c:set var="fieldValue" value="${occurrence[nestedField.name]}" scope="request" />
                                    <c:set var="fieldJsonValue" value="${occurrence[nestedField.name]}" scope="request" />
                                    <c:set var="fieldLevel" value="${level + 1}" scope="request" />
                                    <c:set var="rowIndex" value="${status.index}" scope="request" />
                                    <c:set var="fieldName" value="${fieldBkp.name}" scope="request" />
                                    <jsp:include page="displayFieldValue.jsp" />
                                </td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                    
                    <c:if test="${empty occurrences}">
                        <tr>
                            <td colspan="${fn:length(fieldBkp.fields)}" class="text-center text-muted"><op:translate key="NO_OCCURRENCE" /></td>
                        </tr>
                    </c:if>
                    
                    <c:set var="displayFooter" value="false" />
					<c:forEach var="nestedField" items="${fieldBkp.fields}" varStatus="status">
                  			<c:set var="sumName" value="osivia.${fieldBkp.name}.${nestedField.name}" />
							<c:set var="sumValue" value="${form.record.globalVariablesValues[sumName]}" />
					
							<c:if test="${not empty sumValue}">
                                    		<c:set var="displayFooter" value="true" />
                             </c:if>

                    </c:forEach>     
                                   
                    <c:if test="${displayFooter}">
						<tfoot>
	                        <tr class="active">    
	                           <c:forEach var="nestedField" items="${fieldBkp.fields}" varStatus="status">
	                               	<td>   
	                           			<c:set var="sumName" value="osivia.${fieldBkp.name}.${nestedField.name}" />
										<c:set var="sumValue" value="${form.record.globalVariablesValues[sumName]}" />
								
										<c:if test="${not empty sumValue}">
	                                      		${sumValue}	
	                                    </c:if>
	                                </td>
	                            </c:forEach>
							</tr>                                    
						</tfoot>  
					</c:if>
						                  
                </tbody>
            </table>
        </c:if>
    </c:when>

    <c:when test="${empty fieldValue}">
        <div class="${htmlClasses}"></div>
    </c:when>

    <c:when test="${fieldType eq 'DATE'}">
        <fmt:parseDate var="date" value="${fieldValue}" pattern="dd/MM/yyyy" />
        <fmt:formatDate var="fieldValue" value="${date}" type="date" dateStyle="long" />
        <div class="${htmlClasses}">${fieldValue}</div>
    </c:when>

    <c:when test="${(fieldType eq 'SELECTLIST') or (fieldType eq 'RADIOLIST') or (fieldType eq 'CHECKBOXLIST')}">
        <c:set var="value" value="${fieldValue}" />
        <c:forEach var="option" items="${field.jsonVarOptions}">
            <c:if test="${option['value'] eq value}">
                <c:set var="fieldValue" value="${option['label']}" />
            </c:if>
        </c:forEach>
        <div class="${htmlClasses}">${fieldValue}</div>
    </c:when>

    <c:when test="${fieldType eq 'WYSIWYG'}">
        <c:set var="fieldValue">
            <ttc:transformContent content="${fieldValue}" />
        </c:set>
        <div class="${htmlClasses}">${fieldValue}</div>
    </c:when>

    <c:when test="${fieldType eq 'VOCABULARY'}">
        <c:set var="vocabularyId" value="${field.jsonVarOptions['vocabularyId']}" />

        <c:if test="${not empty vocabularyId}">
            <c:set var="fieldValue">
                <ttc:vocabularyLabel name="${vocabularyId}" key="${fieldValue}" />
            </c:set>
        </c:if>
        <div class="${htmlClasses}">${fieldValue}</div>
    </c:when>

    <c:when test="${fieldType eq 'PERSON'}">
        <c:set var="fieldValue">
            <ttc:user name="${fieldValue}" />
        </c:set>
        <div class="${htmlClasses}">${fieldValue}</div>
    </c:when>

    <c:when test="${fieldType eq 'RECORD'}">
        <div class="${htmlClasses}">
            <c:choose>
                <c:when test="${inputMode}">
                    <h:record-title webId="${fieldValue}" />
                </c:when>
                
                <c:otherwise>
                    <c:choose>
                        <c:when test="${empty rowIndex}"><c:set var="data" value="${form.record.dto.properties['rcd:data'][field.name]}" /></c:when>
                        <c:otherwise><c:set var="data" value="${form.record.dto.properties['rcd:data'][fieldName][rowIndex]}" /></c:otherwise>
                    </c:choose>
                    <c:set var="title" value="${data['dc:title']}" />
                    <c:set var="url"><ttc:transformNuxeoUrl url="/nuxeo/nxpath/default${data['ecm:path']}@view_documents" /></c:set>
                    <a href="${url}" class="no-ajax-link">${title}</a>
                </c:otherwise>
            </c:choose>
        </div>
    </c:when>


    <c:when test="${fieldType eq 'FILE'}">
        <c:choose>
            <c:when test="${empty rowIndex}"><c:set var="uploadedFileKey" value="${field.name}" /></c:when>
            <c:otherwise><c:set var="uploadedFileKey" value="${field.name}_${field.path}|${rowIndex}" /></c:otherwise>
        </c:choose>
        
        <c:set var="file" value="${form.uploadedFiles[uploadedFileKey]}" />

        <c:choose>
            <c:when test="${empty file.url}">
 				<div class="${htmlClasses}">
                    <i class="${file.temporaryMetadata.icon}"></i>
                        <span>${file.temporaryMetadata.fileName}</span>
                </div>               
            </c:when>

            <c:otherwise>
                <div class="${htmlClasses}">
                    <i class="${file.originalMetadata.icon}"></i>
                    <a href="${file.url}" target="_blank" class="no-ajax-link">
                        <span>${file.originalMetadata.fileName}</span>
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </c:when>

    <c:when test="${fieldType eq 'PICTURE'}">
        <c:choose>
            <c:when test="${empty rowIndex}"><c:set var="uploadedFileKey" value="${field.name}" /></c:when>
            <c:otherwise><c:set var="uploadedFileKey" value="${field.name}_${field.path}|${rowIndex}" /></c:otherwise>
        </c:choose>
        <c:set var="file" value="${form.uploadedFiles[uploadedFileKey]}" />
        <c:set var="imageErrorMessage"><op:translate key="IMAGE_ERROR_MESSAGE" /></c:set>

        <c:choose>
            <c:when test="${empty file.url}">
 				<div class="${htmlClasses}">
                    <i class="${file.temporaryMetadata.icon}"></i>
                        <span>${file.temporaryMetadata.fileName}</span>
                </div>     
            </c:when>

            <c:otherwise>
                <a href="${file.url}" class="thumbnail no-margin-bottom no-ajax-link" data-fancybox="gallery" data-caption="${file.originalMetadata.fileName}" data-type="image">
                    <img src="${file.url}" alt="${file.originalMetadata.fileName}" data-error-message="${imageErrorMessage}">
                </a>
            </c:otherwise>
        </c:choose>
    </c:when>

    <c:otherwise>
        <div class="${htmlClasses} ${fieldType eq 'TEXTAREA' ? 'text-pre-wrap' : ''}">${fieldValue}</div>
    </c:otherwise>
</c:choose>