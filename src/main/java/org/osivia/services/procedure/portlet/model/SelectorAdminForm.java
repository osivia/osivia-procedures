package org.osivia.services.procedure.portlet.model;


/**
 * @author Dorian Licois
 */
public class SelectorAdminForm {

    /** selectorId */
    private String selectorId;

    /** label */
    private String label;

    /** procedureWebId */
    private String procedureWebId;


    /**
     * Getter for selectorId.
     * 
     * @return the selectorId
     */
    public String getSelectorId() {
        return selectorId;
    }


    /**
     * Setter for selectorId.
     * 
     * @param selectorId the selectorId to set
     */
    public void setSelectorId(String selectorId) {
        this.selectorId = selectorId;
    }


    /**
     * Getter for label.
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }


    /**
     * Setter for label.
     * 
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
    }


    /**
     * Getter for procedureWebId.
     * 
     * @return the procedureWebId
     */
    public String getProcedureWebId() {
        return procedureWebId;
    }


    /**
     * Setter for procedureWebId.
     * 
     * @param procedureWebId the procedureWebId to set
     */
    public void setProcedureWebId(String procedureWebId) {
        this.procedureWebId = procedureWebId;
    }

}
