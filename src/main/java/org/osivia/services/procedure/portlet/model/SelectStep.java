package org.osivia.services.procedure.portlet.model;


/**
 * @author dorian
 */
public class SelectStep {

    /** label */
    private String label;

    /** identifiant */
    private String identifiant;


    public SelectStep(String label, String identifiant) {
        this.label = label;
        this.identifiant = identifiant;
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
     * Getter for identifiant.
     *
     * @return the identifiant
     */
    public String getIdentifiant() {
        return identifiant;
    }


    /**
     * Setter for identifiant.
     *
     * @param identifiant the identifiant to set
     */
    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }
}
