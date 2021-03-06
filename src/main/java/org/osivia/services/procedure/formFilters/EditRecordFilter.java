package org.osivia.services.procedure.formFilters;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.portlet.model.UploadedFile;
import org.osivia.services.procedure.portlet.model.ProcedureRepository;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;
import fr.toutatice.portail.cms.nuxeo.api.services.INuxeoService;


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
    /** Nuxeo service. */
    private final INuxeoService nuxeoService;


    /**
     * Constructor.
     */
    public EditRecordFilter() {
        super();

        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
        // Nuxeo service
        this.nuxeoService = Locator.findMBean(INuxeoService.class, INuxeoService.MBEAN_NAME);
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
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException, PortalException {
        // Forms service
        IFormsService formsService = this.nuxeoService.getFormsService();
        // Portal controller context
        PortalControllerContext portalControllerContext = context.getPortalControllerContext();
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // fetch model
        String fetchPath = NuxeoController.webIdToFetchPath(context.getModelWebId());
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(fetchPath);

        // fetch variable refs in the currentStep
        Document recordFolder = documentContext.getDocument();
        PropertyMap properties = recordFolder.getProperties();
        PropertyList globalVariablesReferences = getGlobalVariablesReferences(IFormsService.FORM_STEP_REFERENCE, properties);

        // Uploaded files
        Map<String, UploadedFile> uploadedFiles = context.getUploadedFiles();

        // get values of referenced variables
        PropertyMap updateProperties = new PropertyMap();
        Map<String, String> variables = getVariables(context, globalVariablesReferences, uploadedFiles);
        updateProperties.set("rcd:globalVariablesValues", formsService.convertVariablesToJson(portalControllerContext, variables));
        updateProperties.set("rcd:procedureModelWebId", context.getModelWebId());

        // Title
        String title = variables.get(ProcedureRepository.DEFAULT_FIELD_TITLE_NAME);
        if (StringUtils.isNotBlank(title)) {
            updateProperties.set("dc:title", title);
        }

        // Record path
        String recordPath = context.getVariables().get("rcdPath");

        // Update record with values
        INuxeoCommand command = new UpdateRecordCommand(recordPath, updateProperties, uploadedFiles.values());
        Document editedRecord = (Document) nuxeoController.executeNuxeoCommand(command);

        // Reload record
        nuxeoController.getDocumentContext(recordPath).reload();


        context.getVariables().put(IFormsService.REDIRECT_CMS_PATH_PARAMETER, editedRecord.getPath());

        context.getVariables().put(IFormsService.REDIRECT_MESSAGE_PARAMETER,
                bundleFactory.getBundle(nuxeoController.getRequest().getLocale()).getString(NOTIFICATION_KEY));
    }

}
