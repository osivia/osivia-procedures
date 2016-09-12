package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author dorian
 */
public class RetrieveDocumentByWebIdCommand implements INuxeoCommand {

    /** webId of the document to retrieve */
    private String fetchBY;


    public RetrieveDocumentByWebIdCommand(String fetchBY) {
        super();
        this.fetchBY = fetchBY;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.QueryElasticSearch.getId());
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.set("query", "SELECT * FROM Document WHERE ttc:webid = '" + fetchBY + "'");
        return request.execute();
    }

    @Override
    public String getId() {
        return "RetrieveDocumentByWebIdCommand/" + fetchBY;
    }

}
