<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc"%>


<c:set var="namespace"><portlet:namespace /></c:set>


<li class="form-group <c:if test="${form.selectedField.path eq field.path}">fieldSelected</c:if> <c:if test="${field.required eq true}">required</c:if>" id="${field.path}">
      <c:set var="fieldType" value="${form.procedureModel.variables[field.name].type}" />
      <c:set var="fieldNamePath" value="procedureInstance.globalVariablesValues['${field.name}']" />
      <c:set var="fieldValue" value="${form.procedureInstance.globalVariablesValues[field.name]}" />
      <c:set var="fieldVarOptions" value="${form.procedureModel.variables[field.name].varOptions}" />

      <c:choose>
          <c:when test="${field.input}">
              <c:choose>
                  <c:when test="${fieldType eq 'TEXT'}">
                      <form:label path="${fieldNamePath}" cssClass="col-sm-3 col-lg-2 control-label">${field.superLabel}</form:label>
                      <div class="col-sm-9 col-lg-10">
                          <form:input path="${fieldNamePath}" type="text" cssClass="form-control" />
                          <c:if test="${not empty field.helpText}">
                              <span class="help-block">${field.helpText}</span>
                          </c:if>
                      </div>
                  </c:when>
                  
                  <c:when test="${fieldType eq 'TEXTAREA'}">
                      <form:label path="${fieldNamePath}" cssClass="col-sm-3 col-lg-2 control-label">${field.superLabel}</form:label>
                      <div class="col-sm-9 col-lg-10">
                          <form:textarea path="${fieldNamePath}" cssClass="form-control" />
                          <c:if test="${not empty field.helpText}">
                              <span class="help-block">${field.helpText}</span>
                          </c:if>
                      </div>
                  </c:when>
                  
                  <c:when test="${fieldType eq 'DATE'}">
                      <form:label path="${fieldNamePath}" cssClass="col-sm-3 col-lg-2 control-label">${field.superLabel}</form:label>
                      <div class="col-sm-9 col-lg-10">
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
                      <form:label path="${fieldNamePath}" cssClass="col-sm-3 col-lg-2 control-label">${field.superLabel}</form:label>
                      <div class="col-sm-9 col-lg-10">
                          <form:input path="${fieldNamePath}" type="number" cssClass="form-control" />
                          <c:if test="${not empty field.helpText}">
                              <span class="help-block">${field.helpText}</span>
                          </c:if>
                      </div>
                  </c:when>
                  
                  <c:when test="${(fieldType eq 'RADIOLIST')}">
                      <form:label path="${fieldNamePath}" cssClass="col-sm-3 col-lg-2 control-label">${field.superLabel}</form:label>
                      <div class="col-sm-9 col-lg-10">
                          <input type="text" name="${fieldNamePath}" data-varOptions='${fieldVarOptions}' class="hidden field-radioList-json"
                              value="${fieldValue}">
                          <c:if test="${not empty field.helpText}">
                              <span class="help-block">${field.helpText}</span>
                          </c:if>
                      </div>
                  </c:when>
                  
                  <c:when test="${(fieldType eq 'CHECKBOXLIST')}">
                      <form:label path="${fieldNamePath}" cssClass="col-sm-3 col-lg-2 control-label">${field.superLabel}</form:label>
                      <div class="col-sm-9 col-lg-10">
                          <input type="text" name="${fieldNamePath}" data-varOptions='${fieldVarOptions}' class="hidden field-checkboxList-json"
                              value="${fieldValue}">
                          <c:if test="${not empty field.helpText}">
                              <span class="help-block">${field.helpText}</span>
                          </c:if>
                      </div>
                  </c:when>
                  
                  <c:when test="${fieldType eq 'SELECTLIST'}">
                      <form:label path="${fieldNamePath}" cssClass="col-sm-3 col-lg-2 control-label">${field.superLabel}</form:label>
                      <div class="col-sm-9 col-lg-10">
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
                      <form:label path="${fieldNamePath}" cssClass="col-sm-3 col-lg-2 control-label">${field.superLabel}</form:label>
                      <div class="col-sm-9 col-lg-10">
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
                      <form:label path="${fieldNamePath}" cssClass="col-sm-3 col-lg-2 control-label">${field.superLabel}</form:label>
                      <div class="col-sm-9 col-lg-10">
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
                      <form:label path="${fieldNamePath}" cssClass="col-sm-3 col-lg-2 control-label">${field.superLabel}</form:label>
                      <div class="col-sm-9 col-lg-10">
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
                      <form:label path="${fieldNamePath}" cssClass="col-sm-3 col-lg-2 control-label">${field.superLabel}</form:label>
                      <div class="col-sm-9 col-lg-10">
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
                  
                  <c:when test="${fieldType eq 'FILE'}">
                    <c:set var="path" value="uploadedFiles['${field.name}'].upload" />
                    <c:set var="uploadedFile" value="${form.uploadedFiles[field.name]}" />
                
                    <form:label path="${path}" cssClass="col-sm-3 col-lg-2 control-label">${field.superLabel}</form:label>
                    <div class="col-sm-9 col-lg-10">
                        <div class="media-body">
                            <p class="form-control-static">
                                <c:choose>
                                    <c:when test="${uploadedFile.deleted}">
                                        <span class="text-muted"><op:translate key="DELETED_FILE" /></span>
                                    </c:when>
                                    
                                    <c:when test="${not empty uploadedFile.temporaryMetadata.fileName}">
                                        <i class="${uploadedFile.temporaryMetadata.icon}"></i>
                                        <span>${uploadedFile.temporaryMetadata.fileName}</span>
                                    </c:when>
                                    
                                    <c:when test="${not empty uploadedFile.originalMetadata}">
                                        <i class="${uploadedFile.originalMetadata.icon}"></i>
                                        <span>${uploadedFile.originalMetadata.fileName}</span>
                                    </c:when>
                                    
                                    <c:otherwise>
                                        <span class="text-muted"><op:translate key="NO_FILE" /></span>
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                        
                        <div class="media-right media-middle">
                            <div class="text-nowrap">
                                <!-- Upload -->
                                <label class="btn btn-default btn-sm btn-file">
                                    <i class="halflings halflings-folder-open"></i>
                                    <span class="hidden-xs"><op:translate key="FILE_UPLOAD"/></span>
                                    <input type="file" name="${path}" data-change-submit="${namespace}-upload-file">
                                </label>
                                
                                <!-- Delete -->
                                <button type="submit" name="delete-file" value="${field.name}" class="btn btn-default btn-sm" ${empty uploadedFile.originalMetadata and empty uploadedFile.temporaryMetadata ? 'disabled' : ''}>
                                    <i class="halflings halflings-trash"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                </c:when>
                
                <c:when test="${fieldType eq 'PICTURE'}">
                    <c:set var="path" value="uploadedFiles['${field.name}'].upload" />
                    <c:set var="uploadedFile" value="${form.uploadedFiles[field.name]}" />
                    <c:set var="imageErrorMessage"><op:translate key="IMAGE_ERROR_MESSAGE" /></c:set>
                
                    <form:label path="${path}" cssClass="col-sm-3 col-lg-2 control-label">${field.superLabel}</form:label>
                    <div class="col-sm-9 col-lg-10">
                        <div class="thumbnail thumbnail-edition no-margin-bottom text-center">
                            <c:choose>
                                <c:when test="${uploadedFile.deleted}">
                                    <span class="text-muted"><op:translate key="DELETED_PICTURE" /></span>
                                </c:when>
                                
                                <c:when test="${not empty uploadedFile.temporaryFile}">
                                    <jsp:useBean id="currentDate" class="java.util.Date"/>
                                    <portlet:resourceURL id="picture-preview" var="url">
                                        <portlet:param name="variableName" value="${field.name}" />
                                        <portlet:param name="ts" value="${currentDate.time}" />
                                    </portlet:resourceURL>
                                    
                                    <img src="${url}" alt="${uploadedFile.temporaryMetadata.fileName}" data-error-message="${imageErrorMessage}">
                                </c:when>
                                
                                <c:when test="${not empty uploadedFile.url}">
                                    <img src="${uploadedFile.url}" alt="${uploadedFile.originalMetadata.fileName}" data-error-message="${imageErrorMessage}">
                                </c:when>
                                
                                <c:otherwise>
                                    <span class="text-muted"><op:translate key="NO_PICTURE" /></span>
                                </c:otherwise>
                            </c:choose>
                            
                            <div class="caption">
                                <!-- Upload -->
                                <label class="btn btn-default btn-sm btn-file">
                                    <i class="halflings halflings-folder-open"></i>
                                    <span><op:translate key="FILE_UPLOAD"/></span>
                                    <input type="file" name="${path}" data-change-submit="${namespace}-upload-file">
                                </label>
                                
                                <!-- Delete -->
                                <button type="submit" name="delete-file" value="${field.name}" class="btn btn-default btn-sm" ${empty uploadedFile.originalMetadata and empty uploadedFile.temporaryMetadata ? 'disabled' : ''}>
                                    <i class="halflings halflings-trash"></i>
                                </button>
                            </div>
                        </div>
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
                  
                  <c:when test="${fieldType eq 'FILE'}">
                    <c:set var="file" value="${form.uploadedFiles[field.name]}" />
                                    
                    <c:choose>
                        <c:when test="${empty file}">
                            <c:remove var="fieldValue" />
                        </c:when>
                        
                        <c:otherwise>
                            <c:set var="fieldValue"><i class="${file.originalMetadata.icon}"></i> <a href="${file.url}" target="_blank" class="no-ajax-link"><span>${file.originalMetadata.fileName}</span></a></c:set>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                
                <c:when test="${fieldType eq 'PICTURE'}">
                    <c:set var="file" value="${form.uploadedFiles[field.name]}" />
                
                    <c:choose>
                        <c:when test="${empty file}">
                            <c:remove var="fieldValue" />
                        </c:when>
                        
                        <c:otherwise>
                            <c:set var="fieldValue"><img src="${file.url}" alt="${file.originalMetadata.fileName}" class="img-responsive" data-error-message="${imageErrorMessage}"></c:set>
                        </c:otherwise>
                    </c:choose>
                </c:when>
              </c:choose>
          
              <label class="col-sm-3 col-lg-2 control-label">${field.superLabel}</label>
              <div class="col-sm-9 col-lg-10">
                  <div class="form-control-static ${fieldType eq 'TEXTAREA' ? 'text-pre-wrap' : ''}">${fieldValue}</div>
              </div>
          </c:otherwise>
      </c:choose>
    
	<c:forEach var="pathPart" items="${field.path}" varStatus="status">
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
