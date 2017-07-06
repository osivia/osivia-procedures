package org.osivia.services.procedure.formFilters;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * Creation of a record form filter
 * 
 * @author Dorian Licois
 */
public class CreateRecordFilter extends RecordFormFilter {

    /** Identifier. */
    public static final String ID = "CreateRecordFilter";

    /** Label internationalization key. */
    public static final String LABEL_KEY = "CREATE_RECORD_FILTER_LABEL";

    /** Description internationalization key. */
    public static final String DESCRIPTION_KEY = "CREATE_RECORD_FILTER_DESCRIPTION";

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
        PropertyMap createProperties = new PropertyMap();
        Map<String, String> variables = new HashMap<String, String>();
        for (Object variableREfO : globalVariablesReferences.list()) {
            PropertyMap variableREfM = (PropertyMap) variableREfO;
            String variableName = variableREfM.getString("variableName");
            variables.put(variableName, context.getVariables().get(variableName));
        }
        createProperties.set("rcd:globalVariablesValues", this.generateVariablesJSON(variables));
        createProperties.set("rcd:procedureModelWebId", context.getModelWebId());
        createProperties.set("dc:title", recordFolder.getTitle());
        
        // create record with values
        nuxeoController.executeNuxeoCommand(new CreateRecordCommand(new DocRef(recordFolder.getPath()), createProperties, recordFolder.getTitle()));
        
    }

}

