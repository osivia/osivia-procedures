package org.osivia.services.procedure.formFilters;

import java.util.Collection;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.portlet.model.UploadedFile;

/**
 * Update record Nuxeo command.
 * 
 * @author Dorian Licois
 * @see AbstractRecordCommand
 */
public class UpdateRecordCommand extends AbstractRecordCommand {

    /**
     * Constructor.
     * 
     * @param path document path
     * @param properties properties
     * @param uploadedFiles uploaded files
     */
    public UpdateRecordCommand(String path, PropertyMap properties, Collection<UploadedFile> uploadedFiles) {
        super(path, properties, uploadedFiles);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        
        // Document reference
        DocRef ref = new DocRef(this.getPath());

        // Update document
        Document document = documentService.update(ref, this.getProperties(), true);

        // Update blobs
        this.updateBlobs(documentService, document);

        return document;
    }

}
