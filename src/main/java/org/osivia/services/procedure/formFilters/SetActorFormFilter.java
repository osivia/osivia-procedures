package org.osivia.services.procedure.formFilters;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormActors;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * Set actor form filter.
 * 
 * @author Cédric Krommenhoek
 * @see FormFilter
 */
public class SetActorFormFilter implements FormFilter {

    /** Form filter identifier. */
    public static final String ID = "SET_ACTOR";

    /** Actor parameter. */
    private static final String ACTOR_PARAMETER = "actor";
    /** Group indicator parameter. */
    private static final String GROUP_INDICATOR_PARAMETER = "group";

    /** Form filter label internationalization key. */
    private static final String LABEL_INTERNATIONALIZATION_KEY = "SET_ACTOR_LABEL";
    /** Form filter description internationalization key. */
    private static final String DESCRIPTION_INTERNATIONALIZATION_KEY = "SET_ACTOR_DESCRIPTION";


    /**
     * Constructor
     */
    public SetActorFormFilter() {
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
        parameters.put(ACTOR_PARAMETER, FormFilterParameterType.TEXT);
        parameters.put(GROUP_INDICATOR_PARAMETER, FormFilterParameterType.BOOLEAN);
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
    public void execute(FormFilterContext context, FormFilterExecutor executor) {
        // Actors
        FormActors actors = context.getActors();

        // Actor
        String actor = context.getParamValue(executor, ACTOR_PARAMETER);
        // Group indicator
        boolean group = BooleanUtils.toBoolean(context.getParamValue(executor, GROUP_INDICATOR_PARAMETER));
        
        if (StringUtils.isNotBlank(actor)) {
            if (group) {
                actors.getGroups().add(actor);
            } else {
                actors.getUsers().add(actor);
            }
        }
    }

}
