package org.osivia.services.procedure.portlet.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.PortletException;

import net.sf.json.JSONArray;

import org.apache.commons.io.IOUtils;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.services.procedure.portlet.adapter.ProcedureJSONAdapter;
import org.osivia.services.procedure.portlet.command.CreateDocumentCommand;
import org.osivia.services.procedure.portlet.command.DeleteDocumentCommand;
import org.osivia.services.procedure.portlet.command.ListDocumentsCommand;
import org.osivia.services.procedure.portlet.command.LoadVocabularyCommand;
import org.osivia.services.procedure.portlet.command.RetrieveDocumentCommand;
import org.osivia.services.procedure.portlet.command.UpdateDocumentCommand;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;
import org.osivia.services.procedure.portlet.model.Field;
import org.osivia.services.procedure.portlet.model.FilePath;
import org.osivia.services.procedure.portlet.model.Form;
import org.osivia.services.procedure.portlet.model.ObjetMetier;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.Variable;
import org.osivia.services.procedure.portlet.model.VariableTypesEnum;
import org.osivia.services.procedure.portlet.service.IProcedureService;
import org.osivia.services.procedure.portlet.util.ObjetMetierUtil;
import org.osivia.services.procedure.portlet.util.VocabularySelect2Util;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyEntry;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyHelper;

@Service
public class ProcedureServiceImpl implements IProcedureService {

    /** path containing the models */
    private static final String path = "/default-domain/procedures-models";

    @Override
    public ProcedureModel createProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException {

        INuxeoCommand command;
        try {
            command = new RetrieveDocumentCommand(path);
            final Document container = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);
            command = new CreateDocumentCommand(container, procedureModel.getName(), DocumentTypeEnum.PROCEDUREMODEL);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        final Document procedureModelInstance = (Document) nuxeoController.executeNuxeoCommand(command);

        return new ProcedureModel(procedureModelInstance, nuxeoController);
    }

    @Override
    public ProcedureModel retrieveProcedureByPath(NuxeoController nuxeoController, String path) throws PortletException {
        INuxeoCommand command;
        Document document = null;
        ProcedureModel procedureModel = null;
        try {
            command = new RetrieveDocumentCommand(path);
            document = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);
            procedureModel = new ProcedureModel(document, nuxeoController);
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
            final Document currentDocument = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);
            final PropertyMap propMap = new PropertyMap();
            propMap.set("dc:title", procedureModel.getName());
            propMap.set("ttc:webid", procedureModel.getWebId());
            propMap.set("pcd:steps", ProcedureJSONAdapter.getInstance().toJSON(procedureModel.getSteps()));
            propMap.set("pcd:globalVariablesDefinitions", ProcedureJSONAdapter.getInstance().toJSON(procedureModel.getVariables().values()));
            propMap.set("pcd:startingStep", procedureModel.getStartingStep());
            propMap.set("pcd:procedureObjects", ProcedureJSONAdapter.getInstance().toJSON(procedureModel.getProcedureObjects()));
            command = new UpdateDocumentCommand(currentDocument, propMap);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        final Document procedureModelInstance = (Document) nuxeoController.executeNuxeoCommand(command);
        return new ProcedureModel(procedureModelInstance, nuxeoController);
    }

    @Override
    public void deleteProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException {

        INuxeoCommand command;
        try {
            command = new RetrieveDocumentCommand(procedureModel.getPath());
            final Document currentDocument = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);
            command = new DeleteDocumentCommand(currentDocument);
            nuxeoController.executeNuxeoCommand(command);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
    }


    @Override
    public ProcedureInstance retrieveProcedureInstanceByPath(NuxeoController nuxeoController, String path) throws PortletException {

        INuxeoCommand command;
        ProcedureInstance procedureInstance = null;
        try {
            command = new RetrieveDocumentCommand(path);
            final Document currentDocument = ((Documents) nuxeoController.executeNuxeoCommand(command)).get(0);
            procedureInstance = new ProcedureInstance(currentDocument);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        return procedureInstance;
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
            procedureModel = new ProcedureModel(document, nuxeoController);

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
        return portalUrlFactory.getStartPortletUrl(nuxeoController.getPortalCtx(), "osivia-services-procedure-portletInstance", windowProperties,
                PortalUrlType.DEFAULT);
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
        windowProperties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, Constants.PORTLET_VALUE_ACTIVATE);
        windowProperties.put("osivia.ajaxLink", "1");
        windowProperties.put("osivia.procedure.admin", "adminproc");
        return windowProperties;
    }

    @Override
    public String getAddUrl(NuxeoController nuxeoController, IPortalUrlFactory portalUrlFactory) throws PortletException {
        try {
            final Map<String, String> windowProperties = getWindowProperties();
            windowProperties.put("osivia.title", "Créer une procedure");
            return portalUrlFactory.getStartPortletUrl(nuxeoController.getPortalCtx(), "osivia-services-procedure-portletInstance", windowProperties,
                    PortalUrlType.DEFAULT);
        } catch (final PortalException e) {
            throw new PortletException(e);
        }
    }

    @Override
    public void updateFormWithObjectsValues(NuxeoController nuxeoController, Form form) throws PortletException {

        final Map<String, ObjetMetier> ojMap = new HashMap<String, ObjetMetier>();
        for (final Field field : form.getTheCurrentStep().getFields()) {
            updateFormWithObjectsValues(field, ojMap, nuxeoController, form);
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
                            command = new RetrieveDocumentCommand(form.getProcedureInstance().getProcedureObjects()
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
            for (final Field nestedField : field.getFields()) {
                updateFormWithObjectsValues(nestedField, ojMap, nuxeoController, form);
            }
        }
    }

    @Override
    public void updateVocabulariesWithValues(NuxeoController nuxeoController, Form form) throws PortletException {

        final Map<String, Variable> variables = form.getProcedureModel().getVariables();

        List<String> varOptions;
        for (final Entry<String, Variable> entryV : variables.entrySet()) {
            if (VariableTypesEnum.CHECKBOXVOCAB.equals(entryV.getValue().getType()) || VariableTypesEnum.RADIOVOCAB.equals(entryV.getValue().getType())) {
                varOptions = new ArrayList<String>();
                final VocabularyEntry vocabularyEntry = VocabularyHelper.getVocabularyEntry(nuxeoController, entryV.getValue().getVarOptions().get(0));
                for (final VocabularyEntry entry : vocabularyEntry.getChildren().values()) {
                    varOptions.add(entry.getLabel());
                }
                entryV.getValue().setVarOptions(varOptions);
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

}
