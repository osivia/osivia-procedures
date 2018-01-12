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
 * Creation of a record form filter
 * 
 * @author Dorian Licois
 */
public class CreateRecordFilter extends RecordFormFilter {

    /** Identifier. */
    public static final String ID = "CreateRecordFilter";

    /** Label internationalization key. */
    private static final String LABEL_KEY = "CREATE_RECORD_FILTER_LABEL";

    /** Description internationalization key. */
    private static final String DESCRIPTION_KEY = "CREATE_RECORD_FILTER_DESCRIPTION";

    /** Notification internationalization key. */
    private static final String NOTIFICATION_KEY = "RECORD_CREATED_NOTIFICATION";

    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;


    public CreateRecordFilter() {
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
        PropertyMap createProperties = new PropertyMap();
        Map<String, String> variables = new HashMap<String, String>();
        for (Object variableREfO : globalVariablesReferences.list()) {
            PropertyMap variableREfM = (PropertyMap) variableREfO;
            String variableName = variableREfM.getString("variableName");
            variables.put(variableName, context.getVariables().get(variableName));
        }
        createProperties.set("rcd:globalVariablesValues", this.generateVariablesJSON(variables));
        createProperties.set("rcd:procedureModelWebId", context.getModelWebId());
        
        // Title
        String title = variables.get(ProcedureRepository.DEFAULT_FIELD_TITLE_NAME);
        if (StringUtils.isNotBlank(title)) {
            createProperties.set("dc:title", title);
        }
        
        // create record with values
        Document createdRecord = (Document) nuxeoController.executeNuxeoCommand(new CreateRecordCommand(new DocRef(recordFolder.getPath()), createProperties, recordFolder.getTitle()));
        
        context.getVariables().put(IFormsService.REDIRECT_CMS_PATH_PARAMETER, recordFolder.getPath());
        
        context.getVariables().put(IFormsService.REDIRECT_DISPLAYCONTEXT_PARAMETER, "menu");

        context.getVariables().put(IFormsService.REDIRECT_MESSAGE_PARAMETER,
                bundleFactory.getBundle(nuxeoController.getRequest().getLocale()).getString(NOTIFICATION_KEY));
        
    }

}

