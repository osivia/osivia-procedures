package org.osivia.services.procedure.portlet.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
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
import org.osivia.services.procedure.portlet.command.CreateDocumentFromAttachmentCommand;
import org.osivia.services.procedure.portlet.command.CreateDocumentFromBlobCommand;
import org.osivia.services.procedure.portlet.command.DeleteDocumentCommand;
import org.osivia.services.procedure.portlet.command.ListDocumentsCommand;
import org.osivia.services.procedure.portlet.command.StartProcedureCommand;
import org.osivia.services.procedure.portlet.command.UpdateDocumentCommand;
import org.osivia.services.procedure.portlet.command.UpdateProcedureCommand;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;
import org.osivia.services.procedure.portlet.model.FilePath;
import org.osivia.services.procedure.portlet.model.GlobalVariablesValuesType;
import org.osivia.services.procedure.portlet.model.ProcedureInstance;
import org.osivia.services.procedure.portlet.model.ProcedureModel;
import org.osivia.services.procedure.portlet.model.ProcedureObject;
import org.osivia.services.procedure.portlet.model.ProcedureObjectInstance;
import org.osivia.services.procedure.portlet.service.IProcedureService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

@Service
public class ProcedureServiceImpl implements IProcedureService {

    /** path containing the models */
    private static final String path = "/default-domain/procedures-models.1442475923550";


    // ecm://{object}/{variable}
    private static final Pattern objectPattern = Pattern.compile("^ecm:\\/\\/([\\w.]+)\\/([\\w.:]+)");

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
            propMap.set("pcd:procedureObjects", ProcedureJSONAdapter.getInstance().toJSON(procedureModel.getProcedureObjects()));
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

        defaultAction(nuxeoController, procedureModel, procedureInstance);

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
            propMap.set("pi:procedureObjectInstances", ProcedureJSONAdapter.getInstance().toJSON(procedureInstance.getProcedureObjects().values()));
            if (blobs.size() > 0) {
                propMap.set("pi:attachments", ProcedureJSONAdapter.getInstance().toJSON(procedureInstance.getFilesPath().values()));
                command = new StartProcedureCommand(taskTitle, propMap, blobs);
            } else {
                command = new StartProcedureCommand(taskTitle, propMap);
            }

        } catch (final Exception e) {
            throw new PortletException(e);
        }

        Document documentInstance = (Document) nuxeoController.executeNuxeoCommand(command);
        return new ProcedureInstance(documentInstance);
    }

    private void defaultAction(NuxeoController nuxeoController, ProcedureModel procedureModel, ProcedureInstance procedureInstance) throws PortletException {

        List<FilePath> createDocumentList = new ArrayList<FilePath>();
        List<String> updateDocumentList = new ArrayList<String>();
        Map<String, PropertyMap> propMap = new HashMap<String, PropertyMap>();

        // for each variable
        Matcher matcher;
        for (Entry<String, String> gvv : procedureInstance.getGlobalVariablesValues().entrySet()) {
            matcher = objectPattern.matcher(gvv.getKey());

            if (matcher.matches()) {
                // if the name matches the pattern
                String objectName = matcher.group(1);
                String objectProperty = matcher.group(2);

                // find the objectId
                String objectId = getObjectIdByName(procedureInstance, objectName);

                if (objectId == null) {
                    // if there is no objectId
                    if (procedureInstance.getFilesPath().containsKey(objectName)) {
                        // if there is a MultipartFile matching the object

                        // add file to the createDocumentList
                        createDocumentList.add(procedureInstance.getFilesPath().get(objectName));

                        // remove the file from the procedureInstance (to avoid creating attachment)
                        procedureInstance.getFilesPath().remove(objectName);
                    }
                } else {
                    // add object to the updateDocumentList
                    updateDocumentList.add(objectName);
                }
                // add variable to the propertyMap of this object
                if (propMap.containsKey(objectName)) {
                    propMap.get(objectName).set(objectProperty, gvv.getValue());
                } else {
                    PropertyMap prop = new PropertyMap();
                    prop.set(objectProperty, gvv.getValue());
                    propMap.put(objectName, prop);
                }

                // remove the properties from the procedureInstance (to avoid creating them as globalVariables)
                procedureInstance.getGlobalVariablesValues().remove(gvv.getKey());
            }
        }

        // for each file in the createDocumentList
        ProcedureObjectInstance procedureObject;
        for (FilePath filePath : createDocumentList) {
            // create document from file and fileProperties
            INuxeoCommand command;
            try {
                MultipartFile file = filePath.getFile();
                InputStream in = new ByteArrayInputStream(file.getBytes());
                Blob blob = new StreamBlob(in, file.getOriginalFilename(), file.getContentType());

                command = new CreateDocumentFromBlobCommand(getObjectPathByName(procedureModel, filePath), propMap.get(filePath.getVariableName()), blob);
            } catch (Exception e) {
                throw new PortletException(e);
            }
            Document createdDocument = (Document) nuxeoController.executeNuxeoCommand(command);

            // update the ProcedureObjectsMap
            procedureObject = new ProcedureObjectInstance();
            procedureObject.setName(filePath.getVariableName());
            procedureObject.setProcedureObjectid(createdDocument.getId());
            procedureInstance.getProcedureObjects().put(filePath.getFileName(), procedureObject);
        }

        // for each object in the updateDocumentList
        for (String string : updateDocumentList) {
            // update document from documentPath and fileProperties
        }


    }

    /**
     * @param procedureModel
     * @param filePath
     */
    private String getObjectPathByName(ProcedureModel procedureModel, FilePath filePath) {
        for (ProcedureObject object : procedureModel.getProcedureObjects()) {
            if (StringUtils.equals(filePath.getVariableName(), object.getName())) {
                return object.getPath();
            }
        }
        return null;
    }

    /**
     * @param procedureInstance
     * @param objectName
     */
    private String getObjectIdByName(ProcedureInstance procedureInstance, String objectName) {
        for (ProcedureObjectInstance objectInstance : procedureInstance.getProcedureObjects().values()) {
            if(StringUtils.equals(objectInstance.getName(), objectName)){
                return objectInstance.getProcedureObjectid();
            }
        }
        return null;
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
            propMap.set("pi:procedureObjectInstances", ProcedureJSONAdapter.getInstance().toJSON(procedureInstance.getProcedureObjects().values()));
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
            command = new CreateDocumentFromAttachmentCommand(procedureInstance, variableName);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        nuxeoController.executeNuxeoCommand(command);
    }

    @Override
    public List<ProcedureModel> listProcedures(NuxeoController nuxeoController, IPortalUrlFactory portalUrlFactory) throws PortletException {
        INuxeoCommand command;
        List<ProcedureModel> procedureModels = new ArrayList<ProcedureModel>();
        try {
            command = new ListDocumentsCommand(path);
        } catch (final Exception e) {
            throw new PortletException(e);
        }
        PaginableDocuments documentList = (PaginableDocuments) nuxeoController.executeNuxeoCommand(command);
        ProcedureModel procedureModel;
        for (Document document : documentList) {
            procedureModel = new ProcedureModel(document);

            try {
                procedureModel.setUrl(getUrl(nuxeoController, portalUrlFactory, procedureModel));
            } catch (PortalException e) {
                new PortalException(e);
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
    private String getUrl(NuxeoController nuxeoController, IPortalUrlFactory portalUrlFactory, ProcedureModel procedureModel) throws PortalException {
        Map<String, String> windowProperties = new HashMap<String, String>();
        windowProperties.put(Constants.WINDOW_PROP_URI, procedureModel.getPath());
        windowProperties.put("osivia.doctype", DocumentTypeEnum.PROCEDUREMODEL.getName());
        windowProperties.put("osivia.title", "Éditer une procedure");
        windowProperties.put("osivia.hideDecorators", "1");
        windowProperties.put("osivia.ajaxLink", "1");
        windowProperties.put("osivia.procedure.admin", "adminproc");
        return portalUrlFactory.getStartPortletUrl(nuxeoController.getPortalCtx(), "osivia-services-procedure-portletInstance", windowProperties, false);
    }

}
