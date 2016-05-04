package org.osivia.services.procedure.portlet.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Blobs;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.nuxeo.ecm.automation.client.model.StreamBlob;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.services.procedure.portlet.adapter.ProcedureJSONAdapter;
import org.osivia.services.procedure.portlet.command.CreateDocumentCommand;
import org.osivia.services.procedure.portlet.command.CreateDocumentFromBlobCommand;
import org.osivia.services.procedure.portlet.command.DeleteDocumentCommand;
import org.osivia.services.procedure.portlet.command.ListDocumentsCommand;
import org.osivia.services.procedure.portlet.command.RetrieveDocumentCommand;
import org.osivia.services.procedure.portlet.command.StartProcedureCommand;
import org.osivia.services.procedure.portlet.command.UpdateDocumentCommand;
import org.osivia.services.procedure.portlet.command.UpdateDocumentFromBlobCommand;
import org.osivia.services.procedure.portlet.command.UpdateProcedureCommand;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;
import org.osivia.services.procedure.portlet.model.Field;
import org.osivia.services.procedure.portlet.model.FilePath;
import org.osivia.services.procedure.portlet.model.Form;
import org.osivia.services.procedure.portlet.model.GlobalVariablesValuesType;
import org.osivia.services.procedure.portlet.model.ObjetMetier;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.ProcedureObjectInstance;
import org.osivia.services.procedure.portlet.model.Select2Entry;
import org.osivia.services.procedure.portlet.model.Variable;
import org.osivia.services.procedure.portlet.model.VariableTypesEnum;
import org.osivia.services.procedure.portlet.service.IProcedureService;
import org.osivia.services.procedure.portlet.util.ObjetMetierUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyEntry;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyHelper;

@Service
public class ProcedureServiceImpl implements IProcedureService {

    /** path containing the models */
    private static final String path = "/default-domain/procedures-models";

    /** defaultFIlesPath */
    private static final String defaultFIlesPath = "/default-domain/procedurefiles";


    @Override
    public ProcedureModel createProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException {

        INuxeoCommand command;
        try {
            command = new RetrieveDocumentCommand(path);
            final Document container = (Document) nuxeoController.executeNuxeoCommand(command);
            command = new CreateDocumentCommand(container, procedureModel.getName(), DocumentTypeEnum.PROCEDUREMODEL);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        final Document procedureModelInstance = (Document) nuxeoController.executeNuxeoCommand(command);

        return new ProcedureModel(procedureModelInstance);
    }

    @Override
    public ProcedureModel retrieveProcedureByPath(NuxeoController nuxeoController, String path) throws PortletException {
        INuxeoCommand command;
        Document document = null;
        ProcedureModel procedureModel = null;
        try {
            command = new RetrieveDocumentCommand(path);
            document = (Document) nuxeoController.executeNuxeoCommand(command);
            procedureModel = new ProcedureModel(document);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        return procedureModel;
    }

    @Override
    public ProcedureModel updateProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException {
        INuxeoCommand command;
        try {
            command = new RetrieveDocumentCommand(procedureModel.getPath());
            final Document currentDocument = (Document) nuxeoController.executeNuxeoCommand(command);
            final PropertyMap propMap = new PropertyMap();
            propMap.set("dc:title", procedureModel.getName());
            propMap.set("pcd:steps", ProcedureJSONAdapter.getInstance().toJSON(procedureModel.getSteps()));
            propMap.set("pcd:globalVariablesDefinitions", ProcedureJSONAdapter.getInstance().toJSON(procedureModel.getVariables().values()));
            propMap.set("pcd:startingStep", procedureModel.getStartingStep());
            propMap.set("pcd:procedureObjects", ProcedureJSONAdapter.getInstance().toJSON(procedureModel.getProcedureObjects()));
            command = new UpdateDocumentCommand(currentDocument, propMap);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        final Document procedureModelInstance = (Document) nuxeoController.executeNuxeoCommand(command);
        return new ProcedureModel(procedureModelInstance);
    }

    @Override
    public void deleteProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException {

        INuxeoCommand command;
        try {
            command = new RetrieveDocumentCommand(procedureModel.getPath());
            final Document currentDocument = (Document) nuxeoController.executeNuxeoCommand(command);
            command = new DeleteDocumentCommand(currentDocument);
            nuxeoController.executeNuxeoCommand(command);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
    }

    @Override
    public ProcedureInstance createProcedureInstance(NuxeoController nuxeoController, ProcedureModel procedureModel, ProcedureInstance procedureInstance,
            String taskTitle, List<String> groups) throws PortletException {

        // build objet metiers from the supplied values
        final Map<String, ObjetMetier> objetMetiers = ObjetMetierUtil.buildObjetMetiers(procedureModel, procedureInstance);

        // apply the actions
        applyActions(objetMetiers);

        // abstract action applied at every step
        defaultAction(nuxeoController, procedureModel, procedureInstance, objetMetiers);

        INuxeoCommand command;
        try {
            final PropertyMap propMap = new PropertyMap();
            final String groupsList = StringUtils.join(groups, ",");

            propMap.set("pi:currentStep", procedureInstance.getCurrentStep());
            propMap.set("pi:procedureModelPath", procedureModel.getPath());

            final List<GlobalVariablesValuesType> gvvList = new ArrayList<GlobalVariablesValuesType>();
            for (final Entry<String, String> entry : procedureInstance.getGlobalVariablesValues().entrySet()) {
                gvvList.add(new GlobalVariablesValuesType(entry.getKey(), entry.getValue()));
            }
            propMap.set("pi:globalVariablesValues", ProcedureJSONAdapter.getInstance().toJSON(gvvList));

            final Blobs blobs = new Blobs();
            for (final FilePath file : procedureInstance.getFilesPath().values()) {
                if (file.getFile().getSize() > 0) {
                    final InputStream in = new ByteArrayInputStream(file.getFile().getBytes());
                    final Blob blob = new StreamBlob(in, file.getFile().getOriginalFilename(), file.getFile().getContentType());
                    blobs.add(blob);
                }
            }
            propMap.set("pi:procedureObjectInstances", ProcedureJSONAdapter.getInstance().toJSON(procedureInstance.getProcedureObjects().values()));
            if (blobs.size() > 0) {
                propMap.set("pi:attachments", ProcedureJSONAdapter.getInstance().toJSON(procedureInstance.getFilesPath().values()));
                command = new StartProcedureCommand(taskTitle, groupsList, propMap, blobs);
            } else {
                command = new StartProcedureCommand(taskTitle, groupsList, propMap);
            }

        } catch (final Exception e) {
            throw new PortletException(e);
        }

        final Document documentInstance = (Document) nuxeoController.executeNuxeoCommand(command);
        return new ProcedureInstance(documentInstance);
    }

    private void applyActions(Map<String, ObjetMetier> objetMetiers) {
        for (final ObjetMetier objetMetier : objetMetiers.values()) {

            if (StringUtils.isEmpty(objetMetier.getProcedureObject().getPath())) {
                // si le path n'est pas renseigné on place celui par défaut
                objetMetier.getProcedureObject().setPath(defaultFIlesPath);
            }

            if (StringUtils.isEmpty(objetMetier.getProperties().getString("dc:title"))) {
                // si le titre n'est pas renseigné on place celui par défaut
                objetMetier.getProperties().set("dc:title", "titre du document");
            }
        }

    }

    private void defaultAction(NuxeoController nuxeoController, ProcedureModel procedureModel, ProcedureInstance procedureInstance,
            Map<String, ObjetMetier> objetMetiers) throws PortletException {

        ObjetMetier objetMetier;
        ProcedureObjectInstance procedureObject;
        Document documentObject;
        for (final Entry<String, ObjetMetier> objetMetierE : objetMetiers.entrySet()) {

            objetMetier = objetMetierE.getValue();
            if ((objetMetier.getProcedureObjectInstance() == null) || StringUtils.isEmpty(objetMetier.getProcedureObjectInstance().getProcedureObjectid())) {
                // if there is no objectId

                // create document from file and properties of objetMetier
                INuxeoCommand command;
                try {
                    final MultipartFile file = objetMetier.getFilePath().getFile();
                    final InputStream in = new ByteArrayInputStream(file.getBytes());
                    final Blob blob = new StreamBlob(in, file.getOriginalFilename(), file.getContentType());

                    command = new CreateDocumentFromBlobCommand(objetMetier.getProcedureObject().getPath(), objetMetier.getProperties(),
                            blob);
                } catch (final Exception e) {
                    throw new PortletException(e);
                }
                documentObject = (Document) nuxeoController.executeNuxeoCommand(command);

                // update the ProcedureObjectsMap
                procedureObject = new ProcedureObjectInstance();
                procedureObject.setName(objetMetierE.getKey());
                procedureObject.setProcedureObjectid(documentObject.getId());
                procedureInstance.getProcedureObjects().put(procedureObject.getName(), procedureObject);
            } else {
                INuxeoCommand command;
                try {
                    // retrieve the document from object
                    command = new RetrieveDocumentCommand(objetMetier.getProcedureObjectInstance().getProcedureObjectid());
                    documentObject = (Document) nuxeoController.executeNuxeoCommand(command);

                    if ((objetMetier.getFilePath() != null) && (objetMetier.getFilePath().getFile() != null)) {
                        // if a file:content was provided, update the content as well as the properties of objetMetier
                        Blob blob = null;
                        if (objetMetier.getFilePath().getFile().getSize() > 0) {
                            InputStream in;
                            in = new ByteArrayInputStream(objetMetier.getFilePath().getFile().getBytes());
                            blob = new StreamBlob(in, objetMetier.getFilePath().getFile().getOriginalFilename(), objetMetier.getFilePath().getFile()
                                    .getContentType());
                            command = new UpdateDocumentFromBlobCommand(documentObject.getPath(), objetMetier.getProperties(), blob);
                        }
                    } else {
                        // update existing documents with properties of objetMetier
                        command = new UpdateDocumentCommand(documentObject, objetMetier.getProperties());
                    }
                } catch (final Exception e) {
                    throw new PortletException(e);
                }
                nuxeoController.executeNuxeoCommand(command);
            }
        }
    }

    @Override
    public ProcedureInstance retrieveProcedureInstanceByPath(NuxeoController nuxeoController, String path) throws PortletException {

        INuxeoCommand command;
        ProcedureInstance procedureInstance = null;
        try {
            command = new RetrieveDocumentCommand(path);
            final Document currentDocument = (Document) nuxeoController.executeNuxeoCommand(command);
            procedureInstance = new ProcedureInstance(currentDocument);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        return procedureInstance;
    }

    @Override
    public ProcedureInstance updateProcedureInstance(NuxeoController nuxeoController, ProcedureModel procedureModel, ProcedureInstance procedureInstance,
            String procedureInstancePath, String taskTitle, List<String> groups) throws PortletException {

        // build objet metiers from the supplied values
        final Map<String, ObjetMetier> objetMetiers = ObjetMetierUtil.buildObjetMetiers(procedureModel, procedureInstance);

        // apply the actions
        applyActions(objetMetiers);

        // abstract action applied at every step
        defaultAction(nuxeoController, procedureModel, procedureInstance, objetMetiers);

        INuxeoCommand command;
        try {
            final String groupsList = StringUtils.join(groups, ",");
            command = new RetrieveDocumentCommand(procedureInstancePath);
            final Document currentDocument = (Document) nuxeoController.executeNuxeoCommand(command);
            final PropertyMap propMap = new PropertyMap();
            propMap.set("pi:currentStep", procedureInstance.getCurrentStep());
            propMap.set("pi:procedureModelPath", procedureInstance.getProcedureModelPath());

            final List<GlobalVariablesValuesType> gvvList = new ArrayList<GlobalVariablesValuesType>();
            for (final Entry<String, String> entry : procedureInstance.getGlobalVariablesValues().entrySet()) {
                gvvList.add(new GlobalVariablesValuesType(entry.getKey(), entry.getValue()));
            }
            propMap.set("pi:procedureObjectInstances", ProcedureJSONAdapter.getInstance().toJSON(procedureInstance.getProcedureObjects().values()));
            propMap.set("pi:globalVariablesValues", ProcedureJSONAdapter.getInstance().toJSON(gvvList));
            command = new UpdateProcedureCommand(currentDocument, propMap, taskTitle, groupsList);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        final Document documentInstance = (Document) nuxeoController.executeNuxeoCommand(command);
        return new ProcedureInstance(documentInstance);
    }


    @Override
    public List<ProcedureModel> listProcedures(NuxeoController nuxeoController, IPortalUrlFactory portalUrlFactory) throws PortletException {
        INuxeoCommand command;
        final List<ProcedureModel> procedureModels = new ArrayList<ProcedureModel>();
        try {
            command = new ListDocumentsCommand(path);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        final PaginableDocuments documentList = (PaginableDocuments) nuxeoController.executeNuxeoCommand(command);
        ProcedureModel procedureModel;
        for (final Document document : documentList) {
            procedureModel = new ProcedureModel(document);

            try {
                procedureModel.setUrl(getEditUrl(nuxeoController, portalUrlFactory, procedureModel));
            } catch (final PortalException e) {
                new PortletException(e);
            }

            procedureModels.add(procedureModel);
        }
        return procedureModels;
    }

    /**
     * @param nuxeoController
     * @param portalUrlFactory
     * @param procedureModel
     * @throws PortalException
     */
    private String getEditUrl(NuxeoController nuxeoController, IPortalUrlFactory portalUrlFactory, ProcedureModel procedureModel) throws PortalException {
        final Map<String, String> windowProperties = getWindowProperties();
        windowProperties.put(Constants.WINDOW_PROP_URI, procedureModel.getPath());
        windowProperties.put("osivia.title", "Éditer une procedure");
        return portalUrlFactory.getStartPortletUrl(nuxeoController.getPortalCtx(), "osivia-services-procedure-portletInstance", windowProperties, false);
    }

    /**
     * @param nuxeoController
     * @param portalUrlFactory
     * @param procedureModel
     * @throws PortalException
     */
    private Map<String, String> getWindowProperties() throws PortalException {
        final Map<String, String> windowProperties = new HashMap<String, String>();
        windowProperties.put("osivia.doctype", DocumentTypeEnum.PROCEDUREMODEL.getName());
        windowProperties.put("osivia.hideDecorators", "1");
        windowProperties.put("osivia.ajaxLink", "1");
        windowProperties.put("osivia.procedure.admin", "adminproc");
        return windowProperties;
    }

    @Override
    public String getAddUrl(NuxeoController nuxeoController, IPortalUrlFactory portalUrlFactory) throws PortletException {
        try {
            final Map<String, String> windowProperties = getWindowProperties();
            windowProperties.put("osivia.title", "Créer une procedure");
            return portalUrlFactory.getStartPortletUrl(nuxeoController.getPortalCtx(), "osivia-services-procedure-portletInstance", windowProperties, false);
        } catch (final PortalException e) {
            throw new PortletException(e);
        }
    }

    @Override
    public void updateFormWithObjectsValues(NuxeoController nuxeoController, Form form) throws PortletException {

        final Map<String, ObjetMetier> ojMap = new HashMap<String, ObjetMetier>();

        ObjetMetier objetMetier;
        for (final Field field : form.getTheCurrentStep().getFields()) {
            if (ObjetMetierUtil.isObject(field.getName())) {
                if (form.getProcedureInstance().getProcedureObjects().containsKey(ObjetMetierUtil.getObjectName(field.getName()))) {
                    if (ojMap.containsKey(field.getName())) {
                        objetMetier = ojMap.get(field.getName());
                    } else {
                        INuxeoCommand command;
                        try {
                            command = new RetrieveDocumentCommand(form.getProcedureInstance().getProcedureObjects()
                                    .get(ObjetMetierUtil.getObjectName(field.getName())).getProcedureObjectid());
                        } catch (final Exception e) {
                            throw new PortletException(e);
                        }
                        final Document objetMetierDocument = (Document) nuxeoController.executeNuxeoCommand(command);
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
        }
    }

    @Override
    public void updateVocabulariesWithValues(NuxeoController nuxeoController, Form form) throws PortletException {

        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, Variable> variables = form.getProcedureModel().getVariables();

        List<Select2Entry> varOptionsJson;
        List<String> varOptions;
        for (final Entry<String, Variable> entryV : variables.entrySet()) {
            if (VariableTypesEnum.SELECTVOCAB.equals(entryV.getValue().getType()) || VariableTypesEnum.SELECTVOCABMULTI.equals(entryV.getValue().getType())) {
                varOptionsJson = new ArrayList<Select2Entry>();
                final VocabularyEntry vocabularyEntry = VocabularyHelper.getVocabularyEntry(nuxeoController, entryV.getValue().getVarOptions().get(0));
                for (final VocabularyEntry entry : vocabularyEntry.getChildren().values()) {
                    varOptionsJson.add(new Select2Entry(entry.getLabel()));
                }
                try {
                    entryV.getValue().setVarOptionsJson(mapper.writeValueAsString(varOptionsJson));
                    entryV.getValue().setVarOptions(null);
                } catch (final JsonGenerationException e) {
                    throw new PortletException(e);
                } catch (final JsonMappingException e) {
                    throw new PortletException(e);
                } catch (final IOException e) {
                    throw new PortletException(e);
                }
            } else if (VariableTypesEnum.CHECKBOXVOCAB.equals(entryV.getValue().getType()) || VariableTypesEnum.RADIOVOCAB.equals(entryV.getValue().getType())) {
                varOptions = new ArrayList<String>();
                final VocabularyEntry vocabularyEntry = VocabularyHelper.getVocabularyEntry(nuxeoController, entryV.getValue().getVarOptions().get(0));
                for (final VocabularyEntry entry : vocabularyEntry.getChildren().values()) {
                    varOptions.add(entry.getLabel());
                }
                entryV.getValue().setVarOptions(varOptions);
            }
        }
    }
}
