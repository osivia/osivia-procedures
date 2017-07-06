package org.osivia.services.procedure.portlet.command;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;


/**
 * @author Dorian Licois
 */
public class ListProceduresModelsCommand implements INuxeoCommand {

    /** select */
    private static final String select = "SELECT * FROM ProcedureModel, RecordFolder";
    /** where */
    private static final String where = " WHERE ecm:path startswith '";
    /** end */
    private static final String end = "' ";

    /** path */
    private String path;

    /** filter */
    private String filter;

    /**
     * @param path
     * @param filter
     */
    public ListProceduresModelsCommand(String path, String filter) {
        this.path = path;
        this.filter = filter;
    }

    /**
     * @param path
     */
    public ListProceduresModelsCommand(String path) {
        this.path = path;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.QueryElasticSearch.getId());
        StringBuilder sbQuery = new StringBuilder(select);
        if (StringUtils.isNotBlank(path)) {
            sbQuery.append(where).append(path).append(end);
            if(StringUtils.isNotBlank(filter)){
                sbQuery.append(filter);
            }
        }

        String query = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE_N_PUBLISHED, sbQuery.toString());

        request.set("query", query);
        return request.execute();
    }

    @Override
    public String getId() {
        return "ListProceduresModelsCommand/" + path;
    }

}
