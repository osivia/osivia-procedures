package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


public class ListProcedureInstancesByModelListCommand implements INuxeoCommand {
    
    private static final String select = "SELECT * FROM ProcedureInstance";
    private static final String wherePath = " WHERE ecm:path startswith '";
    private static final String end = "'";
    private static final String whereWebId = " AND pi:procedureModelWebId = '";
    private static final String whereEndStep = " AND pi:currentStep <> 'endStep'";
    
    private String path;
    private String modelWebId;
    private boolean includeEnded;


    public ListProcedureInstancesByModelListCommand(String path, String modelWebId) {
        this.path = path;
        this.modelWebId = modelWebId;
    }

    public ListProcedureInstancesByModelListCommand(String path, String modelWebId, boolean includeEnded) {
        this.path = path;
        this.modelWebId = modelWebId;
        this.includeEnded = includeEnded;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.QueryElasticSearch.getId());
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "*");
        StringBuilder queryb = new StringBuilder(select.concat(wherePath).concat(path).concat(end).concat(whereWebId).concat(modelWebId).concat(end));
        if (!includeEnded) {
            queryb.append(whereEndStep);
        }
        request.set("query", queryb.toString());
        return request.execute();
    }

    @Override
    public String getId() {
        return "ListProcedureInstancesByModelListCommand/" + path + "/" + modelWebId;
    }

}
