package org.osivia.services.procedure.portlet.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

@JsonAutoDetect(isGetterVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE,
        creatorVisibility = Visibility.NONE)
public class ProcedureModel {

    /** the name of the procedure */
    @JsonProperty("dc:title")
    private String name;

    /** description of the procedure */
    @JsonProperty("dc:description")
    private String description;

    /** the global variables for the procedure */
    @JsonProperty("pcd:globalVariablesDefinitions")
    private Map<String, Variable> variables;

    /** the ordered list of steps in the procedure */
    @JsonProperty("pcd:steps")
    private List<Step> steps;

    /** startingStep */
    @JsonProperty("pcd:startingStep")
    private String startingStep;

    /** path of the document */
    @JsonIgnore
    private String path;


    public ProcedureModel() {
        variables = new HashMap<String, Variable>();
        steps = new ArrayList<Step>();
    }

    public ProcedureModel(Document document) {
        variables = new HashMap<String, Variable>();
        steps = new ArrayList<Step>();

        PropertyMap properties = document.getProperties();
        name = properties.getString("dc:title");
        path = document.getPath();
        startingStep = properties.getString("pcd:startingStep");

        // global variables
        PropertyList globalVariablesList = properties.getList("pcd:globalVariablesDefinitions");
        Iterator<Object> globalVariableIterator;
        if (globalVariablesList != null) {
            globalVariableIterator = globalVariablesList.list().iterator();
            Variable var;
            while (globalVariableIterator.hasNext()) {
                PropertyMap globalVariable = (PropertyMap) globalVariableIterator.next();
                var = new Variable(globalVariable.getString("name"), globalVariable.getString("label"), VariableTypesEnum.valueOf(globalVariable
                        .getString("type")));
                getVariables().put(var.getName(), var);
            }
        }
        // steps
        PropertyList stepsList = properties.getList("pcd:steps");
        if (stepsList != null) {
            Iterator<Object> stepIterator = stepsList.list().iterator();
            Step step;
            while (stepIterator.hasNext()) {
                PropertyMap stepM = (PropertyMap) stepIterator.next();
                PropertyList widgetList = stepM.getList("globalVariablesReferences");
                step = new Step();
                if (widgetList != null) {
                    Iterator<Object> widgetIterator = widgetList.list().iterator();
                    Field field;
                    while (widgetIterator.hasNext()) {
                        PropertyMap widget = (PropertyMap) widgetIterator.next();
                        field = new Field();
                        field.setInput(widget.getBoolean("isInput"));
                        field.setOrder(Integer.valueOf(widget.getString("order")));
                        Variable variable = getVariables().get(widget.getString("variableLabel"));
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
                    Iterator<Object> actionsIterator = actionsList.list().iterator();
                    Action action;
                    while (actionsIterator.hasNext()) {
                        PropertyMap actionN = (PropertyMap) actionsIterator.next();
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

}
