package org.osivia.services.procedure.portlet.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
creatorVisibility = Visibility.NONE)
public class Field implements Comparable<Field> {

    /** isInput */
    @JsonProperty("isInput")
    @JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
    private boolean isInput;

    /** isInput */
    @JsonProperty("required")
    @JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
    private boolean required;

    /**
     * the name of the field
     */
    @JsonProperty("variableName")
    private String name;

    /**
     * sorting order of the field
     */
    @JsonProperty("order")
    private Integer order;

    /** type */
    @JsonIgnore
    private VariableTypesEnum type;

    /** label */
    @JsonIgnore
    private String label;

    /** value */
    @JsonIgnore
    private String value;

    @JsonIgnore
    private String varOptions;


    public Field() {
    }

    /**
     * constructors with order
     *
     * @param order
     */
    public Field(Integer order) {
        this.order = order;
    }

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
     * @return the sorting order of the field
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * @param set
     *            the sorting order of the field
     */
    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public int compareTo(Field field) {
        return getOrder().compareTo(field.getOrder());
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


    /**
     * Getter for required.
     *
     * @return the required
     */
    public boolean isRequired() {
        return required;
    }


    /**
     * Setter for required.
     *
     * @param required the required to set
     */
    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getVarOptions() {
        return varOptions;
    }

    public void setVarOptions(String varOptions) {
        this.varOptions = varOptions;
    }

}
