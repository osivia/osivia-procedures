package org.osivia.services.procedure.portlet.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
        creatorVisibility = Visibility.NONE)
public class Variable {

    /** name */
    @JsonProperty("name")
    private String name;

    /** Label */
    @JsonProperty("label")
    private String label;

    /** type */
    @JsonProperty("type")
    private VariableTypesEnum type;

    public Variable() {
    }

    public Variable(String name, String label, VariableTypesEnum type) {
        this.name = name;
        this.label = label;
        this.type = type;
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
     * Getter for Label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }


    /**
     * Setter for Label.
     *
     * @param label the label to set
     */
    public void setLabel(String label) {
        this.label = label;
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

}
