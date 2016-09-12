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
    private String fetchBY;


    public RetrieveDocumentCommand(String fetchBY) {
        super();
        this.fetchBY = fetchBY;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.QueryElasticSearch.getId());
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.set("query", "SELECT * FROM Document WHERE ecm:path = '" + fetchBY + "'");
        return request.execute();
    }

    @Override
    public String getId() {
        return "RetrieveDocumentCommand/" + fetchBY;
    }

}
