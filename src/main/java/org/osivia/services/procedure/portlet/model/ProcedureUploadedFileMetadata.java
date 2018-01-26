package org.osivia.services.procedure.portlet.model;

import javax.activation.MimeType;

import org.osivia.portal.api.portlet.model.UploadedFileMetadata;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Uploaded file metadata java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see UploadedFileMetadata
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProcedureUploadedFileMetadata implements UploadedFileMetadata {

    /** File name. */
    private String fileName;
    /** File MIME type. */
    private MimeType mimeType;
    /** File type icon. */
    private String icon;


    /**
     * Constructor.
     */
    public ProcedureUploadedFileMetadata() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileName() {
        return this.fileName;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public MimeType getMimeType() {
        return this.mimeType;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getIcon() {
        return this.icon;
    }


    /**
     * Setter for fileName.
     * 
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Setter for mimeType.
     * 
     * @param mimeType the mimeType to set
     */
    public void setMimeType(MimeType mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Setter for icon.
     * 
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

}
