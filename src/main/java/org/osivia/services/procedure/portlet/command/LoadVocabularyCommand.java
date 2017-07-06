package org.osivia.services.procedure.portlet.command;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * Load vocabulary command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public class LoadVocabularyCommand implements INuxeoCommand {

    /** Vocabulary name. */
    private final String name;

    /**
     * Constructor.
     */
    public LoadVocabularyCommand(String name) {
        super();
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        final OperationRequest request = nuxeoSession.newRequest("Document.GetVocabularies");
        request.setHeader(org.nuxeo.ecm.automation.client.Constants.HEADER_NX_SCHEMAS, "*");
        request.set("vocabularies", name);
        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append("/");
        builder.append(name);
        return builder.toString();
    }

}
