package org.osivia.services.procedure.portlet.service.impl;

import java.util.Map;

import org.osivia.services.procedure.portlet.filter.IFilter;
import org.osivia.services.procedure.portlet.service.IFilterService;


public class FilterServiceImpl implements IFilterService {

    private Map<String, IFilter> filterModelList;


    public FilterServiceImpl() {

    }

    @Override
    public Map<String, IFilter> getFilterModelsList() {
        return filterModelList;
    }


    /**
     * Getter for filterModelList.
     * 
     * @return the filterModelList
     */
    public Map<String, IFilter> getFilterModelList() {
        return filterModelList;
    }


    /**
     * Setter for filterModelList.
     * 
     * @param filterModelList the filterModelList to set
     */
    public void setFilterModelList(Map<String, IFilter> filterModelList) {
        this.filterModelList = filterModelList;
    }


}
