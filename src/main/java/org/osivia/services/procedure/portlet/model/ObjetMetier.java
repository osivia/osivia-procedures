package org.osivia.services.procedure.portlet.model;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;


/**
 * @author Dorian Licois
 */
public class ObjetMetier {

    /** procedureObject */
    private ProcedureObject procedureObject;

    /** procedureObjectInstance */
    private ProcedureObjectInstance procedureObjectInstance;

    /** filePath */
    private FilePath filePath;

    /** properties */
    private PropertyMap properties;


    public ObjetMetier(ProcedureObject procedureObject, ProcedureObjectInstance procedureObjectInstance, FilePath filePath) {
        this.procedureObject = procedureObject;
        this.procedureObjectInstance = procedureObjectInstance;
        this.filePath = filePath;
        properties = new PropertyMap();
    }


    public ObjetMetier(Document document, FilePath filePath) {
        properties = document.getProperties();
        this.filePath = filePath;
    }

    /**
     * Getter for procedureObject.
     *
     * @return the procedureObject
     */
    public ProcedureObject getProcedureObject() {
        return procedureObject;
    }


    /**
     * Setter for procedureObject.
     *
     * @param procedureObject the procedureObject to set
     */
    public void setProcedureObject(ProcedureObject procedureObject) {
        this.procedureObject = procedureObject;
    }


    /**
     * Getter for procedureObjectInstance.
     *
     * @return the procedureObjectInstance
     */
    public ProcedureObjectInstance getProcedureObjectInstance() {
        return procedureObjectInstance;
    }


    /**
     * Setter for procedureObjectInstance.
     *
     * @param procedureObjectInstance the procedureObjectInstance to set
     */
    public void setProcedureObjectInstance(ProcedureObjectInstance procedureObjectInstance) {
        this.procedureObjectInstance = procedureObjectInstance;
    }


    /**
     * Getter for filePath.
     *
     * @return the filePath
     */
    public FilePath getFilePath() {
        return filePath;
    }


    /**
     * Setter for filePath.
     *
     * @param filePath the filePath to set
     */
    public void setFilePath(FilePath filePath) {
        this.filePath = filePath;
    }

    /**
     * Getter for properties.
     * @return the properties
     */
    public PropertyMap getProperties() {
        return properties;
    }

    /**
     * Setter for properties.
     * @param properties the properties to set
     */
    public void setProperties(PropertyMap properties) {
        this.properties = properties;
    }

}
