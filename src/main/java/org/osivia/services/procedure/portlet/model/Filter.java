package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;


/**
 * @author dorian
 */
public class Filter implements Comparable<Filter> {

    /** filterName */
    @JsonIgnore
    private String filterName;

    /** filterPath */
    @JsonProperty("filterPath")
    private String filterPath;

    /** filterInstanceId */
    @JsonProperty("filterInstanceId")
    private String filterInstanceId;

    /** filterId */
    @JsonProperty("filterId")
    private String filterId;

    /** argumentsList */
    @JsonProperty("argumentsList")
    private List<Argument> argumentsList;

    /** filters */
    @JsonIgnore
    private List<Filter> filters;


    public Filter() {
    }

    public Filter(FormFilter formFilter, String path) {
        setFilterPath(path);
        setFilterId(formFilter.getId());
        setFilterInstanceId(getFilterId().concat(getFilterPath()));
        setFilterName(formFilter.getLabelKey());
        if (formFilter.getParameters() != null) {
            final List<Argument> argumentsList = new ArrayList<Argument>(formFilter.getParameters().entrySet().size());
            for (Entry<String, FormFilterParameterType> argumentEntry : formFilter.getParameters().entrySet()) {
                Argument argument = new Argument();
                argument.setArgumentName(argumentEntry.getKey());
                argument.setType(argumentEntry.getValue());
                argumentsList.add(argument);
            }
            setArgumentsList(argumentsList);
        }
    }

    public Filter(PropertyMap propertyMap) {
        setFilterName(propertyMap.getString("filterName"));
        setFilterPath(propertyMap.getString("filterPath"));
        setFilterId(propertyMap.getString("filterId"));
        setFilterInstanceId(propertyMap.getString("filterInstanceId"));

        final PropertyList argumentsPptyList = propertyMap.getList("argumentsList");
        if (argumentsPptyList != null) {
            final List<Argument> argumentsList = new ArrayList<Argument>();
            for (final Object argumentO : argumentsPptyList.list()) {
                final PropertyMap argumentMap = (PropertyMap) argumentO;
                argumentsList.add(new Argument(argumentMap));
            }
            setArgumentsList(argumentsList);
        }
    }

    public void updateFilterPath(String newPath) {
        setFilterPath(newPath);
        setFilterInstanceId(getFilterId().concat(getFilterPath()));
    }


    /**
     * Getter for filterName.
     *
     * @return the filterName
     */
    public String getFilterName() {
        return filterName;
    }


    /**
     * Setter for filterName.
     *
     * @param filterName the filterName to set
     */
    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }


    /**
     * Getter for filterPath.
     *
     * @return the filterPath
     */
    public String getFilterPath() {
        return filterPath;
    }


    /**
     * Setter for filterPath.
     *
     * @param filterPath the filterPath to set
     */
    public void setFilterPath(String filterPath) {
        this.filterPath = filterPath;
    }


    /**
     * Getter for argumentsList.
     *
     * @return the argumentsList
     */
    public List<Argument> getArgumentsList() {
        return argumentsList;
    }


    /**
     * Setter for argumentsList.
     *
     * @param argumentsList the argumentsList to set
     */
    public void setArgumentsList(List<Argument> argumentsList) {
        this.argumentsList = argumentsList;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    @Override
    public int compareTo(Filter filter) {
        int returnValue;

        int index = 0;
        final String[] pathArray = StringUtils.split(getFilterPath(), ',');
        final String[] comparedPathArray = StringUtils.split(filter.getFilterPath(), ',');
        Integer pathPart = Integer.parseInt(pathArray[index]);
        Integer comparedPathPart = Integer.parseInt(comparedPathArray[index]);
        returnValue = pathPart.compareTo(comparedPathPart);
        boolean deeperPath = pathArray.length > index + 1;
        boolean deeperComparedPath = comparedPathArray.length > index + 1;
        if (returnValue == 0 && (deeperPath || deeperComparedPath)) {
            if (deeperPath && !deeperComparedPath) {
                returnValue = 1;
            } else if (!deeperPath && deeperComparedPath) {
                returnValue = -1;
            } else {
                index++;
                returnValue = compare(pathArray, comparedPathArray, index);
            }
        }
        return returnValue;
    }

    private int compare(String[] pathArray, String[] comparedPathArray, int index) {
        Integer pathPart = Integer.parseInt(pathArray[index]);
        Integer comparedPathPart = Integer.parseInt(comparedPathArray[index]);
        int returnValue = pathPart.compareTo(comparedPathPart);
        boolean deeperPath = pathArray.length > index + 1;
        boolean deeperComparedPath = comparedPathArray.length > index + 1;
        if (returnValue == 0 && (deeperPath || deeperComparedPath)) {
            if (deeperPath && !deeperComparedPath) {
                returnValue = 1;
            } else if (!deeperPath && deeperComparedPath) {
                returnValue = -1;
            } else {
                index++;
                returnValue = compare(pathArray, comparedPathArray, index);
            }
        }
        return returnValue;
    }

    /**
     * Getter for filterInstanceId.
     * 
     * @return the filterInstanceId
     */
    public String getFilterInstanceId() {
        return filterInstanceId;
    }

    /**
     * Setter for filterInstanceId.
     * 
     * @param filterInstanceId the filterInstanceId to set
     */
    public void setFilterInstanceId(String filterInstanceId) {
        this.filterInstanceId = filterInstanceId;
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
