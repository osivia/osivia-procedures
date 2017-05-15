package org.osivia.services.procedure.module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.urls.Link;
import org.osivia.services.procedure.portlet.model.Variable;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.portlet.PortletModule;


public class ListProcListModule extends PortletModule {

    public ListProcListModule(PortletContext portletContext) {
        super(portletContext);
    }

    @Override
    protected void doView(RenderRequest request, RenderResponse response, PortletContext portletContext) throws PortletException, IOException {

        NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);

        // Document currentDoc = nuxeoController.getCurrentDoc();

        String path = NuxeoController.getLivePath(nuxeoController.getComputedPath(nuxeoController.getParentPathToCreate()));
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
        Document folder = documentContext.getDocument();


        // on retrouve la liste des champs
        PropertyMap properties = folder.getProperties();
        PropertyList steps = properties.getList("pcd:steps");
        PropertyList variables = ((PropertyMap) steps.list().get(0)).getList("globalVariablesReferences");
        List<Variable> fields = new ArrayList<Variable>(variables.size());
        PropertyMap variableMap;
        Variable field;
        for (Object variableO : variables.list()) {
            variableMap = ((PropertyMap) variableO);
            field = new Variable(variableMap.getString("variableName"), variableMap.getString("superLabel"), null, null);
            fields.add(field);
        }
        request.setAttribute("fields", fields);

        // lien vers procedureModel pour ajouter un nouvel item
        Link addItemLink = nuxeoController.getLink(folder, "listadditem");
        request.setAttribute("addItemUrl", addItemLink.getUrl());


        // composition des éléments du tableau, avec URL
        // Original documents
        List<?> originalDocuments = (List<?>) request.getAttribute("documents");
        
        for (Object object : originalDocuments) {
            DocumentDTO procedureInstance = (DocumentDTO) object;
            procedureInstance.getProperties().put("url", nuxeoController.getLink(procedureInstance.getDocument(), "detailproc").getUrl());
        }
    }
}
