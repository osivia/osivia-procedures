package org.osivia.services.procedure.formFilters;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.services.procedure.portlet.model.ProcedureRepository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;


/**
 * filter to edit a record
 * 
 * @author Dorian Licois
 */
public class EditRecordFilter extends RecordFormFilter {

    /** Identifier. */
    public static final String ID = "EditRecordFilter";

    /** LABEL_KEY */
    private static final String LABEL_KEY = "EDIT_RECORD_FILTER_LABEL";

    /** Label internationalization key. */
    private static final String DESCRIPTION_KEY = "EDIT_RECORD_FILTER_DESCRIPTION";

    /** Notification internationalization key. */
    private static final String NOTIFICATION_KEY = "RECORD_EDITED_NOTIFICATION";

    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;


    public EditRecordFilter() {
        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }

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
        PropertyList globalVariablesReferences = getGlobalVariablesReferences(IFormsService.FORM_STEP_REFERENCE, properties);
        
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

        // Title
        String title = variables.get(ProcedureRepository.DEFAULT_FIELD_TITLE_NAME);
        if (StringUtils.isNotBlank(title)) {
            updateProperties.set("dc:title", title);
        }
        
        // Record path
        String recordPath = context.getVariables().get("rcdPath");

        // update record with values
        Document editedRecord = (Document) nuxeoController.executeNuxeoCommand(new UpdateRecordCommand(new DocRef(recordPath), updateProperties));

        // Reload record
        nuxeoController.getDocumentContext(recordPath).reload();


        context.getVariables().put(IFormsService.REDIRECT_CMS_PATH_PARAMETER, editedRecord.getPath());

        context.getVariables().put(IFormsService.REDIRECT_MESSAGE_PARAMETER,
                bundleFactory.getBundle(nuxeoController.getRequest().getLocale()).getString(NOTIFICATION_KEY));

    }

}
