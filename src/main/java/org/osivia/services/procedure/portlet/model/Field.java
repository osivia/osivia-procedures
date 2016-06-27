package org.osivia.services.procedure.portlet.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

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
    @JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
    private String name;

    @JsonProperty("superLabel")
    private String superLabel;

    @JsonProperty("isFieldSet")
    @JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
    private boolean isFieldSet;

    @JsonProperty("nestedGlobalVariablesReferences")
    private List<Field> fields;

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

    @JsonProperty("path")
    private String path;


    public Field() {
    }

    public Field(PropertyMap propertyMap, Map<String, Variable> variables) {
        setInput(propertyMap.getBoolean("isInput"));
        setRequired(propertyMap.getBoolean("required"));
        setSuperLabel(propertyMap.getString("superLabel"));
        setFieldSet(BooleanUtils.isTrue(propertyMap.getBoolean("isFieldSet")));
        setPath(propertyMap.getString("path"));

        final Variable variable = variables.get(propertyMap.getString("variableName"));
        if (variable != null) {
            setName(variable.getName());
            setLabel(variable.getLabel());
            setType(variable.getType());
            setVarOptions(StringUtils.join(variable.getVarOptions(), ","));
        }
    }

    /**
     * constructors with path
     *
     * @param path
     */
    public Field(String path) {
        this.path = path;
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

    @Override
    public int compareTo(Field field) {
        return StringUtils.substringAfterLast(getPath(), ",").compareTo(StringUtils.substringAfterLast(field.getPath(), ","));
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


    /**
     * Getter for superLabel.
     *
     * @return the superLabel
     */
    public String getSuperLabel() {
        return superLabel;
    }


    /**
     * Setter for superLabel.
     *
     * @param superLabel the superLabel to set
     */
    public void setSuperLabel(String superLabel) {
        this.superLabel = superLabel;
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


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    /**
     * Getter for fields.
     *
     * @return the fields
     */
    public List<Field> getFields() {
        return fields;
    }



    /**
     * Setter for fields.
     *
     * @param fields the fields to set
     */
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

}
