package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author Dorian Licois
 */
public class RetrieveDocumentByIdCommand implements INuxeoCommand {

    /** webId of the document to retrieve */
    private String fetchBY;

    /**
     * @param fetchBY
     */
    public RetrieveDocumentByIdCommand(String fetchBY) {
        super();
        this.fetchBY = fetchBY;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.QueryElasticSearch.getId());
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        String query = "SELECT * FROM Document WHERE ecm:uuid = '" + fetchBY + "'";
        query = query + " AND ecm:currentLifeCycleState <> 'deleted'";
        request.set("query", query);
        return request.execute();
    }

    @Override
    public String getId() {
        return "RetrieveDocumentByWebIdCommand/" + fetchBY;
    }

}
