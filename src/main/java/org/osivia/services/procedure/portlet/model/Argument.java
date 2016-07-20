package org.osivia.services.procedure.portlet.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;


/**
 * @author dorian
 */
public class Argument {

    /** argumentName */
    @JsonProperty("argumentName")
    private String argumentName;

    /** argumentValue */
    @JsonProperty("argumentValue")
    private String argumentValue;

    /** type */
    private FormFilterParameterType type;


    public Argument() {
    }

    public Argument(PropertyMap argumentMap) {
        setArgumentName(argumentMap.getString("argumentName"));
        setArgumentValue(argumentMap.getString("argumentValue"));
    }

    /**
     * Getter for argumentName.
     *
     * @return the argumentName
     */
    public String getArgumentName() {
        return argumentName;
    }


    /**
     * Setter for argumentName.
     *
     * @param argumentName the argumentName to set
     */
    public void setArgumentName(String argumentName) {
        this.argumentName = argumentName;
    }


    /**
     * Getter for argumentValue.
     *
     * @return the argumentValue
     */
    public String getArgumentValue() {
        return argumentValue;
    }


    /**
     * Setter for argumentValue.
     *
     * @param argumentValue the argumentValue to set
     */
    public void setArgumentValue(String argumentValue) {
        this.argumentValue = argumentValue;
    }

    /**
     * Getter for type.
     * 
     * @return the type
     */
    public FormFilterParameterType getType() {
        return type;
    }

    /**
     * Setter for type.
     * 
     * @param type the type to set
     */
    public void setType(FormFilterParameterType type) {
        this.type = type;
    }
}
