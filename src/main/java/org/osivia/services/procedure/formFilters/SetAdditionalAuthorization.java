package org.osivia.services.procedure.formFilters;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * Set additional authorization.
 * 
 * @author Cédric Krommenhoek
 * @see FormFilter
 */
public class SetAdditionalAuthorization implements FormFilter {

    /** Filter identifier. */
    public static final String ID = "SET_ADDITIONAL_AUTHORIZATION";


    /** Additional authorization identifier parameter. */
    private static final String ID_PARAMETER = "id";

    /** Form filter label internationalization key. */
    private static final String LABEL_INTERNATIONALIZATION_KEY = "SET_ADDITIONAL_AUTHORIZATION_LABEL";
    /** Form filter description internationalization key. */
    private static final String DESCRIPTION_INTERNATIONALIZATION_KEY = "SET_ADDITIONAL_AUTHORIZATION_DESCRIPTION";


    /**
     * Constructor.
     */
    public SetAdditionalAuthorization() {
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
        return LABEL_INTERNATIONALIZATION_KEY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_INTERNATIONALIZATION_KEY;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, FormFilterParameterType> getParameters() {
        Map<String, FormFilterParameterType> parameters = new HashMap<String, FormFilterParameterType>();
        parameters.put(ID_PARAMETER, FormFilterParameterType.TEXT);
        return parameters;
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
        // Additional authorizations
        Set<String> additionalAuthorizations = context.getAdditionalAuthorizations();
        
        // Additional authorization identifier
        String id = context.getParamValue(executor, ID_PARAMETER);
        
        if (StringUtils.isNotBlank(id)) {
            additionalAuthorizations.add(id);
        }
    }

}
