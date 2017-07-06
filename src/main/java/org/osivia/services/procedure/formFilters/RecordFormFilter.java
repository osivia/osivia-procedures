package org.osivia.services.procedure.formFilters;

import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

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
        if (stepsList != null && CollectionUtils.isNotEmpty(stepsList.list())) {
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
     */
    protected String generateVariablesJSON(Map<String, String> variables) {
        JSONArray array = new JSONArray();
        for (Entry<String, String> entry : variables.entrySet()) {
            JSONObject object = new JSONObject();
            object.put("name", entry.getKey());
            object.put("value", entry.getValue());
            array.add(object);
        }
        return array.toString();
    }
}
