package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.HashSet;

import org.osivia.services.procedure.formFilters.DeleteOnEndingFormFilter;


public class ProcedureRepository {

    private ProcedureRepository() {
    }

    public static ProcedureModel list() {
        ProcedureModel procedureModel = new ProcedureModel();
        procedureModel.setProcedureType(ProcedureTypeEnum.LIST.name());
        procedureModel.setSteps(new ArrayList<Step>());

        Step step1 = new Step(0);
        step1.setReference("step1");
        step1.setStepName("Ajout d'un élément");
        step1.setActions(new ArrayList<Action>());
        procedureModel.getSteps().add(step1);
        procedureModel.setStartingStep(step1.getReference());
        
        Step step2 = new Step(1);
        step2.setReference("step2");
        step2.setStepName("Édition d'un élément");
        step2.setActions(new ArrayList<Action>());
        procedureModel.getSteps().add(step2);
        
        Action soumettre = new Action();
        soumettre.setActionId("soumettre");
        soumettre.setLabel("Soumettre");
        soumettre.setStepReference(step2.getReference());
        step1.getActions().add(soumettre);
        
        Action modifier = new Action();
        modifier.setActionId("modifier");
        modifier.setLabel("Modifier");
        modifier.setStepReference(step2.getReference());
        step2.getActions().add(modifier);

        Action supprimer = new Action();
        supprimer.setActionId("supprimer");
        supprimer.setLabel("Supprimer");
        supprimer.setStepReference("endStep");
        HashSet<Filter> filtersList = new HashSet<Filter>(1);
        Filter deleteOnEndingFilter = new Filter();
        deleteOnEndingFilter.setFilterId(DeleteOnEndingFormFilter.ID);
        deleteOnEndingFilter.setFilterName("deleteOnEndingFilter");
        deleteOnEndingFilter.setFilterInstanceId(deleteOnEndingFilter.getFilterId().concat("0"));
        deleteOnEndingFilter.setFilterPath("0");
        filtersList.add(deleteOnEndingFilter);
        supprimer.setFiltersList(filtersList);
        step2.getActions().add(supprimer);

        return procedureModel;
    }
}
