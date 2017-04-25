package org.osivia.services.procedure.formFilters;

import java.util.Map;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;

/**
 * Delete on workflow ending form filter.
 * 
 * @author Cédric Krommenhoek
 * @see FormFilter
 */
public class DeleteOnEndingFormFilter implements FormFilter {

    /** Identifier. */
    public static final String ID = "DeleteOnEnding";

    /** Label internationalization key. */
    private static final String LABEL_KEY = "DELETE_ON_ENDING_FILTER_LABEL";
    /** Description internationalization key. */
    private static final String DESCRIPTION_KEY = "DELETE_ON_ENDING_FILTER_DESCRIPTION";


    /**
     * Constructor.
     */
    public DeleteOnEndingFormFilter() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return ID;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabelKey() {
        return LABEL_KEY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, FormFilterParameterType> getParameters() {
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasChildren() {
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
        context.getVariables().put(IFormsService.DELETE_ON_ENDING_PARAMETER, String.valueOf(true));
    }

}
