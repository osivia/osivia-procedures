package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;


/**
 * Factory of common ProcedureModel
 * 
 * @author Dorian Licois
 */
public class ProcedureRepository {

    private static final String RECORD_PARENT_MODEL_WEBID = "procedure_record_folder";

    public static final String DEFAULT_FIELD_TITLE_NAME = "_title";

    private ProcedureRepository() {
    }

    public static ProcedureModel recordFolder() {
        ProcedureModel procedureModel = new ProcedureModel();
        procedureModel.setProcedureType(DocumentTypeEnum.RECORDFOLDER.getDocType());
        procedureModel.setWebIdParent(RECORD_PARENT_MODEL_WEBID);

        procedureModel.setSteps(new ArrayList<Step>(1));

        Step step1 = new Step(0);
        step1.setReference(IFormsService.FORM_STEP_REFERENCE);
        step1.setStepName("Formulaire");
        step1.setActions(new ArrayList<Action>());
        step1.setIndex(0);

        Field fieldTitle = new Field();
        fieldTitle.setInput(true);
        fieldTitle.setLabel("Titre");
        fieldTitle.setName(DEFAULT_FIELD_TITLE_NAME);
        fieldTitle.setPath("0");
        fieldTitle.setRequired(true);
        fieldTitle.setSuperLabel("Titre");
        fieldTitle.setType(VariableTypesEnum.TEXT);
        List<Field> fields = new ArrayList<Field>();
        fields.add(fieldTitle);
        step1.setFields(fields);

        Map<String, Variable> variables = new HashMap<String, Variable>();
        variables.put(DEFAULT_FIELD_TITLE_NAME, new Variable(DEFAULT_FIELD_TITLE_NAME, "Titre", VariableTypesEnum.TEXT, null));
        procedureModel.setVariables(variables);

        procedureModel.getSteps().add(step1);
        procedureModel.setStartingStep(step1.getReference());
        Dashboard dashboard = new Dashboard();
        Column titleColumn = new Column();
        titleColumn.setIndex(0);
        titleColumn.setLabel("Titre");
        titleColumn.setSortable(true);
        titleColumn.setVariableName(DEFAULT_FIELD_TITLE_NAME);
        titleColumn.setEnableLink(true);
        dashboard.getColumns().add(titleColumn);
        procedureModel.getDashboards().add(dashboard);
        

        return procedureModel;
    }
}
