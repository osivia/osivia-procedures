/**
 * 
 */
package org.osivia.services.procedure.portlets.model;


/**
 * @author david
 *
 */
public class ProcedureModel {
    
    /** Name of procedure model. */
    private String name;
    /** Description of procedure.*/
    private String description;
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
