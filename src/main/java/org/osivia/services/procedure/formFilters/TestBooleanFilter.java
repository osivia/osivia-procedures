package org.osivia.services.procedure.formFilters;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;


public class TestBooleanFilter implements FormFilter {

    public static final String ID = "TestBooleanFilter";

    public static final String LABEL_KEY = "TEST_BOOLEAN_FILTER_LABEL";

    public static final String DESCRIPTION_KEY = "TEST_BOOLEAN_FILTER_DESCRIPTION";

    private static final String param_boolean = "param_boolean";

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
        params.put(param_boolean, FormFilterParameterType.BOOLEAN);
        return params;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {
        Boolean paramBooleanValue = BooleanUtils.toBoolean(context.getParamValue(executor, param_boolean));

        String procedureInitiator = context.getParamValue(executor, "${procedureInitiator}");
        String taskInitiator = context.getParamValue(executor, "${taskInitiator}");

        Boolean.TRUE.booleanValue();
    }

}
