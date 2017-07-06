package org.osivia.services.procedure.formFilters;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * @author Dorian Licois
 */
public class CreateRecordCommand implements INuxeoCommand {

    /** RECORD */
    private static final String RECORD = "Record";

    /** properties */
    private PropertyMap properties;

    /** parentRef */
    private DocRef parentRef;

    /** title */
    private String title;


    /**
     * @param parentRef
     * @param properties
     * @param title
     */
    public CreateRecordCommand(DocRef parentRef, PropertyMap properties, String title) {
        this.parentRef = parentRef;
        this.properties = properties;
        this.title = title;
    }

    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        return documentService.createDocument(parentRef, RECORD, title, properties, true);
    }

    @Override
    public String getId() {
        return "CreateRecordCommand/" + parentRef + "/" + properties + "/" + title;
    }

}
