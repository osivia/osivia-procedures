package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;


public class ProcedureRepository {

    private static final String RECORD_PARENT_MODEL_WEBID = "procedure_record_folder";

    public static final String FORM_STEP_REFERENCE = "formulaire";

    private ProcedureRepository() {
    }

    public static ProcedureModel recordFolder() {
        ProcedureModel procedureModel = new ProcedureModel();
        procedureModel.setProcedureType(DocumentTypeEnum.RECORDFOLDER.getDocType());
        procedureModel.setWebIdParent(RECORD_PARENT_MODEL_WEBID);

        procedureModel.setSteps(new ArrayList<Step>(1));

        Step step1 = new Step(0);
        step1.setReference(FORM_STEP_REFERENCE);
        step1.setStepName("Formulaire");
        step1.setActions(new ArrayList<Action>());
        step1.setIndex(0);
        procedureModel.getSteps().add(step1);
        procedureModel.setStartingStep(step1.getReference());
        procedureModel.getDashboards().add(new Dashboard());

        return procedureModel;
    }
}
