package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.procedure.portlet.util.FiltersUtil;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * @author Dorian Licois
 */
@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
creatorVisibility = Visibility.NONE)
public class Action {

    /** stepReference */
    @JsonProperty("stepReference")
    private String stepReference;

    /** label */
    @JsonProperty("label")
    private String label;

    /** actionId */
    @JsonProperty("actionId")
    private String actionId;

    /** filtersList */
    @JsonProperty("filtersList")
    private Set<Filter> filtersList;

    /** filters */
    @JsonIgnore
    private List<Filter> filters;

    public Action() {
        filtersList = new HashSet<Filter>();
        filters = new ArrayList<Filter>();
    }

    /**
     * @param propertyMap
     * @param nuxeoController
     */
    public Action(PropertyMap propertyMap, NuxeoController nuxeoController) {
        setLabel(propertyMap.getString("label"));
        setActionId(propertyMap.getString("actionId"));
        setStepReference(propertyMap.getString("stepReference"));
        filtersList = new HashSet<Filter>();
        filters = new ArrayList<Filter>();

        final PropertyList filtersPList = propertyMap.getList("filtersList");
        if (filtersPList != null) {
            final Map<String, Filter> filterMap = new HashMap<String, Filter>();
            Filter filter;
            for (final Object fltrO : filtersPList.list()) {
                final PropertyMap fltrMap = (PropertyMap) fltrO;
                filter = new Filter(fltrMap, nuxeoController);
                filterMap.put(filter.getFilterPath(), filter);
                filtersList.add(filter);
            }


            // on recrée l'architecture
            boolean completed = false;
            int i = 0;
            while (!completed) {
                final String fieldIndex = String.valueOf(i);
                final Filter mapFilter = filterMap.get(fieldIndex);
                if (mapFilter != null) {
                    // nested filter
                    FiltersUtil.fillFilter(filterMap, mapFilter);

                    // add to parent
                    getFilters().add(mapFilter);
                    i++;
                } else {
                    completed = true;
                }
            }
        }
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
     * Getter for stepReference.
     *
     * @return the stepReference
     */
    public String getStepReference() {
        return stepReference;
    }


    /**
     * Setter for stepReference.
     *
     * @param stepReference the stepReference to set
     */
    public void setStepReference(String stepReference) {
        this.stepReference = stepReference;
    }

    /**
     * Getter for actionId.
     *
     * @return the actionId
     */
    public String getActionId() {
        return actionId;
    }


    /**
     * Setter for actionId.
     *
     * @param actionId the actionId to set
     */
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }


    /**
     * Getter for filtersList.
     *
     * @return the filtersList
     */
    public Set<Filter> getFiltersList() {
        return filtersList;
    }


    /**
     * Setter for filtersList.
     *
     * @param filtersList the filtersList to set
     */
    public void setFiltersList(Set<Filter> filtersList) {
        this.filtersList = filtersList;
    }


    /**
     * Getter for filters.
     *
     * @return the filters
     */
    public List<Filter> getFilters() {
        return filters;
    }


    /**
     * Setter for filters.
     *
     * @param filters the filters to set
     */
    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
}
