package org.osivia.services.procedure.formFilters;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author Dorian Licois
 */
public class UpdateRecordCommand implements INuxeoCommand {

    /** properties */
    private PropertyMap properties;

    /** docRef */
    private DocRef docRef;

    /**
     * @param docRef
     * @param updateProperties
     */
    public UpdateRecordCommand(DocRef docRef, PropertyMap updateProperties) {
        this.docRef = docRef;
        this.properties = updateProperties;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        
        return documentService.update(docRef, properties, true);
    }

    @Override
    public String getId() {
        return "UpdateRecordCommand/" + docRef + "/" + properties;
    }

}
