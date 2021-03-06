package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author Dorian Licois
 */
public class CreateDocumentCommand implements INuxeoCommand {

    /** path the location where to create the document */
    private Document path;

    /** type the type of the document to create */
    private DocumentTypeEnum type;

    /** properties of the document */
    private PropertyMap properties;

    public CreateDocumentCommand(Document path, PropertyMap properties, DocumentTypeEnum type) {
        super();
        this.path = path;
        this.type = type;
        this.properties = properties;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.CreateDocument.getId());
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.setHeader("nx_es_sync", "true");
        request.setInput(path);
        request.set("type", type.getDocType()).set("properties", properties);

        return request.execute();
    }

    @Override
    public String getId() {
        return "CreateDocumentCommand/" + path + "/" + type + "/" + properties;
    };


}
