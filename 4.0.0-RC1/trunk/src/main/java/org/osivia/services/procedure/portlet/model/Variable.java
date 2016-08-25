package org.osivia.services.procedure.portlet.model;

import java.util.Arrays;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
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

    @JsonProperty("varOptions")
    private List<String> varOptions;

    @JsonIgnore
    private String varOptionsJson;

    public Variable() {
    }

    public Variable(Field field) {
        this.name = field.getName();
        this.label = field.getLabel();
        this.type = field.getType();
        String varOptions = field.getVarOptions();
        if (varOptions != null) {
            this.varOptions = Arrays.asList(varOptions.split(","));
        }
    }

    public Variable(AddField field) {
        this.name = field.getVariableName();
        this.label = field.getLabel();
        this.type = field.getType();
        this.varOptions = Arrays.asList(field.getVarOptions().split(","));
    }

    public Variable(String name, String label, VariableTypesEnum type, List<String> varOptions) {
        this.name = name;
        this.label = label;
        this.type = type;
        this.varOptions = varOptions;
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


    /**
     * Getter for varOptions.
     *
     * @return the varOptions
     */
    public List<String> getVarOptions() {
        return varOptions;
    }


    /**
     * Setter for varOptions.
     *
     * @param varOptions the varOptions to set
     */
    public void setVarOptions(List<String> varOptions) {
        this.varOptions = varOptions;
    }

    public String getVarOptionsJson() {
        return varOptionsJson;
    }

    public void setVarOptionsJson(String varOptionsJson) {
        this.varOptionsJson = varOptionsJson;
    }


}
