package org.osivia.services.procedure.formFilters;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author Dorian Licois
 */
public class DeleteRecordCommand implements INuxeoCommand {

    /** docRef */
    private DocRef docRef;

    /**
     * @param docRef
     */
    public DeleteRecordCommand(DocRef docRef) {
        this.docRef = docRef;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        
        documentService.remove(docRef);

        return null;
    }

    @Override
    public String getId() {
        return "DeleteRecordCommand/" + docRef;
    }

}
