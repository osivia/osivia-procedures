package org.osivia.services.procedure.formFilters;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;


public class GoToStepFilter implements FormFilter {

    public static final String ID = "GoToStepFilter";

    public static final String LABEL_KEY = "GOTO_FILTER_LABEL";

    public static final String DESCRIPTION_KEY = "GOTO_FILTER_DESCRIPTION";

    private static final String nextStepVar = "nextStepVar";

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
        params.put(nextStepVar, FormFilterParameterType.STEPREFERENCE);
        return params;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
        String nextStep = context.getParamValue(executor, nextStepVar);
        if (StringUtils.isNotBlank(nextStep)) {
            context.setNextStep(nextStep);
        }
    }

}
