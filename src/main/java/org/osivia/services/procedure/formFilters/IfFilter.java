package org.osivia.services.procedure.formFilters;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;


public class IfFilter implements FormFilter {

    public static final String ID = "IfFilter";

    public static final String LABEL_KEY = "IF_FILTER_LABEL";

    public static final String DESCRIPTION_KEY = "IF_FILTER_DESCRIPTION";

    private static final String condition_key = "IfCondition";

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
        Map<String, FormFilterParameterType> paramMap = new HashMap<String, FormFilterParameterType>();
        paramMap.put(condition_key, FormFilterParameterType.TEXT);
        return paramMap;
    }

    @Override
    public boolean hasChildren() {
        return true;
    }

    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {

        String condition = context.getParamValue(executor, condition_key);
        if (BooleanUtils.toBoolean(condition)) {
            executor.executeChildren(context);
        }
    }
}
