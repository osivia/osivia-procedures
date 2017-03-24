package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author Dorian Licois
 */
public class DeleteDocumentCommand implements INuxeoCommand {

    /** document the document to delete */
    private Document document;


    public DeleteDocumentCommand(Document document) {
        super();
        this.document = document;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.DeleteDocument.getId());
        request.setInput(document);
        return request.execute();
    }

    @Override
    public String getId() {
        return "DeleteDocumentCommand/" + document;
    }

}
