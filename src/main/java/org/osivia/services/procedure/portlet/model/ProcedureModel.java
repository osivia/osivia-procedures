package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

public class ProcedureModel {

    /** the name of the procedure */
    private String name;

    /** description of the procedure */
    private String description;

    /** the global variables for the procedure */
    private Map<String, Variable> variables;

    /** the ordered list of steps in the procedure */
    private List<Step> steps;

    /** procedureObjects */
    private List<ProcedureObject> procedureObjects;

    /** startingStep */
    private String startingStep;

    /** path of the document */
    private String path;

    private String url;

    public ProcedureModel() {
        variables = new HashMap<String, Variable>();
        steps = new ArrayList<Step>();
        procedureObjects = new ArrayList<ProcedureObject>();
    }

    public ProcedureModel(Document document) {
        variables = new HashMap<String, Variable>();
        steps = new ArrayList<Step>();
        procedureObjects = new ArrayList<ProcedureObject>();

        PropertyMap properties = document.getProperties();
        name = properties.getString("dc:title");
        path = document.getPath();
        startingStep = properties.getString("pcd:startingStep");

        // global variables
        PropertyList globalVariablesList = properties.getList("pcd:globalVariablesDefinitions");
        if (globalVariablesList != null) {
            Variable var;
            for (Object globalVariableO : globalVariablesList.list()) {
                PropertyMap globalVariable = (PropertyMap) globalVariableO;
                var = new Variable(globalVariable.getString("name"), globalVariable.getString("label"), VariableTypesEnum.valueOf(globalVariable
                        .getString("type")));
                getVariables().put(var.getName(), var);
            }
        }
        // steps
        PropertyList stepsList = properties.getList("pcd:steps");
        if (stepsList != null) {
            Step step;
            for (Object stepO : stepsList.list()) {
                PropertyMap stepM = (PropertyMap) stepO;
                PropertyList widgetList = stepM.getList("globalVariablesReferences");
                step = new Step();
                if (widgetList != null) {
                    Field field;
                    for (Object widgetO : widgetList.list()) {
                        PropertyMap widget = (PropertyMap) widgetO;
                        field = new Field();
                        field.setInput(widget.getBoolean("isInput"));
                        field.setOrder(Integer.valueOf(widget.getString("order")));
                        Variable variable = getVariables().get(widget.getString("variableName"));
                        if (variable != null) {
                            field.setLabel(variable.getLabel());
                            field.setName(variable.getName());
                            field.setType(variable.getType().name());
                        }
                        step.getFields().add(field);
                    }
                }
                Collections.sort(step.getFields());

                PropertyList actionsList = stepM.getList("actions");
                if (actionsList != null) {
                    Action action;
                    for (Object actionO : actionsList.list()) {
                        PropertyMap actionN = (PropertyMap) actionO;
                        action = new Action();
                        action.setLabel(actionN.getString("label"));
                        action.setStepReference(actionN.getString("stepReference"));
                        step.getActions().add(action);
                    }
                }
                step.setStepName(stepM.getString("name"));
                step.setIndex(stepM.getLong("index").intValue());
                step.setReference(stepM.getString("reference"));
                getSteps().add(step.getIndex(), step);
            }
        }
        PropertyList procedureObjectsList = properties.getList("pcd:procedureObjects");
        if (procedureObjectsList != null) {
            ProcedureObject newProcedureObject;
            for (Object procedureObject : procedureObjectsList.list()) {
                PropertyMap procedureObjectMap = (PropertyMap) procedureObject;
                newProcedureObject = new ProcedureObject();
                newProcedureObject.setName(procedureObjectMap.getString("name"));
                newProcedureObject.setPath(procedureObjectMap.getString("path"));
                newProcedureObject.setType(procedureObjectMap.getString("type"));
                procedureObjects.add(newProcedureObject);
            }
        }
    }

    public void updateGlobalVariableDefinition() {

        Map<String, Variable> globalVariablesDefinition = new HashMap<String, Variable>();
        for (Step step : steps) {
            for (Field field : step.getFields()) {
                if (StringUtils.isNotEmpty(field.getName())) {
                    globalVariablesDefinition.put(field.getName(), new Variable(field.getName(), field.getLabel(), VariableTypesEnum.valueOf(field.getType())));
                }
            }
        }
        setVariables(globalVariablesDefinition);
    }

    public void updateStepsIndexes() {

        Collections.sort(getSteps());
        for (int i = 0; i < getSteps().size(); i++) {
            getSteps().get(i).setIndex(i);
        }
    }

    /**
     * Getter for variables.
     *
     * @return the variables
     */
    public Map<String, Variable> getVariables() {
        return variables;
    }

    /**
     * Setter for variables.
     *
     * @param variables the variables to set
     */
    public void setVariables(Map<String, Variable> variables) {
        this.variables = variables;
    }

    /**
     * Getter for steps.
     *
     * @return the steps
     */
    public List<Step> getSteps() {
        return steps;
    }

    /**
     * Setter for steps.
     *
     * @param steps the steps to set
     */
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    /**
     * Getter for description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for path.
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * Setter for path.
     *
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Getter for startingStep.
     *
     * @return the startingStep
     */
    public String getStartingStep() {
        return startingStep;
    }

    /**
     * Setter for startingStep.
     *
     * @param startingStep the startingStep to set
     */
    public void setStartingStep(String startingStep) {
        this.startingStep = startingStep;
    }

    /**
     * Getter for procedureObjects.
     * @return the procedureObjects
     */
    public List<ProcedureObject> getProcedureObjects() {
        return procedureObjects;
    }

    public ProcedureObject getProcedureObject(String objectName) {
        for (ProcedureObject procedureObject : procedureObjects) {
            if (StringUtils.equals(procedureObject.getName(), objectName)) {
                return procedureObject;
            }
        }
        return null;
    }

    /**
     * Setter for procedureObjects.
     * @param procedureObjects the procedureObjects to set
     */
    public void setProcedureObjects(List<ProcedureObject> procedureObjects) {
        this.procedureObjects = procedureObjects;
    }

    /**
     * Getter for url.
     *
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for url.
     *
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
