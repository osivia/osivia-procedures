package org.osivia.services.procedure.portlet.model;


/**
 * @author dorian
 */
public class AddField {

    /** variableName */
    private String variableName;

    /** type */
    private VariableTypesEnum type;

    /** label */
    private String label;

    private String varOptions;

    private boolean isFieldSet;

    /**
     * Getter for variableName.
     *
     * @return the variableName
     */
    public String getVariableName() {
        return variableName;
    }

    /**
     * Setter for variableName.
     *
     * @param variableName the variableName to set
     */
    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    /**
     * Getter for type.
     *
     * @return the type
     */
    public VariableTypesEnum getType() {
        return type;
    }

    /**
     * Setter for type.
     *
     * @param type the type to set
     */
    public void setType(VariableTypesEnum type) {
        this.type = type;
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

    public String getVarOptions() {
        return varOptions;
    }

    public void setVarOptions(String varOptions) {
        this.varOptions = varOptions;
    }


    /**
     * Getter for isFieldSet.
     * 
     * @return the isFieldSet
     */
    public boolean isFieldSet() {
        return isFieldSet;
    }


    /**
     * Setter for isFieldSet.
     * 
     * @param isFieldSet the isFieldSet to set
     */
    public void setFieldSet(boolean isFieldSet) {
        this.isFieldSet = isFieldSet;
    }

}
