package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author Dorian Licois
 */
public class UpdateDocumentCommand implements INuxeoCommand {

    /** document the document to update */
    private Document document;

    /** properties to update */
    private PropertyMap properties;

    public UpdateDocumentCommand(Document document, PropertyMap properties) {
        super();
        this.document = document;
        this.properties = properties;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.UpdateDocument.getId());
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.setHeader("nx_es_sync", "true");
        request.setInput(document);
        request.set("properties", properties);
        return request.execute();
    }

    @Override
    public String getId() {
        return "UpdateDocumentCommand/" + document + "/" + properties;
    }

}
