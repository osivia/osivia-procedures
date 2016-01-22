package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
creatorVisibility = Visibility.NONE)
public class Step implements Comparable<Step> {

    /** stepName */
    @JsonProperty("name")
    private String stepName;

    /** fields */
    @JsonProperty("globalVariablesReferences")
    private List<Field> fields;

    /** actions */
    @JsonProperty("actions")
    private List<Action> actions;

    /** index */
    @JsonProperty("index")
    private Integer index;

    /** id */
    @JsonProperty("reference")
    private String reference;

    @JsonProperty("authorizedGroups")
    private List<String> groups;


    public Step() {
        fields = new ArrayList<Field>();
        actions = new ArrayList<Action>();
    }

    public Step(Integer index) {
        fields = new ArrayList<Field>();
        actions = new ArrayList<Action>();
        setIndex(index);
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


    /**
     * Getter for actions.
     *
     * @return the actions
     */
    public List<Action> getActions() {
        return actions;
    }


    /**
     * Setter for actions.
     *
     * @param actions the actions to set
     */
    public void setActions(List<Action> actions) {
        this.actions = actions;
    }




    public Integer getHighestOrder() {
        Integer highestOrder = -1;
        final Iterator<Field> it = getFields().iterator();
        while (it.hasNext()) {
            final Field stepField = it.next();
            if (stepField.getOrder() > highestOrder) {
                highestOrder = stepField.getOrder();
            }
        }
        return highestOrder;
    }

    /**
     * Getter for index.
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * Setter for index.
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * Getter for stepName.
     *
     * @return the stepName
     */
    public String getStepName() {
        return stepName;
    }

    /**
     * Setter for stepName.
     *
     * @param stepName the stepName to set
     */
    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    @Override
    public int compareTo(Step step) {
        return getIndex().compareTo(step.getIndex());
    }


    /**
     * Getter for reference.
     *
     * @return the reference
     */
    public String getReference() {
        return reference;
    }


    /**
     * Setter for reference.
     *
     * @param reference the reference to set
     */
    public void setReference(String reference) {
        this.reference = reference;
    }


    /**
     * Getter for groups.
     * 
     * @return the groups
     */
    public List<String> getGroups() {
        return groups;
    }


    /**
     * Setter for groups.
     * 
     * @param groups the groups to set
     */
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

}
