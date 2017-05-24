package org.osivia.services.procedure.portlet.model;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;


/**
 * @author Dorian Licois
 */
public class Form {

    /** procedureModel */
    private ProcedureModel procedureModel;

    /** selectedStep */
    private String selectedStep;

    /** selectedAction */
    private String selectedAction;

    /** selectedTdb */
    private String selectedTdb;

    /** procedureInstance */
    private ProcedureInstance procedureInstance;

    /** alertSuccess */
    private String alertSuccess;

    /** addField */
    private AddField newField;

    /** newFieldSet */
    private AddField newFieldSet;

    /** newColumn */
    private Column newColumn;

    /** selectedFilter */
    private Filter selectedFilter;

    /** selectedField */
    private Field selectedField;

    /** selectedVariable */
    private Variable selectedVariable;

    /** advancedMode */
    private boolean advancedMode;
    
    private VariableTypesEnum[] variableTypesEnum = VariableTypesEnum.values();

    /** procedureInstance */
    private List<ProcedureInstance> procedureInstances;

    public Form(ProcedureModel procedureModel) {
        this.procedureModel = procedureModel;
        newField = new AddField();
        newFieldSet = new AddField();
        newColumn = new Column();
    }

    public Form(ProcedureModel procedureModel, ProcedureInstance procedureInstance) {
        this.procedureModel = procedureModel;
        this.procedureInstance = procedureInstance;
        if ((procedureInstance != null) && (procedureModel != null)) {
            for (Step step : procedureModel.getSteps()) {
                if (StringUtils.equals(procedureInstance.getCurrentStep(), step.getReference())) {
                    selectedStep = String.valueOf(step.getIndex());
                    break;
                }
            }
        }
        newField = new AddField();
        newFieldSet = new AddField();
        newColumn = new Column();
    }


    public Form() {
        newField = new AddField();
        newFieldSet = new AddField();
        newColumn = new Column();
    }

    /**
     * gets the step being curently edited
     *
     * @return the selected step
     */
    public Step getTheSelectedStep() {
        return getProcedureModel().getSteps().get(NumberUtils.toInt(getSelectedTdb()));
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
        if (StringUtils.isNotBlank(getProcedureModel().getWebIdParent())) {
            for (Step parentStep : getProcedureModel().getProcedureParent().getSteps()) {
                if (StringUtils.equals(returnStep, parentStep.getReference())) {
                    Step step = getProcedureModel().getSteps().get(0);
                    parentStep.setFields(step.getFields());
                    return parentStep;
                }
            }
        } else {
            for (final Step step : getProcedureModel().getSteps()) {
                if (StringUtils.equals(returnStep, step.getReference())) {
                    return step;
                }
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
        if (StringUtils.equals(selectedAction, "-1")) {
            return getTheSelectedStep().getInitAction();
        } else {
            return getTheSelectedStep().getActions().get(NumberUtils.toInt(selectedAction));
        }
    }

    public Dashboard getTheSelectedTdb() {
        return getProcedureModel().getDashboards().get(NumberUtils.toInt(getSelectedTdb()));
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
        if (newField != null) {
            return newField;
        } else {
            return new AddField();
        }
    }

    /**
     * Setter for addField.
     *
     * @param addField the addField to set
     */
    public void setNewField(AddField newField) {
        this.newField = newField;
    }

    public String getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    /**
     * Getter for selectedFilter.
     * @return the selectedFilter
     */
    public Filter getSelectedFilter() {
        return selectedFilter;
    }

    /**
     * Setter for selectedFilter.
     * @param selectedFilter the selectedFilter to set
     */
    public void setSelectedFilter(Filter selectedFilter) {
        this.selectedFilter = selectedFilter;
    }

    /**
     * Getter for selectedField.
     * @return the selectedField
     */
    public Field getSelectedField() {
        return selectedField;
    }

    /**
     * Setter for selectedField.
     * @param selectedField the selectedField to set
     */
    public void setSelectedField(Field selectedField) {
        this.selectedField = selectedField;
    }

    /**
     * Getter for newFieldSet.
     *
     * @return the newFieldSet
     */
    public AddField getNewFieldSet() {
        if (newFieldSet != null) {
            return newFieldSet;
        } else {
            return new AddField();
        }
    }

    /**
     * Setter for newFieldSet.
     *
     * @param newFieldSet the newFieldSet to set
     */
    public void setNewFieldSet(AddField newFieldSet) {
        this.newFieldSet = newFieldSet;
    }

    /**
     * Getter for advancedMode.
     * @return the advancedMode
     */
    public boolean isAdvancedMode() {
        return advancedMode;
    }

    /**
     * Setter for advancedMode.
     * @param advancedMode the advancedMode to set
     */
    public void setAdvancedMode(boolean advancedMode) {
        this.advancedMode = advancedMode;
    }

	/**
     * Getter for variableTypesEnum.
     * @return the variableTypesEnum
     */
    public VariableTypesEnum[] getVariableTypesEnum() {
        return variableTypesEnum;
    }

    /**
     * Setter for variableTypesEnum.
     * @param variableTypesEnum the variableTypesEnum to set
     */
    public void setVariableTypesEnum(VariableTypesEnum[] variableTypesEnum) {
        this.variableTypesEnum = variableTypesEnum;
    }

    /**
     * Getter for procedureInstances.
     * 
     * @return the procedureInstances
     */
    public List<ProcedureInstance> getProcedureInstances() {
        return procedureInstances;
    }

    /**
     * Setter for procedureInstances.
     * 
     * @param procedureInstances the procedureInstances to set
     */
    public void setProcedureInstances(List<ProcedureInstance> procedureInstances) {
        this.procedureInstances = procedureInstances;
    }
    
    /**
     * Getter for selectedVariable.
     * @return the selectedVariable
     */
    public Variable getSelectedVariable() {
        return selectedVariable;
    }

    /**
     * Setter for selectedVariable.
     * @param selectedVariable the selectedVariable to set
     */
    public void setSelectedVariable(Variable selectedVariable) {
        this.selectedVariable = selectedVariable;
    }


    /**
     * Getter for selectedTdb.
     * 
     * @return the selectedTdb
     */
    public String getSelectedTdb() {
        return selectedTdb;
    }


    /**
     * Setter for selectedTdb.
     * 
     * @param selectedTdb the selectedTdb to set
     */
    public void setSelectedTdb(String selectedTdb) {
        this.selectedTdb = selectedTdb;
    }


    /**
     * Getter for newColumn.
     * 
     * @return the newColumn
     */
    public Column getNewColumn() {
        if (newColumn == null) {
            newColumn = new Column();
        }
        return newColumn;
    }


    /**
     * Setter for newColumn.
     * 
     * @param newColumn the newColumn to set
     */
    public void setNewColumn(Column newColumn) {
        this.newColumn = newColumn;
    }

}
