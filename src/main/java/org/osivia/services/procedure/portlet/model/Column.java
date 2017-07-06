package org.osivia.services.procedure.portlet.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.nuxeo.ecm.automation.client.model.PropertyMap;


/**
 * @author Dorian Licois
 */
@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
        creatorVisibility = Visibility.NONE)
public class Column implements Comparable<Column> {

    /** Label */
    @JsonProperty("label")
    private String label;

    /** variableName */
    @JsonProperty("variableName")
    private String variableName;

    /** sortable */
    @JsonProperty("sortable")
    private boolean sortable;

    /** index */
    @JsonIgnore
    private Integer index;

    /**
     * @param columnObjectMap
     */
    public Column(PropertyMap columnObjectMap) {
        if (columnObjectMap != null) {
            setLabel(columnObjectMap.getString("label"));
            setVariableName(columnObjectMap.getString("variableName"));
            setSortable(columnObjectMap.getBoolean("sortable"));
        }
    }

    public Column() {
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
     * Getter for sortable.
     * 
     * @return the sortable
     */
    public boolean isSortable() {
        return sortable;
    }


    /**
     * Setter for sortable.
     * 
     * @param sortable the sortable to set
     */
    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }


    /**
     * Getter for index.
     * 
     * @return the index
     */
    public Integer getIndex() {
        return index;
    }


    /**
     * Setter for index.
     * 
     * @param index the index to set
     */
    public void setIndex(Integer index) {
        this.index = index;
    }


    @Override
    public int compareTo(Column column) {
        return getIndex().compareTo(column.getIndex());
    }

}
