package org.osivia.services.procedure.formFilters;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.directory.v2.IDirProvider;
import org.osivia.portal.api.locator.Locator;

import fr.toutatice.outils.ldap.v2.model.ENTPerson;
import fr.toutatice.outils.ldap.v2.model.ENTStructure;
import fr.toutatice.outils.ldap.v2.service.ENTPersonService;
import fr.toutatice.outils.ldap.v2.service.ENTStructureService;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilter;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterExecutor;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterParameterType;

/**
 * Fill variables with structure of initiator data form filter
 *
 * @author Dorian Licois
 * @see FormFilter
 */
public class SetStructureVariableFilter implements FormFilter {

    /** Form filter identifier. */
    public static final String ID = "SET_STRUCTURE_VARIABLE";

    /** Form filter label internationalization key. */
    private static final String LABEL_INTERNATIONALIZATION_KEY = "SET_STRUCTURE_VARIABLE_LABEL";
    /** Form filter description internationalization key. */
    private static final String DESCRIPTION_INTERNATIONALIZATION_KEY = "SET_STRUCTURE_VARIABLE_DESCRIPTION";


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

        IDirProvider directoryProvider = Locator.findMBean(IDirProvider.class, IDirProvider.MBEAN_NAME);

        ENTPersonService personService = directoryProvider.getDirService(ENTPersonService.class);
        ENTStructureService structureService = directoryProvider.getDirService(ENTStructureService.class);
        ENTPerson person = personService.getPerson(procedureInitiator);
        if(person!=null){
            String structRattach = person.getStructRattach();
            if (StringUtils.isNotBlank(structRattach)) {
                context.getVariables().put("procedureInitiator_structRattach", structRattach);

                ENTStructure structure = structureService.getStructure(structRattach);

                if (structure != null) {
                    String codeAcademie = structure.getCodeAcademie();

                    context.getVariables().put("procedureInitiator_typeStructure", structure.getTypestructure());
                    context.getVariables().put("procedureInitiator_structDisplayName", structure.getDisplayName());
                    context.getVariables().put("procedureInitiator_structLocalisation", structure.getLocalisation());
                    context.getVariables().put("procedureInitiator_structCodePostal", structure.getCodePostal());

                }
            }
        }
    }
}
