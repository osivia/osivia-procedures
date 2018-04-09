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
public class ListModelsContainerCommand implements INuxeoCommand {

    /** Select. */
    private static final String SELECT = "SELECT * FROM ProceduresModelsContainer, RecordContainer";
    /** Where. */
    private static final String WHERE = " WHERE ";
    /** Clause begin. */
    private static final String CLAUSE_BEGIN = "ecm:path STARTSWITH '";
    /** Clause end. */
    private static final String CLAUSE_END = "'";

    /** path */
    private String path;

    /**
     * @param path
     */
    public ListModelsContainerCommand(String path) {
        this.path = path;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.QueryElasticSearch.getId());

        StringBuilder query = new StringBuilder();
        query.append(SELECT);

        if (StringUtils.isNotBlank(this.path)) {
            query.append(WHERE);

            StringBuilder clause = new StringBuilder();
            clause.append(CLAUSE_BEGIN);
            clause.append(this.path);
            clause.append(CLAUSE_END);

            String filteredClause = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE_N_PUBLISHED, clause.toString());
            query.append(filteredClause);
        }

        request.set("query", query.toString());

        return request.execute();
    }

    @Override
    public String getId() {
        return "ListModelsContainerCommand/" + path;
    }

}
