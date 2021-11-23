package org.osivia.services.procedure.portlet.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.procedure.portlet.command.CreateDocumentCommand;
import org.osivia.services.procedure.portlet.command.DeleteDocumentCommand;
import org.osivia.services.procedure.portlet.command.ListModelsContainerCommand;
import org.osivia.services.procedure.portlet.command.ListProcedureInstancesByModelListCommand;
import org.osivia.services.procedure.portlet.command.ListProceduresModelsCommand;
import org.osivia.services.procedure.portlet.command.ListRecordTypesCommand;
import org.osivia.services.procedure.portlet.command.ListRecordsCommand;
import org.osivia.services.procedure.portlet.command.LoadVocabularyCommand;
import org.osivia.services.procedure.portlet.command.RetrieveDocumentByIdCommand;
import org.osivia.services.procedure.portlet.command.RetrieveProcedureByStepNameCommand;
import org.osivia.services.procedure.portlet.command.UpdateDocumentCommand;
import org.osivia.services.procedure.portlet.controller.ProcedurePortletAdminController;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;
import org.osivia.services.procedure.portlet.model.Field;
import org.osivia.services.procedure.portlet.model.Form;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.ProcedureUploadedFile;
import org.osivia.services.procedure.portlet.model.ProcedureUploadedFileMetadata;
import org.osivia.services.procedure.portlet.model.Record;
import org.osivia.services.procedure.portlet.model.Step;
import org.osivia.services.procedure.portlet.model.VariableTypesAllEnum;
import org.osivia.services.procedure.portlet.model.WebIdException;
import org.osivia.services.procedure.portlet.service.IProcedureService;
import org.osivia.services.procedure.portlet.util.VocabularySelect2Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * service for use in procedure and records
 * 
 * @author Dorian Licois
 */
@Service
public class ProcedureServiceImpl implements IProcedureService {

    private static final Pattern WEBID_ERROR = Pattern.compile("WebId: .+ already exists\\.");

    /** Temporary file prefix. */
    private static final String TEMPORARY_FILE_PREFIX = "procedure-file-";
    /** Temporary file suffix. */
    private static final String TEMPORARY_FILE_SUFFIX = ".tmp";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Nuxeo service. */
    @Autowired
    private INuxeoService nuxeoService;

    /** Document DAO. */
    @Autowired
    private DocumentDAO documentDao;


    /**
     * Constructor.
     */
    public ProcedureServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ProcedureModel createProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel, String procedurepath)
            throws PortletException, WebIdException {
        INuxeoCommand command;
        try {
            command = new ListModelsContainerCommand(procedurepath);
            final Document container = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);

            DocumentTypeEnum type = StringUtils.isNotBlank(procedureModel.getProcedureType()) ? DocumentTypeEnum.get(procedureModel.getProcedureType())
                    : DocumentTypeEnum.PROCEDUREMODEL;

            command = new CreateDocumentCommand(container, buildProperties(nuxeoController.getPortalCtx(), procedureModel), type);

            final Document procedureModelInstance = (Document) nuxeoController.executeNuxeoCommand(command);

            procedureModel = new ProcedureModel(procedureModelInstance, nuxeoController);
            return procedureModel;
        } catch (final NuxeoException e) {
            String errorMessage = ExceptionUtils.getRootCauseMessage(e);
            if (WEBID_ERROR.matcher(errorMessage).matches()) {
                throw new WebIdException();
            } else {
                throw new PortletException(e);
            }
        } catch (final Exception e) {
            throw new PortletException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ProcedureModel retrieveProcedureByWebId(NuxeoController nuxeoController, String webId) throws PortletException {
        NuxeoController nuxeoController2 = new NuxeoController(nuxeoController.getPortalCtx());
        nuxeoController2.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController2.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

        NuxeoDocumentContext documentContext = nuxeoController2.getDocumentContext(webId);
        return new ProcedureModel(documentContext.getDenormalizedDocument(), nuxeoController2);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProcedureInstance> retrieveProceduresInstanceByModel(NuxeoController nuxeoController, ProcedureModel procedureModel) {
        INuxeoCommand command = new ListProcedureInstancesByModelListCommand(procedureModel.getPath(), procedureModel.getCurrentWebId(), false);
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);
        List<ProcedureInstance> procedureInstanceList = new ArrayList<ProcedureInstance>(documents.size());
        for (Document document : documents) {
            ProcedureInstance procedureInstance = new ProcedureInstance(document);
            procedureInstance.setUrl(nuxeoController.getLink(document).getUrl());
            procedureInstanceList.add(procedureInstance);
        }
        return procedureInstanceList;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ProcedureModel updateProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException, WebIdException {
        ProcedureModel model;
        
        try {
            String webId = procedureModel.getCurrentWebId();
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(webId);
            Document denormalizedDocument = documentContext.getDenormalizedDocument();
            INuxeoCommand command = new UpdateDocumentCommand(denormalizedDocument, buildProperties(nuxeoController.getPortalCtx(), procedureModel));

            Document procedureModelInstance = (Document) nuxeoController.executeNuxeoCommand(command);

            model = new ProcedureModel(procedureModelInstance, nuxeoController);
        } catch (final NuxeoException e) {
            String errorMessage = ExceptionUtils.getRootCauseMessage(e);
            if ((errorMessage != null) && WEBID_ERROR.matcher(errorMessage).matches()) {
                throw new WebIdException();
            } else {
                throw new PortletException(e);
            }

        } catch (final Exception e) {
            throw new PortletException(e);
        }
        
        return model;
    }


    /**
     * Build a PropertyMap holding the data of procedure model.
     * 
     * @param portalControllerContext portal controller context
     * @param procedureModel procedure model
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private PropertyMap buildProperties(PortalControllerContext portalControllerContext, ProcedureModel procedureModel) throws PortletException {
        // Forms service
        IFormsService formsService = this.nuxeoService.getFormsService();

        PropertyMap propMap = new PropertyMap();
        try {
            propMap.set("dc:title", procedureModel.getName());
            String newWebId = StringUtils.isNotBlank(procedureModel.getNewWebId()) ? IFormsService.FORMS_WEB_ID_PREFIX + procedureModel.getNewWebId() : null;
            if (newWebId != null) {
                propMap.set("ttc:webid", newWebId);
            }
            propMap.set("pcd:webIdParent", procedureModel.getWebIdParent());
            propMap.set("pcd:steps", formsService.convertToJson(portalControllerContext, procedureModel.getSteps()));
            procedureModel.updateVariables();
            propMap.set("pcd:globalVariablesDefinitions", formsService.convertToJson(portalControllerContext, procedureModel.getVariables().values()));
            propMap.set("pcd:startingStep", procedureModel.getStartingStep());
            propMap.set("pcd:procedureObjects", formsService.convertToJson(portalControllerContext, procedureModel.getProcedureObjects()));
            propMap.set("pcd:dashboards", formsService.convertToJson(portalControllerContext, procedureModel.getDashboards()));
            
            Map<String, String> rules = new HashMap<>();
            rules.put("rules", procedureModel.getRules());
            propMap.set("pcd:businessRules", formsService.convertToJson(portalControllerContext, rules));
            
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return propMap;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException {
        try {
            String webId = procedureModel.getCurrentWebId();
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(webId);
            Document currentDocument = documentContext.getDenormalizedDocument();
            INuxeoCommand command = new DeleteDocumentCommand(currentDocument);
            nuxeoController.executeNuxeoCommand(command);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ProcedureInstance retrieveProcedureInstanceByWebId(NuxeoController nuxeoController, String webId) throws PortletException {
        ProcedureInstance procedureInstance;

        int savedAuthType = nuxeoController.getAuthType();
        int savedCacheType = nuxeoController.getCacheType();

        try {
            nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
            nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);

            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(webId);
            Document denormalizedDocument = documentContext.getDenormalizedDocument();

            procedureInstance = new ProcedureInstance(denormalizedDocument);
        } catch (Exception e) {
            throw new PortletException(e);
        } finally {
            nuxeoController.setAuthType(savedAuthType);
            nuxeoController.setCacheType(savedCacheType);
        }

        return procedureInstance;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Record retrieveRecordInstanceByWebId(NuxeoController nuxeoController, String webId) throws PortletException {
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(webId);
        return new Record(documentContext.getDenormalizedDocument());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ProcedureInstance retrieveProcedureInstanceById(NuxeoController nuxeoController, String uuid) throws PortletException {

        INuxeoCommand command = new RetrieveDocumentByIdCommand(uuid);
        final Document currentDocument = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);
        PropertyMap properties = currentDocument.getProperties();
        PropertyMap procedureInstanceMap = properties.getMap("nt:pi");
        String piWebId = procedureInstanceMap.getString("ttc:webid");
        return retrieveProcedureInstanceByWebId(nuxeoController, piWebId);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProcedureModel> listProcedures(NuxeoController nuxeoController, String procedurepath) throws PortletException {
        INuxeoCommand command;
        final List<ProcedureModel> procedureModels = new ArrayList<ProcedureModel>();
        Documents documentList;
        try {
            command = new ListModelsContainerCommand(procedurepath);
            documentList = (Documents) nuxeoController.executeNuxeoCommand(command);
            if ((documentList != null) && !documentList.isEmpty()) {
                Document modelsContainer = documentList.get(0);
                command = new ListProceduresModelsCommand(modelsContainer.getPath());
                documentList = (Documents) nuxeoController.executeNuxeoCommand(command);
                if ((documentList != null) && !documentList.isEmpty()) {
                    ProcedureModel procedureModel;
                    for (final Document document : documentList) {
                        procedureModel = new ProcedureModel(document, nuxeoController);
                        try {
                            procedureModel.setUrl(getEditUrl(nuxeoController, procedureModel, procedurepath));
                        } catch (final PortalException e) {
                            new PortletException(e);
                        }
                        procedureModels.add(procedureModel);
                    }
                    // Collections.sort(procedureModels);
                }
            }
        } catch (final Exception e) {
            throw new PortletException(e);
        }

        return procedureModels;
    }


    /**
     * @param nuxeoController
     * @param portalUrlFactory
     * @param procedureModel
     * @throws PortalException
     */
    private String getEditUrl(NuxeoController nuxeoController, ProcedureModel procedureModel, String procedurePath) throws PortalException {

        String startPortletUrl = null;
        if (StringUtils.isNotBlank(procedurePath) && nuxeoController.getDocumentContext(procedurePath).getPermissions().isEditable()) {
            Map<String, String> windowProperties;
            if (StringUtils.equals(procedureModel.getProcedureType(), DocumentTypeEnum.RECORDFOLDER.getDocType())) {
                windowProperties = getWindowProperties(procedurePath, "adminrecord", procedureModel.getProcedureType());
            } else {
                windowProperties = getWindowProperties(procedurePath, "adminproc", procedureModel.getProcedureType());
            }

            windowProperties.put("osivia.services.procedure.webid", procedureModel.getCurrentWebId());
            windowProperties.put("osivia.title", "Éditer une procedure");
            startPortletUrl = nuxeoController.getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(),
                    "osivia-services-procedure-portletInstance", windowProperties, PortalUrlType.DEFAULT);
        }

        return startPortletUrl;
    }


    /**
     * @param nuxeoController
     * @param portalUrlFactory
     * @param procedureModel
     * @throws PortalException
     */
    private Map<String, String> getWindowProperties(String procedurePath, String displayContext, String procedureType) throws PortalException {
        final Map<String, String> windowProperties = new HashMap<String, String>();
        windowProperties.put("osivia.doctype", procedureType);
        windowProperties.put(ProcedurePortletAdminController.PROCEDURE_PATH_KEY, procedurePath);
        windowProperties.put("osivia.hideDecorators", "1");
        windowProperties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, Constants.PORTLET_VALUE_ACTIVATE);
        windowProperties.put("osivia.ajaxLink", "1");
        windowProperties.put("osivia.procedure.admin", displayContext);
        return windowProperties;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getAddUrl(NuxeoController nuxeoController, String procedurePath, String displayContext, String procedureType) throws PortletException {
        try {

            String startPortletUrl = null;
            if (StringUtils.isNotBlank(procedurePath) && nuxeoController.getDocumentContext(procedurePath).getPermissions().isEditable()) {
                final Map<String, String> windowProperties = getWindowProperties(procedurePath, displayContext, procedureType);
                windowProperties.put("osivia.title", "Créer une procedure");
                startPortletUrl = nuxeoController.getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(),
                        "osivia-services-procedure-portletInstance", windowProperties, PortalUrlType.DEFAULT);
            }
            return startPortletUrl;
        } catch (final PortalException e) {
            throw new PortletException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getCloseUrl(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Portal window
        PortalWindow window = WindowFactory.getWindow(request);

        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Contextualization indicator
//        boolean contextualization = "1".equals(window.getProperty("osivia.cms.contextualization"));

        // Close URL
        String url;
//        try {
//            if (contextualization) {
//                // Destroy current page URL
//                url = nuxeoController.getPortalUrlFactory().getDestroyCurrentPageUrl(portalControllerContext);
//            } else {
                // Back URL
                url = nuxeoController.getPortalUrlFactory().getBackURL(portalControllerContext, false, false);
 //           }
//        } catch (PortalException e) {
//            throw new PortletException(e);
//        }

        return url;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Map<String, String>> retrieveStepsByName(NuxeoController nuxeoController, String filter) {
        Documents procedures = (Documents) nuxeoController.executeNuxeoCommand(new RetrieveProcedureByStepNameCommand(filter));
        List<Map<String, String>> results = null;
        Map<String, String> stepResult;
        if (procedures != null) {
            results = new ArrayList<Map<String, String>>(procedures.size());
            stepResult = new HashMap<String, String>(2);
            ProcedureModel procedureModel;
            for (Document document : procedures) {
                procedureModel = new ProcedureModel(document, nuxeoController);
                for (Step step : procedureModel.getSteps()) {
                    if (StringUtils.contains(step.getStepName(), filter)) {
                        stepResult.put("id", step.getReference());
                        stepResult.put("text", step.getStepName());
                        results.add(stepResult);
                    }
                }
            }
        }
        return results;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<ProcedureModel> retrieveProcedureModels(NuxeoController nuxeoController, String procedurepath, String filter) throws PortletException {
        final List<ProcedureModel> procedureModels = new ArrayList<ProcedureModel>();

        Documents documentList = null;
        if (StringUtils.isNotBlank(filter)) {
            filter = "AND dc:title LIKE '%".concat(filter).concat("%'");
            documentList = (Documents) nuxeoController.executeNuxeoCommand(new ListProceduresModelsCommand(procedurepath, filter));

        } else {
            documentList = (Documents) nuxeoController.executeNuxeoCommand(new ListProceduresModelsCommand(procedurepath));
        }
        if (documentList != null) {
            for (Document document : documentList) {
                procedureModels.add(new ProcedureModel(document, nuxeoController));
            }
        }
        return procedureModels;
    }


    /**
     * Update files.
     * 
     * @param nuxeoController Nuxeo controller
     * @param form form
     */
    private void updateFiles(NuxeoController nuxeoController, Form form) {
        // Procedure instance
        ProcedureInstance procedureInstance = form.getProcedureInstance();
        // Record
        Record record = form.getRecord();

        // Document
        Document document;
        if (record != null) {
            document = record.getOriginalDocument();
        } else if (procedureInstance != null) {
            document = procedureInstance.getOriginalDocument();
        } else {
            document = null;
        }

        if (document != null) {
            // Variables
            Map<String, String> variables;
            if (record != null) {
                variables = record.getGlobalVariablesValues();
            } else if (procedureInstance != null) {
                variables = procedureInstance.getGlobalVariablesValues();
            } else {
                variables = null;
            }

            // Files
            PropertyList files = document.getProperties().getList("files:files");

            // Fields
            List<Field> fields = form.getTheCurrentStep().getFields();

            if (MapUtils.isNotEmpty(variables) && (files != null) && !files.isEmpty() && CollectionUtils.isNotEmpty(fields)) {
                // Uploaded files
                Map<String, ProcedureUploadedFile> uploadedFiles = new HashMap<>();

                for (Field field : fields) {
                    String value = variables.get(field.getName());
                    if (StringUtils.isNotEmpty(value)) {
                        updateFieldFile(nuxeoController, document, files, uploadedFiles, field, null, value);
                    }
                }

                form.getUploadedFiles().putAll(uploadedFiles);
            }
        }
    }


    /**
     * Update field file.
     * 
     * @param nuxeoController Nuxeo controller
     * @param document Nuxeo document
     * @param files files
     * @param uploadedFiles uploaded files
     * @param field field
     * @param index row index
     * @param value row value
     */
    private void updateFieldFile(NuxeoController nuxeoController, Document document, PropertyList files, Map<String, ProcedureUploadedFile> uploadedFiles,
            Field field, Integer index, String value) {
        if (VariableTypesAllEnum.FILE.equals(field.getType()) || VariableTypesAllEnum.PICTURE.equals(field.getType())) {
            // JSON value
            JSONObject jsonValue;
            if (StringUtils.isEmpty(value)) {
                jsonValue = null;
            } else {
                try {
                    jsonValue = JSONObject.fromObject(value);
                } catch (JSONException e) {
                    jsonValue = null;
                }
            }

            if ((jsonValue != null) && !jsonValue.isNullObject()) {
                // Uploaded file
                ProcedureUploadedFile uploadedFile = jsonObjectToFile(nuxeoController, document, files, jsonValue);
                if (uploadedFile != null) {
                    String uploadedFileKey = field.getName();
                    if (index != null) {
                        uploadedFileKey = getFileKey(Integer.toString(index), field);
                    }
                    uploadedFiles.put(uploadedFileKey, uploadedFile);
                }
            }
        } else if (VariableTypesAllEnum.FIELDLIST.equals(field.getType())) {
            // JSON array
            JSONArray jsonArray;
            if (StringUtils.isEmpty(value)) {
                jsonArray = null;
            } else {
                try {
                    jsonArray = JSONArray.fromObject(value);
                } catch (JSONException e) {
                    jsonArray = null;
                }
            }

            if ((jsonArray != null) && !jsonArray.isEmpty()) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    // JSON object
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    for (Field nestedField : field.getFields()) {
                        if (jsonObject.has(nestedField.getName())) {
                            String nestedValue = jsonObject.getString(nestedField.getName());
                            if (StringUtils.isNotEmpty(nestedValue)) {
                                this.updateFieldFile(nuxeoController, document, files, uploadedFiles, nestedField, i, nestedValue);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Transform JSON object to uploaded file.
     * 
     * @param nuxeoController Nuxeo controller
     * @param document Nuxeo document
     * @param files
     * @param fieldName
     * @param uploadedFiles
     * @param jsonObject
     */
    private ProcedureUploadedFile jsonObjectToFile(NuxeoController nuxeoController, Document document, PropertyList files, JSONObject jsonObject) {
        String fileName = jsonObject.getString("fileName");
        String digest = jsonObject.getString("digest");

        PropertyMap file = null;
        int index = 0;
        while ((file == null) && (index < files.size())) {
            PropertyMap map = files.getMap(index).getMap("file");
            if (StringUtils.equals(map.getString("name"), fileName) && StringUtils.equals(map.getString("digest"), digest)) {
                file = map;
            } else {
                index++;
            }
        }


        // Uploaded file
        ProcedureUploadedFile uploadedFile;

        if (file == null) {
            uploadedFile = null;
        } else {
            // URL
            String url = nuxeoController.createAttachedFileLink(document.getPath(), String.valueOf(index));
            // MIME type
            MimeType mimeType;
            try {
                mimeType = new MimeType(file.getString("mime-type"));
            } catch (Exception e) {
                mimeType = null;
            }
            // Type icon
            String icon = this.documentDao.getIcon(mimeType);

            uploadedFile = this.applicationContext.getBean(ProcedureUploadedFile.class);
            uploadedFile.setUrl(url);
            uploadedFile.setIndex(index);

            ProcedureUploadedFileMetadata metadata = this.applicationContext.getBean(ProcedureUploadedFileMetadata.class);
            metadata.setFileName(fileName);
            metadata.setMimeType(mimeType);
            metadata.setIcon(icon);
            uploadedFile.setOriginalMetadata(metadata);
        }

        return uploadedFile;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateData(NuxeoController nuxeoController, Form form) throws PortletException {
        updateFiles(nuxeoController, form);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray searchVocabularyValues(PortalControllerContext portalControllerContext, String vocabularyId, String filter) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        nuxeoController.setCacheTimeOut(TimeUnit.HOURS.toMillis(1));
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT);

        // Search results
        JSONArray results = new JSONArray();

        // Nuxeo command
        INuxeoCommand command = new LoadVocabularyCommand(vocabularyId);

        Object object = nuxeoController.executeNuxeoCommand(command);
        if (object instanceof Blob) {
            Blob blob = (Blob) object;
            try {
                String content = IOUtils.toString(blob.getStream(), CharEncoding.UTF_8);
                JSONArray array = JSONArray.fromObject(content);
                results = VocabularySelect2Util.parse(array, filter);
            } catch (IOException e) {
                throw new PortletException(e);
            }
        }

        return results;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getRecordTypes(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // CMS base path
        String basePath = nuxeoController.getBasePath();

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(ListRecordTypesCommand.class, basePath);

        // Documents
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        // Record types
        Map<String, String> recordTypes = new HashMap<>(documents.size());

        for (Document document : documents) {
            String webId = document.getString("ttc:webid");
            String displayName = StringUtils.defaultIfBlank(document.getTitle(), document.getId());

            if (StringUtils.isNotEmpty(webId)) {
                recordTypes.put(webId, displayName);
            }
        }

        return recordTypes;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray searchRecords(PortalControllerContext portalControllerContext, String recordFolderWebId, String filter) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // CMS base path
        String basePath = nuxeoController.getBasePath();

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(ListRecordsCommand.class, basePath, recordFolderWebId, filter);

        // Documents
        Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

        // Search results
        JSONArray results = new JSONArray();

        for (Document document : documents) {
            JSONObject object = new JSONObject();
            object.put("id", document.getString("ttc:webid"));
            object.put("text", StringUtils.defaultIfBlank(document.getTitle(), document.getId()));
            results.add(object);
        }

        return results;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void uploadFile(PortalControllerContext portalControllerContext, Form form) throws PortletException, IOException {
        // Uploaded files
        Map<String, ProcedureUploadedFile> uploadedFiles = form.getUploadedFiles();

        for (Entry<String, ProcedureUploadedFile> entry : uploadedFiles.entrySet()) {
            ProcedureUploadedFile uploadedFile = entry.getValue();

            MultipartFile multipartFile = uploadedFile.getUpload();
            if ((multipartFile != null) && !multipartFile.isEmpty()) {
                // Temporary file
                File temporaryFile = uploadedFile.getTemporaryFile();
                if (temporaryFile != null) {
                    // Delete temporary file
                    temporaryFile.delete();
                }
                temporaryFile = File.createTempFile(TEMPORARY_FILE_PREFIX, TEMPORARY_FILE_SUFFIX);
                temporaryFile.deleteOnExit();
                multipartFile.transferTo(temporaryFile);
                uploadedFile.setTemporaryFile(temporaryFile);
                
                

                // Temporary file metadata
                ProcedureUploadedFileMetadata metadata = this.applicationContext.getBean(ProcedureUploadedFileMetadata.class);

                // File name
                metadata.setFileName(multipartFile.getOriginalFilename());
                // Mime type
                MimeType mimeType;
                try {
                    mimeType = new MimeType(multipartFile.getContentType());
                } catch (MimeTypeParseException e) {
                    mimeType = null;
                }
                metadata.setMimeType(mimeType);
                // Icon
                String icon = this.documentDao.getIcon(mimeType);
                metadata.setIcon(icon);

                uploadedFile.setTemporaryMetadata(metadata);
                uploadedFile.setDeleted(false);
                
     
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFile(PortalControllerContext portalControllerContext, Form form, String variableName) throws PortletException, IOException {
        // Uploaded files
        Map<String, ProcedureUploadedFile> uploadedFiles = form.getUploadedFiles();



        // Uploaded file
        String uploadedFileKey = variableName;
/*        
        if (StringUtils.isNotEmpty(rowIndex)) {
            uploadedFileKey += "|" + rowIndex;
        }
*/        
        ProcedureUploadedFile uploadedFile = uploadedFiles.get(uploadedFileKey);

        if (uploadedFile != null) {
            // Temporary file
            File temporaryFile = uploadedFile.getTemporaryFile();
            if (temporaryFile != null) {
                // Delete temporary file
                temporaryFile.delete();
                uploadedFile.setTemporaryFile(null);
            }

            // Delete temporary file metadata
            uploadedFile.setTemporaryMetadata(null);

            uploadedFile.setDeleted(true);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void picturePreview(PortalControllerContext portalControllerContext, Form form, String variableName) throws PortletException, IOException {
        // Resource response
        ResourceResponse response = (ResourceResponse) portalControllerContext.getResponse();

        // Uploaded files
        Map<String, ProcedureUploadedFile> uploadedFiles = form.getUploadedFiles();

        // Uploaded file
        ProcedureUploadedFile uploadedFile = uploadedFiles.get(variableName);

        // Output stream
        OutputStream outputStream = response.getPortletOutputStream();

        if (uploadedFile != null) {
            // Temporary file
            File temporaryFile = uploadedFile.getTemporaryFile();

            // Upload size
            Long size = new Long(temporaryFile.length());
            response.setContentLength(size.intValue());

            // Content type
            String contentType = response.getContentType();
            response.setContentType(contentType);

            // Character encoding
            response.setCharacterEncoding(CharEncoding.UTF_8);

            // No cache
            response.getCacheControl().setExpirationTime(0);


            // Input steam
            InputStream inputSteam = new FileInputStream(temporaryFile);

            // Copy
            IOUtils.copy(inputSteam, outputStream);
            IOUtils.closeQuietly(inputSteam);
        }

        IOUtils.closeQuietly(outputStream);
    }

    
	public static String getFileKey(String rowIndex, Field nestedField) {
		return nestedField.getName()+"_"+nestedField.getPath() + "|" + rowIndex;
	}
    
}
