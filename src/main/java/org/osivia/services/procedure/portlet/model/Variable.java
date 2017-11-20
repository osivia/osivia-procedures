package org.osivia.services.procedure.portlet.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Dorian Licois
 */
@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
creatorVisibility = Visibility.NONE)
public class Variable implements Comparable<Variable> {

    /** DC_CREATOR */
    public static final Variable DC_CREATOR = new Variable("dc:creator", "Créé par", VariableTypesAllEnum.TEXT, null);
    /** DC_CREATED */
    public static final Variable DC_CREATED = new Variable("dc:created", "Créé le", VariableTypesAllEnum.TEXT, null);
    /** DC_LAST_CONTRIBUTOR */
    public static final Variable DC_LAST_CONTRIBUTOR = new Variable("dc:lastContributor", "Modifié par", VariableTypesAllEnum.TEXT, null);
    /** DC_MODIFIED */
    public static final Variable DC_MODIFIED = new Variable("dc:modified", "Modifié le", VariableTypesAllEnum.TEXT, null);

    /** name */
    @JsonProperty("name")
    private String name;

    /** Label */
    @JsonProperty("label")
    private String label;

    /** type */
    @JsonProperty("type")
    private VariableTypesAllEnum type;

    /** varOptions */
    @JsonProperty("varOptions")
    private String varOptions;

    /** fields in which variable is used */
    @JsonIgnore
    private Map<String, List<Field>> usedInFields;

    public Variable() {
    }

    /**
     * @param field
     */
    public Variable(Field field) {
        this.name = field.getName();
        this.label = field.getLabel();
        this.type = field.getType();
        this.varOptions = field.getVarOptions();
    }

    /**
     * @param field
     */
    public Variable(AddField field) {
        this.name = field.getVariableName();
        this.label = field.getLabel();
        this.type = field.getType();
        this.varOptions = field.getVarOptions();
    }

    /**
     * @param name
     * @param label
     * @param type
     * @param varOptions
     */
    public Variable(String name, String label, VariableTypesAllEnum type, String varOptions) {
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
    public VariableTypesAllEnum getType() {
        return type;
    }


    /**
     * Setter for type.
     *
     * @param type the type to set
     */
    public void setType(VariableTypesAllEnum type) {
        this.type = type;
    }


    /**
     * Getter for varOptions.
     * 
     * @return the varOptions
     */
    public String getVarOptions() {
        return varOptions;
    }


    /**
     * Setter for varOptions.
     * 
     * @param varOptions the varOptions to set
     */
    public void setVarOptions(String varOptions) {
        this.varOptions = varOptions;
    }


    /**
     * Getter for usedInFields.
     *
     * @return the usedInFields
     */
    public Map<String, List<Field>> getUsedInFields() {
        if (usedInFields == null) {
            usedInFields = new HashMap<String, List<Field>>();
        }
        return usedInFields;
    }


    /**
     * Setter for usedInFields.
     *
     * @param usedInFields the usedInFields to set
     */
    public void setUsedInFields(Map<String, List<Field>> usedInFields) {
        this.usedInFields = usedInFields;
    }

    @Override
    public int compareTo(Variable variable) {
        return getName().compareTo(variable.getName());
    }

}
