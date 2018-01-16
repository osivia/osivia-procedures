package org.osivia.services.procedure.portlet.command;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.portal.core.constants.InternalConstants;
import org.osivia.services.procedure.portlet.model.NuxeoOperationEnum;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * List records Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ListRecordsCommand implements INuxeoCommand {

    /** CMS base path. */
    private final String basePath;
    /** Parent record folder webId. */
    private final String recordFolderWebId;
    /** Search filter. */
    private final String filter;


    /**
     * Constructor.
     * 
     * @param basePath CMS base path
     * @param recordFolderWebId parent record folder webId
     * @param filter search filter
     */
    private ListRecordsCommand(String basePath, String recordFolderWebId, String filter) {
        super();
        this.basePath = basePath;
        this.recordFolderWebId = recordFolderWebId;
        this.filter = filter;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'Record' ");
        if (StringUtils.isNotEmpty(this.basePath)) {
            clause.append("AND ecm:path STARTSWITH '").append(this.basePath).append("' ");
        }
        if (StringUtils.isNotBlank(this.recordFolderWebId)) {
            clause.append("AND rcd:procedureModelWebId = '").append(this.recordFolderWebId).append("' ");
        }
        if (StringUtils.isNotBlank(this.filter)) {
            clause.append("AND (dc:title ILIKE '").append(this.filter).append("%' or ecm:fulltext = '").append(this.filter).append("' ) ");
        }
        clause.append("ORDER BY dc:title ASC");

        // Query filter
        NuxeoQueryFilterContext filter = new NuxeoQueryFilterContext(NuxeoQueryFilterContext.STATE_LIVE,
                InternalConstants.PORTAL_CMS_REQUEST_FILTERING_POLICY_LOCAL);

        // Filtered clause
        String filteredClause = NuxeoQueryFilter.addPublicationFilter(filter, clause.toString());

        // Operation request
        OperationRequest request = nuxeoSession.newRequest(NuxeoOperationEnum.QueryElasticSearch.getId());
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, toutatice");
        request.set("query", "SELECT * FROM Document WHERE " + filteredClause);

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append("|");
        builder.append(StringUtils.trimToEmpty(this.basePath));
        builder.append("|");
        builder.append(StringUtils.trimToEmpty(this.recordFolderWebId));
        builder.append("|");
        builder.append(StringUtils.trimToEmpty(this.filter));
        return builder.toString();
    }

}
