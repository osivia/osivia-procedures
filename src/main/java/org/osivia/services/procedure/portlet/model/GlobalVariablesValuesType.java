package org.osivia.services.procedure.portlet.model;


/**
 * @author Dorian Licois
 */
public class GlobalVariablesValuesType {

    /** name */
    private String name;

    /** value */
    private String value;

    public GlobalVariablesValuesType() {
    }

    /**
     * @param name
     * @param value
     */
    public GlobalVariablesValuesType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Getter for name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Getter for value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }


    /**
     * Setter for value.
     *
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
