package org.osivia.services.procedure.formFilters;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Blobs;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.portlet.model.UploadedFile;
import org.osivia.portal.api.portlet.model.UploadedFileMetadata;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Record Nuxeo command abstract super-class.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public abstract class AbstractRecordCommand implements INuxeoCommand {

    /** Files Nuxeo document property. */
    private static final String FILES_PROPERTY = "files:files";


    /** Path. */
    private final String path;
    /** Properties */
    private final PropertyMap properties;
    /** Uploaded files. */
    private final Collection<UploadedFile> uploadedFiles;


    /**
     * Constructor.
     * 
     * @param path path
     * @param properties properties
     * @param uploadedFiles uploaded files
     */
    public AbstractRecordCommand(String path, PropertyMap properties, Collection<UploadedFile> uploadedFiles) {
        super();
        this.path = path;
        this.properties = properties;
        this.uploadedFiles = uploadedFiles;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }


    /**
     * Update blobs.
     * 
     * @param documentService document service
     * @param document document
     * @throws Exception
     */
    protected void updateBlobs(DocumentService documentService, Document document) throws Exception {
        if (CollectionUtils.isNotEmpty(this.uploadedFiles)) {
            // Added blobs
            List<Blob> blobs = new ArrayList<>(this.uploadedFiles.size());
            // Removed blob indexes
            SortedSet<Integer> removedIndexes = new TreeSet<>(Collections.reverseOrder());

            for (UploadedFile uploadedFile : this.uploadedFiles) {
                // Temporary file
                File temporaryFile = uploadedFile.getTemporaryFile();
                // Temporary file metadata
                UploadedFileMetadata metadata = uploadedFile.getTemporaryMetadata();

                if ((uploadedFile.getIndex() != null) && (uploadedFile.isDeleted() || (temporaryFile != null))) {
                    // Remove existing blob
                    removedIndexes.add(uploadedFile.getIndex());
                }

                if (temporaryFile != null) {
                    // File name
                    String fileName = metadata.getFileName();
                    // Mime type
                    String mimeType;
                    if (metadata.getMimeType() == null) {
                        mimeType = null;
                    } else {
                        mimeType = metadata.getMimeType().getBaseType();
                    }

                    // File blob
                    Blob blob = new FileBlob(temporaryFile, fileName, mimeType);

                    blobs.add(blob);
                }
            }

            for (Integer index : removedIndexes) {
                StringBuilder xpath = new StringBuilder();
                xpath.append(FILES_PROPERTY);
                xpath.append("/item[");
                xpath.append(index);
                xpath.append("]");

                documentService.removeBlob(document, xpath.toString());
            }

            if (!blobs.isEmpty()) {
                documentService.setBlobs(document, new Blobs(blobs), FILES_PROPERTY);
            }

            // Delete temporary files
            for (UploadedFile uploadedFile : this.uploadedFiles) {
                if (uploadedFile.getTemporaryFile() != null) {
                    uploadedFile.getTemporaryFile().delete();
                }
            }
        }
    }


    /**
     * Getter for path.
     * 
     * @return the path
     */
    protected String getPath() {
        return path;
    }

    /**
     * Getter for properties.
     * 
     * @return the properties
     */
    protected PropertyMap getProperties() {
        return properties;
    }

    /**
     * Getter for uploadedFiles.
     * 
     * @return the uploadedFiles
     */
    protected Collection<UploadedFile> getUploadedFiles() {
        return uploadedFiles;
    }

}
