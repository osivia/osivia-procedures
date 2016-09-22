package org.osivia.services.procedure.formFilters;

import java.util.HashMap;
import java.util.Map;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;


public class SetVariableAsActorFilter implements FormFilter {

    public static final String ID = "setVariableAsActor";

    public static final String LABEL_KEY = "SET_VARIABLE_AS_ACTOR_LABEL";

    public static final String DESCRIPTION_KEY = "SET_VARIABLE_AS_ACTOR_DESCRIPTION";

    private static final String tempParamVariableKey = "uid";

    @Override
    public void execute(FormFilterContext ctx, FormFilterExecutor executor) {
        // uid to set as Actor
        String uidVariable = ctx.getParamValue(executor, tempParamVariableKey);
        ctx.getActors().getUsers().add(ctx.getVariables().get(uidVariable));
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public Map<String, FormFilterParameterType> getParameters() {
        Map<String, FormFilterParameterType> paramsMap = new HashMap<String, FormFilterParameterType>(1);
        paramsMap.put(tempParamVariableKey, FormFilterParameterType.TEXT);
        return paramsMap;
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_KEY;
    }

    @Override
    public String getLabelKey() {
        return LABEL_KEY;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }
}
