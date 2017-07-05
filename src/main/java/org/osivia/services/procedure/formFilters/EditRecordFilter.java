package org.osivia.services.procedure.formFilters;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.services.procedure.portlet.model.ProcedureRepository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;


/**
 * filter to edit a record
 * 
 * @author Dorian Licois
 */
public class EditRecordFilter implements FormFilter {

    public static final String ID = "EditRecordFilter";

    public static final String LABEL_KEY = "EDIT_RECORD_FILTER_LABEL";

    public static final String DESCRIPTION_KEY = "EDIT_RECORD_FILTER_DESCRIPTION";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getLabelKey() {
        return LABEL_KEY;
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    public Map<String, FormFilterParameterType> getParameters() {
        return null;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
        NuxeoController nuxeoController = new NuxeoController(context.getPortalControllerContext());
        
     // fetch model
        String fetchPath = NuxeoController.webIdToFetchPath(context.getModelWebId());
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(fetchPath);
        
     // fetch variable refs in the currentStep
        Document recordFolder = documentContext.getDocument();
        PropertyMap properties = recordFolder.getProperties();
        String startingStep = context.getVariables().get("pcd:startingStep");
        PropertyList globalVariablesReferences = getGlobalVariablesReferences(startingStep, properties);
        
        
     // get values of referenced variables
        PropertyMap updateProperties = new PropertyMap();
        Map<String, String> variables = new HashMap<String, String>();
        for (Object variableREfO : globalVariablesReferences.list()) {
            PropertyMap variableREfM = (PropertyMap) variableREfO;
            String variableName = variableREfM.getString("variableName");
            variables.put(variableName, context.getVariables().get(variableName));
        }
        updateProperties.set("rcd:globalVariablesValues", this.generateVariablesJSON(variables));
        updateProperties.set("rcd:procedureModelWebId", context.getModelWebId());

        
        // update record with values
        nuxeoController.executeNuxeoCommand(new UpdateRecordCommand(new DocRef(context.getVariables().get("rcdPath")), updateProperties));
    }

    private PropertyList getGlobalVariablesReferences(String startingStep, PropertyMap properties) {
        final PropertyList stepsList = properties.getList("pcd:steps");
        if (stepsList != null && CollectionUtils.isNotEmpty(stepsList.list())) {
            for (Object stepO : stepsList.list()) {
                PropertyMap stepM = (PropertyMap) stepO;
                if (StringUtils.equals(stepM.getString("reference"), startingStep) || StringUtils.equals(startingStep, ProcedureRepository.FORM_STEP_REFERENCE)) {
                    return stepM.getList("globalVariablesReferences");
                }
            }
        }
        return null;
    }

    private String generateVariablesJSON(Map<String, String> variables) {
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
