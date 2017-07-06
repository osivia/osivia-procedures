package org.osivia.services.procedure.portlet.util;

import java.util.ArrayList;
import java.util.Map;

import org.osivia.services.procedure.portlet.model.Filter;


/**
 * 
 * Util class for Filter
 * 
 * @author Dorian Licois
 * @see Filter
 */
public class FiltersUtil {


    private FiltersUtil() {
    }

    /**
     * recursive method to build architecture of FIlters according to their paths
     * 
     * @param fieldMap
     * @param filter
     */
    public static void fillFilter(Map<String, Filter> fieldMap, Filter filter) {
        boolean completed = false;
        int i = 0;
        while (!completed) {
            final String fieldIndex = String.valueOf(i);
            final Filter mapFilter = fieldMap.get(filter.getFilterPath() + "," + fieldIndex);
            if (mapFilter != null) {
                // nested fields
                fillFilter(fieldMap, mapFilter);

                if (filter.getFilters() == null) {
                    filter.setFilters(new ArrayList<Filter>());
                }
                // add to parent
                filter.getFilters().add(mapFilter);
                i++;
            } else {
                completed = true;
            }
        }
    }

}
