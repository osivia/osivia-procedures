package org.osivia.services.procedure.portlet.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;


/**
 * @author Dorian Licois
 */
public class Record {


    /** procedureModelWebId */
    private String procedureModelWebId;

    /** globalVariablesValues */
    private Map<String, String> globalVariablesValues;

    /** creator */
    private String creator;

    /** created */
    private Date created;

    /** lastContributor */
    private String lastContributor;

    /** modified */
    private Date modified;

    public Record(Document document) {
        globalVariablesValues = new HashMap<String, String>();
        PropertyMap documentProperties = document.getProperties();

        // global variables
        PropertyMap gvvList = documentProperties.getMap("rcd:globalVariablesValues");
        if (gvvList != null) {
            for (Entry<String, Object> gvvO : gvvList.getMap().entrySet()) {
                globalVariablesValues.put(gvvO.getKey(), (String) gvvO.getValue());
            }
        }

        setProcedureModelWebId(documentProperties.getString("rcd:procedureModelWebId"));
        setCreator(documentProperties.getString("dc:creator"));
        setCreated(documentProperties.getDate("dc:created"));
        setLastContributor(documentProperties.getString("dc:lastContributor"));
        setModified(documentProperties.getDate("dc:modified"));

    }

    /**
     * Getter for procedureModelWebId.
     * 
     * @return the procedureModelWebId
     */
    public String getProcedureModelWebId() {
        return procedureModelWebId;
    }


    /**
     * Setter for procedureModelWebId.
     * 
     * @param procedureModelWebId the procedureModelWebId to set
     */
    public void setProcedureModelWebId(String procedureModelWebId) {
        this.procedureModelWebId = procedureModelWebId;
    }


    /**
     * Getter for globalVariablesValues.
     * 
     * @return the globalVariablesValues
     */
    public Map<String, String> getGlobalVariablesValues() {
        return globalVariablesValues;
    }


    /**
     * Setter for globalVariablesValues.
     * 
     * @param globalVariablesValues the globalVariablesValues to set
     */
    public void setGlobalVariablesValues(Map<String, String> globalVariablesValues) {
        this.globalVariablesValues = globalVariablesValues;
    }


    /**
     * Getter for creator.
     * 
     * @return the creator
     */
    public String getCreator() {
        return creator;
    }


    /**
     * Setter for creator.
     * 
     * @param creator the creator to set
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }


    /**
     * Getter for created.
     * 
     * @return the created
     */
    public Date getCreated() {
        return created;
    }


    /**
     * Setter for created.
     * 
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }


    /**
     * Getter for lastContributor.
     * 
     * @return the lastContributor
     */
    public String getLastContributor() {
        return lastContributor;
    }


    /**
     * Setter for lastContributor.
     * 
     * @param lastContributor the lastContributor to set
     */
    public void setLastContributor(String lastContributor) {
        this.lastContributor = lastContributor;
    }


    /**
     * Getter for modified.
     * 
     * @return the modified
     */
    public Date getModified() {
        return modified;
    }


    /**
     * Setter for modified.
     * 
     * @param modified the modified to set
     */
    public void setModified(Date modified) {
        this.modified = modified;
    }


}
