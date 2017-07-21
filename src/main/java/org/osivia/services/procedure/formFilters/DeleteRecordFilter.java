package org.osivia.services.procedure.formFilters;

import java.util.Map;

import org.nuxeo.ecm.automation.client.model.DocRef;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;


/**
 * filter to delete a record
 * 
 * @author Dorian Licois
 */
public class DeleteRecordFilter implements FormFilter {

    /** Identifier. */
    public static final String ID = "DeleteRecordFilter";

    /** LABEL_KEY */
    public static final String LABEL_KEY = "DELETE_RECORD_FILTER_LABEL";

    /** Label internationalization key. */
    public static final String DESCRIPTION_KEY = "DELETE_RECORD_FILTER_DESCRIPTION";

    /** Notification internationalization key. */
    private static final String NOTIFICATION_KEY = "RECORD_DELETED_NOTIFICATION";

    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;


    public DeleteRecordFilter() {
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
        
        // update record with values
        nuxeoController.executeNuxeoCommand(new DeleteRecordCommand(new DocRef(context.getVariables().get("rcdPath"))));

        context.getVariables().put(IFormsService.REDIRECT_CMS_PATH_PARAMETER, context.getVariables().get("rcdFolderPath"));

        context.getVariables().put(IFormsService.REDIRECT_DISPLAYCONTEXT_PARAMETER, "menu");

        context.getVariables().put(IFormsService.REDIRECT_MESSAGE_PARAMETER,
                bundleFactory.getBundle(nuxeoController.getRequest().getLocale()).getString(NOTIFICATION_KEY));
    }

}
