package org.osivia.services.procedure.portlet.filter.impl;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormFilterModule;

public class SetActorsFilterModule implements IFormFilterModule {
    
    public static final String KEY = "SET_ACTOR";

    @Override
    public void execute(FormFilterContext ctx) {
        ctx.getActors().getUsers().clear();
        String actor = ctx.getFilterParams().get("assignee");
        //"${actorId}"
        String actorName = ctx.getVariables().get("actorId");
        ctx.getActors().getUsers().add(actorName);
    }

}
