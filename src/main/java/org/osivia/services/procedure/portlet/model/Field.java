package org.osivia.services.procedure.portlet.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
        creatorVisibility = Visibility.NONE)
public class Field implements Comparable<Field> {

    /** isInput */
    @JsonProperty("isInput")
    @JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
    private boolean isInput;

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

    /** value */
    private String value;


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

}
