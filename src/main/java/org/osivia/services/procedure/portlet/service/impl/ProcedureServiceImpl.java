package org.osivia.services.procedure.portlet.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.procedure.portlet.adapter.ProcedureJSONAdapter;
import org.osivia.services.procedure.portlet.command.CreateDocumentCommand;
import org.osivia.services.procedure.portlet.command.DeleteDocumentCommand;
import org.osivia.services.procedure.portlet.command.ListModelsContainerCommand;
import org.osivia.services.procedure.portlet.command.ListProcedureInstancesByModelListCommand;
import org.osivia.services.procedure.portlet.command.ListProceduresModelsCommand;
import org.osivia.services.procedure.portlet.command.LoadVocabularyCommand;
import org.osivia.services.procedure.portlet.command.RetrieveDocumentByIdCommand;
import org.osivia.services.procedure.portlet.command.RetrieveDocumentByWebIdCommand;
import org.osivia.services.procedure.portlet.command.RetrieveProcedureByStepNameCommand;
import org.osivia.services.procedure.portlet.command.UpdateDocumentCommand;
import org.osivia.services.procedure.portlet.controller.ProcedurePortletAdminController;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;
import org.osivia.services.procedure.portlet.model.Field;
import org.osivia.services.procedure.portlet.model.FilePath;
import org.osivia.services.procedure.portlet.model.Form;
import org.osivia.services.procedure.portlet.model.ObjetMetier;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.Record;
import org.osivia.services.procedure.portlet.model.Step;
import org.osivia.services.procedure.portlet.model.Variable;
import org.osivia.services.procedure.portlet.model.VariableTypesAllEnum;
import org.osivia.services.procedure.portlet.model.WebIdException;
import org.osivia.services.procedure.portlet.service.IProcedureService;
import org.osivia.services.procedure.portlet.util.ObjetMetierUtil;
import org.osivia.services.procedure.portlet.util.VocabularySelect2Util;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyEntry;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyHelper;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * service for use in procedure and records
 * 
 * @author Dorian Licois
 */
@Service
public class ProcedureServiceImpl implements IProcedureService {

    private static final Pattern WEBID_ERROR = Pattern.compile("WebId: .+ already exists\\.");

    @Override
    public ProcedureModel createProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel, String procedurepath) throws PortletException,
            WebIdException {

        INuxeoCommand command;
        try {
            command = new ListModelsContainerCommand(procedurepath);
            final Document container = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);


            String webId = StringUtils.isBlank(procedureModel.getNewWebId()) ? null : IFormsService.FORMS_WEB_ID_PREFIX + procedureModel.getNewWebId();

            DocumentTypeEnum type = StringUtils.isNotBlank(procedureModel.getProcedureType()) ? DocumentTypeEnum.get(procedureModel.getProcedureType())
                    : DocumentTypeEnum.PROCEDUREMODEL;

            command = new CreateDocumentCommand(container, buildProperties(procedureModel), type);

            final Document procedureModelInstance = (Document) nuxeoController.executeNuxeoCommand(command);

            return new ProcedureModel(procedureModelInstance, nuxeoController);

        } catch (final NuxeoException e) {

            Throwable rootCause = getRootCause(e);
            String errorMessage = rootCause.getMessage();

            if (WEBID_ERROR.matcher(errorMessage).matches()) {
                throw new WebIdException();
            } else {
                throw new PortletException(e);
            }

        } catch (final Exception e) {
            throw new PortletException(e);
        }
    }

    @Override
    public ProcedureModel retrieveProcedureByWebId(NuxeoController nuxeoController, String webId) throws PortletException {
        INuxeoCommand command;
        Document document = null;
        ProcedureModel procedureModel = null;
        try {
            command = new RetrieveDocumentByWebIdCommand(webId);
            document = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);
            procedureModel = new ProcedureModel(document, nuxeoController);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        return procedureModel;
    }

    /**
     * Finds the root cause in a throwable
     * 
     * @param t
     * @return the root cause
     */
    private Throwable getRootCause(Throwable t) {
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return t;
    }

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

    @Override
    public ProcedureModel updateProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException, WebIdException {
        INuxeoCommand command;
        try {
            command = new RetrieveDocumentByWebIdCommand(procedureModel.getCurrentWebId());
            final Document currentDocument = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);
            command = new UpdateDocumentCommand(currentDocument, buildProperties(procedureModel));

            final Document procedureModelInstance = (Document) nuxeoController.executeNuxeoCommand(command);

            return new ProcedureModel(procedureModelInstance, nuxeoController);

        } catch (final NuxeoException e) {

            Throwable rootCause = getRootCause(e);
            String errorMessage = rootCause.getMessage();

            if (errorMessage != null && WEBID_ERROR.matcher(errorMessage).matches()) {
                throw new WebIdException();
            } else {
                throw new PortletException(e);
            }

        } catch (final Exception e) {
            throw new PortletException(e);
        }
    }

    /**
     * build a PropertyMap holding the data of ProcedureModel
     * 
     * @param procedureModel
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private PropertyMap buildProperties(ProcedureModel procedureModel) throws JsonGenerationException, JsonMappingException, IOException {
        final PropertyMap propMap = new PropertyMap();
        propMap.set("dc:title", procedureModel.getName());
        String webId = StringUtils.isNotBlank(procedureModel.getNewWebId()) ? IFormsService.FORMS_WEB_ID_PREFIX + procedureModel.getNewWebId() : null;
        if (webId != null) {
            propMap.set("ttc:webid", webId);
        }
        propMap.set("pcd:webIdParent", procedureModel.getWebIdParent());
        propMap.set("pcd:steps", ProcedureJSONAdapter.getInstance().toJSON(procedureModel.getSteps()));
        procedureModel.updateVariables();
        propMap.set("pcd:globalVariablesDefinitions", ProcedureJSONAdapter.getInstance().toJSON(procedureModel.getVariables().values()));
        propMap.set("pcd:startingStep", procedureModel.getStartingStep());
        propMap.set("pcd:procedureObjects", ProcedureJSONAdapter.getInstance().toJSON(procedureModel.getProcedureObjects()));
        propMap.set("pcd:dashboards", ProcedureJSONAdapter.getInstance().toJSON(procedureModel.getDashboards()));
        return propMap;
    }

    @Override
    public void deleteProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException {

        INuxeoCommand command;
        try {
            command = new RetrieveDocumentByWebIdCommand(procedureModel.getCurrentWebId());
            final Document currentDocument = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);
            command = new DeleteDocumentCommand(currentDocument);
            nuxeoController.executeNuxeoCommand(command);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
    }


    @Override
    public ProcedureInstance retrieveProcedureInstanceByWebId(NuxeoController nuxeoController, String webId) throws PortletException {

        INuxeoCommand command;
        ProcedureInstance procedureInstance = null;
        try {
            command = new RetrieveDocumentByWebIdCommand(webId);

            nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
            nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT);

            final Document currentDocument = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);
            procedureInstance = new ProcedureInstance(currentDocument);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        return procedureInstance;
    }
    
    @Override
    public Record retrieveRecordInstanceByWebId(NuxeoController nuxeoController, String webId) throws PortletException {
        INuxeoCommand command;
        Record record = null;
        try {
            command = new RetrieveDocumentByWebIdCommand(webId);
            final Document currentDocument = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);
            record = new Record(currentDocument);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        return record;
    }

    @Override
    public ProcedureInstance retrieveProcedureInstanceById(NuxeoController nuxeoController, String uuid) throws PortletException {

        INuxeoCommand command = new RetrieveDocumentByIdCommand(uuid);
        final Document currentDocument = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);
        PropertyMap properties = currentDocument.getProperties();
        PropertyMap procedureInstanceMap = properties.getMap("nt:pi");
        String piWebId = procedureInstanceMap.getString("ttc:webid");
        return retrieveProcedureInstanceByWebId(nuxeoController, piWebId);
    }



    @Override
    public List<ProcedureModel> listProcedures(NuxeoController nuxeoController, String procedurepath) throws PortletException {
        INuxeoCommand command;
        final List<ProcedureModel> procedureModels = new ArrayList<ProcedureModel>();
        Documents documentList;
        try {
            command = new ListModelsContainerCommand(procedurepath);
            documentList = (Documents) nuxeoController.executeNuxeoCommand(command);
            if (documentList != null && !documentList.isEmpty()) {
                Document modelsContainer = documentList.get(0);
                command = new ListProceduresModelsCommand(modelsContainer.getPath());
                documentList = (Documents) nuxeoController.executeNuxeoCommand(command);
            }
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        if (documentList != null && !documentList.isEmpty()) {
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
        }
        return procedureModels;
    }

    /**
     * @param nuxeoController
     * @param portalUrlFactory
     * @param procedureModel
     * @throws PortalException
     */
    private String getEditUrl(NuxeoController nuxeoController, ProcedureModel procedureModel, String procedurepath)
            throws PortalException {

        Map<String, String> windowProperties;
        if (StringUtils.equals(procedureModel.getProcedureType(), DocumentTypeEnum.RECORDFOLDER.getDocType())) {
            windowProperties = getWindowProperties(procedurepath, "adminrecord", procedureModel.getProcedureType());
        } else {
            windowProperties = getWindowProperties(procedurepath, "adminproc", procedureModel.getProcedureType());
        }

        windowProperties.put("osivia.services.procedure.webid", procedureModel.getCurrentWebId());
        windowProperties.put("osivia.title", "Éditer une procedure");
        return nuxeoController.getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(), "osivia-services-procedure-portletInstance",
                windowProperties, PortalUrlType.DEFAULT);
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

    @Override
    public String getAddUrl(NuxeoController nuxeoController, String procedurePath, String displayContext, String procedureType) throws PortletException {
        try {
            final Map<String, String> windowProperties = getWindowProperties(procedurePath, displayContext, procedureType);
            windowProperties.put("osivia.title", "Créer une procedure");
            return nuxeoController.getPortalUrlFactory().getStartPortletUrl(nuxeoController.getPortalCtx(), "osivia-services-procedure-portletInstance",
                    windowProperties, PortalUrlType.DEFAULT);
        } catch (final PortalException e) {
            throw new PortletException(e);
        }
    }

    private void updateFormWithObjectsValues(NuxeoController nuxeoController, Form form) throws PortletException {
        final Map<String, ObjetMetier> ojMap = new HashMap<String, ObjetMetier>();
        Step theCurrentStep = form.getTheCurrentStep();
        if (theCurrentStep != null) {
            for (final Field field : theCurrentStep.getFields()) {
                updateFormWithObjectsValues(field, ojMap, nuxeoController, form);
            }
        }
    }

    /**
     * permet de traverser la liste récursive en mettant à jour les objets métiers
     *
     * @param field
     * @param ojMap
     * @param nuxeoController
     * @param form
     * @throws PortletException
     */
    private void updateFormWithObjectsValues(Field field, Map<String, ObjetMetier> ojMap, NuxeoController nuxeoController, Form form) throws PortletException {

        if (!field.isFieldSet()) {
            final ObjetMetier objetMetier;
            if (ObjetMetierUtil.isObject(field.getName())) {
                if (form.getProcedureInstance().getProcedureObjects().containsKey(ObjetMetierUtil.getObjectName(field.getName()))) {
                    if (ojMap.containsKey(field.getName())) {
                        objetMetier = ojMap.get(field.getName());
                    } else {
                        INuxeoCommand command;
                        try {
                            command = new RetrieveDocumentByWebIdCommand(form.getProcedureInstance().getProcedureObjects()
                                    .get(ObjetMetierUtil.getObjectName(field.getName())).getProcedureObjectid());
                        } catch (final Exception e) {
                            throw new PortletException(e);
                        }
                        final Document objetMetierDocument = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);
                        nuxeoController.setDisplayLiveVersion("1");
                        final String downloadLink = nuxeoController.createFileLink(objetMetierDocument, "file:content");
                        final FilePath filePath = new FilePath();
                        filePath.setDownloadLink(downloadLink);
                        filePath.setFileName(objetMetierDocument.getString("file:filename"));
                        objetMetier = new ObjetMetier(objetMetierDocument, filePath);
                        ojMap.put(field.getName(), objetMetier);
                    }
                    if (ObjetMetierUtil.isContent(field.getName())) {
                        if (form.getProcedureInstance().getFilesPath().containsKey(field.getName())) {
                            form.getProcedureInstance().getFilesPath().get(field.getName()).setDownloadLink(objetMetier.getFilePath().getDownloadLink());
                        } else {
                            form.getProcedureInstance().getFilesPath().put(field.getName(), objetMetier.getFilePath());
                        }
                    } else {
                        form.getProcedureInstance().getGlobalVariablesValues()
                        .put(field.getName(), objetMetier.getProperties().getString(ObjetMetierUtil.getObjectProperty(field.getName())));
                    }
                }
            }
        } else {
            if (field.getFields() != null) {
                for (final Field nestedField : field.getFields()) {
                    updateFormWithObjectsValues(nestedField, ojMap, nuxeoController, form);
                }
            }
        }
    }

    private void updateVocabulariesWithValues(NuxeoController nuxeoController, Form form) throws PortletException {

        final Map<String, Variable> variables = form.getProcedureModel().getVariables();

        List<Map<String, String>> varOptions;
        for (final Entry<String, Variable> entryV : variables.entrySet()) {
            if (VariableTypesAllEnum.CHECKBOXVOCAB.equals(entryV.getValue().getType()) || VariableTypesAllEnum.RADIOVOCAB.equals(entryV.getValue().getType())) {
                varOptions = new ArrayList<Map<String, String>>();
                final VocabularyEntry vocabularyEntry = VocabularyHelper.getVocabularyEntry(nuxeoController, entryV.getValue().getVarOptions());
                Map<String, String> vocabEntry;
                for (final VocabularyEntry entry : vocabularyEntry.getChildren().values()) {
                    vocabEntry =new HashMap<String, String>(2);
                    vocabEntry.put("label", entry.getLabel());
                    vocabEntry.put("value", entry.getId());
                    varOptions.add(vocabEntry);
                }
                try {
                    entryV.getValue().setVarOptions(ProcedureJSONAdapter.getInstance().toJSON(varOptions));
                } catch (JsonGenerationException e) {
                    throw new PortletException(e);
                } catch (JsonMappingException e) {
                    throw new PortletException(e);
                } catch (IOException e) {
                    throw new PortletException(e);
                }
            }
        }
    }

    @Override
    public JSONArray getVocabularyValues(NuxeoController nuxeoController, String filter, String vocabularyName) throws PortletException {
        JSONArray values = new JSONArray();

        final INuxeoCommand command = new LoadVocabularyCommand(vocabularyName);
        final Object object = nuxeoController.executeNuxeoCommand(command);
        if (object instanceof Blob) {
            final Blob blob = (Blob) object;
            try {
                final String content = IOUtils.toString(blob.getStream(), "UTF-8");
                final JSONArray array = JSONArray.fromObject(content);
                values = VocabularySelect2Util.parse(array, filter);
            } catch (final IOException e) {
                throw new PortletException(e);
            }
        }


        return values;
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
        boolean contextualization = "1".equals(window.getProperty("osivia.cms.contextualization"));

        // Close URL
        String url;
        try {
            if (contextualization) {
                // Destroy current page URL
                url = nuxeoController.getPortalUrlFactory().getDestroyCurrentPageUrl(portalControllerContext);
            } else {
                // Back URL
                url = nuxeoController.getPortalUrlFactory().getBackURL(portalControllerContext, false);
            }
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }

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
     * update values of extended variables with options
     * 
     * @param form
     */
    public void updateVarsWithOptions(Form form) {
        Step theCurrentStep = form.getTheCurrentStep();
        if (theCurrentStep != null) {
            List<Field> fields = theCurrentStep.getFields();
            for (Field field : fields) {
                updateVarWithOptions(form, field);
            }
        }
    }

    /**
     * update values of an extended variable with options
     * 
     * @param form
     * @param field
     */
    private void updateVarWithOptions(Form form, Field field) {
        String variableValue = form.getProcedureInstance().getGlobalVariablesValues().get(field.getName());
        if (!field.isInput() && StringUtils.isNotBlank(variableValue)) {

            Map<String, String> varOptionsMap = null;
            String varOptions = field.getVarOptions();
            if (StringUtils.isNotBlank(varOptions)) {
                varOptions = StringUtils.substringBetween(varOptions, "[", "]");
                String[] varOptionT = StringUtils.splitByWholeSeparator(varOptions, "},{");

                varOptionsMap = new HashMap<String, String>(varOptionT.length);

                for (int j = 0; j < varOptionT.length; j++) {
                    String varOption = varOptionT[j];
                    String[] varOptionLV = StringUtils.split(varOption, ',');
                    if (varOptionLV != null) {
                        String varOptionValue = null;
                        String varOptionLabel = null;
                        for (int k = 0; k < varOptionLV.length; k++) {
                            String varOptionLVS = varOptionLV[k];
                            varOptionLVS = StringUtils.replaceChars(varOptionLVS, "\"{}", StringUtils.EMPTY);

                            if (StringUtils.startsWith(varOptionLVS, "label")) {
                                varOptionLabel = StringUtils.substringAfterLast(varOptionLVS, ":");
                            } else if (StringUtils.startsWith(varOptionLVS, "value")) {
                                varOptionValue = StringUtils.substringAfterLast(varOptionLVS, ":");
                            }
                        }
                        if (StringUtils.isNotBlank(varOptionValue) && StringUtils.isNotBlank(varOptionLabel)) {
                            varOptionsMap.put(varOptionValue, varOptionLabel);
                        }
                    }
                }
                String[] values = StringUtils.split(variableValue, ',');
                if (ArrayUtils.isNotEmpty(values)) {
                    for (int i = 0; i < values.length; i++) {
                        values[i] = varOptionsMap.get(values[i]) != null ? varOptionsMap.get(values[i]) : values[i];
                    }
                }
                variableValue = StringUtils.join(values, ',');
                form.getProcedureInstance().getGlobalVariablesValues().put(field.getName(), variableValue);
            }
        }
    }

    @Override
    public void updateData(NuxeoController nuxeoController, Form form) throws PortletException {
        updateFormWithObjectsValues(nuxeoController, form);
        updateVocabulariesWithValues(nuxeoController, form);
        updateVarsWithOptions(form);
    }

}
