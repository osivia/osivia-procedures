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

    /** Select. */
    private static final String SELECT = "SELECT * FROM ProcedureModel, RecordFolder";
    /** Where. */
    private static final String WHERE = " WHERE ";
    /** Clause begin. */
    private static final String CLAUSE_BEGIN = " ecm:path STARTSWITH '";
    /** Clause end. */
    private static final String CLAUSE_END = "' ";
    /** Order. */
    private static final String ORDER = " ORDER BY dc:title";


    /** Path */
    private final String path;
    /** Filter. */
    private final String filter;


    /**
     * Constructor.
     * 
     * @param path path
     */
    public ListProceduresModelsCommand(String path) {
        this(path, null);
    }


    /**
     * Constructor.
     * 
     * @param path path
     * @param filter filter
     */
    public ListProceduresModelsCommand(String path, String filter) {
        super();
        this.path = path;
        this.filter = filter;
    }


    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.QueryElasticSearch.getId());


        StringBuilder query = new StringBuilder(SELECT);
        if (StringUtils.isNotBlank(path)) {
            query.append(WHERE);

            StringBuilder clause = new StringBuilder();
            clause.append(CLAUSE_BEGIN);
            clause.append(path);
            clause.append(CLAUSE_END);
            if (StringUtils.isNotBlank(this.filter)) {
                clause.append(this.filter);
            }
            clause.append(ORDER);

            String filteredClause = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE_N_PUBLISHED, clause.toString());
            query.append(filteredClause);
        }

        request.set("query", query.toString());

        return request.execute();
    }

    @Override
    public String getId() {
        return "ListProceduresModelsCommand/" + path;
    }

}
