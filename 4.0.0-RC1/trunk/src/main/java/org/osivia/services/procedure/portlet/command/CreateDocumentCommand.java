package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.services.procedure.portlet.model.DocumentTypeEnum;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author dorian
 */
public class CreateDocumentCommand implements INuxeoCommand {

    /** path the location where to create the document */
    private Document path;

    /** name the name of the document to create */
    private String name;

    /** type the type of the document to create */
    private DocumentTypeEnum type;

    public CreateDocumentCommand(Document path, String name, DocumentTypeEnum type) {
        super();
        this.path = path;
        this.name = name;
        this.type = type;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.CreateDocument.getId());
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.setInput(path);
        request.set("type", type.getName()).set("name", name).set("properties", "dc:title=" + name);

        return request.execute();
    }

    @Override
    public String getId() {
        return "CreateDocumentCommand/" + path + "/" + name + "/" + type;
    };


}
