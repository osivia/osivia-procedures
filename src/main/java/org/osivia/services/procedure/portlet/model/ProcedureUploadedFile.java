package org.osivia.services.procedure.portlet.model;

import java.io.File;

import org.osivia.portal.api.portlet.model.UploadedFile;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * Uploaded file java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see UploadedFile
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProcedureUploadedFile implements UploadedFile {

    /** Uploaded multipart file. */
    private MultipartFile upload;

    /** Original file URL. */
    private String url;
    /** Original file index. */
    private Integer index;
    /** Original file metadata. */
    private ProcedureUploadedFileMetadata originalMetadata;
    /** Uploaded temporary file. */
    private File temporaryFile;
    /** Temporary file metadata. */
    private ProcedureUploadedFileMetadata temporaryMetadata;
    /** Deleted file indicator. */
    private boolean deleted;


    /**
     * Constructor.
     */
    public ProcedureUploadedFile() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrl() {
        return this.url;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getIndex() {
        return this.index;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ProcedureUploadedFileMetadata getOriginalMetadata() {
        return this.originalMetadata;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public File getTemporaryFile() {
        return this.temporaryFile;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ProcedureUploadedFileMetadata getTemporaryMetadata() {
        return this.temporaryMetadata;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDeleted() {
        return this.deleted;
    }


    /**
     * Getter for upload.
     * 
     * @return the upload
     */
    public MultipartFile getUpload() {
        return upload;
    }

    /**
     * Setter for upload.
     * 
     * @param upload the upload to set
     */
    public void setUpload(MultipartFile upload) {
        this.upload = upload;
    }

    /**
     * Setter for url.
     * 
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Setter for index.
     * 
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * Setter for originalMetadata.
     * 
     * @param originalMetadata the originalMetadata to set
     */
    public void setOriginalMetadata(ProcedureUploadedFileMetadata originalMetadata) {
        this.originalMetadata = originalMetadata;
    }

    /**
     * Setter for temporaryFile.
     * 
     * @param temporaryFile the temporaryFile to set
     */
    public void setTemporaryFile(File temporaryFile) {
        this.temporaryFile = temporaryFile;
    }

    /**
     * Setter for temporaryMetadata.
     * 
     * @param temporaryMetadata the temporaryMetadata to set
     */
    public void setTemporaryMetadata(ProcedureUploadedFileMetadata temporaryMetadata) {
        this.temporaryMetadata = temporaryMetadata;
    }

    /**
     * Setter for deleted.
     * 
     * @param deleted the deleted to set
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
