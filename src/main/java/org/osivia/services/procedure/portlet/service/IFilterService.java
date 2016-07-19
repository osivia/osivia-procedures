package org.osivia.services.procedure.portlet.service;

import java.util.Map;

import org.osivia.services.procedure.portlet.filter.IFilter;


public interface IFilterService {

    public Map<String, IFilter> getFilterModelsList();

}
