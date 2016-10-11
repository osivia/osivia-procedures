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

    /** varOptions */
    private String varOptions;

    /** isInput */
    private boolean isInput;

    /** isRequired */
    private boolean isRequired;

    /** helpText */
    private String helpText;


    /**
     * Getter for isInput.
     *
     * @return the isInput
     */
    public boolean isInput() {
        return isInput;
    }


    /**
     * Setter for isInput.
     *
     * @param isInput the isInput to set
     */
    public void setInput(boolean isInput) {
        this.isInput = isInput;
    }


    /**
     * Getter for isRequired.
     *
     * @return the isRequired
     */
    public boolean isRequired() {
        return isRequired;
    }


    /**
     * Setter for isRequired.
     *
     * @param isRequired the isRequired to set
     */
    public void setRequired(boolean isRequired) {
        this.isRequired = isRequired;
    }


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
     * Getter for helpText.
     *
     * @return the helpText
     */
    public String getHelpText() {
        return helpText;
    }


    /**
     * Setter for helpText.
     *
     * @param helpText the helpText to set
     */
    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }
}
