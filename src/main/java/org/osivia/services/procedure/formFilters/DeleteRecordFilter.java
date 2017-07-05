package org.osivia.services.procedure.formFilters;

import java.util.Map;

import org.nuxeo.ecm.automation.client.model.DocRef;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;


/**
 * filter to delete a record
 * 
 * @author Dorian Licois
 */
public class DeleteRecordFilter implements FormFilter {

    public static final String ID = "DeleteRecordFilter";

    public static final String LABEL_KEY = "DELETE_RECORD_FILTER_LABEL";

    public static final String DESCRIPTION_KEY = "DELETE_RECORD_FILTER_DESCRIPTION";

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
    }

}
