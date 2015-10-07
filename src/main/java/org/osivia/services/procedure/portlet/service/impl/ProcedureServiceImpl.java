package org.osivia.services.procedure.portlet.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Blobs;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.nuxeo.ecm.automation.client.model.StreamBlob;
import org.osivia.services.procedure.portlet.adapter.ProcedureJSONAdapter;
import org.osivia.services.procedure.portlet.command.CreateDocumentCommand;
import org.osivia.services.procedure.portlet.command.CreateDocumentFromBlobCommand;
import org.osivia.services.procedure.portlet.command.DeleteDocumentCommand;
import org.osivia.services.procedure.portlet.command.StartProcedureCommand;
import org.osivia.services.procedure.portlet.command.UpdateDocumentCommand;
import org.osivia.services.procedure.portlet.command.UpdateProcedureCommand;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;
import org.osivia.services.procedure.portlet.model.FilePath;
import org.osivia.services.procedure.portlet.model.GlobalVariablesValuesType;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.service.IProcedureService;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

@Service
public class ProcedureServiceImpl implements IProcedureService {

    private static final String path = "/default-domain/procedures-models.1442475923550";

    @Override
    public ProcedureModel createProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException {

        Document container = nuxeoController.fetchDocument(path, true);
        INuxeoCommand command;
        try {
            command = new CreateDocumentCommand(container, procedureModel.getName(), DocumentTypeEnum.PROCEDUREMODEL);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        Document procedureModelInstance = (Document) nuxeoController.executeNuxeoCommand(command);

        return new ProcedureModel(procedureModelInstance);
    }

    @Override
    public ProcedureModel retrieveProcedureByPath(NuxeoController nuxeoController, String path) throws PortletException {
        Document currentDocument = nuxeoController.fetchDocument(path, true);
        return new ProcedureModel(currentDocument);
    }

    @Override
    public ProcedureModel updateProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException {
        INuxeoCommand command;
        try {
            Document currentDocument = nuxeoController.fetchDocument(procedureModel.getPath(), true);
            PropertyMap propMap = new PropertyMap();
            propMap.set("dc:title", procedureModel.getName());
            propMap.set("pcd:steps", ProcedureJSONAdapter.getInstance().toJSON(procedureModel.getSteps()));
            procedureModel.updateGlobalVariableDefinition();
            propMap.set("pcd:globalVariablesDefinitions", ProcedureJSONAdapter.getInstance().toJSON(procedureModel.getVariables().values()));
            propMap.set("pcd:startingStep", procedureModel.getStartingStep());
            command = new UpdateDocumentCommand(currentDocument, propMap);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        Document procedureModelInstance = (Document) nuxeoController.executeNuxeoCommand(command);
        return new ProcedureModel(procedureModelInstance);
    }

    @Override
    public void deleteProcedure(NuxeoController nuxeoController, ProcedureModel procedureModel) throws PortletException {
        INuxeoCommand command = new DeleteDocumentCommand(nuxeoController.fetchDocument(procedureModel.getPath(), true));
        nuxeoController.executeNuxeoCommand(command);
    }

    @Override
    public ProcedureInstance createProcedureInstance(NuxeoController nuxeoController, ProcedureModel procedureModel, ProcedureInstance procedureInstance,
            String taskTitle) throws PortletException {

        INuxeoCommand command;
        try {
            PropertyMap propMap = new PropertyMap();
            propMap.set("pi:currentStep", procedureInstance.getCurrentStep());
            propMap.set("pi:procedureModelPath", procedureModel.getPath());

            List<GlobalVariablesValuesType> gvvList = new ArrayList<GlobalVariablesValuesType>();
            for (Entry<String, String> entry : procedureInstance.getGlobalVariablesValues().entrySet()) {
                gvvList.add(new GlobalVariablesValuesType(entry.getKey(), entry.getValue()));
            }
            propMap.set("pi:globalVariablesValues", ProcedureJSONAdapter.getInstance().toJSON(gvvList));

            Blobs blobs = new Blobs();
            for (FilePath file : procedureInstance.getFilesPath().values()) {
                if (file.getFile().getSize() > 0) {
                    InputStream in = new ByteArrayInputStream(file.getFile().getBytes());
                    Blob blob = new StreamBlob(in, file.getFile().getOriginalFilename(), file.getFile().getContentType());
                    blobs.add(blob);
                }
            }
            if (blobs.size() > 0) {
                propMap.set("attachments", ProcedureJSONAdapter.getInstance().toJSON(procedureInstance.getFilesPath().values()));
            }

            command = new StartProcedureCommand(taskTitle, propMap, blobs);
        } catch (final Exception e) {
            throw new PortletException(e);
        }

        Document documentInstance = (Document) nuxeoController.executeNuxeoCommand(command);
        return new ProcedureInstance(documentInstance);
    }

    @Override
    public ProcedureInstance retrieveProcedureInstanceByPath(NuxeoController nuxeoController, String path) throws PortletException {
        Document currentDocument = nuxeoController.fetchDocument(path, true);
        return new ProcedureInstance(currentDocument);
    }

    @Override
    public ProcedureInstance updateProcedureInstance(NuxeoController nuxeoController, ProcedureInstance procedureInstance, String procedureInstancePath,
            String taskTitle) throws PortletException {
        INuxeoCommand command;
        try {
            Document currentDocument = nuxeoController.fetchDocument(procedureInstancePath, true);
            PropertyMap propMap = new PropertyMap();
            propMap.set("pi:currentStep", procedureInstance.getCurrentStep());
            propMap.set("pi:procedureModelPath", procedureInstance.getProcedureModelPath());

            List<GlobalVariablesValuesType> gvvList = new ArrayList<GlobalVariablesValuesType>();
            for (Entry<String, String> entry : procedureInstance.getGlobalVariablesValues().entrySet()) {
                gvvList.add(new GlobalVariablesValuesType(entry.getKey(), entry.getValue()));
            }
            propMap.set("pi:globalVariablesValues", ProcedureJSONAdapter.getInstance().toJSON(gvvList));
            command = new UpdateProcedureCommand(currentDocument, propMap, taskTitle);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        Document documentInstance = (Document) nuxeoController.executeNuxeoCommand(command);
        return new ProcedureInstance(documentInstance);
    }

    @Override
    public void createDocumentFromBlob(NuxeoController nuxeoController, String procedureInstancePath, String variableName) throws PortletException {
        INuxeoCommand command;
        try {
            Document procedureInstance = nuxeoController.fetchDocument(procedureInstancePath, true);
            command = new CreateDocumentFromBlobCommand(procedureInstance, variableName);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        nuxeoController.executeNuxeoCommand(command);
    }

}
