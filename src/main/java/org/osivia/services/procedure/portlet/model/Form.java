package org.osivia.services.procedure.portlet.model;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;


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

    private Record record;

    /** addField */
    private AddField newField;

    /** newFieldSet */
    private AddField newFieldSet;

    /** newColumn */
    private Column newColumn;

    /** newExportVar */
    private String newExportVar;

    /** selectedFilter */
    private Filter selectedFilter;

    /** selectedField */
    private Field selectedField;

    /** selectedVariable */
    private Variable selectedVariable;

    /** advancedMode */
    private boolean advancedMode;
    
    /** variableTypesEnum */
    private VariableTypesAllEnum[] variableTypesEnum = VariableTypesAllEnum.filteredValues();

    /** variableTypesAltEnum */
    private VariableTypesAltEnum[] variableTypesAltEnum = VariableTypesAltEnum.values();

    /** procedureInstance */
    private List<ProcedureInstance> procedureInstances;
    
    /** editedStep */
    private Step editedStep;

    /** editedAction */
    private Action editedAction;

    /** editedDashboard */
    private Dashboard editedDashboard;

    /** editedVariables */
    private List<Variable> editedVariables;

    /** Record types. */
    private Map<String, String> recordTypes;


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

    public Form(ProcedureModel procedureModel, Record record) {
        this.procedureModel = procedureModel;
        this.record = record;
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

        if (StringUtils.isNotBlank(procedureModel.getProcedureType()) && DocumentTypeEnum.RECORDFOLDER.equals(procedureModel.getDocumentType())) {
            return procedureModel.getSteps().get(0);
        } else {
            return editedStep;
        }
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
        } else if (getRecord() != null) {
            if (StringUtils.equals(getProcedureModel().getStartingStep(), IFormsService.FORM_STEP_REFERENCE)) {
                return getProcedureModel().getSteps().get(0);
            } else {
                returnStep = getProcedureModel().getStartingStep();
            }
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
            return editedAction;
        }
    }

    public Dashboard getTheSelectedTdb() {
    	if (StringUtils.isNotBlank(procedureModel.getProcedureType()) && DocumentTypeEnum.RECORDFOLDER.equals(procedureModel.getDocumentType())) {
            return procedureModel.getDashboards().get(0);
        } else {
        	return editedDashboard;
        }
    }

    public boolean isSelectedTdbPersisted() {
        if (StringUtils.isNotBlank(getSelectedTdb())) {
            int selectedTdbIndex = NumberUtils.toInt(getSelectedTdb());
            int dashBoardsAmnt = getProcedureModel().getDashboards().size();
            if (selectedTdbIndex >= dashBoardsAmnt) {
                return false;
            }
        }
        return true;
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
    public VariableTypesAllEnum[] getVariableTypesEnum() {
        return variableTypesEnum;
    }

    /**
     * Setter for variableTypesEnum.
     * @param variableTypesEnum the variableTypesEnum to set
     */
    public void setVariableTypesEnum(VariableTypesAllEnum[] variableTypesEnum) {
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

    /**
     * Getter for record.
     * @return the record
     */
    public Record getRecord() {
        return record;
    }

    /**
     * Setter for record.
     * @param record the record to set
     */
    public void setRecord(Record record) {
        this.record = record;
    }

    /**
     * Getter for variableTypesAltEnum.
     * @return the variableTypesAltEnum
     */
    public VariableTypesAltEnum[] getVariableTypesAltEnum() {
        return variableTypesAltEnum;
    }

    /**
     * Setter for variableTypesAltEnum.
     * @param variableTypesAltEnum the variableTypesAltEnum to set
     */
    public void setVariableTypesAltEnum(VariableTypesAltEnum[] variableTypesAltEnum) {
        this.variableTypesAltEnum = variableTypesAltEnum;
    }

    /**
     * Getter for newExportVar.
     * 
     * @return the newExportVar
     */
    public String getNewExportVar() {
        return newExportVar;
    }

    /**
     * Setter for newExportVar.
     * 
     * @param newExportVar the newExportVar to set
     */
    public void setNewExportVar(String newExportVar) {
        this.newExportVar = newExportVar;
    }

    /**
     * Getter for editedStep.
     * @return the editedStep
     */
    public Step getEditedStep() {
        return editedStep;
    }

    /**
     * Setter for editedStep.
     * @param editedStep the editedStep to set
     */
    public void setEditedStep(Step editedStep) {
        this.editedStep = editedStep;
    }

    /**
     * Getter for editedAction.
     * @return the editedAction
     */
    public Action getEditedAction() {
        return editedAction;
    }

    /**
     * Setter for editedAction.
     * @param editedAction the editedAction to set
     */
    public void setEditedAction(Action editedAction) {
        this.editedAction = editedAction;
    }

    /**
     * Getter for editedDashboard.
     * 
     * @return the editedDashboard
     */
    public Dashboard getEditedDashboard() {
        return editedDashboard;
    }

    /**
     * Setter for editedDashboard.
     * 
     * @param editedDashboard the editedDashboard to set
     */
    public void setEditedDashboard(Dashboard editedDashboard) {
        this.editedDashboard = editedDashboard;
    }

    /**
     * Getter for editedVariables.
     * @return the editedVariables
     */
    public List<Variable> getEditedVariables() {
        return editedVariables;
    }

    /**
     * Setter for editedVariables.
     * @param editedVariables the editedVariables to set
     */
    public void setEditedVariables(List<Variable> editedVariables) {
        this.editedVariables = editedVariables;
    }

    /**
     * Getter for recordTypes.
     * 
     * @return the recordTypes
     */
    public Map<String, String> getRecordTypes() {
        return recordTypes;
    }

    /**
     * Setter for recordTypes.
     * 
     * @param recordTypes the recordTypes to set
     */
    public void setRecordTypes(Map<String, String> recordTypes) {
        this.recordTypes = recordTypes;
    }

}
