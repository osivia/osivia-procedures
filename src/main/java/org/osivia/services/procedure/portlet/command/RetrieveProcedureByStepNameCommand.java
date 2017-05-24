package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;


public class RetrieveProcedureByStepNameCommand implements INuxeoCommand {

    private String filterName;

    public RetrieveProcedureByStepNameCommand(String filterName) {
        this.filterName = filterName;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.QueryElasticSearch.getId());
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        String query = "SELECT * FROM ProcedureModel WHERE pcd:steps/name LIKE '%" + filterName + "%'";
        query = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE_N_PUBLISHED, query);
        request.set("query", query);
        return request.execute();
    }

    @Override
    public String getId() {
        return "RetrieveProcedureByStepNameCommand/" + filterName;
    }

}
