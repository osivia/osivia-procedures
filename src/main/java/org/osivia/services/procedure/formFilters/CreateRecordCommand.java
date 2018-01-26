package org.osivia.services.procedure.formFilters;

import java.util.Collection;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.portlet.model.UploadedFile;


/**
 * Create record Nuxeo command.
 * 
 * @author Dorian Licois
 * @see AbstractRecordCommand
 */
public class CreateRecordCommand extends AbstractRecordCommand {

    /** RECORD */
    private static final String RECORD = "Record";


    /**
     * Constructor.
     * 
     * @param path parent document path
     * @param properties properties
     * @param uploadedFiles uploaded files
     */
    public CreateRecordCommand(String path, PropertyMap properties, Collection<UploadedFile> uploadedFiles) {
        super(path, properties, uploadedFiles);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);

        // Parent document reference
        DocRef ref = new DocRef(this.getPath());

        // Create record document
        Document document = documentService.createDocument(ref, RECORD, null, this.getProperties(), true);

        // Update blobs
        this.updateBlobs(documentService, document);

        return document;
    }

}
