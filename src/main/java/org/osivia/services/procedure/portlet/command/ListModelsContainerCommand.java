package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


public class ListModelsContainerCommand implements INuxeoCommand {

    private static final String query = "SELECT * FROM ProceduresModelsContainer WHERE ecm:path startswith '";

    private String path;


    public ListModelsContainerCommand(String path) {
        this.path = path;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.QueryElasticSearch.getId());
        request.set("query", query.concat(path).concat("'"));
        return request.execute();
    }

    @Override
    public String getId() {
        return "ListModelsContainerCommand/" + path;
    }

}
