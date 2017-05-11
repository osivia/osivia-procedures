package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;


public class ProcedureRepository {

    private ProcedureRepository() {
    }

    public static ProcedureModel list() {
        ProcedureModel procedureModel = new ProcedureModel();
        procedureModel.setProcedureType(ProcedureTypeEnum.LIST.name());
        procedureModel.setSteps(new ArrayList<Step>());

        Step step1 = new Step(0);
        step1.setReference("formulaire");
        step1.setStepName("Formulaire");
        step1.setActions(new ArrayList<Action>());
        procedureModel.getSteps().add(step1);
        procedureModel.setStartingStep(step1.getReference());
        
        Step step2 = new Step(1);
        step2.setReference("presentation");
        step2.setStepName("Présentation");
        step2.setActions(new ArrayList<Action>());
        procedureModel.getSteps().add(step2);

        return procedureModel;
    }
}
