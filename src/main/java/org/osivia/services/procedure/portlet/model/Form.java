package org.osivia.services.procedure.portlet.model;

import org.apache.commons.lang.StringUtils;


/**
 * @author dorian
 */
public class Form {

    /** procedureModel */
    private ProcedureModel procedureModel;

    /** selectedStep */
    private String selectedStep;

    /** selectedAction */
    private String selectedAction;

    /** procedureInstance */
    private ProcedureInstance procedureInstance;

    /** alertSuccess */
    private String alertSuccess;

    /** addField */
    private AddField newField;

    private String filterMessage;

    public Form(ProcedureModel procedureModel) {
        this.procedureModel = procedureModel;
        newField = new AddField();
    }

    public Form(ProcedureModel procedureModel, ProcedureInstance procedureInstance) {
        this.procedureModel = procedureModel;
        this.procedureInstance = procedureInstance;
        newField = new AddField();
    }


    public Form() {
        newField = new AddField();
    }

    /**
     * gets the step being curently edited
     *
     * @return the selected step
     */
    public Step getTheSelectedStep() {
        return getProcedureModel().getSteps().get(Integer.valueOf(getSelectedStep()));
    }


    /**
     * gets the step to display
     *
     * @return the current step
     */
    public Step getTheCurrentStep() {
        String returnStep;
        if ((getProcedureInstance() != null) && StringUtils.isNotEmpty(getProcedureInstance().getCurrentStep())) {
            returnStep = getProcedureInstance().getCurrentStep();
        } else {
            returnStep = getProcedureModel().getStartingStep();
        }
        for (final Step step : getProcedureModel().getSteps()) {
            if (StringUtils.equals(returnStep, step.getReference())) {
                return step;
            }
        }
        return null;
    }

    /**
     * gets the Action being curently edited
     *
     * @return the selected Action
     */
    public Action getTheSelectedAction() {
        return getTheCurrentStep().getActions().get(Integer.valueOf(selectedAction));
    }


    /**
     * Getter for selectedStep.
     *
     * @return the selectedStep
     */
    public String getSelectedStep() {
        return selectedStep;
    }


    /**
     * Setter for selectedStep.
     *
     * @param selectedStep the selectedStep to set
     */
    public void setSelectedStep(String selectedStep) {
        this.selectedStep = selectedStep;
    }

    /**
     * Getter for procedureModel.
     *
     * @return the procedureModel
     */
    public ProcedureModel getProcedureModel() {
        return procedureModel;
    }

    /**
     * Setter for procedureModel.
     *
     * @param procedureModel the procedureModel to set
     */
    public void setProcedureModel(ProcedureModel procedureModel) {
        this.procedureModel = procedureModel;
    }

    /**
     * Getter for procedureInstance.
     *
     * @return the procedureInstance
     */
    public ProcedureInstance getProcedureInstance() {
        return procedureInstance;
    }

    /**
     * Setter for procedureInstance.
     *
     * @param procedureInstance the procedureInstance to set
     */
    public void setProcedureInstance(ProcedureInstance procedureInstance) {
        this.procedureInstance = procedureInstance;
    }

    /**
     * Getter for alertSuccess.
     *
     * @return the alertSuccess
     */
    public String getAlertSuccess() {
        return alertSuccess;
    }

    /**
     * Setter for alertSuccess.
     *
     * @param alertSuccess the alertSuccess to set
     */
    public void setAlertSuccess(String alertSuccess) {
        this.alertSuccess = alertSuccess;
    }

    /**
     * Getter for addField.
     *
     * @return the addField
     */
    public AddField getNewField() {
        return newField;
    }

    /**
     * Setter for addField.
     *
     * @param addField the addField to set
     */
    public void setNewField(AddField newField) {
        this.newField = newField;
    }

    public String getFilterMessage() {
        return filterMessage;
    }

    public void setFilterMessage(String filterMessage) {
        this.filterMessage = filterMessage;
    }

    public String getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

}
