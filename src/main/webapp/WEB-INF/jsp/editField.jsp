<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc"%>


<li class="form-group <c:if test="${form.selectedField.path eq field.path}">fieldSelected</c:if> <c:if test="${field.required eq true}">required</c:if>">
    <div class="col-sm-9">
        <div class="row">

            <c:set var="fieldType" value="${form.procedureModel.variables[field.name].type}" />
            <c:set var="fieldNamePath" value="procedureInstance.globalVariablesValues['${field.name}']" />
            <c:set var="fieldValue" value="${form.procedureInstance.globalVariablesValues[field.name]}" />
            <c:set var="fieldVarOptions" value="${form.procedureModel.variables[field.name].varOptions}" />
            <c:set var="downloadLink" value="${form.procedureInstance.filesPath[field.name].downloadLink}" />
            <c:set var="fileName" value="${form.procedureInstance.filesPath[field.name].fileName}" />

            <c:choose>
                <c:when test="${field.input}">
                    <c:choose>
                        <c:when test="${fieldType eq 'TEXT'}">
                            <form:label path="${fieldNamePath}" cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
                            <div class="col-sm-9">
                                <form:input path="${fieldNamePath}" type="text" cssClass="form-control" />
                                <c:if test="${not empty field.helpText}">
                                    <span class="help-block">${field.helpText}</span>
                                </c:if>
                            </div>
                        </c:when>
                        
                        <c:when test="${fieldType eq 'TEXTAREA'}">
                            <form:label path="${fieldNamePath}" cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
                            <div class="col-sm-9">
                                <form:textarea path="${fieldNamePath}" cssClass="form-control" />
                                <c:if test="${not empty field.helpText}">
                                    <span class="help-block">${field.helpText}</span>
                                </c:if>
                            </div>
                        </c:when>
                        
                        <c:when test="${fieldType eq 'DATE'}">
                            <form:label path="${fieldNamePath}" cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
                            <div class="col-sm-9">
                                <div class="input-group">
                                    <span class="input-group-addon"> <i class="halflings halflings-calendar"></i>
                                    </span>
                                    <form:input path="${fieldNamePath}" type="text" cssClass="form-control ${editionMode ? '' : 'dates-selector'}" />
                                </div>
                                <c:if test="${not empty field.helpText}">
                                    <span class="help-block">${field.helpText}</span>
                                </c:if>
                            </div>
                        </c:when>
                        
                        <c:when test="${fieldType eq 'NUMBER'}">
                            <form:label path="${fieldNamePath}" cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
                            <div class="col-sm-9">
                                <form:input path="${fieldNamePath}" type="number" cssClass="form-control" />
                                <c:if test="${not empty field.helpText}">
                                    <span class="help-block">${field.helpText}</span>
                                </c:if>
                            </div>
                        </c:when>
                        
                        <c:when test="${(fieldType eq 'RADIOLIST')}">
                            <form:label path="${fieldNamePath}" cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
                            <div class="col-sm-9">
                                <input type="text" name="${fieldNamePath}" data-varOptions='${fieldVarOptions}' class="hidden field-radioList-json"
                                    value="${fieldValue}">
                                <c:if test="${not empty field.helpText}">
                                    <span class="help-block">${field.helpText}</span>
                                </c:if>
                            </div>
                        </c:when>
                        
                        <c:when test="${(fieldType eq 'CHECKBOXLIST')}">
                            <form:label path="${fieldNamePath}" cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
                            <div class="col-sm-9">
                                <input type="text" name="${fieldNamePath}" data-varOptions='${fieldVarOptions}' class="hidden field-checkboxList-json"
                                    value="${fieldValue}">
                                <c:if test="${not empty field.helpText}">
                                    <span class="help-block">${field.helpText}</span>
                                </c:if>
                            </div>
                        </c:when>
                        
                        <c:when test="${fieldType eq 'SELECTLIST'}">
                            <form:label path="${fieldNamePath}" cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
                            <div class="col-sm-9">
                                <c:if test="${editionMode}">
                                    <!-- Incompatibility in selection Mode  -->
                                    <c:set var="selectStyle" value="form-control" />
                                </c:if>

                                <c:if test="${not editionMode}">
                                    <c:set var="selectStyle" value="field-selectList-select2" />
                                </c:if>

                                <form:select path="${fieldNamePath}" cssClass="${selectStyle}">
                                    <c:forEach items="${field.jsonVarOptions}" var="jsonVarOption">
                                        <form:option value="${jsonVarOption['value']}">${jsonVarOption['label']}</form:option>
                                    </c:forEach>
                                </form:select>

                                <c:if test="${not empty field.helpText}">
                                    <span class="help-block">${field.helpText}</span>
                                </c:if>
                            </div>
                        </c:when>

                        <c:when test="${fieldType eq 'WYSIWYG'}">
                            <form:label path="${fieldNamePath}" cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
                            <div class="col-sm-9">
                                <c:choose>
                                    <c:when test="${editionMode}">
                                        <form:textarea path="${fieldNamePath}" cssClass="form-control" />
                                    </c:when>

                                    <c:otherwise>
                                        <portlet:resourceURL id="editor" var="editorUrl" />
                                        
                                        <form:textarea path="${fieldNamePath}" cssClass="form-control tinymce tinymce-default" data-editor-url="${editorUrl}" />
                                    </c:otherwise>
                                </c:choose>

                                <c:if test="${not empty field.helpText}">
                                    <p class="help-block">${field.helpText}</p>
                                </c:if>
                            </div>
                        </c:when>

                        <c:when test="${fieldType eq 'VOCABULARY'}">
                            <form:label path="${fieldNamePath}" cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
                            <div class="col-sm-9">
                                <c:choose>
                                    <c:when test="${editionMode}">
                                        <form:select path="${fieldNamePath}" cssClass="form-control"></form:select>
                                    </c:when>

                                    <c:otherwise>
                                        <c:set var="vocabularyId" value="${field.jsonVarOptions['vocabularyId']}" />
                                        
                                        <portlet:resourceURL id="vocabulary-search" var="url">
                                            <portlet:param name="vocabularyId" value="${vocabularyId}" />
                                        </portlet:resourceURL>

                                        <div class="input-group select2-bootstrap-append">
                                            <c:set var="searching"><op:translate key="SELECT2_SEARCHING" /></c:set>
                                            <c:set var="noResults"><op:translate key="SELECT2_NO_RESULTS" /></c:set>
                                            <form:select path="${fieldNamePath}" cssClass="form-control select2 select2-default" data-url="${url}" data-searching="${searching}" data-no-results="${noResults}">
                                                <form:option value=""></form:option>
                                                <c:if test="${not empty vocabularyId and not empty fieldValue}">
                                                    <form:option value="${fieldValue}"><ttc:vocabularyLabel name="${vocabularyId}" key="${fieldValue}" /></form:option>
                                                </c:if>
                                            </form:select>
                                            
                                            <span class="input-group-btn">
                                                <button type="button" name="clear" class="btn btn-default">
                                                    <i class="glyphicons glyphicons-delete"></i>
                                                    <span class="sr-only"><op:translate key="DELETE"/></span>
                                                </button>
                                            </span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>

                                <c:if test="${not empty field.helpText}">
                                    <p class="help-block">${field.helpText}</p>
                                </c:if>
                            </div>
                        </c:when>
                        
                        <c:when test="${fieldType eq 'PERSON'}">
                            <form:label path="${fieldNamePath}" cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
                            <div class="col-sm-9">
                                <c:choose>
                                    <c:when test="${editionMode}">
                                        <form:select path="${fieldNamePath}" cssClass="form-control"></form:select>
                                    </c:when>

                                    <c:otherwise>
                                        <portlet:resourceURL id="person-search" var="url" />

                                        <div class="input-group select2-bootstrap-append">
                                            <c:set var="inputTooShort"><op:translate key="SELECT2_INPUT_TOO_SHORT" args="3" /></c:set>
                                            <c:set var="loadingMore"><op:translate key="SELECT2_LOADING_MORE"/></c:set>
                                            <c:set var="searching"><op:translate key="SELECT2_SEARCHING" /></c:set>
                                            <c:set var="noResults"><op:translate key="SELECT2_NO_RESULTS" /></c:set>
                                            <form:select path="${fieldNamePath}" cssClass="form-control select2 select2-person" data-url="${url}" data-minimum-input-length="3" data-input-too-short="${inputTooShort}" data-loading-more="${loadingMore}" data-searching="${searching}" data-no-results="${noResults}">
                                                <form:option value=""></form:option>
                                                <c:if test="${not empty fieldValue}">
                                                    <form:option value="${fieldValue}" data-avatar=""><ttc:user name="${fieldValue}" hideAvatar="true" linkable="false" /></form:option>
                                                </c:if>
                                            </form:select>
                                            
                                            <span class="input-group-btn">
                                                <button type="button" name="clear" class="btn btn-default">
                                                    <i class="glyphicons glyphicons-delete"></i>
                                                    <span class="sr-only"><op:translate key="DELETE"/></span>
                                                </button>
                                            </span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>

                                <c:if test="${not empty field.helpText}">
                                    <p class="help-block">${field.helpText}</p>
                                </c:if>
                            </div>
                        </c:when>
                        
                        <c:when test="${fieldType eq 'RECORD'}">
                            <form:label path="${fieldNamePath}" cssClass="col-sm-3 control-label">${field.superLabel}</form:label>
                            <div class="col-sm-9">
                                <c:choose>
                                    <c:when test="${editionMode}">
                                        <form:select path="${fieldNamePath}" cssClass="form-control"></form:select>
                                    </c:when>

                                    <c:otherwise>
                                        <c:set var="recordFolderWebId" value="${field.jsonVarOptions['recordFolderWebId']}" />
                                        
                                        <portlet:resourceURL id="record-search" var="url">
                                            <portlet:param name="recordFolderWebId" value="${recordFolderWebId}" />
                                        </portlet:resourceURL>

                                        <div class="input-group select2-bootstrap-append">
                                            <c:set var="searching"><op:translate key="SELECT2_SEARCHING" /></c:set>
                                            <c:set var="noResults"><op:translate key="SELECT2_NO_RESULTS" /></c:set>
                                            <form:select path="${fieldNamePath}" cssClass="form-control select2 select2-default" data-url="${url}" data-searching="${searching}" data-no-results="${noResults}">
                                                <form:option value=""></form:option>
                                                <c:if test="${not empty fieldValue}">
                                                    <form:option value="${fieldValue}"><ttc:title path="${fieldValue}" linkable="false" /></form:option>
                                                </c:if>
                                            </form:select>
                                            
                                            <span class="input-group-btn">
                                                <button type="button" name="clear" class="btn btn-default">
                                                    <i class="glyphicons glyphicons-delete"></i>
                                                    <span class="sr-only"><op:translate key="DELETE"/></span>
                                                </button>
                                            </span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>

                                <c:if test="${not empty field.helpText}">
                                    <p class="help-block">${field.helpText}</p>
                                </c:if>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <p>error</p>
                        </c:otherwise>
                    </c:choose>
                </c:when>

                <c:otherwise>
                    <c:choose>
                        <c:when test="${empty fieldValue}"></c:when>
                    
                        <c:when test="${fieldType eq 'DATE'}">
                            <fmt:parseDate var="date" value="${fieldValue}" pattern="dd/MM/yyyy" />
                            <fmt:formatDate var="fieldValue" value="${date}" type="date" dateStyle="long" />
                        </c:when>
                        
                        <c:when test="${(fieldType eq 'SELECTLIST') or (fieldType eq 'RADIOLIST') or (fieldType eq 'CHECKBOXLIST')}">
                            <c:set var="value" value="${fieldValue}" />
                            <c:forEach var="option" items="${field.jsonVarOptions}">
                                <c:if test="${option['value'] eq value}">
                                    <c:set var="fieldValue" value="${option['label']}" />
                                </c:if>
                            </c:forEach>
                        </c:when>
                        
                        <c:when test="${fieldType eq 'WYSIWYG'}">
                            <c:set var="fieldValue"><ttc:transformContent content="${fieldValue}" /></c:set>
                        </c:when>
                        
                        <c:when test="${fieldType eq 'VOCABULARY'}">
                            <c:set var="vocabularyId" value="${field.jsonVarOptions['vocabularyId']}" />
                        
                            <c:if test="${not empty vocabularyId}">
                                <c:set var="fieldValue"><ttc:vocabularyLabel name="${vocabularyId}" key="${fieldValue}" /></c:set>
                            </c:if>
                        </c:when>
                        
                        <c:when test="${fieldType eq 'PERSON'}">
                            <c:set var="fieldValue"><ttc:user name="${fieldValue}" linkable="false" /></c:set>
                        </c:when>
                    </c:choose>
                
                    <label class="col-sm-3 control-label">${field.superLabel}</label>
                    <div class="col-sm-9">
                        <div class="form-control-static ${fieldType eq 'TEXTAREA' ? 'text-pre-wrap' : ''}">${fieldValue}</div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div> <c:forEach var="pathPart" items="${field.path}" varStatus="status">
        <c:choose>
            <c:when test="${not empty form.procedureInstance and not empty form.procedureInstance.currentStep}">
                <c:set var="springPath" value="${status.first ? 'theCurrentStep' : springPath}.fields[${pathPart}]" scope="request" />
            </c:when>
            <c:otherwise>
                <c:set var="springPath" value="${status.first ? 'theSelectedStep' : springPath}.fields[${pathPart}]" scope="request" />
            </c:otherwise>
        </c:choose>
    </c:forEach> 
    <form:hidden path="${springPath}.path" />
</li>
