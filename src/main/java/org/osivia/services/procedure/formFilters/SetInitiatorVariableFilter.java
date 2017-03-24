package org.osivia.services.procedure.formFilters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Name;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.directory.v2.DirServiceFactory;

import fr.toutatice.outils.ldap.v2.model.ENTPerson;
import fr.toutatice.outils.ldap.v2.service.ENTPersonService;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;


/**
 * Fill variables with initiator data form filter
 *
 * @author dorian
 * @see FormFilter
 */
public class SetInitiatorVariableFilter implements FormFilter {

    /** Form filter identifier. */
    public static final String ID = "SET_INITIATOR_VARIABLE";

    /** Form filter label internationalization key. */
    private static final String LABEL_INTERNATIONALIZATION_KEY = "SET_INITIATOR_VARIABLE_LABEL";
    /** Form filter description internationalization key. */
    private static final String DESCRIPTION_INTERNATIONALIZATION_KEY = "SET_INITIATOR_VARIABLE_DESCRIPTION";

    private final ENTPersonService dirService;


    public SetInitiatorVariableFilter() {
        dirService = DirServiceFactory.getService(ENTPersonService.class);
    }


    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getLabelKey() {
        return LABEL_INTERNATIONALIZATION_KEY;
    }

    @Override
    public String getDescriptionKey() {
        return DESCRIPTION_INTERNATIONALIZATION_KEY;
    }

    @Override
    public Map<String, FormFilterParameterType> getParameters() {
        return null;
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public void execute(FormFilterContext context, FormFilterExecutor executor) throws FormFilterException {


        String procedureInitiator = context.getProcedureInitiator();

        if(StringUtils.isNotBlank(procedureInitiator)){
            ENTPerson person = dirService.getPerson(procedureInitiator);
            if(person!=null){

                List<String> namesS = new ArrayList<String>();
                for (Name names : person.getProfiles()) {
                    namesS.add(names.toString());
                }
                String profiles = StringUtils.join(namesS, ',');

                context.getVariables().put("procedureInitiator_avatarUrl", person.getAvatar().getUrl());
                context.getVariables().put("procedureInitiator_cn", person.getCn());
                context.getVariables().put("procedureInitiator_displayName", person.getDisplayName());
                context.getVariables().put("procedureInitiator_dn", person.getDn().toString());
                context.getVariables().put("procedureInitiator_givenName", person.getGivenName());
                context.getVariables().put("procedureInitiator_mail", person.getMail());
                context.getVariables().put("procedureInitiator_profiles", profiles);
                context.getVariables().put("procedureInitiator_sn", person.getSn());
                context.getVariables().put("procedureInitiator_title", person.getTitle());
            }
        }
    }

}
