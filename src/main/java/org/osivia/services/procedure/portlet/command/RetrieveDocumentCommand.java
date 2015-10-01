package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author dorian
 */
public class RetrieveDocumentCommand implements INuxeoCommand {

    /** path the location of the document to retrieve */
    private String path;


    public RetrieveDocumentCommand(String path) {
        super();
        this.path = path;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.RetrieveDocument.getId());
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.set("value", path);
        return request.execute();
    }

    @Override
    public String getId() {
        return "RetrieveDocumentCommand/" + path;
    }

}
