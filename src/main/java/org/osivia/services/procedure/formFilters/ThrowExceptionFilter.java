package org.osivia.services.procedure.formFilters;

import java.util.HashMap;
import java.util.Map;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * Throw filter execption form filter
 *
 * @author Dorian Licois
 * @see FormFilter
 */
public class ThrowExceptionFilter implements FormFilter {

    /** Identifier. */
    public static final String ID = "ThrowExceptionFilter";

    /** Label internationalization key. */
    public static final String LABEL_KEY = "THROW_EXCEPTION_FILTER_LABEL";

    /** Description internationalization key. */
    public static final String DESCRIPTION_KEY = "THROW_EXCEPTION_FILTER_DESCRIPTION";

    /** message parameter */
    private static final String message = "message";

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
        Map<String, FormFilterParameterType> params = new HashMap<String, FormFilterParameterType>();
        params.put(message, FormFilterParameterType.TEXTAREA);
        return params;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
        throw new FormFilterException(context.getParamValue(executor, message));
    }

}
