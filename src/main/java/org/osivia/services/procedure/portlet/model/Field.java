package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
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

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
 * @author Dorian Licois
 */
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

    /** superLabel */
    @JsonProperty("superLabel")
    private String superLabel;

    /** isFieldSet */
    @JsonProperty("isFieldSet")
    @JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
    private boolean isFieldSet;

    /** fields */
    @JsonProperty("nestedGlobalVariablesReferences")
    private List<Field> fields;

    /** type */
    @JsonIgnore
    private VariableTypesAllEnum type;

    /** label */
    @JsonIgnore
    private String label;

    /** value */
    @JsonIgnore
    private String value;

    /** varOptions */
    @JsonIgnore
    private String varOptions;

    /** path */
    @JsonProperty("path")
    private String path;

    /** helpText */
    @JsonProperty("helpText")
    private String helpText;
    
    @JsonIgnore
    private Object jsonVarOptions;

    public Field() {
    }

    /**
     * @param propertyMap
     * @param variables
     */
    public Field(PropertyMap propertyMap, Map<String, Variable> variables) {
        setInput(propertyMap.getBoolean("isInput"));
        setRequired(propertyMap.getBoolean("required"));
        setSuperLabel(propertyMap.getString("superLabel"));
        setFieldSet(BooleanUtils.isTrue(propertyMap.getBoolean("isFieldSet")));
        setPath(propertyMap.getString("path"));
        setHelpText(propertyMap.getString("helpText"));

        final Variable variable = variables.get(propertyMap.getString("variableName"));
        if (variable != null) {
            setName(variable.getName());
            setLabel(variable.getLabel());
            setType(variable.getType());
            setVarOptions(variable.getVarOptions());
        }
    }

    /**
     * constructors with path
     *
     * @param path
     */
    public Field(String path, AddField addField, boolean isFieldSet) {
        setPath(path);
        setInput(addField.isInput());
        setRequired(addField.isRequired());
        setSuperLabel(addField.getLabel());
        setFieldSet(isFieldSet);
        setName(addField.getVariableName());
        setLabel(addField.getLabel());
        setType(addField.getType());
        setVarOptions(addField.getVarOptions());
        setHelpText(addField.getHelpText());
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
        int returnValue;

        int index=0;
        final String[] pathArray = StringUtils.split(getPath(), ',');
        final String[] comparedPathArray = StringUtils.split(field.getPath(), ',');
        Integer pathPart = Integer.parseInt(pathArray[index]);
        Integer comparedPathPart = Integer.parseInt(comparedPathArray[index]);
        returnValue = pathPart.compareTo(comparedPathPart);
        boolean deeperPath = pathArray.length>(index+1);
        boolean deeperComparedPath = comparedPathArray.length>(index+1);
        if((returnValue==0) && (deeperPath ||deeperComparedPath)){
            if(deeperPath && !deeperComparedPath){
                returnValue = 1;
            }else if(!deeperPath && deeperComparedPath){
                returnValue = -1;
            }else{
                index++;
                returnValue =compare(pathArray, comparedPathArray, index);
            }
        }
        return returnValue;
    }

    private int compare(String[] pathArray, String[] comparedPathArray, int index){
        Integer pathPart = Integer.parseInt(pathArray[index]);
        Integer comparedPathPart = Integer.parseInt(comparedPathArray[index]);
        int returnValue = pathPart.compareTo(comparedPathPart);
        boolean deeperPath = pathArray.length>(index+1);
        boolean deeperComparedPath = comparedPathArray.length>(index+1);
        if((returnValue==0) && (deeperPath ||deeperComparedPath)){
            if(deeperPath && !deeperComparedPath){
                returnValue = 1;
            }else if(!deeperPath && deeperComparedPath){
                returnValue = -1;
            }else{
                index++;
                returnValue =compare(pathArray, comparedPathArray, index);
            }
        }
        return returnValue;
    }

    public boolean isDeletable(){
        // if default field
        if(StringUtils.equals(ProcedureRepository.DEFAULT_FIELD_TITLE_NAME, this.name)){
            return false;
        }
        return true;
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

    /**
     * Getter for fields.
     *
     * @return the fields
     */
    public List<Field> getFields() {
        if (fields == null) {
            fields = new ArrayList<>();
        }
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
        
        if (VariableTypesAllEnum.SELECTLIST.equals(type) || VariableTypesAllEnum.RADIOLIST.equals(this.type)
                || VariableTypesAllEnum.CHECKBOXLIST.equals(this.type)) {
        	jsonVarOptions = JSONArray.fromObject(varOptions);
        } else if (VariableTypesAllEnum.VOCABULARY.equals(this.type)) {
            try {
                jsonVarOptions = JSONObject.fromObject(varOptions);
            } catch (JSONException e) {
                jsonVarOptions = null;
            }
        }
    }

    /**
     * Getter for path.
     * 
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Setter for path.
     * 
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    public Object getJsonVarOptions() {
		return jsonVarOptions;
	}

}
