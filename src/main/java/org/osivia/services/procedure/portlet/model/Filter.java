package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;


/**
 * @author dorian
 */
public class Filter {

    /** filterName */
    @JsonProperty("filterName")
    private String filterName;

    /** filterPath */
    @JsonProperty("filterPath")
    private String filterPath;

    /** filterId */
    @JsonProperty("filterId")
    private String filterId;

    /** argumentsList */
    @JsonProperty("argumentsList")
    private List<Argument> argumentsList;

    /** filtersList */
    @JsonIgnore
    private List<Filter> filtersList;


    public Filter() {
    }

    public Filter(FormFilter formFilter, String path) {
        setFilterPath(path);
        setFilterId(formFilter.getId());
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
     * Getter for filtersList.
     *
     * @return the filtersList
     */
    public List<Filter> getFiltersList() {
        return filtersList;
    }


    /**
     * Setter for filtersList.
     *
     * @param filtersList the filtersList to set
     */
    public void setFiltersList(List<Filter> filtersList) {
        this.filtersList = filtersList;
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

}
