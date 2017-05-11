package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


public class RetrieveProcedureByStepNameCommand implements INuxeoCommand {

    private String filterName;

    public RetrieveProcedureByStepNameCommand(String filterName) {
        this.filterName = filterName;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.QueryElasticSearch.getId());
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        request.set("query", "SELECT * FROM ProcedureModel WHERE pcd:steps/name LIKE '%" + filterName + "%'");
        return request.execute();
    }

    @Override
    public String getId() {
        return "RetrieveProcedureByStepNameCommand/" + filterName;
    }

}
