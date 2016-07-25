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

    /** url */
    private String url;

    /** webId */
    private String webId;

    public ProcedureModel() {
        variables = new HashMap<String, Variable>();
        steps = new ArrayList<Step>();
        procedureObjects = new ArrayList<ProcedureObject>();
    }

    public ProcedureModel(Document document) {
        variables = new HashMap<String, Variable>();
        steps = new ArrayList<Step>();
        procedureObjects = new ArrayList<ProcedureObject>();

        final PropertyMap properties = document.getProperties();
        name = properties.getString("dc:title");
        webId = properties.getString("ttc:webid");
        path = document.getPath();
        startingStep = properties.getString("pcd:startingStep");

        // global variables
        final PropertyList globalVariablesList = properties.getList("pcd:globalVariablesDefinitions");
        if (globalVariablesList != null) {
            Variable var;
            for (final Object globalVariableO : globalVariablesList.list()) {
                final PropertyMap globalVariable = (PropertyMap) globalVariableO;

                final List<Object> varOptionsPL = globalVariable.getList("varOptions").list();
                final List<String> varOptionsL = new ArrayList<String>(varOptionsPL.size());
                for (final Object varOption : varOptionsPL) {
                    varOptionsL.add((String) varOption);
                }

                var = new Variable(globalVariable.getString("name"), globalVariable.getString("label"), VariableTypesEnum.valueOf(globalVariable
                        .getString("type")), varOptionsL);
                getVariables().put(var.getName(), var);
            }
        }
        // steps
        final PropertyList stepsList = properties.getList("pcd:steps");
        if (stepsList != null) {
            Step step;
            for (final Object stepO : stepsList.list()) {
                final PropertyMap stepM = (PropertyMap) stepO;
                step = new Step(stepM, getVariables());
                getSteps().add(step.getIndex(), step);
            }
        }
        final PropertyList procedureObjectsList = properties.getList("pcd:procedureObjects");
        if (procedureObjectsList != null) {
            ProcedureObject newProcedureObject;
            for (final Object procedureObject : procedureObjectsList.list()) {
                final PropertyMap procedureObjectMap = (PropertyMap) procedureObject;
                newProcedureObject = new ProcedureObject();
                newProcedureObject.setName(procedureObjectMap.getString("name"));
                newProcedureObject.setPath(procedureObjectMap.getString("path"));
                newProcedureObject.setType(procedureObjectMap.getString("type"));
                procedureObjects.add(newProcedureObject);
            }
        }
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
        for (final ProcedureObject procedureObject : procedureObjects) {
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

    /**
     * Getter for webId.
     * 
     * @return the webId
     */
    public String getWebId() {
        return webId;
    }

    /**
     * Setter for webId.
     * 
     * @param webId the webId to set
     */
    public void setWebId(String webId) {
        this.webId = webId;
    }

}
