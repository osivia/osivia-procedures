package org.osivia.services.procedure.portlet.model;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
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

    /** enableLink */
    @JsonProperty("enableLink")
    private boolean enableLink;

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
            setSortable(BooleanUtils.toBoolean(columnObjectMap.getBoolean("sortable")));
            setEnableLink(BooleanUtils.toBoolean(columnObjectMap.getBoolean("enableLink")));
        }
    }

    public Column() {
    }

    public boolean isDeletable() {
        // if default field
        if (StringUtils.equals(ProcedureRepository.DEFAULT_FIELD_TITLE_NAME, this.variableName)) {
            return false;
        }
        return true;
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
     * Getter for enableLink.
     * 
     * @return the enableLink
     */
    public boolean isEnableLink() {
        return enableLink;
    }


    /**
     * Setter for enableLink.
     * 
     * @param enableLink the enableLink to set
     */
    public void setEnableLink(boolean enableLink) {
        this.enableLink = enableLink;
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
