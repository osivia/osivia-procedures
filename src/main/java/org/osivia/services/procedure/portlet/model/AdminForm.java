package org.osivia.services.procedure.portlet.model;


/**
 * @author dorian
 */
public class AdminForm {

    /** procedurePath */
    private String procedurePath;


    /**
     * @param procedurePath
     */
    public AdminForm(String procedurePath) {
        this.procedurePath = procedurePath;
    }

    /**
     * Getter for procedurePath.
     *
     * @return the procedurePath
     */
    public String getProcedurePath() {
        return procedurePath;
    }

    /**
     * Setter for procedurePath.
     *
     * @param procedurePath the procedurePath to set
     */
    public void setProcedurePath(String procedurePath) {
        this.procedurePath = procedurePath;
    }
}
