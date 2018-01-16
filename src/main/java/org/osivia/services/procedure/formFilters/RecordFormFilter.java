package org.osivia.services.procedure.formFilters;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.procedure.portlet.adapter.ProcedureJSONAdapter;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;


/**
 * Helping abstract class for record related form filters
 *
 * @author Dorian Licois
 */
public abstract class RecordFormFilter implements FormFilter {

    /**
     * retrieves the PropertyList holding the references of the variables for a startingStep
     *
     * @param startingStep
     * @param properties
     * @return
     */
    protected PropertyList getGlobalVariablesReferences(String startingStep, PropertyMap properties) {
        final PropertyList stepsList = properties.getList("pcd:steps");
        if ((stepsList != null) && CollectionUtils.isNotEmpty(stepsList.list())) {
            for (Object stepO : stepsList.list()) {
                PropertyMap stepM = (PropertyMap) stepO;
                if (StringUtils.equals(stepM.getString("reference"), startingStep)) {
                    return stepM.getList("globalVariablesReferences");
                }
            }
        }
        return null;
    }

    /**
     * Builds JSONArray for variables
     *
     * @param variables
     * @return
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    protected String generateVariablesJSON(Map<String, String> variables) throws JsonGenerationException, JsonMappingException, IOException {

        Set<Map<String, String>> objects = new HashSet<>();
        for (Entry<String, String> entry : variables.entrySet()) {
            Map<String, String> object = new HashMap<>(2);
            object.put("name", entry.getKey());
            object.put("value", entry.getValue());
            objects.add(object);
        }

        return ProcedureJSONAdapter.getInstance().toJSON(objects);
    }
}
