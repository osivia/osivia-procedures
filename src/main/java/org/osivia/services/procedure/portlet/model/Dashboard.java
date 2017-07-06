package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonProperty;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;


/**
 * @author Dorian Licois
 */
@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
        creatorVisibility = Visibility.NONE)
public class Dashboard {

    /** name */
    @JsonProperty("name")
    private String name;

    /** groups */
    @JsonProperty("groups")
    private List<String> groups;

    /** columns */
    @JsonProperty("columns")
    private List<Column> columns;


    /**
     * @param dashboardObjectMap
     */
    public Dashboard(PropertyMap dashboardObjectMap) {

        if (dashboardObjectMap != null) {
            setName(dashboardObjectMap.getString("name"));

            final PropertyList groupsList = dashboardObjectMap.getList("groups");
            if (groupsList != null) {
                final List<String> groups = new ArrayList<String>();
                for (final Object groupObject : groupsList.list()) {
                    groups.add((String) groupObject);
                }
                setGroups(groups);
            }

            final PropertyList columnsList = dashboardObjectMap.getList("columns");
            if (columnsList != null) {
                Column column;
                for (final Object columnObject : columnsList.list()) {
                    final PropertyMap columnObjectMap = (PropertyMap) columnObject;
                    column = new Column(columnObjectMap);
                    getColumns().add(column);
                }
            }

        }
    }

    public Dashboard() {
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
     * Getter for groups.
     * 
     * @return the groups
     */
    public List<String> getGroups() {
        return groups;
    }

    /**
     * Getter for groups as string.
     * 
     * @return the groups
     */
    public String getGroupsString() {
        return StringUtils.join(groups, ',');
    }


    /**
     * Setter for groups.
     * 
     * @param groups the groups to set
     */
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }


    /**
     * Getter for columns.
     * 
     * @return the columns
     */
    public List<Column> getColumns() {
        if (columns == null) {
            columns = new ArrayList<Column>();
        }
        return columns;
    }


    /**
     * Setter for columns.
     * 
     * @param columns the columns to set
     */
    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

}
