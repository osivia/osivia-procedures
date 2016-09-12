package org.osivia.services.procedure.formFilters;

import java.util.HashMap;
import java.util.Map;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;


public class DefineVariableFilter implements FormFilter {

    public static final String ID = "DefineVarFilter";

    public static final String LABEL_KEY = "DEFINE_VAR_FILTER_LABEL";

    public static final String DESCRIPTION_KEY = "DEFINE_VAR_FILTER_DESCRIPTION";

    private static final String variableName = "variableName";

    private static final String variableValue = "variableValue";

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
        Map<String, FormFilterParameterType> params = new HashMap<String, FormFilterParameterType>(2);
        params.put(variableName, FormFilterParameterType.TEXT);
        params.put(variableValue, FormFilterParameterType.TEXT);
        return params;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
        context.getVariables().put(context.getParamValue(executor, variableName), context.getParamValue(executor, variableValue));
    }

}
