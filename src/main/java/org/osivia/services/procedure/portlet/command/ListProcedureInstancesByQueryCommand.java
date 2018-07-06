package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author Steux js
 */
public class ListProcedureInstancesByQueryCommand implements INuxeoCommand {
    

    /** includeEnded */
    private String query ;


    /**
     * @param path
     * @param modelWebId
     */
    public ListProcedureInstancesByQueryCommand(String query) {
        this.query = query;
    }



    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.QueryElasticSearch.getId());
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");

        request.set("query", query);
        return request.execute();
    }

    @Override
    public String getId() {
        return "ListProcedureInstancesByQueryCommand/" + query ;
    }

}
