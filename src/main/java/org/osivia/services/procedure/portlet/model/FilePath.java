package org.osivia.services.procedure.portlet.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.web.multipart.MultipartFile;


/**
 * @author Dorian Licois
 */
@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
        creatorVisibility = Visibility.NONE)
public class FilePath {

    /** file */
    @JsonIgnore
    private MultipartFile file;

    /** fileName */
    @JsonProperty("fileName")
    private String fileName;

    /** variableName */
    @JsonProperty("variableName")
    private String variableName;

    /** downloadLink */
    @JsonIgnore
    private String downloadLink;


    /**
     * Getter for file.
     * @return the file
     */
    public MultipartFile getFile() {
        return file;
    }


    /**
     * Setter for file.
     * @param file the file to set
     */
    public void setFile(MultipartFile file) {
        this.file = file;
    }


    /**
     * Getter for variableName.
     * @return the variableName
     */
    public String getVariableName() {
        return variableName;
    }


    /**
     * Setter for variableName.
     * @param variableName the variableName to set
     */
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }


    /**
     * Getter for fileName.
     *
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
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
     * Getter for downloadLink.
     * 
     * @return the downloadLink
     */
    public String getDownloadLink() {
        return downloadLink;
    }


    /**
     * Setter for downloadLink.
     * 
     * @param downloadLink the downloadLink to set
     */
    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }
}
