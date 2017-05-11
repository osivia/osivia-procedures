package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author Dorian Licois
 */
public class CreateDocumentFromAttachmentCommand implements INuxeoCommand {

    /** document the document to update */
    private Document document;

    /** variableName */
    private String variableName;

    public CreateDocumentFromAttachmentCommand(Document document, String variableName) {
        super();
        this.document = document;
        this.variableName = variableName;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.CreateDocumentFromAttachment.getId());
        request.setInput(document);
        request.set("variableName", variableName);
        return request.execute();
    }

    @Override
    public String getId() {
        return "CreateDocumentFromAttachmentCommand/" + document + "/" + variableName;
    }

}
